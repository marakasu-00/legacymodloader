package com.example.compatmod.legacy.api;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public class LegacyTextField extends EditBox {

    public LegacyTextField(Font font, int x, int y, int width, int height) {
        super(font, x, y, width, height, Component.empty());
    }

    public void setText(String text) {
        super.setValue(text);
    }

    public String getText() {
        return super.getValue();
    }

    public void setFocused(boolean focused) {
        super.setFocused(focused);
    }
}
