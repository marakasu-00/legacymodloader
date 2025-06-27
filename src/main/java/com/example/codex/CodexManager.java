package com.example.codex;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class CodexManager {
    public static void openCodexScreen() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> com.example.codex.client.ClientCodexOpener::openScreen);
    }
}
