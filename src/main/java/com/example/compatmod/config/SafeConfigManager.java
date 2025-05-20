package com.example.compatmod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SafeConfigManager {
    private static double tempSliderValue;
    private static boolean tempCheckboxEnabled;
    private static String tempSavedText;

    public static void load() {
        tempSliderValue = ConfigHandler.getSliderValueSafe();
        tempCheckboxEnabled = ConfigHandler.getCheckboxSafe();
        tempSavedText = ConfigHandler.getSavedTextSafe();
    }

    public static void setSlider(double value) {
        tempSliderValue = value;
    }

    public static void setCheckbox(boolean enabled) {
        tempCheckboxEnabled = enabled;
    }

    public static void setText(String text) {
        tempSavedText = text;
    }

    public static double getSlider() {
        return tempSliderValue;
    }

    public static boolean getCheckbox() {
        return tempCheckboxEnabled;
    }

    public static String getText() {
        return tempSavedText;
    }

    public static void apply() {
        ConfigHandler.SLIDER_VALUE.set(tempSliderValue);
        ConfigHandler.CHECKBOX_ENABLED.set(tempCheckboxEnabled);
        ConfigHandler.SAVED_TEXT.set(tempSavedText);
        ConfigHandler.saveConfigSafe();
    }
}