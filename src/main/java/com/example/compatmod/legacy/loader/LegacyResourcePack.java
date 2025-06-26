package com.example.compatmod.legacy.loader;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.resources.IoSupplier;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Set;

/**
 * Resource pack implementation that restricts access to a set of safe
 * sub folders. It simply delegates to {@link PathPackResources} after
 * verifying that the requested path falls under an allowed directory.
 */
public class LegacyResourcePack extends PathPackResources {

    private static final Set<String> ALLOWED_PREFIXES = Set.of(
            "assets/",
            "textures/",
            "models/",
            "sounds/",
            "lang/",
            "legacy_misc/"
    );

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            ".png", ".json", ".lang", ".ogg", ".mcmeta", ".mqo", ".mqoz", ".obj", ".js"
    );

    private final Path root;

    public LegacyResourcePack(String name, Path root, boolean builtin) {
        super(name, root, builtin);
        this.root = root.normalize();
    }

    private static boolean isAllowed(String path) {
        String normalized = path.replace('\\', '/');
        for (String prefix : ALLOWED_PREFIXES) {
            if (normalized.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    private boolean validate(Path target) {
        Path normalized = target.normalize();
        if (!normalized.startsWith(root)) {
            return false;
        }
        Path rel = root.relativize(normalized);
        String pathStr = rel.toString().replace('\\', '/');
        if (!isAllowed(pathStr)) {
            return false;
        }
        String lower = pathStr.toLowerCase();
        for (String ext : ALLOWED_EXTENSIONS) {
            if (lower.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public IoSupplier<InputStream> getResource(PackType type, ResourceLocation location) {
        Path target = root.resolve(type.getDirectory())
                .resolve(location.getNamespace())
                .resolve(location.getPath());
        if (!validate(target)) {
            return null;
        }
        return super.getResource(type, location);
    }

    @Override
    public void listResources(PackType type, String namespace, String path, ResourceOutput output) {
        super.listResources(type, namespace, path, (rl, sup) -> {
            Path target = root.resolve(type.getDirectory())
                    .resolve(rl.getNamespace())
                    .resolve(rl.getPath());
            if (validate(target)) {
                output.accept(rl, sup);
            }
        });
    }
}
