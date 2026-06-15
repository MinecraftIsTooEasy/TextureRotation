package vbonedra.texture_rotation;

import fi.dy.masa.malilib.config.ConfigManager;
import vbonedra.texture_rotation.event.TextureRotationEvent;
import net.fabricmc.api.ModInitializer;

import net.xiaoyu233.fml.ModResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TextureRotationMod implements ModInitializer {
    public static final String MOD_ID = "texture_rotation";
    public static final String MOD_NAME = "TextureRotation";
    public static final String RESOURCE_ID = MOD_ID+":";

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    @Override
    public void onInitialize() {
        ModResourceManager.addResourcePackDomain(MOD_ID);

        TRConfigs.getInstance().load();
        ConfigManager.getInstance().registerConfig(TRConfigs.getInstance());

        TRConfigs.RotateTextures.setValueChangeCallback(config -> {
            net.minecraft.Minecraft mc = net.minecraft.Minecraft.getMinecraft();
            if (mc != null && mc.theWorld != null && mc.renderGlobal != null) {
                mc.renderGlobal.loadRenderers();
            }
        });

        TextureRotationEvent.register();
    }
}
