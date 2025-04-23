package com.example.compatmod.legacy.api;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public class LegacyInventory {

    private final SimpleContainer container;

    public LegacyInventory(int size) {
        this.container = new SimpleContainer(size);
    }

    public LegacyInventory(SimpleContainer container) {
        this.container = container;
    }

    public void setStackInSlot(int index, ItemStack stack) {
        container.setItem(index, stack);
    }

    public ItemStack getStackInSlot(int index) {
        return container.getItem(index);
    }

    public void clear() {
        container.clearContent();
    }

    public int getSize() {
        return container.getContainerSize();
    }

    public boolean isEmpty() {
        return container.isEmpty();
    }

    public SimpleContainer getInternal() {
        return container;
    }
    public boolean addItem(ItemStack stack) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack current = container.getItem(i);
            if (current.isEmpty()) {
                container.setItem(i, stack.copy());
                return true;
            } else if (ItemStack.isSameItemSameTags(current, stack)
                    && current.getCount() < current.getMaxStackSize()) {
                int space = current.getMaxStackSize() - current.getCount();
                int toMove = Math.min(stack.getCount(), space);
                current.grow(toMove);
                stack.shrink(toMove);
                if (stack.isEmpty()) return true;
            }
        }
        return stack.isEmpty();
    }
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stackInSlot = container.getItem(index);
        if (stackInSlot.isEmpty()) return ItemStack.EMPTY;

        if (stackInSlot.getCount() <= count) {
            ItemStack result = stackInSlot;
            container.setItem(index, ItemStack.EMPTY);
            return result;
        } else {
            ItemStack split = stackInSlot.split(count);
            if (stackInSlot.isEmpty()) {
                container.setItem(index, ItemStack.EMPTY);
            }
            return split;
        }
    }

    public ItemStack removeStackFromSlot(int index) {
        ItemStack result = container.getItem(index);
        container.setItem(index, ItemStack.EMPTY);
        return result;
    }
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag items = new ListTag();

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putByte("Slot", (byte)i);
                stack.save(itemTag);
                items.add(itemTag);
            }
        }

        tag.put("Items", items);
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        ListTag items = tag.getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0; i < items.size(); i++) {
            CompoundTag itemTag = items.getCompound(i);
            int slot = itemTag.getByte("Slot") & 255;
            if (slot >= 0 && slot < container.getContainerSize()) {
                container.setItem(slot, ItemStack.of(itemTag));
            }
        }
    }
}
