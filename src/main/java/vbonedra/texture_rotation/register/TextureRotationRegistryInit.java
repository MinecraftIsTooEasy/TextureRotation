package vbonedra.texture_rotation.register;

import net.minecraft.Icon;
import java.util.HashMap;
import java.util.Map;

public class TextureRotationRegistryInit {
    // Хранит связь: Ванильная_Иконка -> Массив из 8 кастомных повернутых/отзеркаленных иконок
    private static final Map<Icon, Icon[]> textureCache = new HashMap<>();

    public static void registerVariants(Icon baseIcon, Icon[] variants) {
        if (baseIcon != null && variants != null && variants.length == 8) {
            textureCache.put(baseIcon, variants);
        }
    }

    public static Icon getVariant(Icon baseIcon, int rotation, boolean flipped) {
        if (baseIcon == null) return null;
        Icon[] variants = textureCache.get(baseIcon);
        if (variants == null) return baseIcon; // Если кастомных вариантов нет, возвращаем оригинал

        int index = rotation % 4;
        if (flipped) {
            index += 4; // Переходим во вторую половину массива (где текстуры с флипом)
        }
        return variants[index];
    }
}
