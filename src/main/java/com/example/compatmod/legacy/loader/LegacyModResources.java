package com.example.compatmod.legacy.loader;

import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.resource.ResourcePackLoader;

import java.io.File;
import java.nio.file.Path;

public class LegacyModResources {

    public static void injectLegacyAssets() {
        Path legacyAssetsPath = new File("run/assets").toPath();

        if (!legacyAssetsPath.toFile().exists()) {
            System.out.println("[LegacyLoader] No legacy assets found at: " + legacyAssetsPath);
            return;
        }

        try {
            System.out.println("[LegacyLoader] Injecting legacy assets from: " + legacyAssetsPath);

            // Forgeのリソースパック読み込みに追加（ただしForgeが対応している必要あり）
            ResourcePackLoader.getPackFinder().addPack(
                    new PathPackResources("legacy_assets", legacyAssetsPath)
            );

        } catch (Exception e) {
            System.err.println("[LegacyLoader] Failed to inject legacy assets");
            e.printStackTrace();
        }
    }
}
