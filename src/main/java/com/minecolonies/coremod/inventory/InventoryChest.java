package com.minecolonies.coremod.inventory;

import com.minecolonies.coremod.tileentities.TileEntityMinecoloniesChest;
import com.minecolonies.coremod.util.ExtendedItemStack;
import com.minecolonies.coremod.util.InventoryUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * The custom chest of the field.
 */
public class InventoryChest extends Container implements IItemHandler
{
    /**
     * The size of a normal inventory.
     */
    private static final int MAX_INVENTORY_INDEX = 27;

    /**
     * The size of the the inventory hotbar.
     */
    private static final int INVENTORY_HOT_BAR_SIZE = 9;

    /**
     * Tag to store the content.
     */
    private static final String TAG_CONTENT = "items";

    /**
     * TileEntity of the chest.
     */
    private final TileEntity entity;

    /**
     * The hashmap with the content of the inventory.
     */
    private final HashMap<ExtendedItemStack, ExtendedItemStack> content = new HashMap<>();

    public InventoryChest(@NotNull final TileEntityMinecoloniesChest tileEntity,
            final InventoryPlayer playerInventory)
    {
        super();

        tileEntity.openInventory(playerInventory.player);
        int i = (3 - 4) * 18;

        for (int j = 0; j < 3; ++j)
        {
            for (int k = 0; k < 9; ++k)
            {
                this.addSlotToContainer(new Slot(tileEntity, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for (int l = 0; l < 3; ++l)
        {
            for (int j1 = 0; j1 < 9; ++j1)
            {
                this.addSlotToContainer(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1)
        {
            this.addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 161 + i));
        }

        this.content.putAll(tileEntity.getInventory().content);
        tileEntity.setInventory(this);
        this.entity = tileEntity;
    }

    public InventoryChest()
    {
        super();
        this.entity = null;
    }

    @Override
    public int getSlots()
    {
        return 27;
    }

    @Override
    protected boolean mergeItemStack(final ItemStack stack, final int startIndex, final int endIndex, final boolean reverseDirection)
    {
        return super.mergeItemStack(stack, startIndex, endIndex, reverseDirection);
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
     * Mark the tileEntity of this inventory dirty.
     */
    private void markDirty()
    {
        if(this.entity != null)
        {
            entity.markDirty();
        }
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
                    final ItemStack returnStack = stack.getStack().copy();
                    returnStack.stackSize = size;
                    return returnStack;
                }
                totalAmount -= size;
            }
            i += divisor;
        }
        return InventoryUtils.EMPTY;
    }

    public static InventoryChest deserializeFromNBT(final NBTTagCompound compound)
    {
        final InventoryChest chest = new InventoryChest();
        final NBTTagList nbttaglist = compound.getTagList(TAG_CONTENT, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            final ExtendedItemStack stack = ExtendedItemStack.readFromNBT(nbttagcompound);
            chest.content.put(stack, stack);
        }
        return chest;
    }

    @Nullable
    @Override
    public ItemStack transferStackInSlot(@NotNull final EntityPlayer playerIn, final int slotIndex)
    {
        if (slotIndex < this.getSlots())
        {
            playerIn.inventory.addItemStackToInventory(this.getStackInSlot(slotIndex));
            this.removeStackFromSlot(slotIndex);
        }
        else
        {
            int index = slotIndex - this.getSlots();
            index = index >= 27 ? index - 27 : index + 9;
            if (playerIn.inventory.getStackInSlot(index) != InventoryUtils.EMPTY)
            {
                int remain = this.addToInventory(playerIn.inventory.getStackInSlot(index));
                playerIn.inventory.getStackInSlot(index).splitStack(64 - remain);
                if (playerIn.inventory.getStackInSlot(index).stackSize == 0)
                {
                    playerIn.inventory.removeStackFromSlot(index);
                }
            }
        }

        markDirty();
        return InventoryUtils.EMPTY;
    }

    private int addToInventory(final ItemStack stackInSlot)
    {
        int fullSlots = 0;

        for(ExtendedItemStack stack: content.values())
        {
            fullSlots += Math.ceil(stack.getAmount() / 64.0);
        }

        if(fullSlots >= getSlots())
        {
            //todo check if can fill up a stack.
            return stackInSlot.stackSize;
        }

        final int stackSize = stackInSlot.stackSize;
        stackInSlot.stackSize = 0;
        if (content.containsKey(new ExtendedItemStack(stackInSlot, 0)))
        {
            ExtendedItemStack ext = content.remove(new ExtendedItemStack(stackInSlot, 0));
            ext.increaseAmount(stackSize);
            content.put(ext, ext);
        }
        else
        {
            final ExtendedItemStack ext = new ExtendedItemStack(stackInSlot, stackSize);
            content.put(ext, ext);
        }

        markDirty();
        return 0;
    }

    public NBTTagCompound serializeNBT()
    {
        final NBTTagCompound compound = new NBTTagCompound();
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
    public ItemStack decrStackSize(int index, int count)
    {
        markDirty();
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
    public ItemStack removeStackFromSlot(int index)
    {
        markDirty();
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
                    final ItemStack returnStack = stack.getAsItemStackWithAmount(size);
                    if(stack.getAmount() <= 0)
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
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     * @param index the index.
     * @param stack the incoming stack.
     */
    public void setInventorySlotContents(int index, @Nullable ItemStack stack)
    {
        markDirty();
        if (stack == null)
        {
            return;
        }

        int i = 0;
        for (final ExtendedItemStack exStack : content.values())
        {
            int totalAmount = exStack.getAmount();
            double divisor = Math.max(64, totalAmount) / 64.0;

            for (int partialStacks = 0; partialStacks < divisor; partialStacks++)
            {
                int size = Math.min(64, totalAmount);

                if (i + partialStacks == index)
                {
                    exStack.setAmount(exStack.getAmount() - size);
                    if(exStack.getAmount() <= 0)
                    {
                        content.remove(exStack);
                    }
                    break;
                }
                totalAmount -= size;
            }
            i += divisor;
        }

        final int stacksize = stack.stackSize;
        stack.stackSize = 0;
        if (content.containsKey(new ExtendedItemStack(stack, 0)))
        {
            ExtendedItemStack ext = content.remove(new ExtendedItemStack(stack, 0));
            ext.increaseAmount(stacksize);
            content.put(ext, ext);
        }
        else
        {
            final ExtendedItemStack ext = new ExtendedItemStack(stack, stacksize);
            content.put(ext, ext);
        }
    }

    @Override
    public boolean canInteractWith(final EntityPlayer playerIn)
    {
        return true;
    }

    public void clear()
    {
        markDirty();
        content.clear();
    }
}
