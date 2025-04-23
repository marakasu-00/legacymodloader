package com.example.compatmod.legacy.api;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class LegacyGuiHelper {

    public static void drawTexturedModalRect(GuiGraphics graphics, ResourceLocation texture,
                                             int x, int y, int u, int v, int width, int height) {
        graphics.blit(texture, x, y, u, v, width, height);
    }

    public static void drawCenteredString(GuiGraphics graphics, Font font,
                                          Component text, int x, int y, int color) {
        int width = font.width(text);
        graphics.drawString(font, text, x - width / 2, y, color, false);
    }

    public static void drawHoveringText(GuiGraphics graphics, Font font,
                                        List<Component> lines, int mouseX, int mouseY) {
        List<FormattedCharSequence> tooltipLines = lines.stream()
                .map(Component::getVisualOrderText)
                .toList();
        graphics.renderTooltip(font, tooltipLines, mouseX, mouseY);
    }

    public static void drawGradientRect(GuiGraphics graphics,
                                        int x1, int y1, int x2, int y2, int colorTop, int colorBottom) {
        graphics.fillGradient(x1, y1, x2, y2, colorTop, colorBottom);
    }
}
