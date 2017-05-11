package com.minecolonies.coremod.inventory;

import com.minecolonies.coremod.tileentities.TileEntityMinecoloniesChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Class which creates the GUI of our field inventory.
 */
@SideOnly(Side.CLIENT)
public class GuiChest extends GuiContainer
{
    /**
     * The resource location of the GUI background.
     */
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");

    private int inventoryRows = 0;
    /**
     * Constructor of the GUI.
     *
     * @param parInventoryPlayer the player inventory.
     * @param tileEntity         the tileEntity of the field, contains the inventory.
     * @param world              the world the field is in.
     * @param location           the location the field is at.
     */
    public GuiChest(final InventoryPlayer parInventoryPlayer, final TileEntityMinecoloniesChest tileEntity , final World world, final BlockPos location)
    {
        super(new ContainerChest(tileEntity, parInventoryPlayer, world, location));
        this.inventoryRows = tileEntity.getSizeInventory() / 9;
        this.ySize = 114 + this.inventoryRows * 18;
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, 3 * 18 + 17);
        this.drawTexturedModalRect(i, j + 3 * 18 + 17, 0, 126, this.xSize, 96);
    }
}

