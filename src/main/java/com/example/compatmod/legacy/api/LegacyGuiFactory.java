package com.example.compatmod.legacy.api;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class LegacyGuiFactory {

    public static Button createButton(int x, int y, int width, int height, String label, Button.OnPress action) {
        return Button.builder(Component.literal(label), action)
                .pos(x, y)
                .size(width, height)
                .build();
    }


    public static EditBox createTextField(int x, int y, int width, int height, String defaultText) {
        EditBox field = new EditBox(
                net.minecraft.client.Minecraft.getInstance().font,
                x, y, width, height,
                Component.empty()
        );
        field.setValue(defaultText);
        return field;
    }

    public static void setEnabled(Button button, boolean enabled) {
        button.active = enabled;
    }

    public static void setText(EditBox box, String text) {
        box.setValue(text);
    }
}
