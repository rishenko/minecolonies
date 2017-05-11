package com.minecolonies.coremod.tileentities;

import com.minecolonies.coremod.inventory.InventoryChest;
import com.minecolonies.coremod.lib.Constants;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;

import javax.annotation.Nullable;

public class TileEntityMinecoloniesChest extends TileEntityChest
{
    /**
     * Tag to store inventory to nbt.
     */
    private static final String TAG_INVENTORY  = "inventory";

    /**
     * The inventory connected with the scarecrow.
     */
    private InventoryChest chest;

    public TileEntityMinecoloniesChest()
    {
        super();
        chest = new InventoryChest();
    }

    /**
     * Returns the stack in the given slot.
     */
    @Nullable
    @Override
    public ItemStack getStackInSlot(int index)
    {
        return chest.getStackInSlot(index);
    }

    @Override
    public void readFromNBT(final NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        chest = InventoryChest.deserializeFromNBT(compound.getCompoundTag(Constants.MOD_ID + TAG_INVENTORY));
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound compound)
    {
        final NBTTagCompound localCompound = super.writeToNBT(compound);
        if(chest != null)
        {
            localCompound.setTag(Constants.MOD_ID + TAG_INVENTORY, chest.serializeNBT());
        }
        return localCompound;
    }

    public void close()
    {
        numPlayersUsing = 0;
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Nullable
    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        return chest.decrStackSize(index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    @Nullable
    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        return chest.removeStackFromSlot(index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack)
    {
        chest.setInventorySlotContents(index, stack);
    }

    /**
     * Clear the inventory.
     */
    @Override
    public void clear()
    {
        chest.clear();
    }

    /**
     * Getter for the inventory.
     * @return the stackHandler.
     */
    public InventoryChest getInventory()
    {
        return this.chest;
    }

    /**
     * Set the inventory.
     * @param inventory to set.
     */
    public void setInventory(final InventoryChest inventory)
    {
        this.chest = inventory;
    }
}