package com.example.compatmod.legacy.entity;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;


public class LegacyPlayer {
    private final Player player;

    public LegacyPlayer(Player player) {
        this.player = player;
    }

    public ItemStack getHeldItemMainhand() {
        return player.getMainHandItem();
    }

    public void sendMessage(String text) {
        player.sendSystemMessage(Component.literal(text));
    }

    public boolean isSneaking() {
        return player.isCrouching();
    }

    public void addItemToInventory(ItemStack stack) {
        player.getInventory().add(stack);
    }

    public Player getHandle() {
        return player;
    }

    public void openLegacyGui(MenuProvider provider) {
        if (player instanceof ServerPlayer serverPlayer) {
            net.minecraftforge.network.NetworkHooks.openScreen(serverPlayer, provider);
        }
    }

    public net.minecraft.world.entity.player.Inventory getInventory() {
        return player.getInventory();
    }

}