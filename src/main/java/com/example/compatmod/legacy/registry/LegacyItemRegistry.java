// ✅ LegacyItemRegistry.java
package com.example.compatmod.legacy.registry;

import com.example.compatmod.legacy.item.LegacyItem;
import com.example.compatmod.legacy.loader.LegacyItemBootstrapper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;

public class LegacyItemRegistry {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, "legacymodloader");

    private static final Map<String, List<RegistryObject<Item>>> legacyItems = new HashMap<>();

    @SuppressWarnings("removal")
    public static void register() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        System.out.println("[LegacyItemRegistry] DeferredRegister<Item> REGISTERED");
    }

    public static void registerItemsFromBootstrap(Map<String, List<String>> legacyMap) {
        for (Map.Entry<String, List<String>> entry : legacyMap.entrySet()) {
            String modId = entry.getKey();
            for (String legacyId : entry.getValue()) {
                String fullId = modId + "_" + legacyId;
                RegistryObject<Item> item = ITEMS.register(fullId, () ->
                        new LegacyItem(modId, legacyId, new Properties())
                );
                legacyItems.computeIfAbsent(modId, k -> new ArrayList<>()).add(item);
                System.out.printf("[LegacyItemRegistry] Registered %s:%s\n", modId, legacyId);
            }

            LegacyCreativeTabs.createTabForMod(modId);
            System.out.printf("[LegacyTab] Created tab for: %s\n", modId);
        }
    }
    public static Set<String> getRegisteredModIds() {
        return legacyItems.keySet();
    }

    public static List<RegistryObject<Item>> getItemsForMod(String modId) {
        return legacyItems.getOrDefault(modId, List.of());
    }

}
