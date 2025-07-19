package com.example.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class LangToJsonConverter {
    private static final String[] LANG_CODES = {"ja_JP", "en_US", "zh_CN", "ko_KR"};

    public static void main(String[] args) throws IOException {
        Path baseLangPath = Paths.get("legacy_assets");

        try (Stream<Path> mods = Files.list(baseLangPath)) {
            mods.filter(Files::isDirectory).forEach(modPath -> {
                Path langDir = modPath.resolve("lang");
                for (String langCode : LANG_CODES) {
                    Path input = langDir.resolve(langCode + ".lang");
                    Path output = langDir.resolve(langCode.toLowerCase() + ".json");

                    if (!Files.exists(input)) {
                        System.out.println("[Skip] .lang not found: " + input);
                        continue;
                    }

                    System.out.println("[Convert] " + input + " -> " + output);

                    Map<String, String> map = new LinkedHashMap<>();

                    try (BufferedReader reader = new BufferedReader(new FileReader(input.toFile()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            line = line.trim();
                            if (line.isEmpty() || line.startsWith("#")) continue;
                            int idx = line.indexOf('=');
                            if (idx < 0) continue;

                            String key = line.substring(0, idx).trim();
                            String value = line.substring(idx + 1).trim();

                            key = convertLegacyKey(key);
                            map.put(key, value);
                        }
                    } catch (IOException e) {
                        System.err.println("[Error] Reading " + input + ": " + e.getMessage());
                        continue;
                    }

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(output.toFile()))) {
                        gson.toJson(map, writer);
                    } catch (IOException e) {
                        System.err.println("[Error] Writing " + output + ": " + e.getMessage());
                    }
                }
            });
        }
    }

    private static String convertLegacyKey(String key) {
        if (key.startsWith("item.")) {
            key = key.replace(".name", "");
            key = key.replace('_', '.');
        } else if (key.startsWith("Category.")) {
            key = "category." + key.substring("Category.".length()).toLowerCase();
        } else if (key.startsWith("Key.")) {
            key = "key.compatmod." + key.substring("Key.".length());
        } else if (key.startsWith("Chat.")) {
            key = "chat.compatmod." + key.substring("Chat.".length());
        }
        return key;
    }
}
