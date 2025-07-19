package com.example.compatmod.legacy.loader;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class LegacyItemBootstrapper {
    public static Map<String, List<String>> getLegacyItemsPerMod() {
        Map<String, List<String>> result = new HashMap<>();

        Path basePath = Paths.get("legacy_assets");
        System.out.println("[Bootstrap] Checking path: " + basePath.toAbsolutePath());
        if (!Files.exists(basePath)) {
            System.out.println("[Bootstrap] Path does not exist: " + basePath.toAbsolutePath());
            return result;
        }

        try (Stream<Path> modDirs = Files.list(basePath)) {
            for (Path modDir : modDirs.collect(Collectors.toList())) {
                String modId = modDir.getFileName().toString();
                Path texPath1 = modDir.resolve("textures/item");
                Path texPath2 = modDir.resolve("textures/items");

                Path texDir = Files.exists(texPath1) ? texPath1 : Files.exists(texPath2) ? texPath2 : null;
                if (texDir == null) continue;

                try (Stream<Path> files = Files.list(texDir)) {
                    List<String> ids = files
                            .filter(p -> p.toString().endsWith(".png"))
                            .map(p -> p.getFileName().toString().replace(".png", ""))
                            .collect(Collectors.toList());

                    if (!ids.isEmpty()) result.put(modId, ids);
                }
            }
        } catch (IOException e) {
            System.err.println("[LegacyItemBootstrapper] Error scanning assets: " + e.getMessage());
        }

        return result;
    }
}
