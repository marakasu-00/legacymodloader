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

    private static final Map<String, List<RegistryObject<Item>>> legacyItems = new HashMap<>();
    private static boolean registered = false;

    @SuppressWarnings("removal")
    public static void register() {
        if (!registered) {
            ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
            System.out.println("[LegacyItemRegistry] DeferredRegister<Item> REGISTERED");
            registered = true;
        } else {
            System.err.println("[LegacyItemRegistry] DeferredRegister<Item> was already registered!");
        }
    }

    public static void addLegacyItem(String modId, String legacyId) {
        String fullId = modId + "_" + legacyId;
        RegistryObject<Item> item = ITEMS.register(fullId, () ->
                new LegacyItem(modId, legacyId, new Properties())
        );

        legacyItems.computeIfAbsent(modId, k -> new ArrayList<>()).add(item);

        // 🔽 DeferredRegister の register は即時ではないため、イベント後に get() されるまで null
        // ここで get() しようとすると NullPointer なのでログ出力には向かない
        // 代わりに ID ベースの情報だけを先に出す
        System.out.printf("[LegacyItemRegistry] Queued registration for %s:%s\n", modId, legacyId);
    }


    public static List<RegistryObject<Item>> getItemsForMod(String modId) {
        return legacyItems.getOrDefault(modId, List.of());
    }

    public static Set<String> getRegisteredModIds() {
        return legacyItems.keySet();
    }
}
