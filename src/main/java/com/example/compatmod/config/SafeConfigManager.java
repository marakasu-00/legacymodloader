package com.example.compatmod.config;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.ForgeConfigSpec;
import org.slf4j.Logger;

public class SafeConfigManager {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static void setSlider(double value) {
        if (value >= 0.0 && value <= 1.0) {
            try {
                ConfigHandler.SLIDER_VALUE.set(value);
                saveConfigSafe();
            } catch (Exception e) {
                LOGGER.error("[SafeConfigManager] Failed to save slider value: {}", e.getMessage());
            }
        } else {
            LOGGER.warn("[SafeConfigManager] Invalid slider value: {}", value);
        }
    }

    public static void setCheckboxEnabled(boolean enabled) {
        try {
            ConfigHandler.CHECKBOX_ENABLED.set(enabled);
            saveConfigSafe();
        } catch (Exception e) {
            LOGGER.error("[SafeConfigManager] Failed to save checkbox: {}", e.getMessage());
        }
    }

    public static void setCheckbox(boolean value) {
        setCheckboxEnabled(value); // 統一処理
    }

    public static void setText(String text) {
        if (text == null) text = "";
        try {
            LOGGER.info("[SafeConfigManager] setText called: {}", text);
            ConfigHandler.SAVED_TEXT.set(text); // ✅ textそのものを保存しているか確認
            saveConfigSafe();
        } catch (Exception e) {
            LOGGER.error("[SafeConfigManager] Failed to save text: {}", e.getMessage());
        }
    }

    public static void saveConfigSafe() {
        try {
            ConfigHandler.COMMON_CONFIG.save();
        } catch (Exception e) {
            LOGGER.error("[SafeConfigManager] Failed to save config: {}", e.getMessage());
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
