package com.example.compatmod.legacy.client;

import com.example.compatmod.legacy.registry.LegacyItemRegistry;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


import java.util.List;

@Mod.EventBusSubscriber(modid = "legacymodloader", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            System.out.println("=== Dumping all LegacyItems grouped by mod ===");
            for (String modId : LegacyItemRegistry.getRegisteredModIds()) {
                List<net.minecraftforge.registries.RegistryObject<Item>> items = LegacyItemRegistry.getItemsForMod(modId);
                System.out.printf("▶ %s (%d items)\n", modId, items.size());
                for (var item : items) {
                    if (item.isPresent()) {
                        System.out.println("- " + item.get().toString());
                    } else {
                        System.out.println("- (not yet present) " + item.getId());
                    }
                }
            }
            System.out.println("=== End ===");
        });
    }
}
