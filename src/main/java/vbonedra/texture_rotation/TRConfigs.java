package vbonedra.texture_rotation;

import fi.dy.masa.malilib.config.ConfigTab;
import fi.dy.masa.malilib.config.SimpleConfigs;
import fi.dy.masa.malilib.config.options.*;
import net.minecraft.Block;
import net.minecraft.StringTranslate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TRConfigs extends SimpleConfigs {
    public static final ConfigBoolean RandomizeTextures = new ConfigBoolean("Randomize Textures",   true, "Turns on/off mod");
    public static final ConfigBoolean FlipX             = new ConfigBoolean("Flip Horizontal",      true, "Flips textures horizontally (left-right)");
    public static final ConfigBoolean FlipY             = new ConfigBoolean("Flip Vertical",        true, "Flips textures vertically (top-bottom)");
    public static final ConfigBoolean Rotate90Degree    = new ConfigBoolean("Rotate 90 Degree",     true, "Rotates textures 90 degree");

    public static final ConfigBoolean RandomizeSandy    = new ConfigBoolean("Randomize Sand-like Blocks",   true, "Master switch for Sandy category");
    public static final ConfigBoolean RandomizeGrassy   = new ConfigBoolean("Randomize Grass-like Blocks",  true, "Master switch for Grassy category");
    public static final ConfigBoolean RandomizeStony    = new ConfigBoolean("Randomize Stone-like Blocks",  true, "Master switch for Stony category");
    public static final ConfigBoolean RandomizePillary  = new ConfigBoolean("Randomize Pillar-like Blocks", true, "Master switch for Pillary category");

    private static final TRConfigs Instance;
    public static final List<ConfigBase<?>> Randomization;
    public static final List<ConfigBase<?>> BlocksListOptions = new ArrayList<>();
    public static final List<List<ConfigBase<?>>> BlocksListTabs = new ArrayList<>();
    public static final List<ConfigBase<?>> Total = new ArrayList<>();
    public static final List<ConfigTab> tabs = new ArrayList<>();

    private static final Map<ConfigInteger, Integer> configToBlockIdMap = new HashMap<>();

    public static final byte[] BLOCK_RANDOMIZATION_TYPE = new byte[4096];

    public static void reinitializeBlockMap() {
        java.util.Arrays.fill(BLOCK_RANDOMIZATION_TYPE, (byte) 0);
        if (!RandomizeTextures.getBooleanValue()) return;
        for (Map.Entry<ConfigInteger, Integer> entry : configToBlockIdMap.entrySet()) {
            ConfigInteger config = entry.getKey();
            int blockID = entry.getValue();
            byte typeValue = (byte) config.getIntegerValue();
            BLOCK_RANDOMIZATION_TYPE[blockID] = typeValue;
        }
    }

    private static void registerBlockConfig(Block block, int defaultType) {
        if (block == null) return;
        String finalConfigName = block.getUnlocalizedName() + " (ID: " + block.blockID + ")";
        ConfigInteger blockConfig = new ConfigInteger(finalConfigName, defaultType, 0, 4, true, "0 = Off, 1 = Sandy, 2 = Stony, 3 = Grassy, 4 = Pillary");
        BlocksListOptions.add(blockConfig);
        configToBlockIdMap.put(blockConfig, block.blockID);
    }

    public TRConfigs(String name, List<ConfigHotkey> hotkeys, List<ConfigBase<?>> values) {
        super(name, hotkeys, values);
    }

    @Override
    public List<ConfigTab> getConfigTabs() { return tabs; }
    public static TRConfigs getInstance() { return Instance; }

    static {
        Randomization = List.of(
                RandomizeTextures,
                FlipX,
                FlipY,
                Rotate90Degree,
                RandomizeSandy,
                RandomizeGrassy,
                RandomizeStony,
                RandomizePillary
        );
        for (Block block : Block.blocksList) {
            if (block == null) continue;
            int id = block.blockID;
            if (block instanceof net.minecraft.BlockFluid // maybe there are some ways to optimize
                    || block instanceof net.minecraft.BlockMounted
                    || block instanceof net.minecraft.BlockDispenser
                    || block instanceof net.minecraft.BlockBed
                    || block instanceof net.minecraft.BlockPistonBase
                    || block instanceof net.minecraft.BlockPistonExtension
                    || block instanceof net.minecraft.BlockPistonMoving
                    || block instanceof net.minecraft.BlockSlab
                    || block instanceof net.minecraft.BlockDoubleSlab
                    || block instanceof net.minecraft.BlockTNT
                    || block instanceof net.minecraft.BlockFire
                    || block instanceof net.minecraft.BlockChest
                    || block instanceof net.minecraft.BlockRedstoneWire
                    || block instanceof net.minecraft.BlockJukeBox
                    || block instanceof net.minecraft.BlockPortal
                    || block instanceof net.minecraft.BlockRedstoneRepeater
                    || block instanceof net.minecraft.BlockHopper
                    || block instanceof net.minecraft.BlockVine
                    || block instanceof net.minecraft.BlockCauldron
                    || block instanceof net.minecraft.BlockBeacon
                    || block instanceof net.minecraft.BlockComparator
                    || block instanceof net.minecraft.BlockDaylightDetector
                    || block instanceof net.minecraft.BlockAnvil
                    || block instanceof net.minecraft.BlockPlant
            ) continue;
            if (id == Block.dirt.blockID
                    || id == Block.sand.blockID
                    || id == Block.gravel.blockID
                    || id == Block.slowSand.blockID
                    || id == Block.blockClay.blockID
                    || id == Block.hardenedClay.blockID
                    || id == Block.stainedClay.blockID
                    || id == Block.blockSnow.blockID
                    || id == Block.netherrack.blockID
                    || id == Block.oreNetherQuartz.blockID
                    || id == Block.sponge.blockID
                    || id == Block.glowStone.blockID
                    || id == Block.whiteStone.blockID
                    || id == Block.cobblestone.blockID
                    || id == Block.cobblestoneMossy.blockID
            ) registerBlockConfig(block, 1);
            else if (id == Block.stone.blockID
                    || id == Block.bedrock.blockID
                    || id == Block.mantleOrCore.blockID
                    || id == Block.oreGold.blockID
                    || id == Block.oreIron.blockID
                    || id == Block.oreCoal.blockID
                    || id == Block.oreLapis.blockID
                    || id == Block.oreDiamond.blockID
                    || id == Block.oreRedstone.blockID
                    || id == Block.oreRedstoneGlowing.blockID
                    || id == Block.oreEmerald.blockID
                    || id == Block.oreCopper.blockID
                    || id == Block.oreSilver.blockID
                    || id == Block.oreMithril.blockID
                    || id == Block.oreAdamantium.blockID
                    || id == Block.cloth.blockID
                    || id == Block.leaves.blockID
            ) registerBlockConfig(block, 2);
            else if (id == Block.grass.blockID
                    || id == Block.mycelium.blockID
                    || id == Block.melon.blockID
                    || id == Block.pumpkin.blockID
                    || id == Block.pumpkinLantern.blockID
            ) registerBlockConfig(block, 3);
            else if (id == Block.wood.blockID
                    || id == Block.blockNetherQuartz.blockID
                    || id == Block.hay.blockID
            ) registerBlockConfig(block, 4);
            else registerBlockConfig(block, 0);
        }

        Total.addAll(Randomization);
        Total.addAll(BlocksListOptions);

        tabs.add(new ConfigTab("General", Randomization));
        tabs.add(new ConfigTab("Blocks Customization", BlocksListOptions));

        Instance = new TRConfigs("Texture Rotation", null, Total);
    }
}