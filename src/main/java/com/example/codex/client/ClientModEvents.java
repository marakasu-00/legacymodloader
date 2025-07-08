package com.example.codex.client;

import com.example.codex.CodexMenus;
import com.example.debug.LegacyItemRegistryDebugger;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = "codex", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(CodexMenus.CODEX.get(), CodexScreenWithMenu::new);
            LegacyItemRegistryDebugger.dumpAllRegisteredLegacyItems();
        });
    }
}
