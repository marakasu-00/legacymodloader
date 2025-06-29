package com.example.codex.client;

import com.example.codex.CodexMenus;
import com.example.codex.CodexMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ClientCodexOpener {
    public static void openScreen() {
        Inventory inv = Minecraft.getInstance().player.getInventory();
        Minecraft.getInstance().setScreen(
                new CodexScreen(new CodexMenu(0, inv, null), inv, Component.literal("Codex"))
        );
        // init() は絶対に呼ばない
    }


}
