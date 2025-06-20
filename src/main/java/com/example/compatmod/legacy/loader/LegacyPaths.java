package com.example.compatmod.legacy.loader;

import java.nio.file.Path;
import java.nio.file.Paths;

/** Utility constants for legacy-related paths. */
public final class LegacyPaths {
    /** Path where legacy assets are extracted and looked for. */
    public static final Path LEGACY_ASSETS_PATH = Paths.get("resources", "assets");

    /** Path where non standard legacy resources are copied. */
    public static final Path LEGACY_MISC_PATH = Paths.get("resources", "legacy_misc");

    private LegacyPaths() {
    }
}
