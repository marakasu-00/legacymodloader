package com.example.compatmod.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigHandler {

    public static final ForgeConfigSpec COMMON_CONFIG;
    public static final ForgeConfigSpec.DoubleValue SLIDER_VALUE;
    public static final ForgeConfigSpec.BooleanValue CHECKBOX_ENABLED;
    public static final ForgeConfigSpec.ConfigValue<String> SAVED_TEXT;

    private static ModConfig currentConfig;
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onConfigLoad(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == COMMON_CONFIG) {
            currentConfig = event.getConfig(); // 正しく currentConfig を設定
        }
    }

    public static void saveConfigSafe() {
        if (currentConfig != null) {
            currentConfig.save();
            LOGGER.info("[ConfigHandler] Config saved via ModConfig.");
        } else {
            LOGGER.error("[ConfigHandler] ModConfig not bound!");
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


    @SuppressWarnings("removal")
    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG, "compatmod-common.toml");
    }

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
