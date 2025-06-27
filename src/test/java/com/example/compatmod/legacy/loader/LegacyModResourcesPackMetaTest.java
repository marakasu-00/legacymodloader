package com.example.compatmod.legacy.loader;

import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraft.SharedConstants;
import net.minecraftforge.fml.loading.FMLPaths;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LegacyModResourcesPackMetaTest {
    @Test
    public void testPackRegistersWithoutMcmeta() throws Exception {
        Path gamedir = Files.createTempDirectory("gamedir");
        String original = System.getProperty("user.dir");
        System.setProperty("user.dir", gamedir.toString());
        FMLPaths.loadAbsolutePaths(gamedir);
        SharedConstants.tryDetectVersion();

        try {
            Path asset = gamedir.resolve("legacy_assets/assets/testmod/sample.txt");
            Files.createDirectories(asset.getParent());
            Files.writeString(asset, "hello");

            List<RepositorySource> sources = new ArrayList<>();
            AddPackFindersEvent event = new AddPackFindersEvent(PackType.CLIENT_RESOURCES, sources::add);
            LegacyModResources.onAddPackFinders(event);

            List<Pack> packs = new ArrayList<>();
            for (RepositorySource src : sources) {
                src.loadPacks(packs::add);
            }

            assertEquals(1, packs.size(), "Pack should register even without pack.mcmeta");
            assertTrue(Files.exists(gamedir.resolve("legacy_assets/pack.mcmeta")), "pack.mcmeta should be created");
        } finally {
            System.setProperty("user.dir", original);
        }
    }
}
