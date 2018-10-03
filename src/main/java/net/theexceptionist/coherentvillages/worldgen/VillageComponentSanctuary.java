package net.theexceptionist.coherentvillages.worldgen;

import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public class VillageComponentSanctuary extends StructureVillagePieces.Village
{
	static int numBlocks = 0;
	static boolean up = false;
	private int villagersSpawned = 0;
    public VillageComponentSanctuary()
    {
    }

    public VillageComponentSanctuary(StructureVillagePieces.Start start, int p_i45568_2_, Random rand, StructureBoundingBox p_i45568_4_, EnumFacing facing)
    {
        super(start, p_i45568_2_);
        this.setCoordBaseMode(facing);
        this.boundingBox = p_i45568_4_;
    }

    public static StructureBoundingBox findPieceBox(StructureVillagePieces.Start start, List<StructureComponent> p_175856_1_, Random rand, int p_175856_3_, int p_175856_4_, int p_175856_5_, EnumFacing facing, int p5)
    {
    	numBlocks = 6  + rand.nextInt(8);
    	
    	StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175856_3_, p_175856_4_, p_175856_5_, 0, 0, 0, 6, 6, 6, facing);
    	return StructureComponent.findIntersecting(p_175856_1_, structureboundingbox) != null ? null : structureboundingbox;
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
     * Mineshafts at the end, it adds Fences...
     */
    @Override
    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
    {
        if (this.averageGroundLvl < 0)
        {
            this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

            if (this.averageGroundLvl < 0)
            {
                return true;
            }

            this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 4 - 1, 0);
        }

        IBlockState iblockstate = this.getBiomeSpecificBlockState(Blocks.STONEBRICK.getDefaultState());
        IBlockState iblockstate1 = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
        IBlockState iblockstate2 = null;
        
        if(worldIn.rand.nextBoolean()) iblockstate2 = iblockstate;
        else iblockstate2 = iblockstate1;
        
        int num = worldIn.rand.nextInt(3);
        
        for(int i = 0; i < num; i++)
        {
        	int x = worldIn.rand.nextInt(6);
        	int z = worldIn.rand.nextInt(6);
        	this.setBlockState(worldIn, Blocks.SAPLING.getDefaultState(), x, 0, z, structureBoundingBoxIn);
        }
        
        for(int i = 0; i < numBlocks; i++)
        {
        	int x = worldIn.rand.nextInt(6);
        	int z = worldIn.rand.nextInt(6);
        	int tall = worldIn.rand.nextInt(6) + 1;
        	
        	for(int y = 0; y < tall; y++)
        	{
        		this.setBlockState(worldIn, iblockstate2, x, y, z, structureBoundingBoxIn);
        	}
        	
        	this.replaceAirAndLiquidDownwards(worldIn, iblockstate2, x, -1, z, structureBoundingBoxIn);
        }
        
        this.spawnVillagers(worldIn, structureBoundingBoxIn, 1, 1, -1, 2);
        return true;
    }
    protected void spawnVillagers(World worldIn, StructureBoundingBox structurebb, int x, int y, int z, int count)
    {
    	int num = count;
        if (this.villagersSpawned  < num)
        {
            for (int i = villagersSpawned; i < num; ++i)
            {
                int j = this.getXWithOffset(x + i, z );
                int l = this.getZWithOffset(x + i, z);
                int k = worldIn.getTopSolidOrLiquidBlock(new BlockPos(j, 80, l)).getY();

                if (!structurebb.isVecInside(new BlockPos(j, k, l)))
                {
                    break;
                }

                ++this.villagersSpawned;
                
                Biome biomeIn = worldIn.getBiome(new BlockPos(j, l, k));
                EntityGolem golem = null;
                if (biomeIn.isSnowyBiome()) 
                {
                	golem = new EntitySnowman(worldIn);
                	num = count * 2;
                }
                else 
                {
                	golem = new EntityIronGolem(worldIn);
                }
                
                if(!this.isZombieInfested)
                {
	                golem.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(j, k, l)), null);
	                golem.setLocationAndAngles((double)j + 0.5D, (double)k, (double)l + 0.5D, 0.0F, 0.0F);
	                //entityvillager.setProfession(null);
	                worldIn.spawnEntity(golem);
                }
                /*if(worldIn.rand.nextInt(100) <= 50){
              
                	AbstractVillagerSoldier entityvillager = new AbstractVillagerSoldier(worldIn);
                	entityvillager.setLocationAndAngles((double)j + 0.5D, (double)k, (double)l + 0.5D, 0.0F, 0.0F);
                    entityvillager.setSpawnPoint((double)j + 0.5D, (double)k, (double)l + 0.5D);
                    //entityvillager.setProfession(null);
                    
                    entityvillager.finalizeMobSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityvillager)), (IEntityLivingData)null, false);
                    worldIn.spawnEntity(entityvillager);
                }else if(worldIn.rand.nextInt(100) <= 50){
               	 	EntityVillagerArcher entityvillager = new EntityVillagerArcher(worldIn);
                	entityvillager.setLocationAndAngles((double)j + 0.5D, (double)k, (double)l + 0.5D, 0.0F, 0.0F);
                    entityvillager.setSpawnPoint((double)j + 0.5D, (double)k, (double)l + 0.5D);
                    //entityvillager.setProfession(null);
                    
                    entityvillager.finalizeMobSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityvillager)), (IEntityLivingData)null, false);
                    worldIn.spawnEntity(entityvillager);
                }
                else
                {
                    	EntityVillager entityvillager = new EntityVillager(worldIn);
                        entityvillager.setLocationAndAngles((double)j + 0.5D, (double)k, (double)l + 0.5D, 0.0F, 0.0F);
                        entityvillager.setProfession(5);
                        entityvillager.finalizeMobSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityvillager)), (IEntityLivingData)null, false);
                        worldIn.spawnEntity(entityvillager);
                }*/
                /*else{
                	EntityVillagerKnight entityvillager = new EntityVillagerKnight(worldIn);
                	entityvillager.setLocationAndAngles((double)j + 0.5D, (double)k, (double)l + 0.5D, 0.0F, 0.0F);
                    entityvillager.setSpawnPoint((double)j + 0.5D, (double)k, (double)l + 0.5D);
                    entityvillager.setProfession(null);
                    
                    entityvillager.finalizeMobSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityvillager)), (IEntityLivingData)null, false);
                    worldIn.spawnEntity(entityvillager);
                }*/
            }
        }
    }
    

}