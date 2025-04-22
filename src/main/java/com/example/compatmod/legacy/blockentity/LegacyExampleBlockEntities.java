package com.example.compatmod.legacy.blockentity;

import com.example.compatmod.legacy.LegacyGameRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

public class LegacyExampleBlockEntities {
    public static RegistryObject<BlockEntityType<LegacyExampleBlockEntity>> EXAMPLE;

    public static void register() {
        // まず、RegistryObject<Block> を取得
        RegistryObject<Block> exampleBlock = LegacyGameRegistry.registerBlock("legacy_block", () ->
                new Block(Block.Properties.of().strength(1.5F, 6.0F)));

        // RegistryObject<Block> を使用して BlockEntity を登録
        EXAMPLE = LegacyGameRegistry.registerBlockEntity("legacy_example",
                (pos, state) -> new LegacyExampleBlockEntity(pos, state),
                exampleBlock
        );
    }
}