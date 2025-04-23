package com.example.compatmod.legacy.api;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class LegacyTextHelper {

    public static Component text(String message) {
        return Component.literal(message);
    }

    public static Component translate(String key) {
        return Component.translatable(key);
    }

    public static Component color(Component base, ChatFormatting color) {
        return base.copy().withStyle(style -> style.withColor(color));
    }

    public static Component append(Component... components) {
        MutableComponent combined = Component.empty();
        for (Component c : components) {
            combined.append(c);
        }
        return combined;
    }
}
