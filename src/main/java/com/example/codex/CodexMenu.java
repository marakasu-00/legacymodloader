package com.example.codex;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class CodexMenu extends AbstractContainerMenu {
    public CodexMenu(int windowId, Inventory inv) {
        super(CodexMenus.CODEX.get(), windowId);
        // 必要な場合は inv から情報取得
    }

    public CodexMenu(int windowId) {
        super(CodexMenus.CODEX.get(), windowId);
    }

    @Override
    public ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
        return null;
    }

    @Override
    public boolean stillValid(net.minecraft.world.entity.player.Player player) {
        return true;
    }
}
