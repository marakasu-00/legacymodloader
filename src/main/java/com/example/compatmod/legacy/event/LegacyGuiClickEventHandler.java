package com.example.compatmod.legacy.event;

import com.example.compatmod.legacy.api.ILegacyMod;
import com.example.compatmod.legacy.client.ILegacyModClient;
import com.example.compatmod.legacy.loader.LegacyModManager;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;

@Mod.EventBusSubscriber(modid = "legacymodloader", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LegacyGuiClickEventHandler {

    @SubscribeEvent
    public static void onGuiMouseClicked(ScreenEvent.MouseButtonPressed.Pre event) {
        Screen screen = event.getScreen();
        double mouseX = event.getMouseX();
        double mouseY = event.getMouseY();
        int button = event.getButton();

        for (ILegacyMod mod : LegacyModManager.getLegacyMods()) {
            if (net.minecraftforge.fml.loading.FMLEnvironment.dist == Dist.CLIENT && mod instanceof ILegacyModClient clientMod) {
                clientMod.onGuiMouseClicked(screen, mouseX, mouseY, button);
            }
        }
    }
}
