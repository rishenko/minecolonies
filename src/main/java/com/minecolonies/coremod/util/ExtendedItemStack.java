package com.minecolonies.coremod.util;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.jetbrains.annotations.NotNull;

/**
 * Extended itemStack which can store an unlimited amount of items.
 */
public class ExtendedItemStack
{
    /**
     * Tag to store the amount to NBT.
     */
    private static final String TAG_AMOUNT = "amount";

    /**
     * The amount.
     */
    private int amount;

    /**
     * ItemStack of the Extension.
     */
    @NotNull
    private final ItemStack stack;

    /**
     * Creates an instance of the storage.
     *
     * @param stack the itemStack.
     * @param amount the amount.
     */
    public ExtendedItemStack(@NotNull final ItemStack stack, int amount)
    {
        this.amount = amount;
        this.stack = stack;
    }

    /**
     * Getter for the quantity.
     *
     * @return the amount.
     */
    public int getAmount()
    {
        return amount;
    }

    /**
     * Setter for the quantity.
     *
     * @param amount the amount.
     */
    public void setAmount(final int amount)
    {
        this.amount = amount;
    }

    /**
     * Increase the quantity by a certain amount.
     * @param increaseBy the amount to increase it by.
     */
    public void increaseAmount(final int increaseBy)
    {
        this.amount += increaseBy;
    }

    /**
     * Decrease the quantity by a certain amount.
     * @param decreaseBy the amount to decrease it by.
     */
    public void decreaseAmount(final int decreaseBy)
    {
        this.amount -= decreaseBy;
    }

    /**
     * Get the itemStack of the extension.
     * @return the stack with amount 0.
     */
    public ItemStack getStack()
    {
        return stack;
    }

    /**
     * Read the stack fields from a NBT object.
     */
    public static ExtendedItemStack readFromNBT(final NBTTagCompound nbt)
    {
        final ItemStack stack = ItemStack.loadItemStackFromNBT(nbt);
        final int amount = nbt.getInteger(TAG_AMOUNT);
        return new ExtendedItemStack(stack, amount);
    }

    /**
     * Write the stack fields to a NBT object. Return the new NBT object.
     */
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt)
    {
        this.stack.writeToNBT(nbt);
        nbt.setInteger(TAG_AMOUNT, amount);
        return nbt;
    }

    /**
     * Get an itemStack with a certain amount.
     * The amount gets reduced of the ExtendedItemStack.
     * @param amount the stackSize
     * @return the new itemStack retrieved of the extended ItemStack.
     */
    public ItemStack getAsItemStackWithAmount(int amount)
    {
        final ItemStack returnStack = stack.copy();
        final int decreaseBy = Math.min(this.amount, amount);
        this.amount-= decreaseBy;
        returnStack.stackSize = decreaseBy;
        return returnStack;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof ExtendedItemStack))
        {
            return false;
        }

        final ExtendedItemStack that = (ExtendedItemStack) o;

        if (!getStack().writeToNBT(new NBTTagCompound()).equals(that.getStack().writeToNBT(new NBTTagCompound())))
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return 31 * getStack().writeToNBT(new NBTTagCompound()).hashCode();
    }
}
