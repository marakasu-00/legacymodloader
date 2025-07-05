package com.example.compatmod.legacyexample;

import com.example.compatmod.legacy.loader.LegacyItemAutoRegistrar;
import com.example.compatmod.legacy.registry.LegacyItemRegistry;
import com.example.compatmod.legacy.registry.LegacyCreativeTabs;
import com.example.debug.LegacyItemRegistryDebugger;
import net.minecraftforge.fml.common.Mod;

@Mod("legacymodloader")
public class LegacyExampleMod {
    public LegacyExampleMod() {
        LegacyItemRegistry.register();
        LegacyCreativeTabs.register();
        LegacyItemAutoRegistrar.registerItemsFromAssets();
    }
}
