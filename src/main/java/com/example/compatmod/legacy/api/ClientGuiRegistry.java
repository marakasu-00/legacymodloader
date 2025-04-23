package com.example.compatmod.legacy.api;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;

import java.util.function.BiFunction;

@OnlyIn(Dist.CLIENT)
public class ClientGuiRegistry {

    public static <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M> & MenuAccess<M>>
    void register(MenuType<M> menuType, BiFunction<M, Inventory, S> factory) {
        MenuScreens.register(menuType, new MenuScreens.ScreenConstructor<M, S>() {
            @Override
            public S create(M menu, Inventory inv, Component title) {
                return factory.apply(menu, inv);
            }
        });
    }

}
