package com.example.compatmod.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent.Loading;

// ConfigHandler.java

@Mod.EventBusSubscriber(modid = "legacymodloader", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigHandler {

    public static final ForgeConfigSpec COMMON_CONFIG;
    public static final ForgeConfigSpec.DoubleValue SLIDER_VALUE;
    public static final ForgeConfigSpec.BooleanValue CHECKBOX_ENABLED;
    public static final ForgeConfigSpec.ConfigValue<String> SAVED_TEXT;

    public static double sliderValue = 0.5;
    public static boolean checkboxEnabled = false;
    public static String savedText = "";

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        SLIDER_VALUE = builder.comment("Slider value").defineInRange("sliderValue", 0.5, 0.0, 1.0);
        CHECKBOX_ENABLED = builder.comment("Checkbox enabled").define("checkboxEnabled", false);
        SAVED_TEXT = builder.comment("Saved text").define("savedText", "");

        COMMON_CONFIG = builder.build();
    }

    @SubscribeEvent
    public static void onConfigLoaded(final Loading event) {
        if (event.getConfig().getSpec() == COMMON_CONFIG) {
            // 安全に読み取ってキャッシュ
            try {
                sliderValue = SLIDER_VALUE.get();
                checkboxEnabled = CHECKBOX_ENABLED.get();
                savedText = SAVED_TEXT.get();
            } catch (IllegalStateException e) {
                // 開発環境用の例外処理
                sliderValue = 0.5;
                checkboxEnabled = false;
                savedText = "";
            }
        }
    }


    // セーフな Getter
    public static double getSliderValueSafe() {
        return safeGet(SLIDER_VALUE, 0.5);
    }

    public static boolean getCheckboxSafe() {
        return safeGet(CHECKBOX_ENABLED, false);
    }

    public static String getSavedTextSafe() {
        return safeGet(SAVED_TEXT, "");
    }

    private static <T> T safeGet(ForgeConfigSpec.ConfigValue<T> value, T defaultValue) {
        try {
            return value.get();
        } catch (IllegalStateException e) {
            return defaultValue;
        }
    }

    // セッター（必要なときに保存も可能）
    public static void setSliderValue(double value) {
        SLIDER_VALUE.set(value);
    }

    public static void setCheckboxEnabled(boolean enabled) {
        CHECKBOX_ENABLED.set(enabled);
    }

    public static void setSavedText(String text) {
        SAVED_TEXT.set(text);
    }

    public static void saveConfig() {
        COMMON_CONFIG.save();
    }
}