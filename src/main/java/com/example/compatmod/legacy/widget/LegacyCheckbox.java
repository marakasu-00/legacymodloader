package com.example.compatmod.legacy.widget;

import com.example.compatmod.config.ConfigHandler;
import com.example.compatmod.config.SafeConfigManager;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class LegacyCheckbox extends Checkbox {

    private final String label;
    private BooleanConsumer responder;

    public LegacyCheckbox(int x, int y, int width, int height, Component message, boolean selected) {
        super(x, y, width, height, Component.empty(), selected); // ← ラベルは空
        this.label = message.getString(); // 自前で描く用
    }

    public void setResponder(BooleanConsumer responder) {
        this.responder = responder;
    }

    @Override
    public void onPress() {
        super.onPress();
        System.out.println("Checkbox toggled: " + selected());
        if (responder != null) {
            responder.accept(this.selected());
        }
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // 描画エリア（外枠）
        int boxX = getX();
        int boxY = getY();
        System.out.println("Rendering Checkbox: selected = " + selected());
        graphics.fill(boxX, boxY, boxX + 12, boxY + 12, 0xFFAAAAAA);  // 灰色の四角

        // チェックマーク描画
        if (selected()) {
            graphics.fill(boxX + 2, boxY + 2, boxX + 10, boxY + 10, 0xFFFFFFFF);  // ✔ 白い中身でON表示
        }

        // ラベル
        int labelX = boxX + 14;
        int labelY = boxY + 2;
        graphics.drawString(Minecraft.getInstance().font, label, labelX, labelY, 0xFFFFFF, false);
    }

}
