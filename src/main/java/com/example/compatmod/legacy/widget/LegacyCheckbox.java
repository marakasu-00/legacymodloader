package com.example.compatmod.legacy.widget;

import com.example.compatmod.config.ConfigHandler;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.network.chat.Component;

public class LegacyCheckbox extends Checkbox {

    private boolean checkboxChecked = false; // 初期値を適宜設定

    public LegacyCheckbox(int x, int y, int width, int height, boolean selected) {
        super(x, y, width, height, Component.empty(), selected);
        refreshLabel();
    }

    @Override
    public void onPress() {
        super.onPress();
        checkboxChecked = selected();
        ConfigHandler.CHECKBOX_ENABLED.set(checkboxChecked);
        ConfigHandler.saveConfigSafe();
        refreshLabel();

        // 必要なら表示テキストを変更
        setMessage(Component.literal(checkboxChecked ? "Feature Enabled" : "Enable Feature"));
    }
    private void refreshLabel() {
        setMessage(Component.literal(selected()? "Feature Enabled" : "Enable Feature"));
    }
}