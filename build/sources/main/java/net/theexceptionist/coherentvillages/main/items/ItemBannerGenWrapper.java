package net.theexceptionist.coherentvillages.main.items;

import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import java.util.Random;

public class ItemBannerGenWrapper {
	public static void placeRandomBannerBlock(World worldIn, Random rand, EnumFacing facing, BlockPos pos)
	{
		ItemStack itemstack = new ItemStack(Items.BANNER);
		NBTTagCompound nbttagcompound1 = itemstack.getOrCreateSubCompound("BlockEntityTag");
        NBTTagList nbttaglist;

        if (nbttagcompound1.hasKey("Patterns", 9))
        {
            nbttaglist = nbttagcompound1.getTagList("Patterns", 10);
        }
        else
        {
            nbttaglist = new NBTTagList();
            nbttagcompound1.setTag("Patterns", nbttaglist);
        }
        
        BannerPattern bannerpattern = BannerPattern.values()[rand.nextInt(BannerPattern.values().length)];

        //System.out.println("Banner-------"+bannerpattern.name());
        
        int k  = rand.nextInt(0xF);
        
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setString("Pattern", bannerpattern.getHashname());
        nbttagcompound.setInteger("Color", k);
        nbttaglist.appendTag(nbttagcompound);
        
        if (facing == EnumFacing.UP)
        {
            int i = 0;//MathHelper.floor((double)((player.rotationYaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15;
            //System.out.print("===================== Banner Up");
            worldIn.setBlockState(pos, Blocks.STANDING_BANNER.getDefaultState().withProperty(BlockStandingSign.ROTATION, Integer.valueOf(i)), 3);
        }
        else
        {
            worldIn.setBlockState(pos, Blocks.WALL_BANNER.getDefaultState().withProperty(BlockWallSign.FACING, facing), 3);
        }

       // System.out.println("Placing Banner");
        
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityBanner)
        {
            ((TileEntityBanner)tileentity).setItemValues(itemstack, false);
        }
	}
}
