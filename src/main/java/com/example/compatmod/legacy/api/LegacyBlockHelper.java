package com.example.compatmod.legacy.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LegacyBlockHelper {

    public static BlockState getBlockState(Level world, BlockPos pos) {
        return world.getBlockState(pos);
    }

    public static void setBlockState(Level world, BlockPos pos, BlockState state) {
        world.setBlock(pos, state, 3); // 3 = notify neighbors and client update
    }

    public static BlockEntity getBlockEntity(Level world, BlockPos pos) {
        return world.getBlockEntity(pos);
    }
}
