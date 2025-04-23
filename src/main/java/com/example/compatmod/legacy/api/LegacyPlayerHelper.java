package com.example.compatmod.legacy.api;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class LegacyPlayerHelper {

    public static void sendMessage(Player player, String message) {
        player.sendSystemMessage(Component.literal(message));
    }

    public static void addPotionEffect(Player player, MobEffectInstance effect) {
        player.addEffect(effect);
    }

    public static ItemStack getHeldItemMainhand(Player player) {
        return player.getMainHandItem();
    }

    public static ItemStack getHeldItemOffhand(Player player) {
        return player.getOffhandItem();
    }
}
