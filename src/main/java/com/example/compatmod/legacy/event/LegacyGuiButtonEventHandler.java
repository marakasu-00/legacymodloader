package com.example.compatmod.legacy.event;

import com.example.compatmod.legacy.api.ILegacyMod;
import com.example.compatmod.legacy.loader.LegacyModManager;
import com.example.compatmod.legacy.widget.LegacyWidgetWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.ScreenEvent.Init;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = "legacymodloader", value = Dist.CLIENT)
public class LegacyGuiButtonEventHandler {

    @SubscribeEvent
    public static void onGuiInitPost(ScreenEvent.Init.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.font == null) {
            System.err.println("[LegacyExample] Minecraft or font is not ready.");
            return;
        }

        Screen screen = event.getScreen();

        EditBox editBox = new EditBox(mc.font, 10, 120, 150, 20, Component.literal("Input"));
        editBox.setMaxLength(50);

        Button button = Button.builder(Component.literal("Submit"), btn -> {
            String input = editBox.getValue().trim();

            if (input.isEmpty()) {
                System.out.println("[LegacyExample] 入力は空です。");
            } else if (!input.matches("^[a-zA-Z0-9]+$")) {
                System.out.println("[LegacyExample] 入力には英数字のみ使用してください。");
            } else {
                System.out.println("[LegacyExample] 有効な入力: " + input);
            }
        }).pos(10, 150).size(100, 20).build();

        // ラップして追加（必要なら LegacyWidgetWrapper 使用）
        event.addListener(editBox);
        event.addListener(button);
    }
    public static void initWidgets(ScreenEvent.Init event, List<LegacyWidgetWrapper> widgets) {
        Screen screen = event.getScreen();

        Button button = Button.builder(Component.literal("Legacy Button"), btn -> {
            System.out.println("[LegacyExample] Legacy Button clicked!");
        }).pos(10, 30).size(100, 20).build();

        widgets.add(new LegacyWidgetWrapper(button));
    }
}