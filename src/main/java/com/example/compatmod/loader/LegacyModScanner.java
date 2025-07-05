package com.example.compatmod.loader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class LegacyModScanner {
    private static final String LEGACY_DIR = "mods/legacy";

    public static Set<String> scanLegacyModIds() {
        Set<String> modIds = new HashSet<>();
        Path modsPath = Path.of(LEGACY_DIR);

        if (!Files.exists(modsPath)) return Set.of();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(modsPath, "*.{jar,zip}")) {
            for (Path modFile : stream) {
                String modId = extractModIdFromArchive(modFile);
                if (modId != null) modIds.add(modId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return modIds;
    }

    private static String extractModIdFromArchive(Path archivePath) {
        try (ZipFile zip = new ZipFile(archivePath.toFile())) {
            // Try mods.toml
            ZipEntry toml = zip.getEntry("META-INF/mods.toml");
            if (toml != null) {
                try (InputStream in = zip.getInputStream(toml)) {
                    return readModIdFromToml(in);
                }
            }

            // Try mcmod.info
            ZipEntry info = zip.getEntry("mcmod.info");
            if (info != null) {
                try (InputStream in = zip.getInputStream(info)) {
                    return readModIdFromInfo(in);
                }
            }

            // Fallback: file name
            String fallback = archivePath.getFileName().toString().split("\\.")[0].toLowerCase();
            return fallback;

        } catch (IOException e) {
            return null;
        }
    }

    private static String readModIdFromToml(InputStream in) throws IOException {
        try (Scanner scanner = new Scanner(in)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.startsWith("modId")) {
                    return line.split("=")[1].replace("\"", "").trim();
                }
            }
        }
        return null;
    }

    private static String readModIdFromInfo(InputStream in) throws IOException {
        try (Scanner scanner = new Scanner(in)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.contains("\"modid\"")) {
                    return line.split(":")[1].replace("\"", "").replace(",", "").trim();
                }
            }
        }
        return null;
    }
}
