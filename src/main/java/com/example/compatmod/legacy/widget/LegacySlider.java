package com.example.compatmod.legacy.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

import java.util.function.DoubleConsumer;

public class LegacySlider extends AbstractSliderButton {

    private boolean dragging = false;
    private DoubleConsumer responder = val -> {}; // デフォルトの空実装を追加

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
        responder.accept(this.value);
        updateMessage();
        System.out.println("[LegacySlider] applyValue called, value: " + this.value);
    }

    public void tick() {
        // スライダーがアニメーションを持つならここで更新
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        // ホバー状態を使わず常にドラッグ可能（通常の挙動）
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        //updateMessage(); // 毎フレーム文字更新（重なり防止）
        this.isHovered = this.isMouseOver(mouseX, mouseY);
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
        boolean result = super.mouseReleased(mouseX, mouseY, button);
        applyValue(); // 明示的に呼ぶ（念のため）
        return result;
    }

    public void setValue(double value) {
        this.value = value;
        updateMessage();
    }

    public void setResponder(DoubleConsumer responder) {
        this.responder = responder;
    }
}
