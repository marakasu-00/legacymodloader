package com.example.compatmod.legacy.screen;

import com.example.compatmod.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class LegacyConfigScreen extends Screen {

    public LegacyConfigScreen() {
        super(Component.literal("Legacy Config"));
    }

    @Override
    protected void init() {
        // スライダー・チェックボックスのGUI追加処理を書く
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(null); // ← メニューへ戻る
    }
}
