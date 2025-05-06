package com.example.compatmod.legacy.event;

import com.example.compatmod.legacy.api.ILegacyMod;
import com.example.compatmod.legacy.loader.LegacyModManager;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
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
    public static void onGuiInit(ScreenEvent.Init event) {
        Screen screen = event.getScreen();

        Button button = Button.builder(Component.literal("Legacy Button"), btn -> {
            System.out.println("[LegacyExample] Legacy Button clicked!");
        }).pos(10, 30).size(100, 20).build();

        event.addListener(button);
    }


}
