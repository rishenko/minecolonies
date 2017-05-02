package com.minecolonies.coremod.tileentities;

import com.minecolonies.coremod.util.ExtendedItemStack;
import com.minecolonies.coremod.util.InventoryUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityChest;

import javax.annotation.Nullable;
import java.util.HashMap;

public class TileEntityMinecoloniesChest extends TileEntityChest
{
    private final HashMap<ExtendedItemStack, ExtendedItemStack> content = new HashMap<>();

    public TileEntityMinecoloniesChest()
    {
        super();
    }

    /**
     * Returns the stack in the given slot.
     */
    @Nullable
    @Override
    public ItemStack getStackInSlot(int index)
    {
        int i = 0;
        for (final ExtendedItemStack stack : content.values())
        {
            int totalAmount = stack.getAmount();
            double divisor = Math.max(64, totalAmount) / 64.0;

            for (int partialStacks = 0; partialStacks < divisor; partialStacks++)
            {
                int size = Math.min(64, totalAmount);

                if (i + partialStacks == index)
                {
                    final ItemStack returnStack = stack.getStack();
                    returnStack.stackSize = size;
                    return returnStack;
                }
                totalAmount -= size;
            }
            i += divisor;
        }
        return InventoryUtils.EMPTY;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        final NBTTagCompound compound = super.serializeNBT();
        final NBTTagList nbttaglist = new NBTTagList();
        for (final ExtendedItemStack stack : content.values())
        {
            stack.writeToNBT(compound);
        }

        compound.setTag("Items", nbttaglist);

        return compound;
    }

    @Override
    public void deserializeNBT(final NBTTagCompound nbt)
    {
        super.deserializeNBT(nbt);
        final NBTTagList nbttaglist = nbt.getTagList("Items", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            final ExtendedItemStack stack = ExtendedItemStack.readFromNBT(nbttagcompound);
            content.put(stack, stack);
        }
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Nullable
    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        this.markDirty();
        int i = 0;
        for (final ExtendedItemStack stack : content.values())
        {
            int totalAmount = stack.getAmount();
            double divisor = Math.max(64, totalAmount) / 64.0;

            for (int partialStacks = 0; partialStacks < divisor; partialStacks++)
            {
                int size = Math.min(64, totalAmount);
                if (i + partialStacks == index)
                {
                    final ItemStack returnStack = stack.getAsItemStackWithAmount(count);
                    if (stack.getAmount() == 0)
                    {
                        content.remove(stack);
                    }
                    return returnStack;
                }
                totalAmount -= size;
            }
            i += divisor;
        }
        return InventoryUtils.EMPTY;
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    @Nullable
    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        this.markDirty();
        int i = 0;
        for (final ExtendedItemStack stack : content.values())
        {
            int totalAmount = stack.getAmount();
            double divisor = Math.max(64, totalAmount) / 64.0;

            for (int partialStacks = 0; partialStacks < divisor; partialStacks++)
            {
                int size = Math.min(64, totalAmount);

                if (i + partialStacks == index)
                {
                    return stack.getAsItemStackWithAmount(size);
                }
                totalAmount -= size;
            }
            i += divisor;
        }
        return InventoryUtils.EMPTY;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack)
    {
        if (stack == null)
        {
            return;
        }

        if (content.containsKey(new ExtendedItemStack(stack, 0)))
        {
            ExtendedItemStack ext = content.remove(new ExtendedItemStack(stack, 0));
            ext.increaseAmount(stack.stackSize);
            content.put(ext, ext);
        }
        else
        {
            final ExtendedItemStack ext = new ExtendedItemStack(stack, stack.stackSize);
            content.put(ext, ext);
        }
        stack.stackSize = 0;

        this.markDirty();
    }

    public boolean addItemStackToInventory(@org.jetbrains.annotations.Nullable final ItemStack itemStackIn)
    {
        return true;
    }

    /**
     * Clear the inventory.
     */
    @Override
    public void clear()
    {
        content.clear();
    }
}