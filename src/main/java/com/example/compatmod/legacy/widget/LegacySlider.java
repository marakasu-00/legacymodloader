package com.example.compatmod.legacy.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

public class LegacySlider extends AbstractSliderButton {

    private boolean dragging = false;

    public LegacySlider(int x, int y, int width, int height, double value) {
        super(x, y, width, height, Component.empty(), value);
        this.active = true;
        this.visible = true;
        updateMessage();
    }

    @Override
    protected void updateMessage() {
        setMessage(Component.literal(String.format("Brightness: %.0f%%", this.value * 100)));
    }

    @Override
    protected void applyValue() {
        System.out.println("[LegacySlider] applyValue called, value: " + this.value);
        updateMessage();
    }

    public void tick() {
        // スライダーがアニメーションを持つならここで更新
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.active && this.visible && this.isHovered()) {
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
        return false;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.isHovered = this.isMouseOver(mouseX, mouseY);

        // 背景を明示的に塗りつぶす（文字のゴースト防止）
        graphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0xFF000000);

        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    public double getValue() {
        return this.value;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isMouseOver(mouseX, mouseY)) {
            this.dragging = true;
            return super.mouseClicked(mouseX, mouseY, button);
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.dragging) {
            this.dragging = false;
            return super.mouseReleased(mouseX, mouseY, button);
        }
        return false;
    }
}
