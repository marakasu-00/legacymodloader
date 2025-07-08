package com.example.codex.client;

import com.example.compatmod.legacy.item.LegacyItem;
import net.minecraft.client.Minecraft;

public class ClientCodexOpener {
    public static void openScreen(LegacyItem item) {
        Minecraft.getInstance().setScreen(new CodexScreen(item));
    }
}

