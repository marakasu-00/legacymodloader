package com.example.compatmod.legacy.registry;

import com.example.compatmod.legacy.item.LegacyItem;
import com.example.compatmod.legacy.loader.LegacyItemBootstrapper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class LegacyItemRegistry {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, "legacymodloader");

    private static final Map<String, List<RegistryObject<Item>>> legacyItems = new HashMap<>();
    private static final Set<String> usedIds = new HashSet<>();

    @SuppressWarnings("removal")
    public static void register() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        System.out.println("[LegacyItemRegistry] DeferredRegister<Item> REGISTERED");

        Path assetRoot = Paths.get("run", "legacy_assets_normalized");
        Map<String, List<String>> legacyMap = LegacyItemBootstrapper.getLegacyItemsPerMod(assetRoot);

        for (Map.Entry<String, List<String>> entry : legacyMap.entrySet()) {
            String modId = entry.getKey();
            for (String legacyId : entry.getValue()) {

                // 正規化してResourceLocationに対応
                String baseId = normalizeResourcePath(modId + "_" + legacyId);
                String safeId = resolveDuplicate(baseId);

                RegistryObject<Item> item = ITEMS.register(safeId, () ->
                        new LegacyItem(modId, legacyId, new Properties())
                );

                legacyItems.computeIfAbsent(modId, k -> new ArrayList<>()).add(item);
                System.out.printf("[LegacyItemRegistry] Registered %s:%s as ID %s\n", modId, legacyId, safeId);
            }

            LegacyCreativeTabs.createTabForMod(modId);
            System.out.printf("[LegacyTab] Created tab for: %s\n", modId);
        }
    }

    // 小文字化と記号の置換
    private static String normalizeResourcePath(String raw) {
        return raw.toLowerCase().replaceAll("[^a-z0-9/._-]", "_");
    }

    // 重複回避用に "_1", "_2", … を付加
    private static String resolveDuplicate(String baseId) {
        String newId = baseId;
        int suffix = 1;
        while (usedIds.contains(newId)) {
            newId = baseId + "_" + suffix;
            suffix++;
        }
        usedIds.add(newId);
        return newId;
    }

    public static Set<String> getRegisteredModIds() {
        return legacyItems.keySet();
    }

    public static List<RegistryObject<Item>> getItemsForMod(String modId) {
        return legacyItems.getOrDefault(modId, List.of());
    }
}
