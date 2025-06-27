package com.example.codex;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CodexMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, "codex");

    public static final RegistryObject<MenuType<CodexMenu>> CODEX =
            MENUS.register("codex", () ->
                    new MenuType<>((windowId, inv) -> new CodexMenu(windowId, inv))
            );

    @SuppressWarnings("removal")
    public static void register() {
        MENUS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}

