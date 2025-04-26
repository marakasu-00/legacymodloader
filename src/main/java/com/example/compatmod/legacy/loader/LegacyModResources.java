package com.example.compatmod.legacy.loader;

import java.io.File;
import java.nio.file.Path;

public class LegacyModResources {
    public static void injectLegacyAssets() {
        Path legacyAssetsPath = new File("run/assets").toPath();

        if (!legacyAssetsPath.toFile().exists()) {
            System.out.println("[LegacyLoader] No legacy assets found at: " + legacyAssetsPath);
            return;
        }

        System.out.println("[LegacyLoader] Legacy assets are placed at: " + legacyAssetsPath);
        // 明示的に登録する必要はない。resources/assets以下にあるとForgeが検出する。
    }
}