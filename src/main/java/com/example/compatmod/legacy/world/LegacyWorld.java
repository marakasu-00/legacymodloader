package com.example.compatmod.legacy.world;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class LegacyWorld {
    private final Level world;

    public LegacyWorld(Level world) {
        this.world = world;
    }

    public boolean isRemote() {
        return world.isClientSide();
    }

    public BlockState getBlockState(BlockPos pos) {
        return world.getBlockState(pos);
    }

    public void setBlock(BlockPos pos, Block block) {
        world.setBlock(pos, block.defaultBlockState(), 3);
    }

    public void setBlockState(BlockPos pos, BlockState state) {
        world.setBlock(pos, state, 3);
    }

    public void spawnEntity(Entity entity) {
        if (!world.isClientSide && world instanceof ServerLevel serverLevel) {
            serverLevel.addFreshEntity(entity);
        }
    }

    public Level getHandle() {
        return world;
    }
}
