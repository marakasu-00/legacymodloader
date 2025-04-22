package com.example.compatmod.legacy.api;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraftforge.network.NetworkHooks;

public class GuiHandler {
    public static void openGui(ServerPlayer player, MenuProvider provider) {
        NetworkHooks.openScreen(player, provider);
    }

    public static void openGui(ServerPlayer player, MenuProvider provider, net.minecraft.core.BlockPos pos) {
        NetworkHooks.openScreen(player, provider, buf -> buf.writeBlockPos(pos));
    }
}
