package com.example.compatmod.legacy.mapping;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Maintains a mapping from legacy item/block identifiers and metadata to
 * their modern ResourceLocation counterparts.
 */
public class LegacyIdMapping {

    private static class Key {
        final String id;
        final int meta;
        Key(String id, int meta) {
            this.id = id;
            this.meta = meta;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Key key)) return false;
            return meta == key.meta && Objects.equals(id, key.id);
        }
        @Override
        public int hashCode() {
            return Objects.hash(id, meta);
        }
    }

    private static final Map<Key, ResourceLocation> ITEM_MAP = new HashMap<>();
    private static final Map<Key, ResourceLocation> BLOCK_MAP = new HashMap<>();

    /**
     * Registers a mapping from a legacy item id and metadata to a modern
     * ResourceLocation.
     */
    public static void registerItemMapping(String oldId, int metadata, ResourceLocation newId) {
        ITEM_MAP.put(new Key(oldId, metadata), newId);
    }

    /**
     * Registers a mapping from a legacy block id and metadata to a modern
     * ResourceLocation.
     */
    public static void registerBlockMapping(String oldId, int metadata, ResourceLocation newId) {
        BLOCK_MAP.put(new Key(oldId, metadata), newId);
    }

    /**
     * Resolves and retrieves the mapped {@link Item} for the given legacy
     * identifier and metadata value.
     */
    public static Item getItem(String oldId, int metadata) {
        ResourceLocation id = resolveItemId(oldId, metadata);
        return id != null ? ForgeRegistries.ITEMS.getValue(id) : null;
    }

    /**
     * Resolves and retrieves the mapped {@link Block} for the given legacy
     * identifier and metadata value.
     */
    public static Block getBlock(String oldId, int metadata) {
        ResourceLocation id = resolveBlockId(oldId, metadata);
        return id != null ? ForgeRegistries.BLOCKS.getValue(id) : null;
    }

    /**
     * Resolves the target ResourceLocation for a legacy item identifier.
     */
    public static ResourceLocation resolveItemId(String oldId, int metadata) {
        ResourceLocation id = ITEM_MAP.get(new Key(oldId, metadata));
        if (id == null && metadata != 0) {
            id = ITEM_MAP.get(new Key(oldId, 0));
        }
        return id;
    }

    /**
     * Resolves the target ResourceLocation for a legacy block identifier.
     */
    public static ResourceLocation resolveBlockId(String oldId, int metadata) {
        ResourceLocation id = BLOCK_MAP.get(new Key(oldId, metadata));
        if (id == null && metadata != 0) {
            id = BLOCK_MAP.get(new Key(oldId, 0));
        }
        return id;
    }
}
