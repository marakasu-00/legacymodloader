package com.example.compatmod.legacy.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class LegacyEditBox extends EditBox {

    private String savedText = "";
    private int savedCursor = 0;

    public LegacyEditBox(int x, int y, int width, int height) {
        super(Minecraft.getInstance().font, x, y, width, height, Component.literal("LegacyEditBox"));
    }

    /** 入力とカーソルの状態を保存 */
    public void saveState() {
        this.savedText = this.getValue();
        this.savedCursor = this.getCursorPosition();
    }

    /** 保存していた内容とカーソル位置を復元 */
    public void restoreState() {
        this.setValue(savedText);
        this.setCursorPosition(savedCursor);
    }
}