package com.example.compatmod.legacy.util;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.*;
import java.util.stream.Stream;

/**
 * legacy_assets_normalized から models/item/*.json を動的生成するユーティリティ
 */
public class LegacyModelGenerator {

    public static void generateModels(Path normalizedAssetsDir, Path outputModelDir) {
        System.out.println("[LegacyModelGenerator] Starting generation...");

        try (Stream<Path> modDirs = Files.list(normalizedAssetsDir)) {
            modDirs.filter(Files::isDirectory).forEach(modDir -> {
                String modId = modDir.getFileName().toString();
                Path texDir = modDir.resolve("textures/item");

                if (!Files.exists(texDir)) {
                    System.out.println("[LegacyModelGenerator] Skip: No texture/item for " + modId);
                    return;
                }

                Path modelOutput = outputModelDir.resolve(modId).resolve("models/item");

                try {
                    Files.createDirectories(modelOutput);

                    try (Stream<Path> files = Files.list(texDir)) {
                        files.filter(p -> p.toString().endsWith(".png")).forEach(png -> {
                            String name = png.getFileName().toString().replace(".png", "");
                            Path modelFile = modelOutput.resolve(name + ".json");

                            JsonObject json = new JsonObject();
                            json.addProperty("parent", "item/generated");

                            JsonObject tex = new JsonObject();
                            tex.addProperty("layer0", modId + ":item/" + name);
                            json.add("textures", tex);

                            try (Writer writer = Files.newBufferedWriter(modelFile)) {
                                writer.write(json.toString());
                                System.out.printf("[LegacyModelGenerator] %s:%s.json generated\n", modId, name);
                            } catch (IOException e) {
                                System.err.printf("[LegacyModelGenerator] Failed to write model %s: %s\n", modelFile, e.getMessage());
                            }
                        });
                    }

                } catch (IOException e) {
                    System.err.printf("[LegacyModelGenerator] IO error in mod %s: %s\n", modId, e.getMessage());
                }
            });

        } catch (IOException e) {
            System.err.println("[LegacyModelGenerator] Cannot list normalized assets dir: " + e.getMessage());
        }

        System.out.println("[LegacyModelGenerator] Finished.");
    }
}
