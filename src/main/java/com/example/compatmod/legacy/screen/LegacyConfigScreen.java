package com.example.compatmod.legacy.screen;

import com.example.compatmod.config.ConfigHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class LegacyConfigScreen extends Screen {

    public LegacyConfigScreen(Component title) {
        super(title);
    }

    @Override
    protected void init() {
        // スライダー・チェックボックスのGUI追加処理を書く
    }

    @Override
    public void onClose() {
        super.onClose();
        ConfigHandler.saveConfigSafe();  // 閉じたときに一括保存
    }
}
