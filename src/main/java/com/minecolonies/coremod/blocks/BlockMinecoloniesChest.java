package com.minecolonies.coremod.blocks;

import com.minecolonies.coremod.MineColonies;
import com.minecolonies.coremod.colony.Colony;
import com.minecolonies.coremod.colony.ColonyManager;
import com.minecolonies.coremod.creativetab.ModCreativeTabs;
import com.minecolonies.coremod.lib.Constants;
import com.minecolonies.coremod.tileentities.TileEntityMinecoloniesChest;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Random;

public class BlockMinecoloniesChest extends BlockChest
{
    private static final float HARDNESS = 10F;
    private static final float RESISTANCE = Float.POSITIVE_INFINITY;

    /**
     * The name of the block.
     */
    private static final String BLOCK_NAME = "blockChest";

    public BlockMinecoloniesChest(final Type chestTypeIn)
    {
        super(chestTypeIn);
        initBlock();
    }

    private void initBlock()
    {
        setRegistryName(BLOCK_NAME);
        setUnlocalizedName(String.format("%s.%s", Constants.MOD_ID.toLowerCase(Locale.ENGLISH), BLOCK_NAME));
        setCreativeTab(ModCreativeTabs.MINECOLONIES);
        //Blast resistance for creepers etc. makes them explosion proof
        setResistance(RESISTANCE);
        //Hardness of 10 takes a long time to mine to not loose progress
        setHardness(HARDNESS);
        GameRegistry.register(this);
        GameRegistry.register((new ItemBlock(this)).setRegistryName(this.getRegistryName()));
    }

    @Override
    public boolean onBlockActivated(
            final World worldIn,
            final BlockPos pos,
            final IBlockState state,
            final EntityPlayer playerIn,
            final EnumHand hand,
            @org.jetbrains.annotations.Nullable final ItemStack heldItem,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ)
    {
        //If the world is server, open the inventory of the field.
        if (!worldIn.isRemote)
        {
            @org.jetbrains.annotations.Nullable final Colony colony = ColonyManager.getColony(worldIn, pos);
            if (colony != null)
            {
                playerIn.openGui(MineColonies.instance, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune)
    {
        return ItemBlock.getItemFromBlock(Blocks.CHEST);
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityMinecoloniesChest();
    }
}
