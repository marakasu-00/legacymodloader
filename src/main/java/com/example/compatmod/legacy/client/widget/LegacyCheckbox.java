package com.example.compatmod.legacy.client.widget;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.example.compatmod.config.ConfigHandler;
import com.example.compatmod.config.SafeConfigManager;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
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
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int boxX = getX();
        int boxY = getY();

        // 外枠
        graphics.fill(boxX, boxY, boxX + 12, boxY + 12, 0xFFAAAAAA);

        // チェックON時
        if (selected()) {
            graphics.fill(boxX + 2, boxY + 2, boxX + 10, boxY + 10, 0xFFFFFFFF);
        }

        graphics.drawString(Minecraft.getInstance().font, "Enabled", boxX + 14, boxY + 2, 0xFFFFFF, false);
    }



}
