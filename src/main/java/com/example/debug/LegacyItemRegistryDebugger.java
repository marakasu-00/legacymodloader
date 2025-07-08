package com.example.debug;

import com.example.compatmod.legacy.item.LegacyItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class LegacyItemRegistryDebugger {

    public static void dumpAllRegisteredLegacyItems() {
        System.out.println("=== Dumping all LegacyItems grouped by mod ===");

        Map<String, List<LegacyItem>> byMod = new TreeMap<>();

        for (Item item : ForgeRegistries.ITEMS.getValues()) {
            if (item instanceof LegacyItem legacy) {
                byMod.computeIfAbsent(legacy.getModId(), id -> new ArrayList<>()).add(legacy);
            }
        }

        if (byMod.isEmpty()) {
            System.out.println("⚠ No LegacyItems registered.");
            return;
        }

        for (Map.Entry<String, List<LegacyItem>> entry : byMod.entrySet()) {
            String modId = entry.getKey();
            System.out.printf("▶ %s (%d items)%n", modId, entry.getValue().size());
            for (LegacyItem item : entry.getValue()) {
                System.out.printf("   - %s%n", item.getLegacyId());
            }
        }

        System.out.println("=== End ===");
    }
}
