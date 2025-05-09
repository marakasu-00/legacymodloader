package com.example.compatmod.legacy.widget;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

public class LegacySlider extends AbstractSliderButton {

    public LegacySlider(int x, int y, int width, int height, double initialValue) {
        super(x, y, width, height, Component.literal("Value: " + (int)(initialValue * 100)), initialValue);
    }

    @Override
    protected void updateMessage() {
        setMessage(Component.literal(String.format("Brightness: %.1f", getValue())));
    }

    @Override
    protected void applyValue() {
        System.out.println("[LegacyExample] Slider value changed: " + value);
    }
    public void tick() {
        // 必要ならアニメーションや値更新などの処理を書く
    }
    public double getValue() {
        return this.value;
    }

}
