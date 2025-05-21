package com.example.compatmod.legacy.widget;

import com.example.compatmod.config.ConfigHandler;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.network.chat.Component;

public class LegacyCheckbox extends Checkbox {
    public LegacyCheckbox(int x, int y, int width, int height, Component message, boolean selected) {
        super(x, y, width, height, message, selected);
    }

    @Override
    public void onPress() {
        super.onPress();
        ConfigHandler.setCheckboxEnabled(selected());
        ConfigHandler.saveConfigSafe();
        updateLabel(); // 状態変更に応じてラベルを更新
    }

    private void updateLabel() {
        setMessage(Component.literal(selected() ? "Feature Enabled" : "Enable Feature"));
    }
}