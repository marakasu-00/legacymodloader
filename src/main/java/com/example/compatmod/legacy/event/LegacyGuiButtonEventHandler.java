package com.example.compatmod.legacy.event;

import com.example.compatmod.legacy.api.ILegacyMod;
import com.example.compatmod.legacy.loader.LegacyModManager;
import com.example.compatmod.legacy.widget.LegacyWidgetWrapper;
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
    public static void onGuiInit(ScreenEvent.Init event,List<LegacyWidgetWrapper> widgets) {
        Screen screen = event.getScreen();

        // 1. EditBox を作成
        EditBox editBox = new EditBox(screen.getMinecraft().font, 10, 10, 100, 20, Component.literal("Legacy Input"));
        widgets.add(new LegacyWidgetWrapper(editBox));

        // 2. Button を作成し、editBox の値を使う
        Button button = Button.builder(Component.literal("Legacy Button"), btn -> {
            String input = editBox.getValue().trim();
            System.out.println("[LegacyExample] Legacy Button clicked!" + input);
        }).pos(10, 30).size(100, 20).build();

        widgets.add(new LegacyWidgetWrapper(button));
    }
}
