package com.example.compatmod;

import com.example.compatmod.legacy.loader.LegacyModJarLoader;
import com.example.compatmod.legacy.loader.LegacyModManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.io.File;

@Mod("legacymodloader")
public class LegacyModLoader {
    @SuppressWarnings("removal")
    public LegacyModLoader() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // レガシーMODロード開始
        File modsFolder = new File("mods");
        LegacyModJarLoader loader = new LegacyModJarLoader(modsFolder);
        loader.loadAllLegacyMods();
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::doClientStuff);
        LegacyModManager.loadLegacyMods();
    }

    private void setup(final FMLCommonSetupEvent event) {
        // 共通の初期化処理
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // クライアント専用の初期化処理
    }
}