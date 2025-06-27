package com.example.compatmod.legacy.loader;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import com.example.compatmod.legacy.loader.LegacyResourcePack;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import com.example.compatmod.legacy.loader.LegacyConfig;
import com.example.compatmod.config.ConfigHandler;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.compatmod.legacy.loader.LegacyPaths.LEGACY_ASSETS_PATH;

@Mod.EventBusSubscriber(modid = "legacymodloader", bus = Mod.EventBusSubscriber.Bus.MOD)
public class LegacyModResources {

    /**
     * Verify that resources under the given legacy assets path do not clash with
     * already present resources.
     */
    public static void verifyNoDuplicateResources(Path legacyAssetsPath, LegacyConfig.PackPriority priority) {
        try {
            Set<String> legacy = collectResources(legacyAssetsPath.resolve("assets"));
            Set<String> existing = collectExistingResources();
            Set<String> dup = legacy.stream().filter(existing::contains).collect(Collectors.toSet());

            if (!dup.isEmpty()) {
                String msg = "[LegacyLoader] Duplicate legacy assets detected: " + dup;
                if (priority == LegacyConfig.PackPriority.HIGH) {
                    System.err.println(msg);
                    throw new IllegalStateException("Legacy assets conflict with existing resources");
                } else {
                    System.out.println("[LegacyLoader] Warning: " + msg);
                }
            }
        } catch (IOException e) {
            System.err.println("[LegacyLoader] Failed scanning legacy assets");
        }
    }

    private static Set<String> collectExistingResources() throws IOException {
        Set<String> all = new HashSet<>();
        Path gameDir = FMLPaths.GAMEDIR.get();
        Path resources = gameDir.resolve("resources").resolve("assets");
        if (Files.exists(resources)) {
            all.addAll(collectResources(resources));
        }
        Path packsDir = gameDir.resolve("resourcepacks");
        if (Files.isDirectory(packsDir)) {
            try (Stream<Path> stream = Files.list(packsDir)) {
                for (Path p : stream.toList()) {
                    if (Files.isDirectory(p)) {
                        all.addAll(collectResources(p.resolve("assets")));
                    } else if (p.toString().endsWith(".zip")) {
                        all.addAll(collectResourcesFromZip(p));
                    }
                }
            }
        }
        return all;
    }

    private static Set<String> collectResourcesFromZip(Path zip) throws IOException {
        Set<String> set = new HashSet<>();
        try (FileSystem fs = FileSystems.newFileSystem(zip, (ClassLoader) null)) {
            Path assets = fs.getPath("assets");
            if (Files.exists(assets)) {
                set.addAll(collectResources(assets));
            }
        }
        return set;
    }

    private static Set<String> collectResources(Path root) throws IOException {
        Set<String> set = new HashSet<>();
        if (!Files.exists(root)) return set;
        try (Stream<Path> stream = Files.walk(root)) {
            stream.filter(Files::isRegularFile).forEach(p -> {
                String rel = root.relativize(p).toString().replace(java.io.File.separatorChar, '/');
                set.add(rel);
            });
        }
        return set;
    }

    public static boolean checkLegacyAssetsExist() {
        Path legacyAssetsPath = FMLPaths.GAMEDIR.get().resolve(LEGACY_ASSETS_PATH);

        if (!legacyAssetsPath.toFile().exists()) {
            System.out.println("[LegacyLoader] No legacy assets found at: " + legacyAssetsPath);
            return false;
        }

        System.out.println("[LegacyLoader] Legacy assets detected at: " + legacyAssetsPath);
        return true;
    }

    /**
     * Convert a {@link LegacyConfig.PackPriority} to the corresponding
     * {@link ConfigHandler.Priority} enum value.
     */
    public static ConfigHandler.Priority toConfigPriority(LegacyConfig.PackPriority priority) {
        if (priority == null) {
            return ConfigHandler.Priority.LOW;
        }
        return priority == LegacyConfig.PackPriority.HIGH
                ? ConfigHandler.Priority.HIGH
                : ConfigHandler.Priority.LOW;
    }

    @SubscribeEvent
    public static void onAddPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            Path legacyAssetsPath = FMLPaths.GAMEDIR.get().resolve(LEGACY_ASSETS_PATH);

            if (legacyAssetsPath.toFile().exists()) {
                System.out.println("[LegacyLoader] Registering legacy assets path: " + legacyAssetsPath);

                LegacyConfig.PackPriority priority = LegacyConfig.packPriority;
                verifyNoDuplicateResources(legacyAssetsPath, priority);
                ConfigHandler.Priority configPriority = toConfigPriority(priority);

                event.addRepositorySource(consumer -> {

                    Pack.Position position = configPriority == ConfigHandler.Priority.HIGH ? Pack.Position.TOP : Pack.Position.BOTTOM;

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
                });
            } else {
                System.out.println("[LegacyLoader] No legacy assets found to register at: " + legacyAssetsPath);
            }
        }
    }
}
