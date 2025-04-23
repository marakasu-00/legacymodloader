package com.example.compatmod.legacy.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;

public class LegacyTileHelper {

    public static CompoundTag getTileData(BlockEntity tile) {
        return tile.getUpdateTag();
    }

    public static void markDirty(BlockEntity tile) {
        tile.setChanged(); // 1.20 equivalent of markDirty()
    }

    public static void sync(BlockEntity tile) {
        if (tile.getLevel() instanceof ServerLevel serverLevel) {
            if (tile.getUpdatePacket() instanceof ClientboundBlockEntityDataPacket packet) {
                for (ServerPlayer player : serverLevel.players()) {
                    player.connection.send(packet);
                }
            }
        }
    }

}
