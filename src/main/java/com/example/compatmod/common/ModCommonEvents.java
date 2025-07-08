package com.example.compatmod.common;

import com.example.compatmod.legacy.loader.LegacyItemAutoRegistrar;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = "legacymodloader", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCommonEvents {

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            System.out.println("[ModCommonEvents] Starting legacy item auto-registration");
            LegacyItemAutoRegistrar.registerItemsFromAssets();
            System.out.println("[ModCommonEvents] Finished legacy item auto-registration");
        });
    }
}
