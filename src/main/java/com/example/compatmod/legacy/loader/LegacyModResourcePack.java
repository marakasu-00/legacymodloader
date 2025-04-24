package com.example.compatmod.legacy.loader;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PackResources;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceMetadata;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class LegacyModResourcePack implements PackResources {

    private final Map<ResourceLocation, byte[]> resources;

    public LegacyModResourcePack(Map<ResourceLocation, byte[]> resources) {
        this.resources = resources;
    }

    @Override
    public InputStream getRootResource(String fileName) throws IOException {
        return null; // 通常は null（pack.png や pack.mcmeta に対応する場合は実装）
    }

    @Override
    public InputStream getResource(PackType type, ResourceLocation location) throws IOException {
        byte[] data = resources.get(location);
        return data != null ? new java.io.ByteArrayInputStream(data) : null;
    }

    @Override
    public boolean hasResource(PackType type, ResourceLocation location) {
        return resources.containsKey(location);
    }

    @Override
    public Set<String> getNamespaces(PackType type) {
        return Set.of("legacy"); // 実際に含まれている modid を返すのが望ましい
    }

    @Override
    public void close() {
        // リソースクローズ処理が必要な場合はここに
    }

    @Override
    public String getName() {
        return "LegacyModResourcePack";
    }

    @Override
    public Optional<ResourceMetadata> getResourceMetadata(PackType type, ResourceLocation location) {
        return Optional.empty();
    }

    @Override
    public void listResources(PackType type, String namespace, String path, Predicate<ResourceLocation> locationFilter, java.util.function.BiConsumer<ResourceLocation, Resource> output) {
        resources.keySet().stream()
                .filter(location -> location.getNamespace().equals(namespace) && location.getPath().startsWith(path))
                .filter(locationFilter)
                .forEach(location -> {
                    try {
                        Resource resource = new Resource(() -> getResource(type, location), () -> ResourceMetadata.EMPTY);
                        output.accept(location, resource);
                    } catch (IOException ignored) {}
                });
    }
}
