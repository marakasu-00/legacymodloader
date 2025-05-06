package com.example.compatmod.legacy.event;

import com.example.compatmod.legacy.api.ILegacyMod;
import com.example.compatmod.legacy.loader.LegacyModManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.client.event.InputEvent.Key;

@Mod.EventBusSubscriber(modid = "legacymodloader", value = Dist.CLIENT)
public class LegacyChatEventHandler {

    @SubscribeEvent
    public static void onKeyPressed(ScreenEvent.KeyPressed.Pre event) {
        Screen screen = event.getScreen();
        int keyCode = event.getKeyCode();
        int scanCode = event.getScanCode();
        int modifiers = event.getModifiers();

        for (ILegacyMod mod : LegacyModManager.getLegacyMods()) {
            mod.onGuiKeyPressed(screen, keyCode, scanCode, modifiers);
        }
    }
    @SubscribeEvent
    public static void onKeyInput(Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen != null) {
            int keyCode = event.getKey();
            int scanCode = event.getScanCode();
            int modifiers = event.getModifiers();

            for (ILegacyMod mod : LegacyModManager.getLegacyMods()) {
                mod.onGuiKeyPressed(mc.screen, keyCode, scanCode, modifiers);
            }
        }
    }
    @SubscribeEvent
    public static void onChatSent(ClientChatEvent event) {
        String message = event.getMessage();
        for (ILegacyMod mod : LegacyModManager.getLegacyMods()) {
            mod.onChatInput(message);
        }
    }
}
