package com.example.compatmod.legacy.loader;

import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraft.SharedConstants;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LegacyConfigPriorityTest {
    @Test
    public void testHighPriorityTopPosition() throws Exception {
        Path gamedir = Files.createTempDirectory("gamedir");
        String originalDir = System.getProperty("user.dir");
        System.setProperty("user.dir", gamedir.toString());
        FMLPaths.loadAbsolutePaths(gamedir);
        SharedConstants.tryDetectVersion();

        try {
            Path configDir = FMLPaths.CONFIGDIR.get();
            Files.createDirectories(configDir);
            Files.writeString(configDir.resolve("legacy_loader.toml"), "[resource_pack]\npriority = \"high\"\n");

            LegacyConfig.packPriority = LegacyConfig.PackPriority.LOW;
            LegacyConfig.load();

            Path legacy = gamedir.resolve("legacy_assets/assets/test");
            Files.createDirectories(legacy);
            Files.writeString(legacy.resolve("dummy.txt"), "a");
            Files.writeString(gamedir.resolve("legacy_assets/pack.mcmeta"), "{\"pack\":{\"pack_format\":15,\"description\":\"test\"}}\n");

            List<Pack> packs = new ArrayList<>();
            AddPackFindersEvent event = new AddPackFindersEvent(PackType.CLIENT_RESOURCES, src -> src.loadPacks(packs::add));
            LegacyModResources.onAddPackFinders(event);

            assertEquals(1, packs.size());
            assertEquals(Pack.Position.TOP, packs.get(0).getDefaultPosition());
        } finally {
            System.setProperty("user.dir", originalDir);
        }
    }

    @Test
    public void testLowPriorityBottomPosition() throws Exception {
        Path gamedir = Files.createTempDirectory("gamedir");
        String originalDir = System.getProperty("user.dir");
        System.setProperty("user.dir", gamedir.toString());
        FMLPaths.loadAbsolutePaths(gamedir);
        SharedConstants.tryDetectVersion();

        try {
            Path configDir = FMLPaths.CONFIGDIR.get();
            Files.createDirectories(configDir);
            Files.writeString(configDir.resolve("legacy_loader.toml"), "[resource_pack]\npriority = \"low\"\n");

            LegacyConfig.packPriority = LegacyConfig.PackPriority.HIGH;
            LegacyConfig.load();

            Path legacy = gamedir.resolve("legacy_assets/assets/test");
            Files.createDirectories(legacy);
            Files.writeString(legacy.resolve("dummy.txt"), "a");
            Files.writeString(gamedir.resolve("legacy_assets/pack.mcmeta"), "{\"pack\":{\"pack_format\":15,\"description\":\"test\"}}\n");

            List<Pack> packs = new ArrayList<>();
            AddPackFindersEvent event = new AddPackFindersEvent(PackType.CLIENT_RESOURCES, src -> src.loadPacks(packs::add));
            LegacyModResources.onAddPackFinders(event);

            assertEquals(1, packs.size());
            assertEquals(Pack.Position.BOTTOM, packs.get(0).getDefaultPosition());
        } finally {
            System.setProperty("user.dir", originalDir);
        }
    }
}
