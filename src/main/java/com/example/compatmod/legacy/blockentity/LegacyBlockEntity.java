package com.example.compatmod.legacy.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;

public abstract class LegacyBlockEntity extends BlockEntity {

    public LegacyBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void markDirty() {
        setChanged();
    }

    public CompoundTag getLegacyNBT() {
        return saveWithFullMetadata();
    }

    public void loadLegacyNBT(CompoundTag tag) {
        load(tag);
    }
}
