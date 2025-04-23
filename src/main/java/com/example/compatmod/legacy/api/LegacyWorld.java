package com.example.compatmod.legacy.api;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LegacyWorld {

    private final Level level;

    public LegacyWorld(Level level) {
        this.level = level;
    }

    public BlockState getBlockState(BlockPos pos) {
        return level.getBlockState(pos);
    }

    public void setBlock(BlockPos pos, BlockState state) {
        level.setBlockAndUpdate(pos, state);
    }

    public BlockEntity getBlockEntity(BlockPos pos) {
        return level.getBlockEntity(pos);
    }

    public boolean isClient() {
        return level.isClientSide();
    }

    public boolean isServer() {
        return !level.isClientSide();
    }

    public void spawnEntity(Entity entity) {
        if (level instanceof ServerLevel server) {
            server.addFreshEntity(entity);
        }
    }

    public Level getLevel() {
        return level;
    }
}
