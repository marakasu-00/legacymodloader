package com.example.compatmod.legacy.api.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(modid = "legacymodloader", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class LegacyEventHooks {

    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            //System.out.println("[LegacyEventHooks] Client tick event fired.");
        }
    }

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiOverlayEvent.Post event) {
        //System.out.println("[LegacyEventHooks] Render GUI overlay event fired.");
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        LOGGER.info("[LegacyEventHooks] Key input event fired: {}", event.getKey());
    }

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        LOGGER.info("[LegacyEventHooks] Player right-click block event fired at {}", event.getPos());
    }
}
