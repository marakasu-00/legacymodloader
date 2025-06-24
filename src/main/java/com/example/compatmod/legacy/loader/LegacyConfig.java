package com.example.compatmod.legacy.loader;

import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Simple loader for legacy_loader.toml configuration.
 */
public final class LegacyConfig {

    public enum PackPriority { HIGH, LOW }

    private static final String CONFIG_FILE = "legacy_loader.toml";

    public static PackPriority packPriority = PackPriority.LOW;

    private LegacyConfig() {}

    /**
     * Loads the configuration file from the config directory.
     */
    public static void load() {
        Path file = FMLPaths.CONFIGDIR.get().resolve(CONFIG_FILE);
        if (!Files.exists(file)) {
            System.out.println("[LegacyLoader] No config found, using defaults");
            return;
        }

        try {
            List<String> lines = Files.readAllLines(file);
            boolean inSection = false;
            for (String rawLine : lines) {
                String line = rawLine.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                if (line.startsWith("[") && line.endsWith("]")) {
                    inSection = line.equalsIgnoreCase("[resource_pack]");
                    continue;
                }
                if (inSection && line.startsWith("priority")) {
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        String value = parts[1].trim().replace("\"", "").replace("'", "").toLowerCase();
                        if ("high".equals(value)) {
                            packPriority = PackPriority.HIGH;
                        } else {
                            packPriority = PackPriority.LOW;
                        }
                    }
                }
            }
            System.out.println("[LegacyLoader] Loaded resource pack priority: " + packPriority);
        } catch (IOException e) {
            System.err.println("[LegacyLoader] Failed to load config: " + e.getMessage());
        }
    }
}
