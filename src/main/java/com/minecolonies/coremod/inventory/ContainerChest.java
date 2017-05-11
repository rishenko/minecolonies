package com.minecolonies.coremod.inventory;

import com.minecolonies.coremod.colony.Colony;
import com.minecolonies.coremod.colony.ColonyManager;
import com.minecolonies.coremod.colony.permissions.Permissions;
import com.minecolonies.coremod.tileentities.TileEntityMinecoloniesChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The container of the custom chest.
 */
public class ContainerChest extends Container
{
    /**
     * The colony of the field.
     */
    @Nullable
    private final Colony colony;


    /**
     * The fields location.
     */
    private BlockPos location;

    /**
     * The inventory of the field.
     */
    private InventoryChest inventory;

    /**
     * Private constructor to create field from NBT.
     *
     * @param colony the colony the field belongs to.
     */
    private ContainerChest(final Colony colony)
    {
        super();
        this.colony = colony;
    }

    /**
     * Creates an instance of our field container, this may be serve to open the GUI.
     *
     * @param chest the tileEntity of the field containing the inventory.
     * @param playerInventory     the player inventory.
     * @param world               the world.
     * @param location            the position of the field.
     */
    public ContainerChest(@NotNull final TileEntityMinecoloniesChest chest, final InventoryPlayer playerInventory, @NotNull final World world, @NotNull final BlockPos location)
    {
        super();
        this.colony = ColonyManager.getColony(world, location);
        this.location = location;
        this.inventory = chest.getInventory();

        int i = (3 - 4) * 18;

        for (int j = 0; j < 3; ++j)
        {
            for (int k = 0; k < 9; ++k)
            {
                this.addSlotToContainer(new Slot(chest, k + j * 9, 8 + k * 18, 18 + j * 18));
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
    }

    @Override
    protected final Slot addSlotToContainer(final Slot slotToAdd)
    {
        return super.addSlotToContainer(slotToAdd);
    }

    @Override
    public ItemStack transferStackInSlot(final EntityPlayer player, int slotIndex)
    {
        ItemStack itemstack = null;
        final Slot slot = this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            final TileEntity entity = player.worldObj.getTileEntity(this.location);

            int minPlayerSlot = inventorySlots.size() - player.inventory.mainInventory.length;
            if (slotIndex < minPlayerSlot)
            {
                if(entity instanceof TileEntityMinecoloniesChest)
                {
                    ((TileEntityMinecoloniesChest) entity).removeFromContent(itemstack);
                }
                if (!this.mergeItemStack(itemstack1, minPlayerSlot, this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, minPlayerSlot, false))
            {
                if(entity instanceof TileEntityMinecoloniesChest)
                {
                    ((TileEntityMinecoloniesChest) entity).addToContent(itemstack);
                }
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack) null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(@NotNull final EntityPlayer playerIn)
    {
        return getColony().getPermissions().hasPermission(playerIn, Permissions.Action.OPEN_CONTAINER);
    }

    /**
     * Returns the colony of the field.
     *
     * @return {@link com.minecolonies.coremod.colony.Colony} of the current object.
     */
    @Nullable
    public Colony getColony()
    {
        return this.colony;
    }

}
