package com.example.codex.client;

import com.example.codex.CodexMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.client.gui.GuiGraphics;

public class CodexScreen extends AbstractContainerScreen<CodexMenu> {
    public CodexScreen(CodexMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int x, int y) {
        // 背景描画しない例（透過）
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }
}
