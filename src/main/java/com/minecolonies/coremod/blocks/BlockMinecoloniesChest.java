package com.minecolonies.coremod.blocks;

import com.minecolonies.coremod.creativetab.ModCreativeTabs;
import com.minecolonies.coremod.lib.Constants;
import com.minecolonies.coremod.tileentities.TileEntityMinecoloniesChest;
import net.minecraft.block.BlockChest;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Locale;

public class BlockMinecoloniesChest extends BlockChest
{
    //todo on destroyed drop normal chest!
    /**
     * The name of the block.
     */
    private static final String BLOCK_NAME = "blockChest";

    protected BlockMinecoloniesChest(final Type chestTypeIn)
    {
        super(chestTypeIn);
        initBlock();
    }

    private void initBlock()
    {
        setRegistryName(BLOCK_NAME);
        setUnlocalizedName(String.format("%s.%s", Constants.MOD_ID.toLowerCase(Locale.ENGLISH), BLOCK_NAME));
        setCreativeTab(ModCreativeTabs.MINECOLONIES);
        GameRegistry.register(this);
        GameRegistry.register((new ItemBlock(this)).setRegistryName(this.getRegistryName()));
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
