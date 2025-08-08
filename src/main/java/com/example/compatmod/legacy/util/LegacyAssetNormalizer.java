package com.example.compatmod.legacy.util;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;

public class LegacyAssetNormalizer {

    /**
     * Normalize assets from legacy_assets into a normalized directory and return the map.
     *
     * @param legacyRoot Path to run/legacy_assets
     * @param normalizedRoot Path to run/legacy_assets_normalized
     * @return Map<String modId, List<String legacyId>>
     */
    public static Map<String, java.util.List<String>> normalizeAndCopy(Path legacyRoot, Path normalizedRoot) {
        Map<String, java.util.List<String>> result = new HashMap<>();

        try (DirectoryStream<Path> modDirs = Files.newDirectoryStream(legacyRoot)) {
            for (Path modDir : modDirs) {
                if (!Files.isDirectory(modDir)) continue;

                String modId = modDir.getFileName().toString();
                Path texturesItems = modDir.resolve("textures/items");
                Path texturesItem = modDir.resolve("textures/item");

                Path sourceTexDir = Files.exists(texturesItems) ? texturesItems : (Files.exists(texturesItem) ? texturesItem : null);
                if (sourceTexDir == null) continue;

                Path targetTexDir = normalizedRoot.resolve(modId).resolve("textures/item");
                Files.createDirectories(targetTexDir);

                java.util.List<String> ids = new java.util.ArrayList<>();

                try (DirectoryStream<Path> files = Files.newDirectoryStream(sourceTexDir, "*.png")) {
                    for (Path png : files) {
                        String legacyId = png.getFileName().toString().replaceAll("\\.png$", "");
                        Path target = targetTexDir.resolve(legacyId + ".png");

                        // Resolve name conflict
                        int counter = 1;
                        while (Files.exists(target)) {
                            target = targetTexDir.resolve(legacyId + "_" + counter + ".png");
                            counter++;
                        }

                        Files.copy(png, target);
                        ids.add(target.getFileName().toString().replaceAll("\\.png$", ""));
                    }
                }

                if (!ids.isEmpty()) {
                    result.put(modId, ids);
                    System.out.printf("[Normalizer] %s: %d normalized textures\n", modId, ids.size());
                }
            }

        } catch (IOException e) {
            System.err.println("[Normalizer] Failed to normalize assets: " + e.getMessage());
        }

        return result;
    }
}
