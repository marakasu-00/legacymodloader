package com.example.compatmod.legacy.loader;

import net.minecraftforge.fml.loading.FMLPaths;

import static com.example.compatmod.legacy.loader.LegacyPaths.LEGACY_ASSETS_PATH;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class LegacyModResourceHelper {

    public static void loadLegacyResources() {
        Path sourceDir = FMLPaths.GAMEDIR.get().resolve(LEGACY_ASSETS_PATH);
        Path targetDir = FMLPaths.GAMEDIR.get().resolve("legacy_assets");

        if (!Files.exists(sourceDir)) {
            System.out.println("[LegacyLoader] No legacy assets found at: " + sourceDir);
            return;
        }

        try (Stream<Path> paths = Files.walk(sourceDir)) {
            paths.forEach(source -> {
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
