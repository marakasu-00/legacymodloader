package com.example.compatmod.legacy.api;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class LegacyEntityManager {

    public static Entity getEntityByID(Level level, int id) {
        return level.getEntity(id);
    }

    public static void removeEntity(Level level, Entity entity) {
        if (!level.isClientSide) {
            entity.discard(); // サーバー上でのみ削除
        }
    }
}
