package com.example.compatmod.legacy.loader;

import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.*;

public class LegacyModResourceHelper {

    public static void loadLegacyResources() {
        Path sourceDir = Paths.get("run/assets");
        Path targetDir = FMLPaths.GAMEDIR.get().resolve("legacy_assets");

        if (!Files.exists(sourceDir)) {
            System.out.println("[LegacyLoader] No legacy assets found at: " + sourceDir);
            return;
        }

        try {
            Files.walk(sourceDir).forEach(source -> {
                try {
                    Path dest = targetDir.resolve(sourceDir.relativize(source));
                    if (Files.isDirectory(source)) {
                        Files.createDirectories(dest);
                    } else {
                        Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    System.err.println("[LegacyLoader] Failed to copy: " + source);
                }
            });

            System.out.println("[LegacyLoader] Legacy assets copied to: " + targetDir);

        } catch (IOException e) {
            System.err.println("[LegacyLoader] Error during asset copying.");
            e.printStackTrace();
        }
    }
}
