package com.example.compatmod.legacy;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LegacyGameRegistry {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "compatmod");
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "compatmod");

    public static void registerItem(Item item, String name) {
        ITEMS.register(name, () -> item);
    }

    public static void registerBlock(Block block, String name) {
        BLOCKS.register(name, () -> block);
    }

    public static DeferredRegister<Item> getItemRegister() {
        return ITEMS;
    }

    public static DeferredRegister<Block> getBlockRegister() {
        return BLOCKS;
    }
}