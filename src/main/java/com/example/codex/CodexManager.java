package com.example.codex;

import com.example.compatmod.legacy.item.LegacyItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class CodexManager {
    public static void openCodexScreen() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            com.example.codex.client.CodexOpenerClient.openScreen();
        });
    }
}

