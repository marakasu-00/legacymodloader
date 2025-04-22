package com.example.compatmod;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("legacymodloader")
public class LegacyModLoader {
    @SuppressWarnings("removal")
    public LegacyModLoader() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::doClientStuff);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // 共通の初期化処理
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // クライアント専用の初期化処理
    }
}