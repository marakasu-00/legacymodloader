package com.example.compatmod.legacy;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.commons.lang3.function.TriFunction;
import com.example.compatmod.legacy.mapping.LegacyIdMapping;

import java.util.function.BiFunction;
import java.util.function.Supplier;

@SuppressWarnings("removal")
public class LegacyGameRegistry {

    public static final String MODID = "compatmod";

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "legacymodloader");

    public static void registerItem(Item item, String name) {
        ITEMS.register(name, () -> item);
        LegacyIdMapping.registerItemMapping(name, 0, new ResourceLocation(MODID, name));
    }

    public static void registerBlock(Block block, String name) {
        BLOCKS.register(name, () -> block);
        LegacyIdMapping.registerBlockMapping(name, 0, new ResourceLocation(MODID, name));
        // also map the block item for legacy references
        LegacyIdMapping.registerItemMapping(name, 0, new ResourceLocation(MODID, name));
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
        LegacyIdMapping.registerItemMapping(name, 0, new ResourceLocation(MODID, name));
        return ITEMS.register(name, supplier);
    }
    public static RegistryObject<Block> registerBlock(String name, Supplier<Block> supplier) {
        // ブロックを登録
        RegistryObject<Block> block = BLOCKS.register(name, supplier);
        // ブロックアイテムも同時に登録
        ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        LegacyIdMapping.registerBlockMapping(name, 0, new ResourceLocation(MODID, name));
        LegacyIdMapping.registerItemMapping(name, 0, new ResourceLocation(MODID, name));
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
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, "legacymodloader");

    static {
        // 他と同様に登録
        MENUS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    public static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> registerBlockEntity(
            String name,
            TriFunction<BlockEntityType<T>, BlockPos, BlockState, T> factory,
            RegistryObject<Block> block
    ) {
        final RegistryObject<BlockEntityType<T>>[] holder = new RegistryObject[1];

        holder[0] = BLOCK_ENTITIES.register(name,
                () -> BlockEntityType.Builder.of(
                        (pos, state) -> factory.apply(holder[0].get(), pos, state),
                        block.get()
                ).build(null)
        );

        return holder[0];
    }

    public static Item getMappedItem(String oldId, int metadata) {
        return LegacyIdMapping.getItem(oldId, metadata);
    }

    public static Block getMappedBlock(String oldId, int metadata) {
        return LegacyIdMapping.getBlock(oldId, metadata);
    }


}