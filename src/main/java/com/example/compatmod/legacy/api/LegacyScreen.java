package com.example.compatmod.legacy.api;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class LegacyScreen extends Screen {

    protected LegacyScreen(Component title) {
        super(title);
    }

    // クリック互換
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return onMouseClicked((int) mouseX, (int) mouseY, button) || super.mouseClicked(mouseX, mouseY, button);
    }

    protected boolean onMouseClicked(int mouseX, int mouseY, int button) {
        return false;
    }

    // マウス離した時
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return onMouseReleased((int) mouseX, (int) mouseY, button) || super.mouseReleased(mouseX, mouseY, button);
    }

    protected boolean onMouseReleased(int mouseX, int mouseY, int button) {
        return false;
    }

    // キー入力互換
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return onKeyPressed(keyCode, scanCode, modifiers) || super.keyPressed(keyCode, scanCode, modifiers);
    }

    protected boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    // 文字入力
    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return onCharTyped(codePoint, modifiers) || super.charTyped(codePoint, modifiers);
    }

    protected boolean onCharTyped(char codePoint, int modifiers) {
        return false;
    }
}
