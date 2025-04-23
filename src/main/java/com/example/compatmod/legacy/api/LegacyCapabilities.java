package com.example.compatmod.legacy.api;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.Optional;

public class LegacyCapabilities {

    public static <T> Optional<T> getCapability(ItemStack stack, Capability<T> cap) {
        return stack.getCapability(cap).resolve();
    }

    public static <T> Optional<T> getCapability(Entity entity, Capability<T> cap) {
        return entity.getCapability(cap).resolve();
    }

    public static <T> Optional<T> getCapability(BlockEntity tile, Capability<T> cap) {
        return tile.getCapability(cap).resolve();
    }

    public static <T> Optional<T> getCapability(ICapabilityProvider provider, Capability<T> cap) {
        return provider.getCapability(cap).resolve();
    }
}
