package com.example.compatmod.config;

import net.minecraftforge.fml.config.ModConfig;

public class SafeConfigManager {

    private static ModConfig currentConfig;

    public static void setSlider(double value) {
        if (value >= 0.0 && value <= 1.0) {
            try {
                ConfigHandler.SLIDER_VALUE.set(value);
                saveConfigSafe();
            } catch (Exception e) {
                System.err.println("[SafeConfigManager] Failed to save slider value: " + e.getMessage());
            }
        } else {
            System.out.println("[SafeConfigManager] Invalid slider value: " + value);
        }
    }

    public static void setCheckboxEnabled(boolean enabled) {
        try {
            ConfigHandler.CHECKBOX_ENABLED.set(enabled);
            saveConfigSafe();
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
            System.out.println("[SafeConfigManager] setText called: " + text);
            ConfigHandler.SAVED_TEXT.set(text); // ✅ textそのものを保存しているか確認
            saveConfigSafe();
        } catch (Exception e) {
            System.err.println("[SafeConfigManager] Failed to save text: " + e.getMessage());
        }
    }

    public static void saveConfigSafe() {
        try {
            ConfigHandler.COMMON_CONFIG.save();
        } catch (Exception e) {
            System.err.println("[SafeConfigManager] Failed to save config: " + e.getMessage());
        }
    }

    public static void save() {
        if (currentConfig == null) {
            // Configが未割り当てならエラーログを残すだけでスキップ
            System.err.println("[SafeConfigManager] No config assigned; skipping save.");
            return;
        }

        try {
            currentConfig.save();
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
