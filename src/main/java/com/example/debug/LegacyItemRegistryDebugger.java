package com.example.debug;

import com.example.compatmod.legacy.item.LegacyItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public class LegacyItemRegistryDebugger {

    public static void dumpRegisteredLegacyItems(String modId) {
        System.out.println("=== Dumping LegacyItems for mod: " + modId + " ===");
        for (Item item : ForgeRegistries.ITEMS.getValues()) {
            if (item instanceof LegacyItem legacy && legacy.getModId().equals(modId)) {
                System.out.printf("✔ Found: legacyId = %s (modId = %s)%n", legacy.getLegacyId(), legacy.getModId());
            }
        }
        System.out.println("=== End ===");
    }
}
