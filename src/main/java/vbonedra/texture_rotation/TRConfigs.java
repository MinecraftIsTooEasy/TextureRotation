package vbonedra.texture_rotation;

import fi.dy.masa.malilib.config.ConfigTab;
import fi.dy.masa.malilib.config.SimpleConfigs;
import fi.dy.masa.malilib.config.options.*;

import java.util.ArrayList;
import java.util.List;

public class TRConfigs extends SimpleConfigs {
    public static final ConfigBoolean RotateTextures = new ConfigBoolean("Rotate Textures (needs render update)", true);

    private static final TRConfigs Instance;
    public static final List<ConfigBase<?>> MainBase;
    public static final List<ConfigBase<?>> Total;
    public static final List<ConfigTab> tabs;

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
        MainBase = List.of(
                RotateTextures
        );
        Total.addAll(MainBase);
        tabs.add(new ConfigTab("Texture Rotation", MainBase));
        Instance = new TRConfigs("Texture Rotation", null, Total);
    }
}