package com.example.compatmod.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigHandler {

    // === スペックと各値 ===
    public static final ForgeConfigSpec COMMON_CONFIG;
    public static final ForgeConfigSpec.DoubleValue SLIDER_VALUE;
    public static final ForgeConfigSpec.BooleanValue CHECKBOX_ENABLED;
    public static final ForgeConfigSpec.ConfigValue<String> SAVED_TEXT;
    private static ModConfig activeConfig;

    public static void bindConfig(ModConfig config) {
        activeConfig = config;
    }

        public static ModConfig currentConfig; // 保存用

        @SubscribeEvent
        public static void onConfigLoad(ModConfigEvent.Loading event) {
            if (event.getConfig().getSpec() == COMMON_CONFIG) {
                currentConfig = event.getConfig();
            }
        }

    public static void saveConfigSafe() {
        try {
            //COMMON_CONFIG.save();
        } catch (Exception e) {
            System.err.println("[ConfigHandler] Failed to save config: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void saveConfig() {
        if (activeConfig != null) {
            //activeConfig.save();
        } else {
            System.err.println("Config not yet bound to ModConfig, cannot save.");
        }
    }

    public static void setSliderValue(double val) {
        SLIDER_VALUE.set(val);
    }

    public static void setCheckboxEnabled(boolean b) {
        CHECKBOX_ENABLED.set(b);
    }

    public static void setSavedText(String text) {
        SAVED_TEXT.set(text);
    }

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        SLIDER_VALUE = builder.comment("Slider value")
                .defineInRange("sliderValue", 0.5, 0.0, 1.0);

        CHECKBOX_ENABLED = builder.comment("Checkbox enabled")
                .define("checkboxEnabled", false);

        SAVED_TEXT = builder.comment("Saved text")
                .define("savedText", "");

        COMMON_CONFIG = builder.build();
    }

    // === 登録メソッド：1回だけ呼び出す ===
    @SuppressWarnings("removal")
    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG, "compatmod-common.toml");
    }

    // === 安全な取得メソッド ===
    public static double getSliderValueSafe() {
        try {
            return SLIDER_VALUE.get();
        } catch (IllegalStateException e) {
            return 0.5;
        }
    }

    public static boolean getCheckboxSafe() {
        try {
            return CHECKBOX_ENABLED.get();
        } catch (IllegalStateException e) {
            return false;
        }
    }

    public static String getSavedTextSafe() {
        try {
            return SAVED_TEXT.get();
        } catch (IllegalStateException e) {
            return "";
        }
    }
}
