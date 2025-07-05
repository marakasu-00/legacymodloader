package com.example.compatmod.legacy.loader;

import com.example.compatmod.legacy.registry.LegacyItemRegistry;
import com.example.compatmod.legacy.registry.LegacyCreativeTabs;
import com.example.compatmod.loader.LegacyModScanner;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class LegacyItemAutoRegistrar {

    public static void registerItemsFromAssets() {
        for (String modId : LegacyModScanner.scanLegacyModIds()) {
            Path texDir = Paths.get("run/legacy_assets", modId, "textures", "item");
            if (!Files.exists(texDir)) {
                System.out.println("[Skip] No texture directory for mod: " + modId);
                continue;
            }

            try (Stream<Path> files = Files.list(texDir)) {
                files.filter(p -> p.toString().endsWith(".png")).forEach(p -> {
                    String fileName = p.getFileName().toString();
                    String legacyId = fileName.substring(0, fileName.length() - 4); // remove .png

                    LegacyItemRegistry.addLegacyItem(modId, legacyId);
                    System.out.printf("[LegacyItem] Registered %s:%s\n", modId, legacyId);
                });
            } catch (IOException e) {
                System.err.printf("[Error] Failed to read textures for mod %s: %s\n", modId, e.getMessage());
            }

            LegacyCreativeTabs.createTabForMod(modId);
            System.out.println("[LegacyTab] Created tab for: " + modId);
        }
    }
}
