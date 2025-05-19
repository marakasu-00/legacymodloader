package com.example.compatmod;

import com.example.compatmod.config.ConfigHandler;
import com.example.compatmod.legacy.loader.LegacyModJarLoader;
import com.example.compatmod.legacy.loader.LegacyModManager;
import com.example.compatmod.legacy.loader.LegacyModResourceHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.mtr.legacymodloader.Config;

import java.io.File;

@Mod("legacymodloader")
public class LegacyModLoader {
    @SuppressWarnings("removal")
    public LegacyModLoader() {
        ConfigHandler.register();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // レガシーMODロード開始
        //File modsFolder = new File("mods");
        //LegacyModJarLoader loader = new LegacyModJarLoader(modsFolder);
        //loader.loadAllLegacyMods();
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::doClientStuff);
        //MinecraftForge.EVENT_BUS.register(ConfigHandler.class);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // 設定ファイルはこのタイミングで安全に読み取り可能
            double slider = ConfigHandler.getSliderValueSafe();
            boolean enabled = ConfigHandler.getCheckboxSafe();
            String text = ConfigHandler.getSavedTextSafe();

            // 必要な処理をここで行う
            LegacyModManager.loadLegacyMods();
        });
    }

    // LegacyModLoader.java
    private void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            double slider = ConfigHandler.getSliderValueSafe();
            boolean enabled = ConfigHandler.getCheckboxSafe();
            String text = ConfigHandler.getSavedTextSafe();

            System.out.println("Loaded slider=" + slider + " enabled=" + enabled + " text=" + text);
        });
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // クライアント専用の初期化処理
        LegacyModResourceHelper.loadLegacyResources();
    }
}