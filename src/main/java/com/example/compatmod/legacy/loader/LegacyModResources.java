package com.example.compatmod.legacy.loader;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;


import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.compatmod.legacy.loader.LegacyPaths.LEGACY_ASSETS_PATH;

@Mod.EventBusSubscriber(modid = "legacymodloader", bus = Mod.EventBusSubscriber.Bus.MOD)
public class LegacyModResources {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static boolean checkLegacyAssetsExist() {
        Path legacyAssetsPath = FMLPaths.GAMEDIR.get().resolve(LEGACY_ASSETS_PATH);

        if (!legacyAssetsPath.toFile().exists()) {
            LOGGER.warn("[LegacyLoader] No legacy assets found at: {}", legacyAssetsPath);
            return false;
        }

        LOGGER.info("[LegacyLoader] Legacy assets detected at: {}", legacyAssetsPath);
        return true;
    }

    @SubscribeEvent
    public static void onAddPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            Path legacyAssetsPath = FMLPaths.GAMEDIR.get().resolve(LEGACY_ASSETS_PATH);

            if (legacyAssetsPath.toFile().exists()) {
                LOGGER.info("[LegacyLoader] Registering legacy assets path: {}", legacyAssetsPath);

                ConfigHandler.Priority priority = ConfigHandler.getLegacyAssetPrioritySafe();
                verifyNoDuplicateResources(legacyAssetsPath, priority);

                event.addRepositorySource(consumer -> {

                    Pack.Position position = priority == ConfigHandler.Priority.HIGH ? Pack.Position.TOP : Pack.Position.BOTTOM;

                    Pack pack = Pack.readMetaAndCreate(
                            "legacy_assets",
                            Component.literal("Legacy Assets"),
                            true,
                            (factory) -> new PathPackResources("legacy_assets", legacyAssetsPath, true),
                            PackType.CLIENT_RESOURCES,

                            position,

                            PackSource.BUILT_IN
                    );

                    if (pack != null) {
                        consumer.accept(pack);
                    }
                });
            } else {
                LOGGER.warn("[LegacyLoader] No legacy assets found to register at: {}", legacyAssetsPath);
            }
        }
    }
}
