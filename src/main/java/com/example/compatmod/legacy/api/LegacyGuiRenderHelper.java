package com.example.compatmod.legacy.api;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Optional;

public class LegacyGuiRenderHelper {

    public static void drawText(GuiGraphics guiGraphics, Font font,String text, int x, int y, int color) {
        guiGraphics.drawString(font, text, x, y, color, false);
    }

    public static void drawCenteredText(GuiGraphics guiGraphics, Font font, String text, int centerX, int y, int color) {
        int width = font.width(text);
        guiGraphics.drawString(font, text, centerX - width / 2, y, color, false);
    }

    public static void drawTooltip(GuiGraphics guiGraphics, Font font, List<Component> tooltip, int x, int y) {
        guiGraphics.renderTooltip(font, tooltip, Optional.empty(), x, y);
    }
}
