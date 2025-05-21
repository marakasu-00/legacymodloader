package com.example.compatmod.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.io.IOException;

public class SafeConfigManager {

    public static void setSlider(double value) {
        if (value >= 0.0 && value <= 1.0) {
            ConfigHandler.SLIDER_VALUE.set(value);
        } else {
            System.out.println("[SafeConfigManager] Invalid slider value: " + value);
        }
    }

    public static void setCheckboxEnabled(boolean enabled) {
        ConfigHandler.CHECKBOX_ENABLED.set(enabled);
    }

    public static void setCheckbox(boolean value) {
        ConfigHandler.CHECKBOX_ENABLED.set(value);

        ConfigHandler.saveConfigSafe();
    }

    public static void setText(String text) {
        if (text == null) {
            text = "";
        }
        ConfigHandler.SAVED_TEXT.set(text);
    }

    public static void saveConfigSafe() {
        ConfigHandler.COMMON_CONFIG.save();
    }

    public static double getSlider() {
        return ConfigHandler.getSliderValueSafe();
    }

    public static boolean getCheckbox() {
        return ConfigHandler.getCheckboxSafe();
    }

    public static String getText() {
        return ConfigHandler.getSavedTextSafe();
    }
}