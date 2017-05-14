package com.minecolonies.coremod.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

/**
 * The custom chest of the field.
 */
public class InventoryChest extends ItemStackHandler
{
    public InventoryChest()
    {
        super(27);
    }

    @Override
    public void setStackInSlot(final int slot, final ItemStack stack)
    {
        super.setStackInSlot(slot, stack);
    }

    @Override
    public ItemStack insertItem(final int slot, final ItemStack stack, final boolean simulate)
    {
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(final int slot, final int amount, final boolean simulate)
    {
        return super.extractItem(slot, amount, simulate);
    }
}
