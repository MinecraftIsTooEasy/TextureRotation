package vbonedra.texture_rotation.mixin;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.texture_rotation.TRConfigs;

import static vbonedra.texture_rotation.TRConfigs.*;

@Mixin(RenderBlocks.class)
public class RenderBlocksMixin {

    @Shadow private boolean flipTexture;
    @Shadow private int uvRotateTop;
    @Shadow private int uvRotateBottom;
    @Shadow private int uvRotateNorth;
    @Shadow private int uvRotateSouth;
    @Shadow private int uvRotateEast;
    @Shadow private int uvRotateWest;

    @Shadow double[] u;
    @Shadow double[] v;

    @Shadow private Icon overrideBlockTexture;

    @Unique private boolean FlipX = false;
    @Unique private boolean FlipY = false;

    @Unique private int getBlockHash(int x, int y, int z, int blockID) {
        int hash = x * 7375653 ^ y * 19349663 ^ z * 83492791;
        if (TRConfigs.UseBlockIdToRandomize.getBooleanValue()) {
            hash ^= blockID * 13271443;
        }
        hash = (hash ^ (hash >>> 16)) * 0x45d9f3b;
        hash = (hash ^ (hash >>> 16)) * 0x45d9f3b;
        hash = hash ^ (hash >>> 16);
        return Math.abs(hash);
    }

    @Inject(method = "renderStandardBlock(Lnet/minecraft/Block;III)Z", at = @At("HEAD"))
    private void beforeRenderStandardBlock(Block par1Block, int par2, int par3, int par4, CallbackInfoReturnable<Boolean> cir) {
        if (!TRConfigs.RandomizeTextures.getBooleanValue()) return;
        if (par1Block == null) return;
        if (!isRandomized(par1Block)) return;
        if (this.overrideBlockTexture != null && this.overrideBlockTexture.getIconName().contains("destroy")) return;

        int hash = getBlockHash(par2, par3, par4, par1Block.blockID);
        if (par1Block.blockID == Block.oreRedstoneGlowing.blockID) {
            hash = getBlockHash(par2, par3, par4, Block.oreRedstone.blockID);
        }

        // false because done in special way, using it might break custom rotation
        this.flipTexture = false;
        // 8 variants
        int variant = hash % 8;
        // 0,4 - vanilla
        // 1,5 - x
        // 2,6 - y
        // 3,7 - xy
        this.FlipX = TRConfigs.FlipX.getBooleanValue() && (variant == 1 || variant == 3 || variant == 5 || variant == 7);
        this.FlipY = TRConfigs.FlipY.getBooleanValue() && (variant == 2 || variant == 3 || variant == 6 || variant == 7);
        // used to rotate blocks by 90 degree to add 4 more variants
        int rotation = TRConfigs.Rotate90Degree.getBooleanValue() ? (variant >= 4 ? 1 : 0) : 0;

        if (isSandy(par1Block)) {
            this.uvRotateTop = rotation;
            this.uvRotateBottom = rotation;
            this.uvRotateNorth = rotation;
            this.uvRotateSouth = rotation;
            this.uvRotateEast = rotation;
            this.uvRotateWest = rotation;
        }
        else if (isStony(par1Block)) {
            this.uvRotateTop = 0;
            this.uvRotateBottom = 0;
            this.uvRotateNorth = 0;
            this.uvRotateSouth = 0;
            this.uvRotateEast = 0;
            this.uvRotateWest = 0;
        }
        else if (isGrassy(par1Block)) {
            this.uvRotateTop = rotation;
            this.uvRotateBottom = rotation;
            this.uvRotateNorth = 0;
            this.uvRotateSouth = 0;
            this.uvRotateEast = 0;
            this.uvRotateWest = 0;
            this.FlipY = false;
        }
    }


    // top and bottom
    @Inject(
            method = {
                    "renderFaceYNeg(Lnet/minecraft/Block;DDDLnet/minecraft/Icon;)V",
                    "renderFaceYPos(Lnet/minecraft/Block;DDDLnet/minecraft/Icon;)V"
            },
            at = @At(value = "INVOKE", target = "Lnet/minecraft/Tessellator;add4VerticesWithUVandAO([D[D[D[D[D[F[F[F[I)V")
    ) private void flipYFacesAO(Block par1Block, double par2, double par4, double par6, net.minecraft.Icon par8Icon, CallbackInfo ci) {
        applyHorizontalAndVerticalFlipY(par1Block);
    }
    @Inject(
            method = {
                    "renderFaceYNeg(Lnet/minecraft/Block;DDDLnet/minecraft/Icon;)V",
                    "renderFaceYPos(Lnet/minecraft/Block;DDDLnet/minecraft/Icon;)V"
            },
            at = @At(value = "INVOKE", target = "Lnet/minecraft/Tessellator;add4VerticesWithUV([D[D[D[D[D)V")
    ) private void flipYFacesNoAO(Block par1Block, double par2, double par4, double par6, net.minecraft.Icon par8Icon, CallbackInfo ci) {
        applyHorizontalAndVerticalFlipY(par1Block);
    }
    // all 4 sides
    @Inject(
            method = {
                    "renderFaceZNeg(Lnet/minecraft/Block;DDDLnet/minecraft/Icon;)V",
                    "renderFaceZPos(Lnet/minecraft/Block;DDDLnet/minecraft/Icon;)V",
                    "renderFaceXNeg(Lnet/minecraft/Block;DDDLnet/minecraft/Icon;)V",
                    "renderFaceXPos(Lnet/minecraft/Block;DDDLnet/minecraft/Icon;)V"
            },
            at = @At(value = "INVOKE", target = "Lnet/minecraft/Tessellator;add4VerticesWithUVandAO([D[D[D[D[D[F[F[F[I)V")
    ) private void flipSidesAO(Block par1Block, double par2, double par4, double par6, net.minecraft.Icon par8Icon, CallbackInfo ci) {
        applyFlipsToSides(par1Block);
    }
    @Inject(
            method = {
                    "renderFaceZNeg(Lnet/minecraft/Block;DDDLnet/minecraft/Icon;)V",
                    "renderFaceZPos(Lnet/minecraft/Block;DDDLnet/minecraft/Icon;)V",
                    "renderFaceXNeg(Lnet/minecraft/Block;DDDLnet/minecraft/Icon;)V",
                    "renderFaceXPos(Lnet/minecraft/Block;DDDLnet/minecraft/Icon;)V"
            },
            at = @At(value = "INVOKE", target = "Lnet/minecraft/Tessellator;add4VerticesWithUV([D[D[D[D[D)V")
    ) private void flipSidesNoAO(Block par1Block, double par2, double par4, double par6, net.minecraft.Icon par8Icon, CallbackInfo ci) {
        applyFlipsToSides(par1Block);
    }


    @Unique
    private void applyHorizontalAndVerticalFlipY(Block par1Block) {
        if (!TRConfigs.RandomizeTextures.getBooleanValue()) return;
        if (!isRandomized(par1Block)) return;
        if (this.FlipX) {
            double temp0 = this.u[0]; double temp1 = this.u[1];
            this.u[0] = this.u[3]; this.u[1] = this.u[2];
            this.u[3] = temp0;     this.u[2] = temp1;
        }
        if (this.FlipY) {
            double temp0 = this.v[0]; double temp3 = this.v[3];
            this.v[0] = this.v[1]; this.v[3] = this.v[2];
            this.v[1] = temp0;     this.v[2] = temp3;
        }
    }

    @Unique
    private void applyFlipsToSides(Block par1Block) {
        if (!TRConfigs.RandomizeTextures.getBooleanValue()) return;
        if (!isRandomized(par1Block)) return;
        if (isPillary(par1Block)) {
            if (this.FlipY) {
                double minV = Math.min(Math.min(this.v[0], this.v[1]), Math.min(this.v[2], this.v[3]));
                double maxV = Math.max(Math.max(this.v[0], this.v[1]), Math.max(this.v[2], this.v[3]));
                for (int i = 0; i < 4; i++) this.v[i] = (minV + maxV) - this.v[i];
            }
            return;
        }
        if (this.FlipX) {
            double minU = Math.min(Math.min(this.u[0], this.u[1]), Math.min(this.u[2], this.u[3]));
            double maxU = Math.max(Math.max(this.u[0], this.u[1]), Math.max(this.u[2], this.u[3]));
            for (int i = 0; i < 4; i++) {
                this.u[i] = (minU + maxU) - this.u[i];
            }
        }
        if (this.FlipY) {
            double minV = Math.min(Math.min(this.v[0], this.v[1]), Math.min(this.v[2], this.v[3]));
            double maxV = Math.max(Math.max(this.v[0], this.v[1]), Math.max(this.v[2], this.v[3]));
            for (int i = 0; i < 4; i++) {
                this.v[i] = (minV + maxV) - this.v[i];
            }
        }
    }


    @Inject(method = "renderStandardBlock(Lnet/minecraft/Block;III)Z", at = @At("RETURN"))
    private void afterRenderStandardBlock(Block par1Block, int par2, int par3, int par4, CallbackInfoReturnable<Boolean> cir) {
        if (!TRConfigs.RandomizeTextures.getBooleanValue()) return;
        if (!isRandomized(par1Block)) return;
        this.FlipX = false;
        this.FlipY = false;
        this.uvRotateTop = 0;
        this.uvRotateBottom = 0;
        this.uvRotateNorth = 0;
        this.uvRotateSouth = 0;
        this.uvRotateEast = 0;
        this.uvRotateWest = 0;
    }

    @Unique
    private boolean isSandy(Block par1Block) {
        return BLOCK_RANDOMIZATION_TYPE[par1Block.blockID] == 1 && RandomizeSandy.getBooleanValue();
    }
    @Unique private boolean isStony(Block par1Block) {
        return BLOCK_RANDOMIZATION_TYPE[par1Block.blockID] == 2 && RandomizeStony.getBooleanValue();
    }
    @Unique private boolean isGrassy(Block par1Block) {
        return BLOCK_RANDOMIZATION_TYPE[par1Block.blockID] == 3 && RandomizeGrassy.getBooleanValue();
    }
    @Unique private boolean isPillary(Block par1Block) {
        return BLOCK_RANDOMIZATION_TYPE[par1Block.blockID] == 4 && RandomizePillary.getBooleanValue();
    }


    @Unique private boolean isRandomized(Block par1Block) {
        return isSandy(par1Block)
                || isGrassy(par1Block)
                || isStony(par1Block)
                || isPillary(par1Block)
                ;
    }
}
