package com.example.compatmod.legacy.registry;

import com.example.compatmod.legacy.item.LegacyItem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;


import java.util.*;

@Mod.EventBusSubscriber(modid = "compatmod", bus = Mod.EventBusSubscriber.Bus.MOD)
public class LegacyCreativeTabs {

    @SuppressWarnings("removal")
    public static final ResourceKey<Registry<CreativeModeTab>> CREATIVE_TAB_REGISTRY =
            ResourceKey.createRegistryKey(new net.minecraft.resources.ResourceLocation("minecraft", "creative_mode_tab"));

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(CREATIVE_TAB_REGISTRY, "legacymodloader");

    private static final Map<String, RegistryObject<CreativeModeTab>> tabRegistry = new HashMap<>();

    @SuppressWarnings("removal")
    public static void register() {
        TABS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static void createTabForMod(String modId) {
        String tabId = "legacy_" + modId;
        RegistryObject<CreativeModeTab> tab = TABS.register(tabId, () ->
                CreativeModeTab.builder()
                        .title(Component.literal("Legacy: " + modId))
                        .icon(() -> new ItemStack(Items.BOOK)) // 仮アイコン
                        .displayItems((params, output) -> {
                            for (var item : LegacyItemRegistry.getItemsForMod(modId)) {
                                output.accept(new ItemStack(item.get()));
                            }
                        })
                        .build()
        );
        tabRegistry.put(modId, tab);
    }

    public static Optional<CreativeModeTab> getTab(String modId) {
        return Optional.ofNullable(tabRegistry.get(modId)).map(RegistryObject::get);
    }
}
