package com.example.compatmod.legacy.api;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraftforge.network.NetworkHooks;

public class LegacyPlayer {

    private final Player player;

    public LegacyPlayer(Player player) {
        this.player = player;
    }

    public ItemStack getHeldItemMainhand() {
        return player.getMainHandItem();
    }

    public boolean isCreative() {
        return player.isCreative();
    }

    public boolean isSpectator() {
        return player.isSpectator();
    }

    public String getName() {
        return player.getName().getString();
    }

    public void openGui(MenuProvider provider) {
        if (player instanceof ServerPlayer sp) {
            NetworkHooks.openScreen(sp, provider);
        }
    }

    public Player getPlayer() {
        return player;
    }
}
