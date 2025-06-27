package com.example.compatmod.legacy.loader;

import net.minecraftforge.fml.loading.FMLPaths;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Simple loader for legacy_loader.toml configuration.
 */
public final class LegacyConfig {

    private static final Logger LOGGER = LogUtils.getLogger();

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
            LOGGER.info("[LegacyLoader] No config found, using defaults");
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
            LOGGER.info("[LegacyLoader] Loaded resource pack priority: {}", packPriority);
        } catch (IOException e) {
            LOGGER.error("[LegacyLoader] Failed to load config: {}", e.getMessage());
        }
    }
}
