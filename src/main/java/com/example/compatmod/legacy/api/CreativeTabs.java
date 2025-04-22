package com.example.compatmod.legacy.api;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.core.registries.Registries;

public class CreativeTabs {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "legacymodloader");

    public static final RegistryObject<CreativeModeTab> MISC = TABS.register("legacy_misc", () ->
            CreativeModeTab.builder()
                    .icon(() -> new ItemStack(Items.BOOK))
                    .title(Component.literal("Legacy - Misc"))
                    .displayItems((params, output) -> {
                        // 自動で追加されるから空でもOK
                    })
                    .build());

    // 他にも TOOLS, COMBAT など必要なら追加可能
}
