package com.example.compatmod.legacy.loader;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.example.compatmod.legacy.loader.LegacyPaths.LEGACY_MISC_PATH;

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
        Path miscOutputDir = gameDir.resolve(LEGACY_MISC_PATH);

        if (!Files.isDirectory(modsDir)) {
            LOGGER.warn("[LegacyLoader] No legacy mod directory found at {}", modsDir);
            return;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(modsDir)) {
            boolean archiveFound = false;
            for (Path archive : stream) {
                String name = archive.getFileName().toString().toLowerCase();
                if (name.endsWith(".jar") || name.endsWith(".zip")) {
                    archiveFound = true;
                    boolean extracted = extractAssetsFromArchive(archive, assetsDir, legacyCopyDir, miscOutputDir);
                    if (!extracted) {
                        LOGGER.info("[LegacyLoader] Skipped {} as it contained no assets", archive.getFileName());
                    }
                }
            }
            if (!archiveFound) {
                LOGGER.warn("[LegacyLoader] No legacy jar files found in {}", modsDir);
            }
        } catch (IOException e) {
            LOGGER.error("[LegacyLoader] Failed to scan legacy mods directory {}", modsDir, e);
        }
    }

    private static boolean extractAssetsFromArchive(Path archive, Path outputDir, Path duplicateDir, Path miscDir) {
        boolean foundAsset = false;
        LOGGER.info("[Codex] Extracting legacy resources from {}", archive.getFileName());

        try (ZipFile zipFile = new ZipFile(archive.toFile())) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                if (entry.isDirectory()) {
                    continue;
                }

                if (name.startsWith("assets/")) {
                    foundAsset = true;
                    Path relative = Paths.get(name).subpath(1, Paths.get(name).getNameCount());
                    Path target = outputDir.resolve(relative);
                    Files.createDirectories(target.getParent());
                    try (InputStream in = zipFile.getInputStream(entry); OutputStream out = Files.newOutputStream(target, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                        in.transferTo(out);
                    }
                    LOGGER.info("[LegacyLoader] Extracted {} from {}", relative, archive.getFileName());

                    Path dup = duplicateDir.resolve(relative);
                    Files.createDirectories(dup.getParent());
                    Files.copy(target, dup, StandardCopyOption.REPLACE_EXISTING);
                } else if (isLegacyResource(name)) {
                    foundAsset = true;
                    Path target = miscDir.resolve(archive.getFileName().toString()).resolve(name);
                    Files.createDirectories(target.getParent());
                    try (InputStream in = zipFile.getInputStream(entry)) {
                        Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("[LegacyLoader] Failed extracting assets from {}", archive.getFileName(), e);
        }

        return foundAsset;
    }

    private static final Set<String> LEGACY_RESOURCE_PREFIXES = Set.of(
            "textures/", "models/", "sounds/", "obj/", "skin/", "rtm/", "ngtlib/"
    );

    private static final Set<String> LEGACY_EXTENSIONS = Set.of(
            ".png", ".ogg", ".wav", ".mp3", ".json", ".obj", ".mtl", ".txt", ".cfg", ".dat", ".mcmeta", ".jpg", ".jpeg"
    );

    /**
     * Determines whether the given entry path represents a legacy resource that should be copied.
     */
    static boolean isLegacyResource(String name) {
        String lower = name.toLowerCase();
        for (String prefix : LEGACY_RESOURCE_PREFIXES) {
            if (lower.startsWith(prefix)) {
                for (String ext : LEGACY_EXTENSIONS) {
                    if (lower.endsWith(ext)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
