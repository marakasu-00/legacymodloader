package com.example.compatmod.legacy.event;

import com.example.compatmod.legacy.api.ILegacyMod;
import com.example.compatmod.legacy.loader.LegacyModManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

@Mod.EventBusSubscriber(modid = "legacymodloader", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class LegacyEventDispatcher {

    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onRenderOverlayPre(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay().id().toString().equals("minecraft:hotbar")) {
            GuiGraphics guiGraphics = event.getGuiGraphics();
            for (ILegacyMod mod : LegacyModManager.getLegacyMods()) {
                mod.onPreRenderOverlay(guiGraphics); // 前描画用メソッド
            }
        }
    }

    @SubscribeEvent
    public static void onRenderOverlayPost(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay().id().toString().equals("minecraft:hotbar")) {
            GuiGraphics guiGraphics = event.getGuiGraphics();
            for (ILegacyMod mod : LegacyModManager.getLegacyMods()) {
                mod.onRenderOverlay(guiGraphics); // 後描画用メソッド
            }
        }
    }


    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        BlockPos pos = event.getPos();
        BlockState state = event.getLevel().getBlockState(pos); // getWorld() → getLevel()
        if (state.getBlock() == Blocks.CHEST) {
            LOGGER.info("[LegacyEventHooks] Player interacted with a chest!");
        }
    }
    @SubscribeEvent
    public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        LOGGER.info("[LegacyEventHooks] Player right-clicked in the air!");
        for (ILegacyMod mod : LegacyModManager.getLegacyMods()) {
            mod.onPlayerInteract();
        }
    }
    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        for (ILegacyMod mod : LegacyModManager.getLegacyMods()) {
            mod.onPlayerInteract();
        }
    }
    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        LOGGER.info("[LegacyEventHooks] Player left-click block event fired at {}", event.getPos());

        for (ILegacyMod mod : LegacyModManager.getLegacyMods()) {
            mod.onPlayerInteract();  // ここは場合によって別メソッドに分けてもOK
        }
    }
    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        ItemStack itemStack = event.getItemStack();
        if (itemStack.getItem() == Items.DIAMOND) {
            LOGGER.info("[LegacyEventHooks] Player used a diamond!");
            // カスタム動作をここに追加
        }
    }
    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        LOGGER.info("[LegacyEventHooks] Player left-clicked in the air!");
        for (ILegacyMod mod : LegacyModManager.getLegacyMods()) {
            mod.onPlayerInteract();
        }
    }
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        LivingEntity target = (LivingEntity) event.getTarget();
        for (ILegacyMod mod : LegacyModManager.getLegacyMods()) {
            mod.onEntityInteract(target);
        }
    }
}
