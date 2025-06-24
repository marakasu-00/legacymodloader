package com.example.compatmod.legacy.loader;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import static com.example.compatmod.legacy.loader.LegacyConfig.packPriority;
import static com.example.compatmod.legacy.loader.LegacyConfig.PackPriority.HIGH;

import java.io.File;
import java.nio.file.Path;

import static com.example.compatmod.legacy.loader.LegacyPaths.LEGACY_ASSETS_PATH;

@Mod.EventBusSubscriber(modid = "legacymodloader", bus = Mod.EventBusSubscriber.Bus.MOD)
public class LegacyModResources {

    public static boolean checkLegacyAssetsExist() {
        Path legacyAssetsPath = FMLPaths.GAMEDIR.get().resolve(LEGACY_ASSETS_PATH);

        if (!legacyAssetsPath.toFile().exists()) {
            System.out.println("[LegacyLoader] No legacy assets found at: " + legacyAssetsPath);
            return false;
        }

        System.out.println("[LegacyLoader] Legacy assets detected at: " + legacyAssetsPath);
        return true;
    }

    @SubscribeEvent
    public static void onAddPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            Path legacyAssetsPath = FMLPaths.GAMEDIR.get().resolve(LEGACY_ASSETS_PATH);

            if (legacyAssetsPath.toFile().exists()) {
                System.out.println("[LegacyLoader] Registering legacy assets path: " + legacyAssetsPath);

                event.addRepositorySource(consumer -> {
                    Pack.Position pos = packPriority == HIGH ? Pack.Position.TOP : Pack.Position.BOTTOM;
                    Pack pack = Pack.readMetaAndCreate(
                            "legacy_assets",
                            Component.literal("Legacy Assets"), // ここ重要！
                            true,
                            (factory) -> new PathPackResources("legacy_assets", legacyAssetsPath, true),
                            PackType.CLIENT_RESOURCES,
                            pos,
                            PackSource.BUILT_IN
                    );

                    if (pack != null) {
                        consumer.accept(pack);
                    }
                });
            } else {
                System.out.println("[LegacyLoader] No legacy assets found to register at: " + legacyAssetsPath);
            }
        }
    }
}
