package com.example.compatmod.legacy.lang;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class LegacyLangManager {

    private static final Map<String, String> displayNameMap = new HashMap<>();
    private static final Gson GSON = new Gson();

    public static void loadAll() {
        Path legacyDir = Paths.get("mods", "legacy");
        if (!Files.exists(legacyDir)) return;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(legacyDir, "*.{jar,zip}")) {
            for (Path mod : stream) {
                try (ZipFile zip = new ZipFile(mod.toFile())) {
                    Enumeration<? extends ZipEntry> entries = zip.entries();
                    while (entries.hasMoreElements()) {
                        ZipEntry entry = entries.nextElement();
                        String name = entry.getName();

                        if (name.matches("assets/.+/lang/(ja_JP|en_us)\\.(lang|json)")) {
                            String modid = name.split("/")[1];

                            try (InputStream in = zip.getInputStream(entry)) {
                                if (name.endsWith(".lang")) {
                                    parseLangFormat(modid, in);
                                } else if (name.endsWith(".json")) {
                                    parseJsonFormat(modid, in);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("[LangLoad] 読み込み失敗: " + mod);
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.err.println("[LangLoad] legacy mods ディレクトリ読み込み失敗");
        }
    }

    private static void parseLangFormat(String modid, InputStream in) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            reader.lines()
                    .filter(line -> line.contains("="))
                    .forEach(line -> {
                        String[] kv = line.split("=", 2);
                        String key = kv[0].trim();
                        String value = kv[1].trim();
                        if (key.startsWith("item." + modid + ".")) {
                            String itemName = key.substring(("item." + modid + ".").length()).replace(".name", "");
                            displayNameMap.put(modid + ":" + itemName, value);
                        }
                    });
        } catch (Exception e) {
            System.err.println("[LangLoad] .lang形式 読み込み失敗: " + modid);
        }
    }

    private static void parseJsonFormat(String modid, InputStream in) {
        try {
            Map<String, String> map = GSON.fromJson(new InputStreamReader(in), new TypeToken<Map<String, String>>(){}.getType());
            for (var entry : map.entrySet()) {
                if (entry.getKey().startsWith("item." + modid + ".")) {
                    String itemName = entry.getKey().substring(("item." + modid + ".").length());
                    displayNameMap.put(modid + ":" + itemName, entry.getValue());
                }
            }
        } catch (Exception e) {
            System.err.println("[LangLoad] .json形式 読み込み失敗: " + modid);
        }
    }

    public static Optional<String> getDisplayName(String modid, String legacyId) {
        return Optional.ofNullable(displayNameMap.get(modid + ":" + legacyId));
    }
}
