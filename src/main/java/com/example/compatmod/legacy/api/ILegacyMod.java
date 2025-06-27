package com.example.compatmod.legacy.api;


import net.minecraft.world.entity.LivingEntity;



public interface ILegacyMod {

    default void onLoad() {}

    default void onInit() {}

    default void onEnable() {}

    default void onServerTick() {}

    default void onPlayerInteract() {}

    default void onEntityInteract(LivingEntity target) {}

    default void onChat(String messagePart) {
        // 実装するModがオーバーライド可能
    }
    default void onChatInput(String message) {}
}
