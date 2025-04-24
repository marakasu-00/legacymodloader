package com.example.compatmod.legacy.api;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * tick対応のScreen
 */
public abstract class LegacyTickableScreen extends LegacyScreen {
    protected LegacyTickableScreen(Component title) {
        super(title);
    }

    /**
     * tickイベントがあればここに記述
     */
    public void tick() {
        // オーバーライドして使う
    }
}
