package com.example.compatmod.legacy.loader;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class LegacyResourcePackTest {
    @Test
    public void testAllowedResourceAccessible() throws Exception {
        Path root = Files.createTempDirectory("pack");
        Path file = root.resolve("assets/testmod/textures/ok.json");
        Files.createDirectories(file.getParent());
        Files.writeString(file, "hello");

        LegacyResourcePack pack = new LegacyResourcePack("test", root, true);
        ResourceLocation rl = new ResourceLocation("testmod", "textures/ok.json");
        assertNotNull(pack.getResource(PackType.CLIENT_RESOURCES, rl), "Allowed resource should be served");
    }

    @Test
    public void testPathTraversalDenied() throws Exception {
        Path root = Files.createTempDirectory("pack");
        Files.createDirectories(root.resolve("assets/testmod"));
        Path secret = root.resolveSibling("secret.txt");
        Files.writeString(secret, "bad");

        LegacyResourcePack pack = new LegacyResourcePack("test", root, true);
        ResourceLocation rl = new ResourceLocation("testmod", "../secret.txt");
        assertNull(pack.getResource(PackType.CLIENT_RESOURCES, rl), "Traversal outside pack should be denied");
    }

    @Test
    public void testDisallowedExtensionDenied() throws Exception {
        Path root = Files.createTempDirectory("pack");
        Path file = root.resolve("assets/testmod/textures/bad.txt");
        Files.createDirectories(file.getParent());
        Files.writeString(file, "bad");

        LegacyResourcePack pack = new LegacyResourcePack("test", root, true);
        ResourceLocation rl = new ResourceLocation("testmod", "textures/bad.txt");
        assertNull(pack.getResource(PackType.CLIENT_RESOURCES, rl), "Disallowed extension should be denied");
    }
}
