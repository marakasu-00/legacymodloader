package com.example.compatmod.legacy.widget;

import com.example.compatmod.config.SafeConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import com.example.compatmod.config.SafeConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

import java.util.function.DoubleConsumer;

public class LegacySlider extends AbstractSliderButton {
    private final String labelPrefix;
    private final double min;
    private final double max;
    private DoubleConsumer responder;

    public LegacySlider(int x, int y, int width, int height, double min, double max, double initial, String labelPrefix) {
        super(x, y, width, height, Component.literal(""), (initial - min) / (max - min));
        this.min = min;
        this.max = max;
        this.labelPrefix = labelPrefix;
    }

    @Override
    protected void updateMessage() {
        //updateLabel();
    }

    private void updateLabel() {
        int percent = (int) (getValue() * 100);
        this.setMessage(Component.literal(labelPrefix + ": " + percent + "%"));
    }

    @Override
    protected void applyValue() {
        double actual = getValue();
        SafeConfigManager.setSlider(actual);
        if (responder != null) responder.accept(actual);
    }

    public void setResponder(DoubleConsumer responder) {
        this.responder = responder;
    }

    public double getValue() {
        return min + value * (max - min);
    }

    public void tick() {
        // 必要に応じて毎tickの処理を書く
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // スライダー本体描画（ラベルなし）
        super.renderWidget(graphics, mouseX, mouseY, partialTick);

        // 手動ラベル描画：中央に
        String label = labelPrefix + ": " + (int)(getValue() * 100) + "%";
        int labelWidth = Minecraft.getInstance().font.width(label);
        int labelX = getX() + (width - labelWidth) / 2;
        int labelY = getY() + (height - 8) / 2;

        graphics.drawString(
                Minecraft.getInstance().font,
                label,
                labelX,
                labelY,
                0xFFFFFF,
                false
        );
    }
}
