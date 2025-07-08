package com.example.compatmod.legacyexample;

import com.example.compatmod.legacy.registry.LegacyItemRegistry;
import com.example.compatmod.legacy.registry.LegacyCreativeTabs;
import net.minecraftforge.fml.common.Mod;

@Mod("legacymodloader")
public class LegacyExampleMod {
    public LegacyExampleMod() {
        // 1. register() は一番最初
        LegacyItemRegistry.register();
        LegacyCreativeTabs.register();

        // 2. アイテムの登録は CommonSetup へ任せる（もうやってる）
        // → ModCommonEvents.onCommonSetup() で registerItemsFromAssets()
    }
}
