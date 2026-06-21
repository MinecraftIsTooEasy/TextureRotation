package vbonedra.texture_rotation;

import fi.dy.masa.malilib.config.ConfigManager;
import vbonedra.texture_rotation.event.TextureRotationEvent;
import net.fabricmc.api.ModInitializer;
import net.xiaoyu233.fml.ModResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static vbonedra.texture_rotation.TRConfigs.reinitializeBlockMap;

public class TextureRotationMod implements ModInitializer {
    public static final String MOD_ID = "texture_rotation";
    public static final String MOD_NAME = "TextureRotation";
    public static final String RESOURCE_ID = MOD_ID + ":";

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    @Override
    public void onInitialize() {
//        ModResourceManager.addResourcePackDomain(MOD_ID);
//        ModResourceManager.addResourcePackDomain("MITE");
        ModResourceManager.addResourcePackDomain("minecraft");


        TRConfigs.getInstance().load();
        ConfigManager.getInstance().registerConfig(TRConfigs.getInstance());

        reinitializeBlockMap();

        @SuppressWarnings("rawtypes")
        fi.dy.masa.malilib.config.interfaces.IValueChangeCallback configAndChunkReloader = config -> {
            reinitializeBlockMap();
            TRConfigs.getInstance().save();

            net.minecraft.Minecraft mc = net.minecraft.Minecraft.getMinecraft();
            if (mc != null && mc.theWorld != null && mc.renderGlobal != null) {
                mc.renderGlobal.loadRenderers();
            }
        };

        TRConfigs.RandomizeTextures.setValueChangeCallback(configAndChunkReloader);
        TRConfigs.FlipX.setValueChangeCallback(configAndChunkReloader);
        TRConfigs.FlipY.setValueChangeCallback(configAndChunkReloader);
        TRConfigs.Rotate90Degree.setValueChangeCallback(configAndChunkReloader);
        TRConfigs.UseBlockIdToRandomize.setValueChangeCallback(configAndChunkReloader);

        TRConfigs.RandomizeSandy.setValueChangeCallback(configAndChunkReloader);
        TRConfigs.RandomizeGrassy.setValueChangeCallback(configAndChunkReloader);
        TRConfigs.RandomizeStony.setValueChangeCallback(configAndChunkReloader);
        TRConfigs.RandomizePillary.setValueChangeCallback(configAndChunkReloader);

        for (fi.dy.masa.malilib.config.options.ConfigBase<?> blockConfig : TRConfigs.BlocksListOptions) {
            blockConfig.setValueChangeCallback(configAndChunkReloader);
        }

        TextureRotationEvent.register();
    }
}
