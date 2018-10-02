package net.theexceptionist.coherentvillages.worldgen;


import java.util.List;
import java.util.Random;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.theexceptionist.coherentvillages.entity.soldier.AbstractVillagerSoldier;
import net.theexceptionist.coherentvillages.entity.soldier.EntityVillagerGuard;
import net.theexceptionist.coherentvillages.entity.soldier.EntityVillagerMilitia;
import net.theexceptionist.coherentvillages.entity.soldier.EntityVillagerPeasant;

public class VillageComponentVillageWall extends StructureVillagePieces.Village
    {
		private BlockPos center;
		private int radius;
		private World worldIn;

        public VillageComponentVillageWall()
        {
        }

        public VillageComponentVillageWall(StructureVillagePieces.Start start, Random rand, int p_i45566_2_, EnumFacing facing, BlockPos center, int radius, World worldIn)
        {
            super(start, p_i45566_2_);
            this.center = center;
            this.radius = radius;
            this.worldIn = worldIn;
        }

        /**
         * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
         * Mineshafts at the end, it adds Fences...
         */
        
        public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
        {
        	//System.out.println("Generating"+this.getXWithOffset(0, 0)+" "+this.getZWithOffset(0, 0));
        	int x = center.getX();
        	int z = center.getZ();
        	
        	BlockPos nWall = new BlockPos(x, worldIn.getTopSolidOrLiquidBlock(new BlockPos(x, 80, z)).getY(), z + radius);
            BlockPos sWall = new BlockPos(x, worldIn.getTopSolidOrLiquidBlock(new BlockPos(x, 80, z)).getY(), z - radius);
            BlockPos eWall = new BlockPos(x + radius, worldIn.getTopSolidOrLiquidBlock(new BlockPos(x, 80, z)).getY(), z);
            BlockPos wWall = new BlockPos(x - radius, worldIn.getTopSolidOrLiquidBlock(new BlockPos(x, 80, z)).getY(), z);
            
            for(int i = x - radius; i < x + radius; i++)
            {
            	worldIn.setBlockState(new BlockPos(i, worldIn.getTopSolidOrLiquidBlock(new BlockPos(i, 80, z + radius)).getY(), z+ radius), Blocks.STONEBRICK.getDefaultState()); 
            	worldIn.setBlockState(new BlockPos(i, worldIn.getTopSolidOrLiquidBlock(new BlockPos(i, 80, z - radius)).getY(), z - radius), Blocks.STONEBRICK.getDefaultState()); 
            }
            
            for(int i = z - radius; i < z + radius; i++)
            {
            	worldIn.setBlockState(new BlockPos(x + radius, worldIn.getTopSolidOrLiquidBlock(new BlockPos(x + radius, 80, i)).getY(), i), Blocks.STONEBRICK.getDefaultState()); 
            	worldIn.setBlockState(new BlockPos(x - radius, worldIn.getTopSolidOrLiquidBlock(new BlockPos(x - radius, 80, i)).getY(), i), Blocks.STONEBRICK.getDefaultState()); 
            }
        }
        
        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
        {
        	//this.getXWithOffset(x, z)
        	//System.out.println("Generating"+this.getXWithOffset(0, 0)+" "+this.getZWithOffset(0, 0));
        	/*Village village = worldIn.getVillageCollection().getNearestVillage(new BlockPos(x, worldIn.getTopSolidOrLiquidBlock(new BlockPos(x, 80, z)).getY(), z), 30);
            int radius = village.getVillageRadius() * 2;
            System.out.println("Radius: "+radius);
            BlockPos nWall = new BlockPos(x, worldIn.getTopSolidOrLiquidBlock(new BlockPos(x, 80, z)).getY(), z + radius);
            BlockPos sWall = new BlockPos(x, worldIn.getTopSolidOrLiquidBlock(new BlockPos(x, 80, z)).getY(), z - radius);
            BlockPos eWall = new BlockPos(x + radius, worldIn.getTopSolidOrLiquidBlock(new BlockPos(x, 80, z)).getY(), z);
            BlockPos wWall = new BlockPos(x - radius, worldIn.getTopSolidOrLiquidBlock(new BlockPos(x, 80, z)).getY(), z);
            
            for(int i = x - radius; i < x + radius; i++)
            {
            	worldIn.setBlockState(new BlockPos(i, worldIn.getTopSolidOrLiquidBlock(new BlockPos(i, 80, z + radius)).getY(), z+ radius), Blocks.STONEBRICK.getDefaultState()); 
            	worldIn.setBlockState(new BlockPos(i, worldIn.getTopSolidOrLiquidBlock(new BlockPos(i, 80, z - radius)).getY(), z - radius), Blocks.STONEBRICK.getDefaultState()); 
            }
            
            for(int i = z - radius; i < z + radius; i++)
            {
            	worldIn.setBlockState(new BlockPos(x + radius, worldIn.getTopSolidOrLiquidBlock(new BlockPos(x + radius, 80, i)).getY(), i), Blocks.STONEBRICK.getDefaultState()); 
            	worldIn.setBlockState(new BlockPos(x - radius, worldIn.getTopSolidOrLiquidBlock(new BlockPos(x - radius, 80, i)).getY(), i), Blocks.STONEBRICK.getDefaultState()); 
            }*/
            return true;
        }
        
       /* protected void spawnVillagers(World worldIn, StructureBoundingBox structurebb, int x, int y, int z, int count)
        {
            if (this.villagersSpawned < count)
            {
                for (int i = villagersSpawned; i < count; ++i)
                {
                    int j = this.getXWithOffset(x + i, z);
                    int k = this.getYWithOffset(y);
                    int l = this.getZWithOffset(x + i, z);

                    if (!structurebb.isVecInside(new BlockPos(j, k, l)))
                    {
                        break;
                    }

                    ++this.villagersSpawned;

                    AbstractVillagerSoldier entityvillager = null;
                    if(worldIn.rand.nextInt(100) < 50) entityvillager = new EntityVillagerMilitia(worldIn);
                    else if (worldIn.rand.nextInt(100) < 50) entityvillager = new EntityVillagerGuard(worldIn);
                    else entityvillager = new EntityVillagerPeasant(worldIn);
                   
                  	if(entityvillager.isCanSpawn())
                  	{
	                    entityvillager.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(j, k, l)), null);
	                    entityvillager.setLocationAndAngles((double)j + 0.5D, (double)k, (double)l + 0.5D, 0.0F, 0.0F);
	                    entityvillager.setSpawnPoint((double)j + 0.5D, (double)k, (double)l + 0.5D);
	                    //entityvillager.setProfession(null);
	                    
	                    entityvillager.finalizeMobSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityvillager)), (IEntityLivingData)null, false);
	                    worldIn.spawnEntity(entityvillager);
                  	}
                }
            }
        }*/
 
}
