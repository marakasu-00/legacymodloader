package com.example.compatmod.legacy.loader;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class LegacyItemBootstrapper {

    public static Map<String, List<String>> getLegacyItemsPerMod(Path legacyAssetsRoot) {
        Map<String, List<String>> result = new HashMap<>();

        System.out.println("[Bootstrap] Checking path: " + legacyAssetsRoot.toAbsolutePath());
        if (!Files.exists(legacyAssetsRoot)) {
            System.out.println("[Bootstrap] Path does not exist: " + legacyAssetsRoot.toAbsolutePath());
            return result;
        }

        try (Stream<Path> modDirs = Files.list(legacyAssetsRoot)) {
            for (Path modDir : modDirs.collect(Collectors.toList())) {
                String modId = modDir.getFileName().toString();
                Path texPath1 = modDir.resolve("textures/item");
                Path texPath2 = modDir.resolve("textures/items");

                Path texPath = Files.exists(texPath1) ? texPath1 :
                        Files.exists(texPath2) ? texPath2 : null;

                if (texPath == null) continue;

                try (Stream<Path> files = Files.list(texPath)) {
                    List<String> items = files
                            .filter(p -> p.toString().endsWith(".png"))
                            .map(p -> p.getFileName().toString().replaceAll("\\.png$", ""))
                            .collect(Collectors.toList());

                    if (!items.isEmpty()) {
                        result.put(modId, items);
                        System.out.printf("[Bootstrap] %s: %d items discovered\n", modId, items.size());
                    }
                } catch (IOException e) {
                    System.err.printf("[Error] Failed reading tex dir for %s: %s\n", modId, e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("[Error] Cannot list legacy_assets dir: " + e.getMessage());
        }

        return result;
    }
}
