package com.example.compatmod;

import com.example.compatmod.legacy.LegacyGameRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("legacymodloader")
@SuppressWarnings("removal")
public class LegacyModLoader {

    public LegacyModLoader() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        LegacyGameRegistry.registerBlock("legacy_block", () ->
                new Block(BlockBehaviour.Properties.of().strength(1.5F, 6.0F)));
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::doClientStuff);

        // テスト用アイテムを登録
        LegacyGameRegistry.registerItem("legacy_test_item", () -> new Item(new Properties()));
    }

    private void setup(final FMLCommonSetupEvent event) {
        // 共通の初期化処理
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // クライアント専用の初期化処理
    }
}