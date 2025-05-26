package com.example.compatmod.legacy.blockentity;

import com.example.compatmod.legacy.blockentity.LegacyBlockEntity;
import com.example.compatmod.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MyFurnaceBlockEntity extends LegacyBlockEntity {
    public MyFurnaceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    // tick処理などあればここに
    public void tick() {
        markDirty();
    }
}
