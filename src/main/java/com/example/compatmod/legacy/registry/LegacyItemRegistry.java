package com.example.compatmod.legacy.registry;

import com.example.compatmod.legacy.item.LegacyItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.*;

public class LegacyItemRegistry {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, "legacymodloader");

    // modid -> List of item registryobjects
    private static final Map<String, List<RegistryObject<Item>>> legacyItems = new HashMap<>();

    @SuppressWarnings("removal")
    public static void register() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static void addLegacyItem(String modId, String legacyId) {
        String fullId = modId + "_" + legacyId;
        RegistryObject<Item> item = ITEMS.register(fullId, () ->
                new LegacyItem(modId, legacyId, new Properties())
        );

        legacyItems.computeIfAbsent(modId, k -> new ArrayList<>()).add(item);
    }

    public static List<RegistryObject<Item>> getItemsForMod(String modId) {
        return legacyItems.getOrDefault(modId, List.of());
    }

    public static Set<String> getRegisteredModIds() {
        return legacyItems.keySet();
    }
}
