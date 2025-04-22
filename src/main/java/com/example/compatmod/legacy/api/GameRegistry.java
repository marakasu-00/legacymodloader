package com.example.compatmod.legacy.api;

import com.example.compatmod.legacy.LegacyGameRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class GameRegistry {

    public static void registerItem(Supplier<Item> itemSupplier, String name) {
        System.out.println("[CompatAPI] GameRegistry.registerItem() called for: " + name);
        LegacyGameRegistry.registerItem(name, itemSupplier);
    }
    public static void registerBlock(Supplier<Block> blockSupplier, String name) {
        System.out.println("[CompatAPI] GameRegistry.registerBlock() called for: " + name);
        LegacyGameRegistry.registerBlock(name, blockSupplier);
    }
    public static <T extends BlockEntity> void registerTileEntity(
            String name,
            BiFunction<BlockPos, BlockState, T> factory,
            Supplier<Block> blockSupplier
    ) {
        RegistryObject<Block> block = LegacyGameRegistry.registerBlock(name, blockSupplier);
        LegacyGameRegistry.registerBlockEntity(name, factory, block);
    }
}
