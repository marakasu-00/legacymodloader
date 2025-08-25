package com.example.compatmod.legacy.tools;

import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.*;
import java.util.stream.Stream;

public class LegacyModelGenerator {
    private static final Path LEGACY_ASSETS_DIR = FMLPaths.GAMEDIR.get().resolve("legacy_assets").resolve("assets");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void generateAllModels() {
        if (!Files.exists(LEGACY_ASSETS_DIR)) return;

        try (Stream<Path> modDirs = Files.list(LEGACY_ASSETS_DIR)) {
            modDirs.filter(Files::isDirectory).forEach(modDir -> {
                Path texturesDir = modDir.resolve("textures/item");
                Path modelsDir = modDir.resolve("models/item");

                if (!Files.exists(texturesDir)) return;
                try {
                    Files.createDirectories(modelsDir);
                } catch (IOException e) {
                    System.err.println("[LegacyModelGen] モデルディレクトリ作成失敗: " + modelsDir);
                    return;
                }

                try (Stream<Path> pngs = Files.list(texturesDir)) {
                    pngs.filter(p -> p.getFileName().toString().endsWith(".png")).forEach(png -> {
                        String fileName = png.getFileName().toString().replace(".png", "");
                        Path modelJson = modelsDir.resolve(fileName + ".json");

                        JsonObject json = new JsonObject();
                        json.addProperty("parent", "item/generated");

                        JsonObject tex = new JsonObject();
                        tex.addProperty("layer0", modDir.getFileName().toString() + ":item/" + fileName);
                        json.add("textures", tex);

                        try (Writer writer = Files.newBufferedWriter(modelJson)) {
                            GSON.toJson(json, writer);
                            System.out.println("[LegacyModelGen] 生成: " + modelJson);
                        } catch (IOException e) {
                            System.err.println("[LegacyModelGen] 書き込み失敗: " + modelJson);
                        }
                    });
                } catch (IOException e) {
                    System.err.println("[LegacyModelGen] テクスチャ一覧取得失敗: " + texturesDir);
                }
            });
        } catch (IOException e) {
            System.err.println("[LegacyModelGen] mods/legacy_assets/assets 走査失敗");
        }
    }
}
