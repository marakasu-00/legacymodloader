package com.example.compatmod.common;

import com.example.compatmod.legacy.loader.LegacyItemBootstrapper;
import com.example.compatmod.legacy.registry.LegacyItemRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = "legacymodloader", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCommonEvents {

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            System.out.println("[ModCommonEvents] Bootstrap start");

            Path path = Paths.get("run/legacy_assets_normalized");
            Map<String, List<String>> legacyMap = LegacyItemBootstrapper.getLegacyItemsPerMod(path);
            LegacyItemRegistry.registerItemsFromBootstrap(legacyMap);

            System.out.println("[ModCommonEvents] Bootstrap done");
        });
    }

}
