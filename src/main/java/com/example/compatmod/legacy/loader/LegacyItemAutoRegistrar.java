package com.example.compatmod.legacy.loader;

import com.example.compatmod.legacy.registry.LegacyItemRegistry;
import com.example.compatmod.legacy.registry.LegacyCreativeTabs;
import com.example.compatmod.loader.LegacyModScanner;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Stream;

public class LegacyItemAutoRegistrar {

    public static void registerItemsFromAssets() {
        for (String modId : LegacyModScanner.scanLegacyModIds()) {
            List<Path> candidatePaths = List.of(
                    Paths.get("run/legacy_assets", modId, "textures", "items")
            );

            Path texDir = candidatePaths.stream().filter(Files::exists).findFirst().orElse(null);
            if (texDir == null) {
                System.out.println("[Skip] No texture directory for mod: " + modId);
                continue;
            }

            System.out.println("[Found] Texture directory for mod: " + modId + " at " + texDir);

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
