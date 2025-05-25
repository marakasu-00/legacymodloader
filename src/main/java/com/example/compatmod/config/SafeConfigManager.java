package com.example.compatmod.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.io.IOException;

public class SafeConfigManager {

    public static void setSlider(double value) {
        if (value >= 0.0 && value <= 1.0) {
            try {
            } catch (Exception e) {
                System.err.println("[SafeConfigManager] Failed to save slider value: " + e.getMessage());
            }
        } else {
            System.out.println("[SafeConfigManager] Invalid slider value: " + value);
        }
    }

    public static void setCheckboxEnabled(boolean enabled) {
        try {

        } catch (Exception e) {
            System.err.println("[SafeConfigManager] Failed to save checkbox: " + e.getMessage());
        }
    }

    public static void setCheckbox(boolean value) {
        setCheckboxEnabled(value); // 統一処理
    }

    public static void setText(String text) {
        if (text == null) text = "";
        try {

        } catch (Exception e) {
            System.err.println("[SafeConfigManager] Failed to save text: " + e.getMessage());
        }
    }

    public static void saveConfigSafe() {
        try {
        } catch (Exception e) {
            System.err.println("[SafeConfigManager] Failed to save config: " + e.getMessage());
        }
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