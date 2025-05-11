package com.example.compatmod.legacy.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

import java.util.function.DoubleConsumer;

public class LegacySlider extends AbstractSliderButton {

    public LegacySlider(int x, int y, int width, int height, double value) {
        super(x, y, width, height, Component.empty(), value);
        updateMessage();
    }

    @Override
    protected void updateMessage() {
        setMessage(Component.literal(String.format("Brightness: %.1f%%", this.value * 100)));
    }

    @Override
    protected void applyValue() {
        System.out.println("[LegacySlider] value = " + this.value);
    }
}
