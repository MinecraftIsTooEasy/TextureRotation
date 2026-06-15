package vbonedra.texture_rotation;

import fi.dy.masa.malilib.config.ConfigTab;
import fi.dy.masa.malilib.config.SimpleConfigs;
import fi.dy.masa.malilib.config.interfaces.IConfigOptionListEntry;
import fi.dy.masa.malilib.config.options.*;
import net.minecraft.Block;

import java.util.ArrayList;
import java.util.List;

public class TRConfigs extends SimpleConfigs {
    public static final ConfigBoolean RandomizeTextures = new ConfigBoolean("Randomize Textures",   true, "Turns on/off mod");
    public static final ConfigBoolean FlipX             = new ConfigBoolean("Flip Horizontal",      true, "Flips textures horizontally (left-right)");
    public static final ConfigBoolean FlipY             = new ConfigBoolean("Flip Vertical",        true, "Flips textures vertically (top-bottom)");
    public static final ConfigBoolean Rotate90Degree    = new ConfigBoolean("Rotate 90 Degree",     true, "Rotates textures 90 degree");

    public static final ConfigBoolean RandomizeSandy    = new ConfigBoolean("Randomize Sand-like Blocks",   true, "Full random");
    public static final ConfigBoolean RandomizeGrassy   = new ConfigBoolean("Randomize Grass-like Blocks",  true, "Only Flip Horizontal on sides");
    public static final ConfigBoolean RandomizeStony    = new ConfigBoolean("Randomize Stone-like Blocks",  true, "Only Flip Horizontal and Vertical");
    public static final ConfigBoolean RandomizePillary  = new ConfigBoolean("Randomize Pillar-like Blocks", true, "Only Flip Horizontal and Vertical");


    private static final TRConfigs Instance;
    public static final List<ConfigBase<?>> Randomization;
    public static final List<ConfigBase<?>> Categories;
    public static final List<ConfigBase<?>> Total;
    public static final List<ConfigTab> tabs;

    public static final byte[] BLOCK_RANDOMIZATION_TYPE = new byte[4096];
    public static void reinitializeBlockMap() {
        java.util.Arrays.fill(BLOCK_RANDOMIZATION_TYPE, (byte) 0);

        BLOCK_RANDOMIZATION_TYPE[Block.dirt.blockID] = 1;
        BLOCK_RANDOMIZATION_TYPE[Block.sand.blockID] = 1;
        BLOCK_RANDOMIZATION_TYPE[Block.gravel.blockID] = 1;
        BLOCK_RANDOMIZATION_TYPE[Block.slowSand.blockID] = 1;
        BLOCK_RANDOMIZATION_TYPE[Block.blockClay.blockID] = 1;
        BLOCK_RANDOMIZATION_TYPE[Block.hardenedClay.blockID] = 1;
        BLOCK_RANDOMIZATION_TYPE[Block.stainedClay.blockID] = 1;
        BLOCK_RANDOMIZATION_TYPE[Block.blockSnow.blockID] = 1;
        BLOCK_RANDOMIZATION_TYPE[Block.netherrack.blockID] = 1;
        BLOCK_RANDOMIZATION_TYPE[Block.oreNetherQuartz.blockID] = 1;
        BLOCK_RANDOMIZATION_TYPE[Block.sponge.blockID] = 1;
        BLOCK_RANDOMIZATION_TYPE[Block.glowStone.blockID] = 1;
        BLOCK_RANDOMIZATION_TYPE[Block.whiteStone.blockID] = 1;
        BLOCK_RANDOMIZATION_TYPE[Block.cobblestone.blockID] = 1;
        BLOCK_RANDOMIZATION_TYPE[Block.cobblestoneMossy.blockID] = 1;

        BLOCK_RANDOMIZATION_TYPE[Block.stone.blockID] = 2;
        BLOCK_RANDOMIZATION_TYPE[Block.bedrock.blockID] = 2;
        BLOCK_RANDOMIZATION_TYPE[Block.mantleOrCore.blockID] = 2;

        BLOCK_RANDOMIZATION_TYPE[Block.oreGold.blockID] = 2;
        BLOCK_RANDOMIZATION_TYPE[Block.oreIron.blockID] = 2;
        BLOCK_RANDOMIZATION_TYPE[Block.oreCoal.blockID] = 2;
        BLOCK_RANDOMIZATION_TYPE[Block.oreLapis.blockID] = 2;
        BLOCK_RANDOMIZATION_TYPE[Block.oreDiamond.blockID] = 2;
        BLOCK_RANDOMIZATION_TYPE[Block.oreRedstone.blockID] = 2;
        BLOCK_RANDOMIZATION_TYPE[Block.oreRedstoneGlowing.blockID] = 2;
        BLOCK_RANDOMIZATION_TYPE[Block.oreEmerald.blockID] = 2;
        BLOCK_RANDOMIZATION_TYPE[Block.oreCopper.blockID] = 2;
        BLOCK_RANDOMIZATION_TYPE[Block.oreSilver.blockID] = 2;
        BLOCK_RANDOMIZATION_TYPE[Block.oreMithril.blockID] = 2;
        BLOCK_RANDOMIZATION_TYPE[Block.oreAdamantium.blockID] = 2;

        BLOCK_RANDOMIZATION_TYPE[Block.cloth.blockID] = 2;
        BLOCK_RANDOMIZATION_TYPE[Block.leaves.blockID] = 2;

        BLOCK_RANDOMIZATION_TYPE[Block.grass.blockID] = 3;
        BLOCK_RANDOMIZATION_TYPE[Block.mycelium.blockID] = 3;
        BLOCK_RANDOMIZATION_TYPE[Block.melon.blockID] = 3;
        BLOCK_RANDOMIZATION_TYPE[Block.pumpkin.blockID] = 3;
        BLOCK_RANDOMIZATION_TYPE[Block.pumpkinLantern.blockID] = 3;

        BLOCK_RANDOMIZATION_TYPE[Block.wood.blockID] = 4;
        BLOCK_RANDOMIZATION_TYPE[Block.blockNetherQuartz.blockID] = 4;
        BLOCK_RANDOMIZATION_TYPE[Block.hay.blockID] = 4;

    }


    public enum RotationType implements fi.dy.masa.malilib.config.interfaces.IConfigOptionListEntry {
        OFF("OFF", "No randomization"),
        SANDY("SANDY", "8 variants (Full random)"),
        STONY("STONY", "4 variants (Flips only)"),
        GRASSY("GRASSY", "Top/Bottom flips only"),
        PILLARY("PILLARY", "Smart axis alignment");


        private final String name;
        private final String comment;
        RotationType(String name, String comment) {
            this.name = name;
            this.comment = comment;
        }

        @Override
        public String getStringValue() { return this.name; }

        @Override
        public String getDisplayName() { return this.name; }
        @Override
        public IConfigOptionListEntry cycle(boolean forward) {
            int values = values().length;
            int next = (this.ordinal() + (forward ? 1 : -1) + values) % values;
            return values()[next];
        }
        @Override
        public IConfigOptionListEntry fromString(String value) {
            for (RotationType type : values()) {
                if (type.name.equalsIgnoreCase(value)) return type;
            }
            return OFF;
        }
    }


    public TRConfigs(String name, List<ConfigHotkey> hotkeys, List<ConfigBase<?>> values) {
        super(name, hotkeys, values);
    }

    public List<ConfigTab> getConfigTabs() {
        return tabs;
    }

    public static TRConfigs getInstance() {
        return Instance;
    }

    static {
        Total = new ArrayList<>();
        tabs = new ArrayList<>();
        Randomization = List.of(
                RandomizeTextures,
                FlipX,
                FlipY,
                Rotate90Degree
        );
        Categories = List.of(
                RandomizeSandy,
                RandomizeGrassy,
                RandomizeStony,
                RandomizePillary
        );
        Total.addAll(Randomization);
        Total.addAll(Categories);
        tabs.add(new ConfigTab("Randomization", Randomization));
        tabs.add(new ConfigTab("Categories", Categories));
        Instance = new TRConfigs("Texture Rotation", null, Total);
    }
}