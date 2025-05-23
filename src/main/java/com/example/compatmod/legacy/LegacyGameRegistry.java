package com.example.compatmod.legacy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.BiFunction;
import java.util.function.Supplier;

@SuppressWarnings("removal")
public class LegacyGameRegistry {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "compatmod");
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "compatmod");
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "legacymodloader");

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
    static {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        com.example.compatmod.legacy.api.CreativeTabs.TABS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    public static RegistryObject<Item> registerItem(String name, Supplier<Item> supplier) {
        return ITEMS.register(name, supplier);
    }
    public static RegistryObject<Block> registerBlock(String name, Supplier<Block> supplier) {
        // ブロックを登録
        RegistryObject<Block> block = BLOCKS.register(name, supplier);
        // ブロックアイテムも同時に登録
        ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }
    public static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> registerBlockEntity(
            String name,
            BiFunction<BlockPos, BlockState, T> factory,
            RegistryObject<Block> block
    ) {
        return BLOCK_ENTITIES.register(name,
                () -> BlockEntityType.Builder.of(factory::apply, block.get()).build(null));
    }
}