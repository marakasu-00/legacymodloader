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
        super.renderWidget(graphics, mouseX, mouseY, partialTick); // チェック枠 + ON/OFF描画込み

        // ラベル描画のみ追加
        int labelX = getX() + 14; // チェックボックスの右
        int labelY = getY() + (height - 8) / 2;
        graphics.drawString(Minecraft.getInstance().font, label, labelX, labelY, 0xFFFFFF, false);

        // ✅ 強制チェックマーク（仮）
        if (selected()) {
            graphics.fill(getX() + 4, getY() + 4, getX() + 10, getY() + 10, 0xFFFFFFFF); // 白い四角
        }
    }
}