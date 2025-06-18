package com.example.compatmod.legacy.loader;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Utility responsible for scanning legacy mod jars and extracting their
 * {@code assets/**} directories into the development resources path.
 */
public final class LegacyModAssetLoader {

    private static final Logger LOGGER = LogUtils.getLogger();

    private LegacyModAssetLoader() {
    }

    /**
     * Scans the {@code mods/legacy} directory for jar files and extracts any
     * contained assets into {@code run/resources/assets} while also duplicating
     * them into {@code run/legacy_assets}.
     */
    public static void loadLegacyAssets() {
        Path gameDir = FMLPaths.GAMEDIR.get();
        Path modsDir = gameDir.resolve("mods").resolve("legacy");
        Path assetsDir = gameDir.resolve("resources").resolve("assets");
        Path legacyCopyDir = gameDir.resolve("legacy_assets");

        if (!Files.isDirectory(modsDir)) {
            LOGGER.warn("[LegacyLoader] No legacy mod directory found at {}", modsDir);
            return;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(modsDir, "*.jar")) {
            boolean jarFound = false;
            for (Path jar : stream) {
                jarFound = true;
                boolean extracted = extractAssetsFromJar(jar, assetsDir, legacyCopyDir);
                if (!extracted) {
                    LOGGER.info("[LegacyLoader] Skipped {} as it contained no assets", jar.getFileName());
                }
            }
            if (!jarFound) {
                LOGGER.warn("[LegacyLoader] No legacy jar files found in {}", modsDir);
            }
        } catch (IOException e) {
            LOGGER.error("[LegacyLoader] Failed to scan legacy mods directory {}", modsDir, e);
        }
    }

    private static boolean extractAssetsFromJar(Path jar, Path outputDir, Path duplicateDir) {
        boolean foundAsset = false;
        LOGGER.debug("[LegacyLoader] Inspecting {}", jar.getFileName());

        try (InputStream fis = Files.newInputStream(jar);
             ZipInputStream zis = new ZipInputStream(fis)) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String name = entry.getName();
                if (!entry.isDirectory() && name.startsWith("assets/")) {
                    foundAsset = true;
                    Path relative = Paths.get(name).subpath(1, Paths.get(name).getNameCount());
                    Path target = outputDir.resolve(relative);
                    Files.createDirectories(target.getParent());
                    try (OutputStream out = Files.newOutputStream(target, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                        zis.transferTo(out);
                    }
                    LOGGER.info("[LegacyLoader] Extracted {} from {}", relative, jar.getFileName());

                    Path dup = duplicateDir.resolve(relative);
                    Files.createDirectories(dup.getParent());
                    Files.copy(target, dup, StandardCopyOption.REPLACE_EXISTING);
                }
                zis.closeEntry();
            }
        } catch (IOException e) {
            LOGGER.error("[LegacyLoader] Failed extracting assets from {}", jar.getFileName(), e);
        }

        return foundAsset;
    }
}
