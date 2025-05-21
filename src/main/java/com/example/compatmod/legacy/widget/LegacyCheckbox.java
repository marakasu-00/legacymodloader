package com.example.compatmod.legacy.widget;

import com.example.compatmod.config.ConfigHandler;
import com.example.compatmod.config.SafeConfigManager;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class LegacyCheckbox extends Checkbox {

    private Consumer<Boolean> responder = b -> {}; // デフォルトの空実装を追加

    public LegacyCheckbox(int x, int y, int width, int height, Component message, boolean selected) {
        super(x, y, width, height, message, selected);
        updateMessage();
    }

    public void setResponder(Consumer<Boolean> responder) {
        this.responder = responder;
    }

    @Override
    public void onPress() {
        super.onPress();
        SafeConfigManager.setCheckbox(selected());
        updateMessage();
    }

    private void updateMessage() {
        setMessage(Component.literal(selected() ? "Feature Enabled" : "Enable Feature"));
    }
}