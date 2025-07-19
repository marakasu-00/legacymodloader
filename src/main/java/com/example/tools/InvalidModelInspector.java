package com.example.tools;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Stream;

public class InvalidModelInspector {
    private static final Gson GSON = new Gson();
    private static final List<String> UNSUPPORTED_EXTENSIONS = List.of(".mqo", ".npm", ".ngtz", ".ngto", ".obj");

    public static void main(String[] args) throws IOException {
        Path basePath = Paths.get("legacy_assets");

        try (Stream<Path> paths = Files.walk(basePath)) {
            paths.filter(Files::isRegularFile)
                    .forEach(InvalidModelInspector::inspectFile);
        }
    }

    private static void inspectFile(Path file) {
        String fileName = file.getFileName().toString().toLowerCase();

        for (String ext : UNSUPPORTED_EXTENSIONS) {
            if (fileName.endsWith(ext)) {
                System.err.printf("Unsupported model format detected: %s%n", file);
                return;
            }
        }

        if (fileName.endsWith(".json")) {
            inspectJson(file);
        }
    }

    private static void inspectJson(Path jsonFile) {
        try (Reader reader = Files.newBufferedReader(jsonFile)) {
            JsonObject root = GSON.fromJson(reader, JsonObject.class);

            if (root.has("parent")) {
                String parent = root.get("parent").getAsString();
                if (!isValidResourceLocation(parent)) {
                    System.err.printf("Invalid parent in %s: %s%n", jsonFile, parent);
                }
            }

            if (root.has("textures") && root.get("textures").isJsonObject()) {
                JsonObject textures = root.getAsJsonObject("textures");
                for (String key : textures.keySet()) {
                    String value = textures.get(key).getAsString();
                    if (!isValidResourceLocation(value)) {
                        System.err.printf("Invalid texture '%s' in %s: %s%n", key, jsonFile, value);
                    } else {
                        Path texturePath = resolveTexturePath(value);
                        if (!Files.exists(texturePath)) {
                            System.err.printf("Missing texture file for '%s' in %s: %s%n", key, jsonFile, texturePath);
                        }
                    }
                }
            }

        } catch (JsonSyntaxException e) {
            System.err.printf("JSON parse error in %s: %s%n", jsonFile, e.getMessage());
        } catch (IOException e) {
            System.err.printf("I/O error reading %s: %s%n", jsonFile, e.getMessage());
        }
    }

    private static boolean isValidResourceLocation(String value) {
        return value.matches("^[a-z0-9_\\-\\.]+:[a-z0-9_\\-\\/\\.]+$");
    }

    private static Path resolveTexturePath(String resourceLocation) {
        String[] parts = resourceLocation.split(":");
        if (parts.length != 2) return Paths.get("INVALID_PATH");
        String modId = parts[0];
        String path = parts[1];
        return Paths.get("legacy_assets", modId, "textures", "", path + ".png").normalize();
    }
}
