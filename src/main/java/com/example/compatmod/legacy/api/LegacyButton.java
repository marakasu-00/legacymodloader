package com.example.compatmod.legacy.api;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class LegacyButton extends Button {
    private final int id;

    public LegacyButton(int id, int x, int y, int width, int height, String text, OnPress onPress) {
        super(x, y, width, height, Component.literal(text), onPress, DEFAULT_NARRATION);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
