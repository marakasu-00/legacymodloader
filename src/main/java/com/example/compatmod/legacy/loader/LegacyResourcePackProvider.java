package com.example.compatmod.legacy.loader;

import com.example.compatmod.config.ConfigHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;

import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * RepositorySource that supplies the LegacyResourcePack when requested.
 */
public class LegacyResourcePackProvider implements RepositorySource {

    private final Path legacyAssetsPath;
    private final ConfigHandler.Priority priority;

    public LegacyResourcePackProvider(Path legacyAssetsPath, ConfigHandler.Priority priority) {
        this.legacyAssetsPath = legacyAssetsPath;
        this.priority = priority;
    }

    @Override
    public void loadPacks(Consumer<Pack> consumer) {
        Pack.Position position = priority == ConfigHandler.Priority.HIGH ?
                Pack.Position.TOP : Pack.Position.BOTTOM;

        Pack pack = Pack.readMetaAndCreate(
                "legacy_assets",
                Component.literal("Legacy Assets"),
                true,
                (factory) -> new LegacyResourcePack("legacy_assets", legacyAssetsPath, true),
                PackType.CLIENT_RESOURCES,
                position,
                PackSource.BUILT_IN
        );

        if (pack != null) {
            consumer.accept(pack);
        }
    }
}
