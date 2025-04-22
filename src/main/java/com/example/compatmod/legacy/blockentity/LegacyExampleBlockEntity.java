package com.example.compatmod.legacy.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LegacyExampleBlockEntity extends BlockEntity {
    public LegacyExampleBlockEntity(BlockPos pos, BlockState state) {
        super(LegacyExampleBlockEntities.EXAMPLE.get(), pos, state);
    }
}
