package com.example.compatmod.legacy.api;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class LegacyEntitySpawner {

    public static boolean spawnEntity(Level world, Entity entity) {
        if (world instanceof ServerLevel serverLevel) {
            return serverLevel.addFreshEntity(entity);
        }
        return false; // クライアント側では生成しない
    }
}
