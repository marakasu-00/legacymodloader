package com.example.compatmod.legacy.loader;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class LegacyItemBootstrapper {
    public static Map<String, List<String>> getLegacyItemsPerMod(Path legacyAssetsRoot) {
        Map<String, List<String>> result = new HashMap<>();

        if (!Files.exists(legacyAssetsRoot)) {
            System.out.println("[Bootstrap] Path does not exist: " + legacyAssetsRoot);
            return result;
        }

        System.out.println("[Bootstrap] Scanning directory: " + legacyAssetsRoot);

        try (Stream<Path> modDirs = Files.list(legacyAssetsRoot)) {
            for (Path modDir : modDirs.collect(Collectors.toList())) {
                String modId = modDir.getFileName().toString();
                List<String> itemList = new ArrayList<>();

                for (String subDir : List.of("textures/items", "textures/item")) {
                    Path texDir = modDir.resolve(subDir);
                    if (Files.exists(texDir)) {
                        try (Stream<Path> files = Files.list(texDir)) {
                            files.filter(p -> p.toString().endsWith(".png"))
                                    .map(p -> p.getFileName().toString().replace(".png", ""))
                                    .forEach(itemList::add);
                        } catch (IOException e) {
                            System.err.printf("[Error] Failed to read textures for mod %s: %s\n", modId, e.getMessage());
                        }
                    }
                }

                if (!itemList.isEmpty()) {
                    result.put(modId, itemList);
                    System.out.printf("[Bootstrap] %s: %d items discovered\n", modId, itemList.size());
                }
            }
        } catch (IOException e) {
            System.err.println("[Error] Cannot list legacy_assets dir: " + e.getMessage());
        }

        return result;
    }

}
