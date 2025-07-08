package com.example.compatmod.legacy.item;

import com.example.codex.client.CodexOpenerClient;
import com.example.compatmod.legacy.client.item.LegacyItemRenderer;
import com.example.compatmod.legacy.lang.LegacyLangManager;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class LegacyItem extends Item {
    private final String legacyId;
    private final String modId;
    private static final List<LegacyItem> ALL = new ArrayList<>();


    public LegacyItem(String modId, String legacyId, Properties props) {
        super(props);
        this.modId = modId;
        this.legacyId = legacyId;
        ALL.add(this);
    }

    public static List<LegacyItem> getAllLegacyItems() {
        return ALL;
    }

    public String getLegacyId() {
        return legacyId;
    }

    public String getModId() {
        return modId;
    }

    @Override
    public String toString() {
        return "LegacyItem[" + modId + ":" + legacyId + "]";
    }

    @Override
    public Component getName(ItemStack stack) {
        return LegacyLangManager.getDisplayName(modId, legacyId)
                .map(Component::literal)
                .orElseGet(() -> Component.literal(modId + ":" + legacyId));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) {
            CodexOpenerClient.openScreen(); // ← LegacyItem を渡す
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    public void initializeClient(java.util.function.Consumer<net.minecraft.client.item.RenderProperties> consumer) {
        consumer.accept(new net.minecraft.client.item.RenderProperties() {
            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return LegacyItemRenderer.INSTANCE;
            }
        });
    }

}
