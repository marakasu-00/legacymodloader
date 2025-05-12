package com.example.compatmod.legacy.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

public class LegacySlider extends AbstractSliderButton {

    public LegacySlider(int x, int y, int width, int height, double value) {
        super(x, y, width, height, Component.empty(), value);
        this.active = true;
        this.visible = true;
    }

    @Override
    protected void updateMessage() {
        setMessage(Component.literal(String.format("Brightness: %.0f%%", this.value * 100)));
    }

    @Override
    public void applyValue() {
        System.out.println("[LegacySlider] applyValue called, value: " + this.value);
        updateMessage();
    }

    public void tick() {
        // スライダーがアニメーションを持つならここで更新
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    public double getValue() {
        return this.value;
    }
}
