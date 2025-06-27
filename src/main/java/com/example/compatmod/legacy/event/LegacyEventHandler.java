package com.example.compatmod.legacy.event;

import com.example.compatmod.legacy.loader.LegacyModManager;
import com.example.compatmod.legacy.api.ILegacyMod;
import com.example.compatmod.legacy.client.ILegacyModClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class LegacyEventHandler {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            for (ILegacyMod mod : LegacyModManager.getLegacyMods()) {
                if (net.minecraftforge.fml.loading.FMLEnvironment.dist == Dist.CLIENT && mod instanceof ILegacyModClient clientMod) {
                    clientMod.onClientTick();
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
        for (ILegacyMod mod : LegacyModManager.getLegacyMods()) {
            if (net.minecraftforge.fml.loading.FMLEnvironment.dist == Dist.CLIENT && mod instanceof ILegacyModClient clientMod) {
                clientMod.onRenderOverlay();
            }
        }
    }
}
