package com.minecolonies.coremod.tileentities;

import com.minecolonies.blockout.Log;
import com.minecolonies.coremod.util.ExtendedItemStack;
import com.minecolonies.coremod.util.InventoryUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.HashMap;

public class TileEntityMinecoloniesChest extends TileEntityChest implements IItemHandler
{
    /**
     * Tag to store the content.
     */
    private static final String TAG_CONTENT = "items";

    /**
     * The hashmap with the content of the inventory.
     */
    private final HashMap<ExtendedItemStack, ExtendedItemStack> content = new HashMap<>();

    public TileEntityMinecoloniesChest()
    {
        super();
    }

    @Override
    public void readFromNBT(final NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        content.clear();
        final NBTTagList nbttaglist = compound.getTagList(TAG_CONTENT, net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            final ExtendedItemStack stack = ExtendedItemStack.readFromNBT(nbttagcompound);
            content.put(stack, stack);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound compound)
    {
        final NBTTagList nbttaglist = new NBTTagList();
        for (final ExtendedItemStack stack : content.values())
        {
            final NBTTagCompound stackCompound = new NBTTagCompound();
            stack.writeToNBT(stackCompound);
            nbttaglist.appendTag(stackCompound);
        }
        compound.setTag(TAG_CONTENT, nbttaglist);
        return compound;
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Nullable
    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        final ItemStack returnStack = super.decrStackSize(index, count);
        if(!InventoryUtils.isItemStackEmpty(returnStack))
        {
            final ItemStack copy = returnStack.copy();
            copy.stackSize = 0;
            if (content.containsKey(new ExtendedItemStack(copy, 0)))
            {
                final ExtendedItemStack ext = content.remove(new ExtendedItemStack(copy, 0));
                if(ext.getAmount() > 0)
                {
                    content.put(ext, ext);
                    Log.getLogger().info("Has: " + ext.getAmount() + " " + ext.getStack().getDisplayName());
                }
                Log.getLogger().info("Has: 0" + ext.getStack().getDisplayName());

            }
        }
        return returnStack;
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    @Nullable
    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        final ItemStack returnStack = super.removeStackFromSlot(index);
        if(!InventoryUtils.isItemStackEmpty(returnStack))
        {
            removeFromContent(returnStack);
        }
        return returnStack;
    }

    public void addToContent(final ItemStack stack)
    {
        final ItemStack copy = stack.copy();
        copy.stackSize = 0;
        if (content.containsKey(new ExtendedItemStack(copy, 0)))
        {
            final ExtendedItemStack ext = content.remove(new ExtendedItemStack(copy, 0));
            content.put(ext, ext);
            Log.getLogger().info("Has: " + ext.getAmount() + " " + ext.getStack().getDisplayName());
        }
        else
        {
            final ExtendedItemStack ext = new ExtendedItemStack(copy, stack.stackSize);
            Log.getLogger().info("Has: " + ext.getAmount() + " " + ext.getStack().getDisplayName());
            content.put(ext, ext);
        }
    }

    public void removeFromContent(final ItemStack stack)
    {
        final ItemStack copy = stack.copy();
        copy.stackSize = 0;
        if (content.containsKey(new ExtendedItemStack(copy, 0)))
        {
            final ExtendedItemStack ext = content.remove(new ExtendedItemStack(copy, 0));

            if(ext.getAmount() > 0)
            {
                Log.getLogger().info("Has: " + ext.getAmount() + " " + ext.getStack().getDisplayName());
                content.put(ext, ext);
            }
            Log.getLogger().info("Has: 0 " + ext.getStack().getDisplayName());
        }
    }

    @Override
    public int getSlots()
    {
        return 27;
    }

    @Nullable
    @Override
    public ItemStack getStackInSlot(final int index)
    {
        return super.getStackInSlot(index);
    }

    @Override
    public ItemStack insertItem(final int slot, final ItemStack stack, final boolean simulate)
    {
        return null;
    }

    @Override
    public ItemStack extractItem(final int slot, final int amount, final boolean simulate)
    {
        return null;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack)
    {
        if(!InventoryUtils.isItemStackEmpty(stack) && worldObj!= null && !worldObj.isRemote)
        {
            final ItemStack stackAtSlot = getStackInSlot(index);
            final ItemStack copy = stack.copy();

            if(stackAtSlot != null && stackAtSlot.isItemEqual(stack))
            {
                copy.stackSize -= stackAtSlot.stackSize;
            }

            addToContent(copy);
        }
        super.setInventorySlotContents(index, stack);
    }

    /**
     * Clear the inventory.
     */
    @Override
    public void clear()
    {
        content.clear();
        super.clear();
    }
}