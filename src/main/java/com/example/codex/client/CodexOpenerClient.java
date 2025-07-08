package com.example.codex.client;

import com.example.codex.CodexMenu;
import com.example.codex.client.CodexScreen;
import com.example.compatmod.legacy.item.LegacyItem;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class CodexOpenerClient {
    public static void openScreen(LegacyItem item) {
        Minecraft.getInstance().setScreen(new CodexScreen(item));
    }

}
