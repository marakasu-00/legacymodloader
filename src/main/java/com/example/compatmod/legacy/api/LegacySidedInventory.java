package com.example.compatmod.legacy.api;

import net.minecraft.core.Direction;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class LegacySidedInventory extends LegacyInventory {
    private final Map<Direction, int[]> slotAccessMap = new EnumMap<>(Direction.class);

    public LegacySidedInventory(int size) {
        super(size);
    }

    public void setSlotsForSide(Direction side, int... slots) {
        slotAccessMap.put(side, slots);
    }

    public int[] getSlotsForFace(Direction side) {
        return slotAccessMap.getOrDefault(side, new int[0]);
    }

    public boolean canInsertItem(int index, ItemStack stack, Direction direction) {
        return Arrays.stream(getSlotsForFace(direction)).anyMatch(i -> i == index);
    }

    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return Arrays.stream(getSlotsForFace(direction)).anyMatch(i -> i == index);
    }
}
