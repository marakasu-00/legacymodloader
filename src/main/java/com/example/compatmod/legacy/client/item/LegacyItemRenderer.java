package com.example.compatmod.legacy.client.item;

import com.example.compatmod.legacy.item.LegacyItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.PoseStack;

import javax.annotation.WillClose;

public class LegacyItemRenderer extends BlockEntityWithoutLevelRenderer {

    private final ItemRenderer itemRenderer;

    public LegacyItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @SuppressWarnings("removal")
    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context,
                             PoseStack poseStack, MultiBufferSource buffer,
                             int light, int overlay) {
        if (!(stack.getItem() instanceof LegacyItem legacy)) return;

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        BakedModel model = itemRenderer.getModel(stack, null, null, 0);

        itemRenderer.render(
                stack, context, false, poseStack, buffer, light, overlay, model
        );
    }
}
