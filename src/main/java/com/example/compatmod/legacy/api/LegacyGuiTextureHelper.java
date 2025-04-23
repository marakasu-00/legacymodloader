package com.example.compatmod.legacy.api;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class LegacyGuiTextureHelper {

    public static void drawTexture(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int u, int v, int width, int height) {
        guiGraphics.blit(texture, x, y, u, v, width, height);
    }
}
