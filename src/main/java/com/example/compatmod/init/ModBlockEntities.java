package com.example.compatmod.init;

import com.example.compatmod.legacy.LegacyGameRegistry;
import com.example.compatmod.legacy.blockentity.MyFurnaceBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static RegistryObject<BlockEntityType<MyFurnaceBlockEntity>> MY_FURNACE;

    public static void register() {
        RegistryObject<Block> furnaceBlock = LegacyGameRegistry.registerBlock("legacy_furnace",
                () -> new Block(Block.Properties.of().strength(3.5F)));

        MY_FURNACE = LegacyGameRegistry.registerBlockEntity("legacy_furnace",
                (type,pos,state) -> new MyFurnaceBlockEntity(type,pos,state),
                furnaceBlock);
    }
}
