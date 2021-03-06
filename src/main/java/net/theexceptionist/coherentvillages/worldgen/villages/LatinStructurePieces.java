package net.theexceptionist.coherentvillages.worldgen.villages;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeSavanna;
import net.minecraft.world.biome.BiomeTaiga;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;
import net.theexceptionist.coherentvillages.main.Main;
import net.theexceptionist.coherentvillages.main.entity.EntityHumanVillager;
import net.theexceptionist.coherentvillages.main.entity.EntityWarg;
import net.theexceptionist.coherentvillages.main.entity.EntityWraith;
import net.theexceptionist.coherentvillages.main.entity.attributes.AttributeRace;
import net.theexceptionist.coherentvillages.main.entity.attributes.AttributeVocation;
import net.theexceptionist.coherentvillages.main.items.ItemBannerGenWrapper;
import net.theexceptionist.coherentvillages.worldgen.villages.arrays.LatinBuildingsArray;
import net.theexceptionist.coherentvillages.worldgen.villages.chartypes.CharBlockTypes;

public class LatinStructurePieces
{
    public static void registerVillagePieces()
    {
        MapGenStructureIO.registerStructureComponent(LatinStructurePieces.ArcherHut.class, "ViBH");//**
        MapGenStructureIO.registerStructureComponent(LatinStructurePieces.Field1.class, "ViDF");//**
        MapGenStructureIO.registerStructureComponent(LatinStructurePieces.Field2.class, "ViF");//**
        MapGenStructureIO.registerStructureComponent(LatinStructurePieces.Torch.class, "ViL");//**
        MapGenStructureIO.registerStructureComponent(LatinStructurePieces.Banner.class, "ViB");
        MapGenStructureIO.registerStructureComponent(LatinStructurePieces.GuardHouse.class, "ViGH");//**
        MapGenStructureIO.registerStructureComponent(LatinStructurePieces.Shrine.class, "ViSH");//
        MapGenStructureIO.registerStructureComponent(LatinStructurePieces.SmallHut.class, "ViSmH");//**
        MapGenStructureIO.registerStructureComponent(LatinStructurePieces.ConsulHouse.class, "ViST");//**
        MapGenStructureIO.registerStructureComponent(LatinStructurePieces.BathHouse.class, "ViS");//**
        MapGenStructureIO.registerStructureComponent(LatinStructurePieces.Start.class, "ViStart");//**
        MapGenStructureIO.registerStructureComponent(LatinStructurePieces.Path.class, "ViSR");//**
        MapGenStructureIO.registerStructureComponent(LatinStructurePieces.TallHouse.class, "ViLH");//**
        MapGenStructureIO.registerStructureComponent(LatinStructurePieces.Well.class, "ViW");//**
    }

    public static List<LatinStructurePieces.PieceWeight> getStructureVillageWeightedPieceList(Random random, int size)
    {
        List<LatinStructurePieces.PieceWeight> list = Lists.<LatinStructurePieces.PieceWeight>newArrayList();
        list.add(new LatinStructurePieces.PieceWeight(LatinStructurePieces.Shrine.class, 4, MathHelper.getInt(random, 2 + size, 4 + size * 2)));
        list.add(new LatinStructurePieces.PieceWeight(LatinStructurePieces.ConsulHouse.class, 20, MathHelper.getInt(random, 0 + size, 1 + size)));
        list.add(new LatinStructurePieces.PieceWeight(LatinStructurePieces.ArcherHut.class, 20, MathHelper.getInt(random, 0 + size, 2 + size)));
        list.add(new LatinStructurePieces.PieceWeight(LatinStructurePieces.SmallHut.class, 3, MathHelper.getInt(random, 2 + size, 5 + size * 3)));
        list.add(new LatinStructurePieces.PieceWeight(LatinStructurePieces.TallHouse.class, 15, MathHelper.getInt(random, 0 + size, 2 + size)));
        list.add(new LatinStructurePieces.PieceWeight(LatinStructurePieces.Field1.class, 3, MathHelper.getInt(random, 1 + size, 4 + size)));
        list.add(new LatinStructurePieces.PieceWeight(LatinStructurePieces.Field2.class, 3, MathHelper.getInt(random, 2 + size, 4 + size * 2)));
        list.add(new LatinStructurePieces.PieceWeight(LatinStructurePieces.BathHouse.class, 15, MathHelper.getInt(random, 0, 1 + size)));
        list.add(new LatinStructurePieces.PieceWeight(LatinStructurePieces.GuardHouse.class, 8, MathHelper.getInt(random, 0 + size, 3 + size * 2)));
        Iterator<LatinStructurePieces.PieceWeight> iterator = list.iterator();

        while (iterator.hasNext())
        {
            if ((iterator.next()).villagePiecesLimit == 0)
            {
                iterator.remove();
            }
        }

        return list;
    }

    private static int updatePieceWeight(List<LatinStructurePieces.PieceWeight> p_75079_0_)
    {
        boolean flag = false;
        int i = 0;

        for (LatinStructurePieces.PieceWeight NordStructurePieces$pieceweight : p_75079_0_)
        {
            if (NordStructurePieces$pieceweight.villagePiecesLimit > 0 && NordStructurePieces$pieceweight.villagePiecesSpawned < NordStructurePieces$pieceweight.villagePiecesLimit)
            {
                flag = true;
            }

            i += NordStructurePieces$pieceweight.villagePieceWeight;
        }

        return flag ? i : -1;
    }

    private static LatinStructurePieces.Village findAndCreateComponentFactory(LatinStructurePieces.Start start, LatinStructurePieces.PieceWeight weight, List<StructureComponent> structureComponents, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing, int componentType)
    {
        Class <? extends LatinStructurePieces.Village > oclass = weight.villagePieceClass;
        LatinStructurePieces.Village NordStructurePieces$village = null;

        if (oclass == LatinStructurePieces.Shrine.class)
        {
            NordStructurePieces$village = LatinStructurePieces.Shrine.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);
        }
        else if (oclass == LatinStructurePieces.ConsulHouse.class)
        {
            NordStructurePieces$village = LatinStructurePieces.ConsulHouse.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);
        }
        else if (oclass == LatinStructurePieces.ArcherHut.class)
        {
            NordStructurePieces$village = LatinStructurePieces.ArcherHut.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);
        }
        else if (oclass == LatinStructurePieces.SmallHut.class)
        {
            NordStructurePieces$village = LatinStructurePieces.SmallHut.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);
        }
        else if (oclass == LatinStructurePieces.GuardHouse.class)
        {
            NordStructurePieces$village = LatinStructurePieces.GuardHouse.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);
        }
        else if (oclass == LatinStructurePieces.Field1.class)
        {
            NordStructurePieces$village = LatinStructurePieces.Field1.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);
        }
        else if (oclass == LatinStructurePieces.Field2.class)
        {
            NordStructurePieces$village = LatinStructurePieces.Field2.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);
        }
        else if (oclass == LatinStructurePieces.BathHouse.class)
        {
            NordStructurePieces$village = LatinStructurePieces.BathHouse.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);
        }
        else if (oclass == LatinStructurePieces.TallHouse.class)
        {
            NordStructurePieces$village = LatinStructurePieces.TallHouse.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);
        }

        return NordStructurePieces$village;
    }

    private static LatinStructurePieces.Village generateComponent(LatinStructurePieces.Start start, List<StructureComponent> structureComponents, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing, int componentType)
    {
        int i = updatePieceWeight(start.structureVillageWeightedPieceList);

        if (i <= 0)
        {
            return null;
        }
        else
        {
            int j = 0;

            while (j < 5)
            {
                ++j;
                int k = rand.nextInt(i);

                for (LatinStructurePieces.PieceWeight NordStructurePieces$pieceweight : start.structureVillageWeightedPieceList)
                {
                    k -= NordStructurePieces$pieceweight.villagePieceWeight;

                    if (k < 0)
                    {
                        if (!NordStructurePieces$pieceweight.canSpawnMoreVillagePiecesOfType(componentType) || NordStructurePieces$pieceweight == start.lastPlaced && start.structureVillageWeightedPieceList.size() > 1)
                        {
                            break;
                        }

                        LatinStructurePieces.Village NordStructurePieces$village = findAndCreateComponentFactory(start, NordStructurePieces$pieceweight, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);

                        if (NordStructurePieces$village != null)
                        {
                            ++NordStructurePieces$pieceweight.villagePiecesSpawned;
                            start.lastPlaced = NordStructurePieces$pieceweight;

                            if (!NordStructurePieces$pieceweight.canSpawnMoreVillagePieces())
                            {
                                start.structureVillageWeightedPieceList.remove(NordStructurePieces$pieceweight);
                            }

                            return NordStructurePieces$village;
                        }
                    }
                }
            }

            StructureBoundingBox structureboundingbox = LatinStructurePieces.Torch.findPieceBox(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing);

            if (structureboundingbox != null)
            {
               // if(rand.nextInt(100) < 50) return new LatinStructurePieces.Banner(start, componentType, rand, structureboundingbox, EnumFacing.UP);///return new LatinStructurePieces.Torch(start, componentType, rand, structureboundingbox, facing);
                //else return new LatinStructurePieces.Banner(start, componentType, rand, structureboundingbox, EnumFacing.UP);
            	return new LatinStructurePieces.Torch(start, componentType, rand, structureboundingbox, facing);
            }
            else
            {
                return null;
            }
        }
    }

    private static StructureComponent generateAndAddComponent(LatinStructurePieces.Start start, List<StructureComponent> structureComponents, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing, int componentType)
    {
        if (componentType > 50)
        {
            return null;
        }
        else if (Math.abs(structureMinX - start.getBoundingBox().minX) <= 112 && Math.abs(structureMinZ - start.getBoundingBox().minZ) <= 112)
        {
            StructureComponent structurecomponent = generateComponent(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType + 1);

            if (structurecomponent != null)
            {
                structureComponents.add(structurecomponent);
                start.pendingHouses.add(structurecomponent);
                return structurecomponent;
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    private static StructureComponent generateAndAddRoadPiece(LatinStructurePieces.Start start, List<StructureComponent> p_176069_1_, Random rand, int p_176069_3_, int p_176069_4_, int p_176069_5_, EnumFacing facing, int p_176069_7_)
    {
    	//System.out.print("Exectuing");
        if (p_176069_7_ > 3 + start.terrainType)
        {
            return null;
        }
        else if (Math.abs(p_176069_3_ - start.getBoundingBox().minX) <= 112 && Math.abs(p_176069_5_ - start.getBoundingBox().minZ) <= 112)
        {
            StructureBoundingBox structureboundingbox = LatinStructurePieces.Path.findPieceBox(start, p_176069_1_, rand, p_176069_3_, p_176069_4_, p_176069_5_, facing);

            if (structureboundingbox != null && structureboundingbox.minY > 10)
            {
                StructureComponent structurecomponent = new LatinStructurePieces.Path(start, p_176069_7_, rand, structureboundingbox, facing);
                p_176069_1_.add(structurecomponent);
                start.pendingRoads.add(structurecomponent);
                return structurecomponent;
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    public static class ConsulHouse extends LatinStructurePieces.Village
        {
            public ConsulHouse()
            {
            }

            public ConsulHouse(LatinStructurePieces.Start start, int type, Random rand, StructureBoundingBox p_i45564_4_, EnumFacing facing)
            {
                super(start, type);
                this.setCoordBaseMode(facing);
                this.front = facing;
                this.boundingBox = p_i45564_4_;
            }

            public static LatinStructurePieces.ConsulHouse createPiece(LatinStructurePieces.Start start, List<StructureComponent> p_175854_1_, Random rand, int p_175854_3_, int p_175854_4_, int p_175854_5_, EnumFacing facing, int p_175854_7_)
            {
                StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175854_3_, p_175854_4_, p_175854_5_, 0, 0, 0, LatinBuildingsArray.CONSUL_WIDTH, LatinBuildingsArray.CONSUL_HEIGHT, LatinBuildingsArray.CONSUL_LENGTH, facing);
                return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175854_1_, structureboundingbox) == null ? new LatinStructurePieces.ConsulHouse(start, p_175854_7_, rand, structureboundingbox, facing) : null;
            }

            /**
             * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
             * Mineshafts at the end, it adds Fences...
             */
            public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
            {
                if (this.averageGroundLvl < 0)
                {
                    this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

                    if (this.averageGroundLvl < 0)
                    {
                        return true;
                    }

                    this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + LatinBuildingsArray.CONSUL_HEIGHT - 1, 0);
                }

                this.placeStructureBase(worldIn, structureBoundingBoxIn, randomIn, LatinBuildingsArray.consul, LatinBuildingsArray.CONSUL_LENGTH, LatinBuildingsArray.CONSUL_HEIGHT, LatinBuildingsArray.CONSUL_WIDTH, front);

                this.spawnVillagers(worldIn, structureBoundingBoxIn, 2, 1, 2, 1, AttributeVocation.CLASS_MERCENARY, 0, true);
                return true;
            }

            protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession)
            {
                return 2;
            }
        }

    public static class Field1 extends LatinStructurePieces.Village
        {
            /** First crop type for this field. */
            private Block cropTypeA;
            /** Second crop type for this field. */
            private Block cropTypeB;
            /** Third crop type for this field. */
            private Block cropTypeC;
            /** Fourth crop type for this field. */
            private Block cropTypeD;

            public Field1()
            {
            }

            public Field1(LatinStructurePieces.Start start, int type, Random rand, StructureBoundingBox p_i45570_4_, EnumFacing facing)
            {
                super(start, type);
                this.setCoordBaseMode(facing);
                this.boundingBox = p_i45570_4_;
                this.cropTypeA = this.getRandomCropType(rand);
                this.cropTypeB = this.getRandomCropType(rand);
                this.cropTypeC = this.getRandomCropType(rand);
                this.cropTypeD = this.getRandomCropType(rand);
            }

            /**
             * (abstract) Helper method to write subclass data to NBT
             */
            protected void writeStructureToNBT(NBTTagCompound tagCompound)
            {
                super.writeStructureToNBT(tagCompound);
                tagCompound.setInteger("CA", Block.REGISTRY.getIDForObject(this.cropTypeA));
                tagCompound.setInteger("CB", Block.REGISTRY.getIDForObject(this.cropTypeB));
                tagCompound.setInteger("CC", Block.REGISTRY.getIDForObject(this.cropTypeC));
                tagCompound.setInteger("CD", Block.REGISTRY.getIDForObject(this.cropTypeD));
            }

            /**
             * (abstract) Helper method to read subclass data from NBT
             */
            protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
            {
                super.readStructureFromNBT(tagCompound, p_143011_2_);
                this.cropTypeA = Block.getBlockById(tagCompound.getInteger("CA"));
                this.cropTypeB = Block.getBlockById(tagCompound.getInteger("CB"));
                this.cropTypeC = Block.getBlockById(tagCompound.getInteger("CC"));
                this.cropTypeD = Block.getBlockById(tagCompound.getInteger("CD"));

                if (!(this.cropTypeA instanceof BlockCrops))
                {
                    this.cropTypeA = Blocks.WHEAT;
                }

                if (!(this.cropTypeB instanceof BlockCrops))
                {
                    this.cropTypeB = Blocks.CARROTS;
                }

                if (!(this.cropTypeC instanceof BlockCrops))
                {
                    this.cropTypeC = Blocks.POTATOES;
                }

                if (!(this.cropTypeD instanceof BlockCrops))
                {
                    this.cropTypeD = Blocks.BEETROOTS;
                }
            }

            private Block getRandomCropType(Random rand)
            {
            	return Blocks.WHEAT;
            }

            public static LatinStructurePieces.Field1 createPiece(LatinStructurePieces.Start start, List<StructureComponent> p_175851_1_, Random rand, int p_175851_3_, int p_175851_4_, int p_175851_5_, EnumFacing facing, int p_175851_7_)
            {
            	if(start.isBanditInfested) return null;
                StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175851_3_, p_175851_4_, p_175851_5_, 0, 0, 0, 13, 4, 9, facing);
                return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175851_1_, structureboundingbox) == null ? new LatinStructurePieces.Field1(start, p_175851_7_, rand, structureboundingbox, facing) : null;
            }

            /**
             * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
             * Mineshafts at the end, it adds Fences...
             */
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

                IBlockState iblockstate = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 12, 4, 8, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 2, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 1, 5, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 1, 8, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 0, 1, 11, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 0, 0, 8, iblockstate, iblockstate, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 0, 6, 0, 8, iblockstate, iblockstate, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 0, 0, 12, 0, 8, iblockstate, iblockstate, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 11, 0, 0, iblockstate, iblockstate, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 8, 11, 0, 8, iblockstate, iblockstate, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 1, 3, 0, 7, Blocks.WATER.getDefaultState(), Blocks.WATER.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 0, 1, 9, 0, 7, Blocks.WATER.getDefaultState(), Blocks.WATER.getDefaultState(), false);

                for (int i = 1; i <= 7; ++i)
                {
                    int j = ((BlockCrops)this.cropTypeA).getMaxAge();
                    int k = j / 3;
                    this.setBlockState(worldIn, this.cropTypeA.getStateFromMeta(MathHelper.getInt(randomIn, k, j)), 1, 1, i, structureBoundingBoxIn);
                    this.setBlockState(worldIn, this.cropTypeA.getStateFromMeta(MathHelper.getInt(randomIn, k, j)), 2, 1, i, structureBoundingBoxIn);
                    int l = ((BlockCrops)this.cropTypeB).getMaxAge();
                    int i1 = l / 3;
                    this.setBlockState(worldIn, this.cropTypeB.getStateFromMeta(MathHelper.getInt(randomIn, i1, l)), 4, 1, i, structureBoundingBoxIn);
                    this.setBlockState(worldIn, this.cropTypeB.getStateFromMeta(MathHelper.getInt(randomIn, i1, l)), 5, 1, i, structureBoundingBoxIn);
                    int j1 = ((BlockCrops)this.cropTypeC).getMaxAge();
                    int k1 = j1 / 3;
                    this.setBlockState(worldIn, this.cropTypeC.getStateFromMeta(MathHelper.getInt(randomIn, k1, j1)), 7, 1, i, structureBoundingBoxIn);
                    this.setBlockState(worldIn, this.cropTypeC.getStateFromMeta(MathHelper.getInt(randomIn, k1, j1)), 8, 1, i, structureBoundingBoxIn);
                    int l1 = ((BlockCrops)this.cropTypeD).getMaxAge();
                    int i2 = l1 / 3;
                    this.setBlockState(worldIn, this.cropTypeD.getStateFromMeta(MathHelper.getInt(randomIn, i2, l1)), 10, 1, i, structureBoundingBoxIn);
                    this.setBlockState(worldIn, this.cropTypeD.getStateFromMeta(MathHelper.getInt(randomIn, i2, l1)), 11, 1, i, structureBoundingBoxIn);
                }

                for (int j2 = 0; j2 < 9; ++j2)
                {
                    for (int k2 = 0; k2 < 13; ++k2)
                    {
                        this.clearCurrentPositionBlocksUpwards(worldIn, k2, 4, j2, structureBoundingBoxIn);
                        this.replaceAirAndLiquidDownwards(worldIn, Blocks.DIRT.getDefaultState(), k2, -1, j2, structureBoundingBoxIn);
                    }
                }

                return true;
            }
        }

    public static class Field2 extends LatinStructurePieces.Village
        {
            /** First crop type for this field. */
            private Block cropTypeA;
            /** Second crop type for this field. */
            private Block cropTypeB;

            public Field2()
            {
            }

            public Field2(LatinStructurePieces.Start start, int p_i45569_2_, Random rand, StructureBoundingBox p_i45569_4_, EnumFacing facing)
            {
                super(start, p_i45569_2_);
                this.setCoordBaseMode(facing);
                this.boundingBox = p_i45569_4_;
                this.cropTypeA = this.getRandomCropType(rand);
                this.cropTypeB = this.getRandomCropType(rand);
            }

            /**
             * (abstract) Helper method to write subclass data to NBT
             */
            protected void writeStructureToNBT(NBTTagCompound tagCompound)
            {
                super.writeStructureToNBT(tagCompound);
                tagCompound.setInteger("CA", Block.REGISTRY.getIDForObject(this.cropTypeA));
                tagCompound.setInteger("CB", Block.REGISTRY.getIDForObject(this.cropTypeB));
            }

            /**
             * (abstract) Helper method to read subclass data from NBT
             */
            protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
            {
                super.readStructureFromNBT(tagCompound, p_143011_2_);
                this.cropTypeA = Block.getBlockById(tagCompound.getInteger("CA"));
                this.cropTypeB = Block.getBlockById(tagCompound.getInteger("CB"));
            }

            private Block getRandomCropType(Random rand)
            {
            	if(rand.nextInt(50) <= 25) return Blocks.BEETROOTS;
            	return Blocks.WHEAT;
            }

            public static LatinStructurePieces.Field2 createPiece(LatinStructurePieces.Start start, List<StructureComponent> p_175852_1_, Random rand, int p_175852_3_, int p_175852_4_, int p_175852_5_, EnumFacing facing, int p_175852_7_)
            {
            	if(start.isBanditInfested) return null;
                StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175852_3_, p_175852_4_, p_175852_5_, 0, 0, 0, 7, 4, 9, facing);
                return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175852_1_, structureboundingbox) == null ? new LatinStructurePieces.Field2(start, p_175852_7_, rand, structureboundingbox, facing) : null;
            }

            /**
             * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
             * Mineshafts at the end, it adds Fences...
             */
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

                IBlockState iblockstate = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 6, 4, 8, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 2, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 1, 5, 0, 7, Blocks.FARMLAND.getDefaultState(), Blocks.FARMLAND.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 0, 0, 8, iblockstate, iblockstate, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 0, 6, 0, 8, iblockstate, iblockstate, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 5, 0, 0, iblockstate, iblockstate, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 8, 5, 0, 8, iblockstate, iblockstate, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 1, 3, 0, 7, Blocks.WATER.getDefaultState(), Blocks.WATER.getDefaultState(), false);

                for (int i = 1; i <= 7; ++i)
                {
                    int j = ((BlockCrops)this.cropTypeA).getMaxAge();
                    int k = j / 3;
                    this.setBlockState(worldIn, this.cropTypeA.getStateFromMeta(MathHelper.getInt(randomIn, k, j)), 1, 1, i, structureBoundingBoxIn);
                    this.setBlockState(worldIn, this.cropTypeA.getStateFromMeta(MathHelper.getInt(randomIn, k, j)), 2, 1, i, structureBoundingBoxIn);
                    int l = ((BlockCrops)this.cropTypeB).getMaxAge();
                    int i1 = l / 3;
                    this.setBlockState(worldIn, this.cropTypeB.getStateFromMeta(MathHelper.getInt(randomIn, i1, l)), 4, 1, i, structureBoundingBoxIn);
                    this.setBlockState(worldIn, this.cropTypeB.getStateFromMeta(MathHelper.getInt(randomIn, i1, l)), 5, 1, i, structureBoundingBoxIn);
                }

                for (int j1 = 0; j1 < 9; ++j1)
                {
                    for (int k1 = 0; k1 < 7; ++k1)
                    {
                        this.clearCurrentPositionBlocksUpwards(worldIn, k1, 4, j1, structureBoundingBoxIn);
                        this.replaceAirAndLiquidDownwards(worldIn, Blocks.DIRT.getDefaultState(), k1, -1, j1, structureBoundingBoxIn);
                    }
                }

                return true;
            }
        }

    public static class GuardHouse extends LatinStructurePieces.Village
        {
            public GuardHouse()
            {
            }

            public GuardHouse(LatinStructurePieces.Start start, int type, Random rand, StructureBoundingBox p_i45567_4_, EnumFacing facing)
            {
                super(start, type);
                this.setCoordBaseMode(facing);
                this.front = facing;
                this.boundingBox = p_i45567_4_;
            }

            public static LatinStructurePieces.GuardHouse createPiece(LatinStructurePieces.Start start, List<StructureComponent> p_175857_1_, Random rand, int p_175857_3_, int p_175857_4_, int p_175857_5_, EnumFacing facing, int p_175857_7_)
            {
                StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175857_3_, p_175857_4_, p_175857_5_, 0, 0, 0, LatinBuildingsArray.GUARDHOUSE_WIDTH, LatinBuildingsArray.GUARDHOUSE_HEIGHT, LatinBuildingsArray.GUARDHOUSE_LENGTH, facing);
                return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175857_1_, structureboundingbox) == null ? new LatinStructurePieces.GuardHouse(start, p_175857_7_, rand, structureboundingbox, facing) : null;
            }

            /**
             * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
             * Mineshafts at the end, it adds Fences...
             */
            public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
            {
                if (this.averageGroundLvl < 0)
                {
                    this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

                    if (this.averageGroundLvl < 0)
                    {
                        return true;
                    }

                    this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + LatinBuildingsArray.GUARDHOUSE_HEIGHT, 0);
                }
                this.placeStructureBase(worldIn, structureBoundingBoxIn, randomIn, LatinBuildingsArray.guardhut_1, LatinBuildingsArray.GUARDHOUSE_LENGTH, LatinBuildingsArray.GUARDHOUSE_HEIGHT, LatinBuildingsArray.GUARDHOUSE_WIDTH, front);

                this.spawnVillagers(worldIn, structureBoundingBoxIn, 2, 1, 2, 2, AttributeVocation.CLASS_SOLDIER, 0, true);
                return true;
            }

            protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession)
            {
                return villagersSpawnedIn == 0 ? 4 : super.chooseProfession(villagersSpawnedIn, currentVillagerProfession);
            }
        }

    public static class ArcherHut extends LatinStructurePieces.Village
        {
            public ArcherHut()
            {
            }

            public ArcherHut(LatinStructurePieces.Start start, int type, Random rand, StructureBoundingBox p_i45571_4_, EnumFacing facing)
            {
                super(start, type);
                this.setCoordBaseMode(facing);
                this.front = facing;
                this.boundingBox = p_i45571_4_;
            }

            public static LatinStructurePieces.ArcherHut createPiece(LatinStructurePieces.Start start, List<StructureComponent> p_175850_1_, Random rand, int p_175850_3_, int p_175850_4_, int p_175850_5_, EnumFacing facing, int p_175850_7_)
            {
                StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175850_3_, p_175850_4_, p_175850_5_, 0, 0, 0, LatinBuildingsArray.ARCHERHUT_WIDTH, LatinBuildingsArray.ARCHERHUT_HEIGHT, LatinBuildingsArray.ARCHERHUT_LENGTH, facing);
                return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175850_1_, structureboundingbox) == null ? new LatinStructurePieces.ArcherHut(start, p_175850_7_, rand, structureboundingbox, facing) : null;
            }

            /**
             * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
             * Mineshafts at the end, it adds Fences...
             */
            public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
            {
                if (this.averageGroundLvl < 0)
                {
                    this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

                    if (this.averageGroundLvl < 0)
                    {
                        return true;
                    }

                    this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + LatinBuildingsArray.ARCHERHUT_HEIGHT - 1, 0);
                }

                this.placeStructureBase(worldIn, structureBoundingBoxIn, randomIn, LatinBuildingsArray.archer_hut_1, LatinBuildingsArray.ARCHERHUT_LENGTH, LatinBuildingsArray.ARCHERHUT_HEIGHT, LatinBuildingsArray.ARCHERHUT_WIDTH, front);
                this.spawnVillagers(worldIn, structureBoundingBoxIn, 3, 2, -2, 2, AttributeVocation.CLASS_ARCHER, 0, true);
                return true;
            }

            protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession)
            {
                return 1;
            }
        }

    public static class BathHouse extends LatinStructurePieces.Village
        {
            private boolean hasMadeChest;

            public BathHouse()
            {
            }

            public BathHouse(LatinStructurePieces.Start start, int type, Random rand, StructureBoundingBox p_i45563_4_, EnumFacing facing)
            {
                super(start, type);
                this.front = facing;
                this.setCoordBaseMode(facing);
                this.boundingBox = p_i45563_4_;
            }

            public static LatinStructurePieces.BathHouse createPiece(LatinStructurePieces.Start start, List<StructureComponent> p_175855_1_, Random rand, int p_175855_3_, int p_175855_4_, int p_175855_5_, EnumFacing facing, int p_175855_7_)
            {
                StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175855_3_, p_175855_4_, p_175855_5_, 0, 0, 0, LatinBuildingsArray.BATHHOUSE_WIDTH, LatinBuildingsArray.BATHHOUSE_HEIGHT, LatinBuildingsArray.BATHHOUSE_LENGTH, facing);
                return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175855_1_, structureboundingbox) == null ? new LatinStructurePieces.BathHouse(start, p_175855_7_, rand, structureboundingbox, facing) : null;
            }

            /**
             * (abstract) Helper method to write subclass data to NBT
             */
            protected void writeStructureToNBT(NBTTagCompound tagCompound)
            {
                super.writeStructureToNBT(tagCompound);
                tagCompound.setBoolean("Chest", this.hasMadeChest);
            }

            /**
             * (abstract) Helper method to read subclass data from NBT
             */
            protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
            {
                super.readStructureFromNBT(tagCompound, p_143011_2_);
                this.hasMadeChest = tagCompound.getBoolean("Chest");
            }

            /**
             * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
             * Mineshafts at the end, it adds Fences...
             */
            public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
            {
                if (this.averageGroundLvl < 0)
                {
                    this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

                    if (this.averageGroundLvl < 0)
                    {
                        return true;
                    }

                    this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + LatinBuildingsArray.BATHHOUSE_HEIGHT - 1, 0);
                }

                this.placeStructureBase(worldIn, structureBoundingBoxIn, randomIn, LatinBuildingsArray.bathhouse_1, LatinBuildingsArray.BATHHOUSE_LENGTH, LatinBuildingsArray.BATHHOUSE_HEIGHT, LatinBuildingsArray.BATHHOUSE_WIDTH, front);

                this.spawnVillagers(worldIn, structureBoundingBoxIn, 2, 1, 1, 1, AttributeVocation.CLASS_ALCHEMIST, randomIn.nextInt(2), false);
                return true;
            }

            protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession)
            {
                return 3;
            }
        }

    public static class TallHouse extends LatinStructurePieces.Village
        {
            public TallHouse()
            {
            }

            public TallHouse(LatinStructurePieces.Start start, int type, Random rand, StructureBoundingBox p_i45561_4_, EnumFacing facing)
            {
                super(start, type);
                this.front = facing;
                this.setCoordBaseMode(facing);
                this.boundingBox = p_i45561_4_;
            }

            public static LatinStructurePieces.TallHouse createPiece(LatinStructurePieces.Start start, List<StructureComponent> p_175849_1_, Random rand, int p_175849_3_, int p_175849_4_, int p_175849_5_, EnumFacing facing, int p_175849_7_)
            {
                StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175849_3_, p_175849_4_, p_175849_5_, 0, 0, 0, LatinBuildingsArray.TALLHOUSE_WIDTH, LatinBuildingsArray.TALLHOUSE_HEIGHT, LatinBuildingsArray.TALLHOUSE_HEIGHT, facing);
                return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175849_1_, structureboundingbox) == null ? new LatinStructurePieces.TallHouse(start, p_175849_7_, rand, structureboundingbox, facing) : null;
            }

            /**
             * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
             * Mineshafts at the end, it adds Fences...
             */
            public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
            {
            	if (this.averageGroundLvl < 0)
                {
                    this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

                    if (this.averageGroundLvl < 0)
                    {
                        return true;
                    }

                    this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + LatinBuildingsArray.TALLHOUSE_HEIGHT - 1, -1);
                }

                this.placeStructureBase(worldIn, structureBoundingBoxIn, randomIn, LatinBuildingsArray.tallhouse_0, LatinBuildingsArray.TALLHOUSE_LENGTH, LatinBuildingsArray.TALLHOUSE_HEIGHT, LatinBuildingsArray.TALLHOUSE_WIDTH, front);
                //this.placeTorch(worldIn, EnumFacing.EAST, 2, 3, 0, structureBoundingBoxIn);
                //this.placeTorch(worldIn, EnumFacing.WEST, 3, 3, 0, structureBoundingBoxIn);
                //this.placeTorch(worldIn, EnumFacing.NORTH, 5, 3, 0, structureBoundingBoxIn);
                
                //this.placeTorch(worldIn, EnumFacing.SOUTH, 6, 3, -1, structureBoundingBoxIn);
                


                this.spawnVillagers(worldIn, structureBoundingBoxIn, 4, 1, 2, 2, AttributeVocation.CLASS_VILLAGER, randomIn.nextInt(100) < 50 ? 1 : 2, false);
                return true;
            }

	
        }

    public static class Shrine extends LatinStructurePieces.Village
        {
            private boolean isRoofAccessible;

            public Shrine()
            {
            }

            public Shrine(LatinStructurePieces.Start start, int p_i45566_2_, Random rand, StructureBoundingBox p_i45566_4_, EnumFacing facing)
            {
                super(start, p_i45566_2_);
                this.setCoordBaseMode(facing);
                this.front = facing;
                this.boundingBox = p_i45566_4_;
                this.isRoofAccessible = rand.nextBoolean();
            }

            /**
             * (abstract) Helper method to write subclass data to NBT
             */
            protected void writeStructureToNBT(NBTTagCompound tagCompound)
            {
                super.writeStructureToNBT(tagCompound);
                tagCompound.setBoolean("Terrace", this.isRoofAccessible);
            }

            /**
             * (abstract) Helper method to read subclass data from NBT
             */
            protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
            {
                super.readStructureFromNBT(tagCompound, p_143011_2_);
                this.isRoofAccessible = tagCompound.getBoolean("Terrace");
            }

            public static LatinStructurePieces.Shrine createPiece(LatinStructurePieces.Start start, List<StructureComponent> p_175858_1_, Random rand, int p_175858_3_, int p_175858_4_, int p_175858_5_, EnumFacing facing, int p_175858_7_)
            {
                StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175858_3_, p_175858_4_, p_175858_5_, 0, 0, 0, LatinBuildingsArray.SHRINE_WIDTH, LatinBuildingsArray.SHRINE_HEIGHT, LatinBuildingsArray.SHRINE_LENGTH, facing);
                return StructureComponent.findIntersecting(p_175858_1_, structureboundingbox) != null ? null : new LatinStructurePieces.Shrine(start, p_175858_7_, rand, structureboundingbox, facing);
            }

            /**
             * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
             * Mineshafts at the end, it adds Fences...
             */
            public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
            {
                if (this.averageGroundLvl < 0)
                {
                    this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

                    if (this.averageGroundLvl < 0)
                    {
                        return true;
                    }

                    this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + LatinBuildingsArray.SHRINE_HEIGHT - 1, 0);
                }
                
               this.placeStructureBase(worldIn, structureBoundingBoxIn, randomIn, LatinBuildingsArray.shrine_0, LatinBuildingsArray.SHRINE_LENGTH, LatinBuildingsArray.SHRINE_HEIGHT, LatinBuildingsArray.SHRINE_WIDTH, front);
                this.spawnVillagers(worldIn, structureBoundingBoxIn, -1, 1, 2, 1, AttributeVocation.CLASS_MAGE, 0, false);
                return true;
            }
        }

    public static class Path extends LatinStructurePieces.Road
        {
            private int length;

            public Path()
            {
            }

            public Path(LatinStructurePieces.Start start, int p_i45562_2_, Random rand, StructureBoundingBox p_i45562_4_, EnumFacing facing)
            {
                super(start, p_i45562_2_);
                this.setCoordBaseMode(facing);
                this.boundingBox = p_i45562_4_;
                this.length = Math.max(p_i45562_4_.getXSize(), p_i45562_4_.getZSize());
            }

            /**
             * (abstract) Helper method to write subclass data to NBT
             */
            protected void writeStructureToNBT(NBTTagCompound tagCompound)
            {
                super.writeStructureToNBT(tagCompound);
                tagCompound.setInteger("Length", this.length);
            }

            /**
             * (abstract) Helper method to read subclass data from NBT
             */
            protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
            {
                super.readStructureFromNBT(tagCompound, p_143011_2_);
                this.length = tagCompound.getInteger("Length");
            }

            /**
             * Initiates construction of the Structure Component picked, at the current Location of StructGen
             */
            public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
            {
                boolean flag = false;

                for (int i = rand.nextInt(5); i < this.length - 8; i += 2 + rand.nextInt(5))
                {
                    StructureComponent structurecomponent = this.getNextComponentNN((LatinStructurePieces.Start)componentIn, listIn, rand, 0, i);

                    if (structurecomponent != null)
                    {
                        i += Math.max(structurecomponent.getBoundingBox().getXSize(), structurecomponent.getBoundingBox().getZSize());
                        flag = true;
                    }
                }

                for (int j = rand.nextInt(5); j < this.length - 8; j += 2 + rand.nextInt(5))
                {
                    StructureComponent structurecomponent1 = this.getNextComponentPP((LatinStructurePieces.Start)componentIn, listIn, rand, 0, j);

                    if (structurecomponent1 != null)
                    {
                        j += Math.max(structurecomponent1.getBoundingBox().getXSize(), structurecomponent1.getBoundingBox().getZSize());
                        flag = true;
                    }
                }

                EnumFacing enumfacing = this.getCoordBaseMode();

                if (flag && rand.nextInt(3) > 0 && enumfacing != null)
                {
                    switch (enumfacing)
                    {
                        case NORTH:
                        default:
                            LatinStructurePieces.generateAndAddRoadPiece((LatinStructurePieces.Start)componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST, this.getComponentType());
                            break;
                        case SOUTH:
                            LatinStructurePieces.generateAndAddRoadPiece((LatinStructurePieces.Start)componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.WEST, this.getComponentType());
                            break;
                        case WEST:
                            LatinStructurePieces.generateAndAddRoadPiece((LatinStructurePieces.Start)componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                            break;
                        case EAST:
                            LatinStructurePieces.generateAndAddRoadPiece((LatinStructurePieces.Start)componentIn, listIn, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                    }
                }

                if (flag && rand.nextInt(3) > 0 && enumfacing != null)
                {
                    switch (enumfacing)
                    {
                        case NORTH:
                        default:
                            LatinStructurePieces.generateAndAddRoadPiece((LatinStructurePieces.Start)componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, this.getComponentType());
                            break;
                        case SOUTH:
                            LatinStructurePieces.generateAndAddRoadPiece((LatinStructurePieces.Start)componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.EAST, this.getComponentType());
                            break;
                        case WEST:
                            LatinStructurePieces.generateAndAddRoadPiece((LatinStructurePieces.Start)componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                            break;
                        case EAST:
                            LatinStructurePieces.generateAndAddRoadPiece((LatinStructurePieces.Start)componentIn, listIn, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                    }
                }
            }

            public static StructureBoundingBox findPieceBox(LatinStructurePieces.Start start, List<StructureComponent> p_175848_1_, Random rand, int p_175848_3_, int p_175848_4_, int p_175848_5_, EnumFacing facing)
            {
                for (int i = 7 * MathHelper.getInt(rand, 3, 5); i >= 7; i -= 7)
                {
                    StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175848_3_, p_175848_4_, p_175848_5_, 0, 0, 0, 3, 3, i, facing);

                    if (StructureComponent.findIntersecting(p_175848_1_, structureboundingbox) == null)
                    {
                        return structureboundingbox;
                    }
                }

                return null;
            }

            /**
             * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
             * Mineshafts at the end, it adds Fences...
             */
            public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
            {
                IBlockState iblockstate = this.getBiomeSpecificBlockState(Blocks.GRASS_PATH.getDefaultState());
                IBlockState iblockstate1 = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
                IBlockState iblockstate2 = this.getBiomeSpecificBlockState(Blocks.GRAVEL.getDefaultState());
                IBlockState iblockstate3 = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
               // IBlockState iblockstate5 = this.getBiomeSpecificBlockState(Blocks.STONEBRICK.getDefaultState());

                for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i)
                {
                    for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; ++j)
                    {
                        BlockPos blockpos = new BlockPos(i, 64, j);

                        if (structureBoundingBoxIn.isVecInside(blockpos))
                        {
                            blockpos = worldIn.getTopSolidOrLiquidBlock(blockpos).down();

                            if (blockpos.getY() < worldIn.getSeaLevel())
                            {
                                blockpos = new BlockPos(blockpos.getX(), worldIn.getSeaLevel() - 1, blockpos.getZ());
                            }

                            while (blockpos.getY() >= worldIn.getSeaLevel() - 1)
                            {
                                IBlockState iblockstate4 = worldIn.getBlockState(blockpos);

                                if (iblockstate4.getBlock() == Blocks.GRASS && worldIn.isAirBlock(blockpos.up()))
                                {
                                    worldIn.setBlockState(blockpos, iblockstate, 2);
                                    break;
                                }

                                if (iblockstate4.getMaterial().isLiquid())
                                {
                                    worldIn.setBlockState(blockpos, iblockstate1, 2);
                                    break;
                                }

                                if (iblockstate4.getBlock() == Blocks.SAND || iblockstate4.getBlock() == Blocks.SANDSTONE || iblockstate4.getBlock() == Blocks.RED_SANDSTONE)
                                {
                                    worldIn.setBlockState(blockpos, iblockstate2, 2);
                                    worldIn.setBlockState(blockpos.down(), iblockstate3, 2);
                                    break;
                                }

                                blockpos = blockpos.down();
                            }
                        }
                    }
                }

                return true;
            }
        }

    public static class PieceWeight
        {
            public Class <? extends LatinStructurePieces.Village > villagePieceClass;
            public final int villagePieceWeight;
            public int villagePiecesSpawned;
            public int villagePiecesLimit;

            public PieceWeight(Class <? extends LatinStructurePieces.Village > p_i2098_1_, int p_i2098_2_, int p_i2098_3_)
            {
                this.villagePieceClass = p_i2098_1_;
                this.villagePieceWeight = p_i2098_2_;
                this.villagePiecesLimit = p_i2098_3_;
            }

            public boolean canSpawnMoreVillagePiecesOfType(int componentType)
            {
                return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
            }

            public boolean canSpawnMoreVillagePieces()
            {
                return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
            }
        }

    public abstract static class Road extends LatinStructurePieces.Village
        {
            public Road()
            {
            }

            protected Road(LatinStructurePieces.Start start, int type)
            {
                super(start, type);
            }
        }

    public static class Start extends LatinStructurePieces.Well
        {
            public BiomeProvider biomeProvider;
            /** World terrain type, 0 for normal, 1 for flap map */
            public int terrainType;
            public LatinStructurePieces.PieceWeight lastPlaced;
            /**
             * Contains List of all spawnable Structure Piece Weights. If no more Pieces of a type can be spawned, they
             * are removed from this list
             */
            public List<LatinStructurePieces.PieceWeight> structureVillageWeightedPieceList;
            public List<StructureComponent> pendingHouses = Lists.<StructureComponent>newArrayList();
            public List<StructureComponent> pendingRoads = Lists.<StructureComponent>newArrayList();
            public Biome biome;

            public Start()
            {
            }

            public Start(BiomeProvider biomeProviderIn, int p_i2104_2_, Random rand, int p_i2104_4_, int p_i2104_5_, List<LatinStructurePieces.PieceWeight> p_i2104_6_, int p_i2104_7_)
            {
                super((LatinStructurePieces.Start)null, 0, rand, p_i2104_4_, p_i2104_5_);
                this.biomeProvider = biomeProviderIn;
                this.structureVillageWeightedPieceList = p_i2104_6_;
                this.terrainType = p_i2104_7_;
                Biome biome = biomeProviderIn.getBiome(new BlockPos(p_i2104_4_, 0, p_i2104_5_), Biomes.DEFAULT);
                this.biome = biome;
                this.startPiece = this;

                if (biome instanceof BiomeDesert)
                {
                    this.structureType = 1;
                }
                else if (biome instanceof BiomeSavanna)
                {
                    this.structureType = 2;
                }
                else if (biome instanceof BiomeTaiga)
                {
                    this.structureType = 3;
                }

                this.setStructureType(this.structureType);
                this.isZombieInfested = rand.nextInt(100) <= Main.latin_zombie_infest_rate;
                if(!this.isZombieInfested) 
                {
                	this.isBanditInfested = rand.nextInt(100) <= Main.latin_bandit_infest_rate;
                }
                else 
                {
                	this.isBanditInfested = false;
                }
            }
        }

    public static class Torch extends LatinStructurePieces.Village
        {
            public Torch()
            {
            }

            public Torch(LatinStructurePieces.Start start, int p_i45568_2_, Random rand, StructureBoundingBox p_i45568_4_, EnumFacing facing)
            {
                super(start, p_i45568_2_);
                this.setCoordBaseMode(facing);
                this.boundingBox = p_i45568_4_;
            }

            public static StructureBoundingBox findPieceBox(LatinStructurePieces.Start start, List<StructureComponent> p_175856_1_, Random rand, int p_175856_3_, int p_175856_4_, int p_175856_5_, EnumFacing facing)
            {
            	if(start.isBanditInfested || start.isZombieInfested) return null;
                StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175856_3_, p_175856_4_, p_175856_5_, 0, 0, 0, 1, 4, 1, facing);
                return StructureComponent.findIntersecting(p_175856_1_, structureboundingbox) != null ? null : structureboundingbox;
            }

            /**
             * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
             * Mineshafts at the end, it adds Fences...
             */
            public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
            {
                if (this.averageGroundLvl < 0)
                {
                    this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

                    if (this.averageGroundLvl < 0)
                    {
                        return true;
                    }

                    this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 3 - 1, 0);
                }

                IBlockState iblockstate = Blocks.COBBLESTONE_WALL.getDefaultState();
                //System.out.println("Can tick: "+noSpreadFire.getTickRandomly());
                this.setBlockState(worldIn, iblockstate, 1, 1, 1, structureBoundingBoxIn);
                this.setBlockState(worldIn, iblockstate, 1, 2, 1, structureBoundingBoxIn);
                this.setBlockState(worldIn, iblockstate, 1, 3, 1, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.GLOWSTONE.getDefaultState(), 1, 4, 1, structureBoundingBoxIn);
                // this.setBlockState(worldIn, noSpreadFire.getDefaultState(), 2, 2, 2, structureBoundingBoxIn);
                
               //this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
                /*this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 2, 3, 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
                this.setBlockState(worldIn, iblockstate, 1, 0, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, iblockstate, 1, 1, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, iblockstate, 1, 2, 0, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.WOOL.getStateFromMeta(EnumDyeColor.WHITE.getDyeDamage()), 1, 3, 0, structureBoundingBoxIn);
                this.placeTorch(worldIn, EnumFacing.EAST, 2, 3, 0, structureBoundingBoxIn);
                this.placeTorch(worldIn, EnumFacing.NORTH, 1, 3, 1, structureBoundingBoxIn);
                this.placeTorch(worldIn, EnumFacing.WEST, 0, 3, 0, structureBoundingBoxIn);
                this.placeTorch(worldIn, EnumFacing.SOUTH, 1, 3, -1, structureBoundingBoxIn);*/
                return true;
            }
        }

    public static class Banner extends LatinStructurePieces.Village
    {
        public Banner()
        {
        }

        public Banner(LatinStructurePieces.Start start, int p_i45568_2_, Random rand, StructureBoundingBox p_i45568_4_, EnumFacing facing)
        {
            super(start, p_i45568_2_);
            this.front = facing;
            this.setCoordBaseMode(facing);
            this.boundingBox = p_i45568_4_;
        }

        public static StructureBoundingBox findPieceBox(LatinStructurePieces.Start start, List<StructureComponent> p_175856_1_, Random rand, int p_175856_3_, int p_175856_4_, int p_175856_5_, EnumFacing facing)
        {
        	if(start.isBanditInfested || start.isZombieInfested) return null;
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175856_3_, p_175856_4_, p_175856_5_, 0, 0, 0, 1, 3, 1, facing);
            return StructureComponent.findIntersecting(p_175856_1_, structureboundingbox) != null ? null : structureboundingbox;
        }

        /**
         * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
         * Mineshafts at the end, it adds Fences...
         */
        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
        {
            if (this.averageGroundLvl < 0)
            {
                this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

                if (this.averageGroundLvl < 0)
                {
                    return true;
                }

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 3 - 1, 0);
            }
            
            int j = this.getXWithOffset(0, 0);
            //j -= 0.5D;
            int k = this.getYWithOffset(0);
            int l = this.getZWithOffset(0, 0); 

           // IBlockState iblockstate = Blocks.COBBLESTONE_WALL.getDefaultState();
            //System.out.println("Can tick: "+noSpreadFire.getTickRandomly());
            ItemBannerGenWrapper.placeRandomBannerBlock(worldIn, worldIn.rand, front, new BlockPos(j, k, l));
            //this.setBlockState(worldIn, Blocks.STANDING_BANNER.getDefaultState(), 1, 2, 1, structureBoundingBoxIn);
            
           //this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
            /*this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 2, 3, 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.setBlockState(worldIn, iblockstate, 1, 0, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate, 1, 1, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, iblockstate, 1, 2, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.WOOL.getStateFromMeta(EnumDyeColor.WHITE.getDyeDamage()), 1, 3, 0, structureBoundingBoxIn);
            this.placeTorch(worldIn, EnumFacing.EAST, 2, 3, 0, structureBoundingBoxIn);
            this.placeTorch(worldIn, EnumFacing.NORTH, 1, 3, 1, structureBoundingBoxIn);
            this.placeTorch(worldIn, EnumFacing.WEST, 0, 3, 0, structureBoundingBoxIn);
            this.placeTorch(worldIn, EnumFacing.SOUTH, 1, 3, -1, structureBoundingBoxIn);*/
            return true;
        }
    }

    
    public abstract static class Village extends StructureComponent
        {
            protected int averageGroundLvl = -1;
            /** The number of villagers that have been spawned in this component. */
            private int villagersSpawned;
            protected int structureType;
            protected boolean isZombieInfested;
            protected boolean isBanditInfested;
            protected EnumFacing front;
            EntityHumanVillager ruler;
            protected LatinStructurePieces.Start startPiece;

            public Village()
            {
            }

            protected Village(LatinStructurePieces.Start start, int type)
            {
                super(type);

                if (start != null)
                {
                    this.structureType = start.structureType;
                    this.isZombieInfested = start.isZombieInfested;
                    this.isBanditInfested = start.isBanditInfested;
                    this.ruler = null;
                    startPiece = start;
                }
            }

            /**
             * (abstract) Helper method to write subclass data to NBT
             */
            protected void writeStructureToNBT(NBTTagCompound tagCompound)
            {
                tagCompound.setInteger("HPos", this.averageGroundLvl);
                tagCompound.setInteger("VCount", this.villagersSpawned);
                tagCompound.setByte("Type", (byte)this.structureType);
                tagCompound.setBoolean("Zombie", this.isZombieInfested);
                tagCompound.setBoolean("Bandit", this.isBanditInfested);
            }

            /**
             * (abstract) Helper method to read subclass data from NBT
             */
            protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
            {
                this.averageGroundLvl = tagCompound.getInteger("HPos");
                this.villagersSpawned = tagCompound.getInteger("VCount");
                this.structureType = tagCompound.getByte("Type");

                if (tagCompound.getBoolean("Desert"))
                {
                    this.structureType = 1;
                }

                this.isZombieInfested = tagCompound.getBoolean("Zombie");
                this.isBanditInfested = tagCompound.getBoolean("Bandit");
            }

            /**
             * Gets the next village component, with the bounding box shifted -1 in the X and Z direction.
             */
            @Nullable
            protected StructureComponent getNextComponentNN(LatinStructurePieces.Start start, List<StructureComponent> structureComponents, Random rand, int p_74891_4_, int p_74891_5_)
            {
                EnumFacing enumfacing = this.getCoordBaseMode();

                if (enumfacing != null)
                {
                    switch (enumfacing)
                    {
                        case NORTH:
                        default:
                            return LatinStructurePieces.generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX - 1, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ + p_74891_5_, EnumFacing.WEST, this.getComponentType());
                        case SOUTH:
                            return LatinStructurePieces.generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX - 1, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ + p_74891_5_, EnumFacing.WEST, this.getComponentType());
                        case WEST:
                            return LatinStructurePieces.generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX + p_74891_5_, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                        case EAST:
                            return LatinStructurePieces.generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX + p_74891_5_, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                    }
                }
                else
                {
                    return null;
                }
            }

            /**
             * Gets the next village component, with the bounding box shifted +1 in the X and Z direction.
             */
            @Nullable
            protected StructureComponent getNextComponentPP(LatinStructurePieces.Start start, List<StructureComponent> structureComponents, Random rand, int p_74894_4_, int p_74894_5_)
            {
                EnumFacing enumfacing = this.getCoordBaseMode();

                if (enumfacing != null)
                {
                    switch (enumfacing)
                    {
                        case NORTH:
                        default:
                            return LatinStructurePieces.generateAndAddComponent(start, structureComponents, rand, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74894_4_, this.boundingBox.minZ + p_74894_5_, EnumFacing.EAST, this.getComponentType());
                        case SOUTH:
                            return LatinStructurePieces.generateAndAddComponent(start, structureComponents, rand, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74894_4_, this.boundingBox.minZ + p_74894_5_, EnumFacing.EAST, this.getComponentType());
                        case WEST:
                            return LatinStructurePieces.generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX + p_74894_5_, this.boundingBox.minY + p_74894_4_, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                        case EAST:
                            return LatinStructurePieces.generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX + p_74894_5_, this.boundingBox.minY + p_74894_4_, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                    }
                }
                else
                {
                    return null;
                }
            }

            /**
             * Discover the y coordinate that will serve as the ground level of the supplied BoundingBox. (A median of
             * all the levels in the BB's horizontal rectangle).
             */
            protected int getAverageGroundLevel(World worldIn, StructureBoundingBox structurebb)
            {
                int i = 0;
                int j = 0;
                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                for (int k = this.boundingBox.minZ; k <= this.boundingBox.maxZ; ++k)
                {
                    for (int l = this.boundingBox.minX; l <= this.boundingBox.maxX; ++l)
                    {
                        blockpos$mutableblockpos.setPos(l, 64, k);

                        if (structurebb.isVecInside(blockpos$mutableblockpos))
                        {
                            i += Math.max(worldIn.getTopSolidOrLiquidBlock(blockpos$mutableblockpos).getY(), worldIn.provider.getAverageGroundLevel() - 1);
                            ++j;
                        }
                    }
                }

                if (j == 0)
                {
                    return -1;
                }
                else
                {
                    return i / j;
                }
            }

            protected static boolean canVillageGoDeeper(StructureBoundingBox structurebb)
            {
                return structurebb != null && structurebb.minY > 10;
            }

            /**
             * Spawns a number of villagers in this component. Parameters: world, component bounding box, x offset, y
             * offset, z offset, number of villagers
             */
            protected void spawnVillagers(World worldIn, StructureBoundingBox structurebb, int x, int y, int z, int count, int vocation, int rank, boolean upgrade)
            {
                if (this.villagersSpawned < count)
                {
                    for (int i = this.villagersSpawned; i < count; ++i)
                    {
                        int j = this.getXWithOffset(x, z + i);
                        //j -= 0.5D;
                        int k = this.getYWithOffset(y);
                        int l = this.getZWithOffset(x, z + i);
                        
                        
                        //l -= 0.5D;

                        if (!structurebb.isVecInside(new BlockPos(j, k, l)))
                        {
                            break;
                        }

                        ++this.villagersSpawned;

                        
                        
                        if (this.isZombieInfested)
                        {
                            EntityZombieVillager entityzombievillager = new EntityZombieVillager(worldIn);
                            entityzombievillager.setLocationAndAngles((double)j + 0.5D, (double)k, (double)l + 0.5D, 0.0F, 0.0F);
                            entityzombievillager.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityzombievillager)), (IEntityLivingData)null);
                            entityzombievillager.enablePersistence();
                            worldIn.spawnEntity(entityzombievillager);
                        }
                        else if(this.isBanditInfested)
                        {
                        	EntityHumanVillager entityHumanVillager = new EntityHumanVillager(worldIn, WorldGenVillage.LATIN_ID, AttributeRace.getFromIDRace(WorldGenVillage.LATIN_ID).getRandomBandit(worldIn), EntityHumanVillager.getRandomGender(worldIn), vocation == AttributeVocation.CLASS_RULER ? true : false);                            
                        	entityHumanVillager.setLocationAndAngles((double)j + 0.5D, (double)k, (double)l + 0.5D, 0.0F, 0.0F);                        	if(worldIn.rand.nextInt(100) <= Main.wraith_turn_rate) entityHumanVillager.setShifter(true, new EntityWraith(worldIn), false, 1);

                        	if(worldIn.rand.nextInt(100) <= Main.wraith_turn_rate) entityHumanVillager.setShifter(true, new EntityWraith(worldIn), false, 1);
                            //entityHumanVillager.setProfession(this.chooseForgeProfession(i, entityHumanVillager.getProfessionForge()));
                            //entityHumanVillager.finalizeMobSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityHumanVillager)), (IEntityLivingData)null, false);
                            worldIn.spawnEntity(entityHumanVillager);
                            
                            if(entityHumanVillager.isRuler())
                        	{
                        		entityHumanVillager.getFaction().setBandit(true);
                        		this.ruler = entityHumanVillager;
                        	}
                        	
                            if(upgrade)
                            {
                            	if(worldIn.rand.nextInt(100) < Main.upgrade_chance) entityHumanVillager.upgrade();
                            	if(worldIn.rand.nextInt(100) < Main.upgrade_chance/5) entityHumanVillager.upgrade();
                            }
                        }
                        else if(vocation == AttributeVocation.CLASS_RULER)
                        {
                        	if(this.ruler == null)
                        	{
                            	EntityHumanVillager entityRuler = new EntityHumanVillager(worldIn, WorldGenVillage.LATIN_ID, AttributeRace.getFromIDRace(WorldGenVillage.LATIN_ID).getRandomRuler(worldIn), EntityHumanVillager.getRandomGender(worldIn), true);                            
                        		this.ruler = entityRuler;
                        	}
                        	
                        	if(worldIn.rand.nextInt(100) <= Main.wraith_turn_rate) ruler.setShifter(true, new EntityWraith(worldIn), false, 1);
                        	
                        	/*AttributeFaction faction = new AttributeFaction(worldIn, new BlockPos(x, 70, z), AttributeRace.getFromIDRace(WorldGenVillage.LATIN_ID), NameGenerator.generateRandomName(worldIn.rand, AttributeRace.getFromIDRace(WorldGenVillage.LATIN_ID)));
                            faction.setBandit(isBanditInfested);
                            faction.setName();
                            faction.addRuler(ruler);
                            WorldGenVillage.nordManager.addFaction(faction);
                            WorldGenVillage.nordManager.addRuler(faction.getID(), ruler);
                            System.out.println(ruler.getName()+" - "+faction.getTitleName());*/
                        	//EntityHumanVillager entityHumanVillager = new EntityHumanVillager(worldIn, WorldGenVillage.LATIN_ID, AttributeRace.getFromIDRace(WorldGenVillage.LATIN_ID).getRandomRuler(worldIn), EntityHumanVillager.getRandomGender(worldIn));                            
                        	this.ruler.setLocationAndAngles((double)j + 0.5D, (double)k, (double)l + 0.5D, 0.0F, 0.0F);
                            //entityHumanVillager.setProfession(this.chooseForgeProfession(i, entityHumanVillager.getProfessionForge()));
                            //entityHumanVillager.finalizeMobSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityHumanVillager)), (IEntityLivingData)null, false);
                            worldIn.spawnEntity(this.ruler);
                        }
                        else if(vocation == AttributeVocation.CLASS_MERCENARY)
                        {
                        	EntityHumanVillager entityHumanVillager = new EntityHumanVillager(worldIn, WorldGenVillage.LATIN_ID, AttributeRace.getFromIDRace(WorldGenVillage.LATIN_ID).getRandomMercenary(worldIn), EntityHumanVillager.getRandomGender(worldIn), false);                            
                        	entityHumanVillager.setLocationAndAngles((double)j + 0.5D, (double)k, (double)l + 0.5D, 0.0F, 0.0F);
                    
                        	 //entityHumanVillager.setProfession(this.chooseForgeProfession(i, entityHumanVillager.getProfessionForge()));
                           // entityHumanVillager.finalizeMobSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityHumanVillager)), (IEntityLivingData)null, false);
                            worldIn.spawnEntity(entityHumanVillager);
                            
                            if(upgrade)
                            {
                            	if(worldIn.rand.nextInt(100) < Main.upgrade_chance) entityHumanVillager.upgrade();
                            	if(worldIn.rand.nextInt(100) < Main.upgrade_chance/5) entityHumanVillager.upgrade();
                            }
                        }
                        else
                        {
                        	if(this.ruler == null)
                        	{
                            	EntityHumanVillager entityRuler = new EntityHumanVillager(worldIn, WorldGenVillage.LATIN_ID, AttributeRace.getFromIDRace(WorldGenVillage.LATIN_ID).getRandomRuler(worldIn), EntityHumanVillager.getRandomGender(worldIn), true);                            
                        		this.ruler = entityRuler;
                        	}
                        	
                        	EntityHumanVillager entityHumanVillager = new EntityHumanVillager(worldIn, WorldGenVillage.LATIN_ID, vocation, rank, EntityHumanVillager.getRandomGender(worldIn));                            
                        	entityHumanVillager.setLocationAndAngles((double)j + 0.5D, (double)k, (double)l + 0.5D, 0.0F, 0.0F);
                        	entityHumanVillager.setRuler(ruler);
                        	
                        	if(worldIn.rand.nextInt(100) <= Main.wraith_turn_rate) entityHumanVillager.setShifter(true, new EntityWraith(worldIn), false, 1);
                            
                        	//entityHumanVillager.setProfession(this.chooseForgeProfession(i, entityHumanVillager.getProfessionForge()));
                           // entityHumanVillager.finalizeMobSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityHumanVillager)), (IEntityLivingData)null, false);
                            worldIn.spawnEntity(entityHumanVillager);
                            
                            if(upgrade)
                            {
                            	if(worldIn.rand.nextInt(100) < Main.upgrade_chance) entityHumanVillager.upgrade();
                            	if(worldIn.rand.nextInt(100) < Main.upgrade_chance/5) entityHumanVillager.upgrade();
                            }
                        }
                    }
                }
            }

            @Deprecated // Use Forge version below.
            protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession)
            {
                return currentVillagerProfession;
            }
            protected net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession chooseForgeProfession(int count, net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession prof)
            {
                return net.minecraftforge.fml.common.registry.VillagerRegistry.getById(chooseProfession(count, net.minecraftforge.fml.common.registry.VillagerRegistry.getId(prof)));
            }

            protected IBlockState getBiomeSpecificBlockState(IBlockState blockstateIn)
            {
                net.minecraftforge.event.terraingen.BiomeEvent.GetVillageBlockID event = new net.minecraftforge.event.terraingen.BiomeEvent.GetVillageBlockID(startPiece == null ? null : startPiece.biome, blockstateIn);
                net.minecraftforge.common.MinecraftForge.TERRAIN_GEN_BUS.post(event);
                if (event.getResult() == net.minecraftforge.fml.common.eventhandler.Event.Result.DENY) return event.getReplacement();
                /*if (this.structureType == 1)
                {
                    if (blockstateIn.getBlock() == Blocks.LOG || blockstateIn.getBlock() == Blocks.LOG2)
                    {
                        return Blocks.SANDSTONE.getDefaultState();
                    }

                    if (blockstateIn.getBlock() == Blocks.COBBLESTONE)
                    {
                        return Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.DEFAULT.getMetadata());
                    }

                    if (blockstateIn.getBlock() == Blocks.PLANKS)
                    {
                        return Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata());
                    }

                    if (blockstateIn.getBlock() == Blocks.OAK_STAIRS)
                    {
                        return Blocks.SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, blockstateIn.getValue(BlockStairs.FACING));
                    }

                    if (blockstateIn.getBlock() == Blocks.STONE_STAIRS)
                    {
                        return Blocks.SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, blockstateIn.getValue(BlockStairs.FACING));
                    }

                    if (blockstateIn.getBlock() == Blocks.GRAVEL)
                    {
                        return Blocks.SANDSTONE.getDefaultState();
                    }
                }
                else if (this.structureType == 3)
                {
                    if (blockstateIn.getBlock() == Blocks.LOG || blockstateIn.getBlock() == Blocks.LOG2)
                    {
                        return Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLog.LOG_AXIS, blockstateIn.getValue(BlockLog.LOG_AXIS));
                    }

                    if (blockstateIn.getBlock() == Blocks.PLANKS)
                    {
                        return Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE);
                    }

                    if (blockstateIn.getBlock() == Blocks.OAK_STAIRS)
                    {
                        return Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, blockstateIn.getValue(BlockStairs.FACING));
                    }

                    if (blockstateIn.getBlock() == Blocks.OAK_FENCE)
                    {
                        return Blocks.SPRUCE_FENCE.getDefaultState();
                    }
                }
                else if (this.structureType == 2)
                {
                    if (blockstateIn.getBlock() == Blocks.LOG || blockstateIn.getBlock() == Blocks.LOG2)
                    {
                        return Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockLog.LOG_AXIS, blockstateIn.getValue(BlockLog.LOG_AXIS));
                    }

                    if (blockstateIn.getBlock() == Blocks.PLANKS)
                    {
                        return Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA);
                    }

                    if (blockstateIn.getBlock() == Blocks.OAK_STAIRS)
                    {
                        return Blocks.ACACIA_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, blockstateIn.getValue(BlockStairs.FACING));
                    }

                    if (blockstateIn.getBlock() == Blocks.COBBLESTONE)
                    {
                        return Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y);
                    }

                    if (blockstateIn.getBlock() == Blocks.OAK_FENCE)
                    {
                        return Blocks.ACACIA_FENCE.getDefaultState();
                    }
                }*/
                if(Blocks.LOG == blockstateIn.getBlock()  || blockstateIn.getBlock()  == Blocks.LOG2){
                	return Blocks.QUARTZ_BLOCK.getDefaultState().withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.LINES_Y);
				}
				
				if(Blocks.COBBLESTONE == blockstateIn.getBlock() ){
					return Blocks.QUARTZ_BLOCK.getDefaultState();//.withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.CHISELED);//.withProperty(BlockLog.LOG_AXIS, event.getOriginal().getValue(BlockLog.LOG_AXIS))
					
				}
				
				if(Blocks.PLANKS == blockstateIn.getBlock() ){
					return Blocks.BRICK_BLOCK.getDefaultState();//return Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK);
					
				}
				
				if(Blocks.OAK_FENCE == blockstateIn.getBlock() ){
					return Blocks.COBBLESTONE_WALL.getDefaultState();
					
				}
				
				if(Blocks.OAK_STAIRS == blockstateIn.getBlock() ){
					return Blocks.BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, blockstateIn.getValue(BlockStairs.FACING));
					
				}
				
				if(Blocks.STONE_STAIRS == blockstateIn.getBlock()){
					return Blocks.QUARTZ_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, blockstateIn.getValue(BlockStairs.FACING));//return Blocks.BIRCH_STAIRS.getDefaultState()
				}
				
				if(Blocks.GRASS_PATH == blockstateIn.getBlock() ){
					return Blocks.QUARTZ_BLOCK.getDefaultState().withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.CHISELED);					
				}
				
				if(Blocks.GRAVEL == blockstateIn.getBlock() ){
					return Blocks.BRICK_BLOCK.getDefaultState();//.withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.CHISELED);					
				}
				
				if(Blocks.DOUBLE_STONE_SLAB == blockstateIn.getBlock() ){
					return Blocks.QUARTZ_BLOCK.getDefaultState().withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.LINES_X);
				}
				
				if(Blocks.STONEBRICK == blockstateIn.getBlock() ){
					return Blocks.QUARTZ_BLOCK.getDefaultState().withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.CHISELED);					
				}

                return blockstateIn;
            }

            protected BlockDoor biomeDoor()
            {
                /*switch (this.structureType)
                {
                    case 2:
                        return Blocks.ACACIA_DOOR;
                    case 3:
                        return Blocks.SPRUCE_DOOR;
                    default:
                        return Blocks.OAK_DOOR;
                }*/
            	return Blocks.OAK_DOOR;
            }

            protected void createVillageDoor(World p_189927_1_, StructureBoundingBox p_189927_2_, Random p_189927_3_, int p_189927_4_, int p_189927_5_, int p_189927_6_, EnumFacing p_189927_7_, Mirror mirror)
            {
                if (!this.isZombieInfested)
                {
                	//this.biomeDoor().getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.NORTH);
                	//this.biomeDoor().getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.NORTH).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER).withMirror(Mirror.LEFT_RIGHT);
                    this.generateDoor(p_189927_1_, p_189927_2_, p_189927_3_, p_189927_4_, p_189927_5_, p_189927_6_, EnumFacing.NORTH, (BlockDoor) this.biomeDoor().getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.NORTH).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER).withMirror(mirror).getBlock());
                }
                else if(this.isBanditInfested && p_189927_1_.rand.nextInt(100) > 25)
                {
                    this.generateDoor(p_189927_1_, p_189927_2_, p_189927_3_, p_189927_4_, p_189927_5_, p_189927_6_, EnumFacing.NORTH, (BlockDoor) this.biomeDoor().getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.NORTH).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER).withMirror(mirror).getBlock());
                }
            }

            protected void placeTorch(World p_189926_1_, EnumFacing p_189926_2_, int p_189926_3_, int p_189926_4_, int p_189926_5_, StructureBoundingBox p_189926_6_)
            {
            	//System.out.println(p_189926_2_.getName()+" Placing: "+p_189926_3_+" "+p_189926_4_+" "+p_189926_5_);
                if (!this.isZombieInfested || !this.isBanditInfested)
                {
                    this.setBlockState(p_189926_1_, Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, p_189926_2_), p_189926_3_, p_189926_4_, p_189926_5_, p_189926_6_);
                }
            }

            /**
             * Replaces air and liquid from given position downwards. Stops when hitting anything else than air or
             * liquid
             */
            protected void replaceAirAndLiquidDownwards(World worldIn, IBlockState blockstateIn, int x, int y, int z, StructureBoundingBox boundingboxIn)
            {
                IBlockState iblockstate = this.getBiomeSpecificBlockState(blockstateIn);
                super.replaceAirAndLiquidDownwards(worldIn, iblockstate, x, y, z, boundingboxIn);
            }

            protected void setStructureType(int p_189924_1_)
            {
                this.structureType = p_189924_1_;
            }
            
            protected void placeStructureBase(World worldIn, StructureBoundingBox structureBoundingBoxIn, Random randomIn, char[][] plan, int length, int height, int width, EnumFacing front) {
				IBlockState iblockstate = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
                IBlockState iblockstate1 = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState().withProperty(BlockLog.LOG_AXIS, EnumAxis.X));
                IBlockState iblockstate2 = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());
                IBlockState iblockstate3 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST));
                IBlockState iblockstate4 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST));
                IBlockState iblockstate7 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
                IBlockState iblockstate5 = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
                IBlockState iblockstate6 = this.getBiomeSpecificBlockState(Blocks.GLASS.getDefaultState());
                IBlockState iblockstate8 = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());
                IBlockState iblockstate9 = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
                IBlockState iblockstate10 = this.getBiomeSpecificBlockState(Blocks.STONEBRICK.getDefaultState());
                IBlockState iblockstate14 = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
                IBlockState iblockstate11 = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST));
                IBlockState iblockstate12 = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST));
                IBlockState iblockstate13 = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
                IBlockState iblockstate15 = this.getBiomeSpecificBlockState(Blocks.LADDER.getDefaultState());//.withProperty(BlockStairs.FACING, EnumFacing.NORTH));
               
                int j = this.getXWithOffset(0, 0);
                //j -= 0.5D;
                int k = this.getYWithOffset(0);
                int l = this.getZWithOffset(0, 0); 
                
                for(int y = 0; y < height; y++)
                {
                	for(int z = 0; z < length; z++)
                	{
	                	for(int x = 0; x < width; x++)
	                	{
	                		switch(plan[y][x + (z * width)])
	                		{
		                		case CharBlockTypes.FENCE:
		                		{
		                			this.setBlockState(worldIn, iblockstate8, x, y, z, structureBoundingBoxIn);
		                		}
		                		break;
		                		case CharBlockTypes.BANNER_HANGING:
		                		{
		                			EnumFacing face = front;
		                			if(EnumFacing.SOUTH == front)
		                			{
		                				face = EnumFacing.NORTH;
		                			}
		                			if(EnumFacing.NORTH == front)
		                			{
		                				face = EnumFacing.SOUTH;
		                				continue;
		                			}
		                			if(EnumFacing.EAST == front)
		                			{
		                				face = EnumFacing.WEST;
		                				continue;
		                			}
		                			if(EnumFacing.WEST == front)
		                			{
		                				face = EnumFacing.EAST;
		                				continue;
		                			}
		                			
		                			ItemBannerGenWrapper.placeRandomBannerBlock(worldIn, worldIn.rand, face, new BlockPos(j + x, k + y, l + z));
		                		}
		                		break;
		                		case CharBlockTypes.BANNER:
		                		{
		                			ItemBannerGenWrapper.placeRandomBannerBlock(worldIn, worldIn.rand, EnumFacing.UP, new BlockPos(j + x, k + y, l + z));
		                		}
		                		break;
		                		case CharBlockTypes.LADDER:
		                		{
		                			this.setBlockState(worldIn, iblockstate15, x, y, z, structureBoundingBoxIn);
		                		}
		                		break;
		                		case CharBlockTypes.STONEBRICK:
		                		{
		                			this.setBlockState(worldIn, iblockstate10, x, y, z, structureBoundingBoxIn);	
		                		}
		                		break;
		                		case CharBlockTypes.COBBLESTONE:
		                		{
		                			this.setBlockState(worldIn, iblockstate, x, y, z, structureBoundingBoxIn);
		                		}
		                		break;
		                		case CharBlockTypes.LOG_UP:
		                		{
		                			this.setBlockState(worldIn, iblockstate2, x, y, z, structureBoundingBoxIn);
		                		}
		                		break;
		                		case CharBlockTypes.AIR:
		                		{
		                			this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), x, y, z, structureBoundingBoxIn);
		                		}
		                		break;
		                		case CharBlockTypes.CRAFTING_TABLE:
		                		{
		                			this.setBlockState(worldIn, Blocks.CRAFTING_TABLE.getDefaultState(), x, y, z, structureBoundingBoxIn);
		                		}
		                		break;
		                		case CharBlockTypes.DOOR_NORTH:
		                		{
		                			this.createVillageDoor(worldIn, structureBoundingBoxIn, randomIn, x, y, z, EnumFacing.NORTH, Mirror.NONE);
		                            if (this.getBlockStateFromPos(worldIn, x, y - 1, z - 1, structureBoundingBoxIn).getMaterial() == Material.AIR)
		                            {
		                            	this.setBlockState(worldIn, iblockstate7, x, y - 1, z - 1, structureBoundingBoxIn);
		                            }
		                            if (this.getBlockStateFromPos(worldIn, x, y - 2, z - 1, structureBoundingBoxIn).getBlock() == Blocks.GRASS_PATH)
                                    {
                                        this.setBlockState(worldIn, Blocks.GRASS.getDefaultState(), x, y - 2, z - 1, structureBoundingBoxIn);
                                    }   
		                		}
		                		break;
		                		case CharBlockTypes.DOOR_SOUTH:
		                		{
		                		    this.setBlockState(worldIn, this.biomeDoor().getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.NORTH), x, y, z, structureBoundingBoxIn);
		                		    this.setBlockState(worldIn, this.biomeDoor().getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.NORTH).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER).withMirror(Mirror.LEFT_RIGHT), x, y + 1, z, structureBoundingBoxIn);
		                		    if (this.getBlockStateFromPos(worldIn, x, y - 1, z - 1, structureBoundingBoxIn).getMaterial() == Material.AIR)
			                        {
			                          	this.setBlockState(worldIn, iblockstate7, x, y - 1, z - 1, structureBoundingBoxIn);
			                        }
			                        if (this.getBlockStateFromPos(worldIn, x, y - 2, z - 1, structureBoundingBoxIn).getBlock() == Blocks.GRASS_PATH)
	                                {
	                                   this.setBlockState(worldIn, Blocks.GRASS.getDefaultState(), x, y - 2, z - 1, structureBoundingBoxIn);
	                                }   
		                			//this.biomeDoor().getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.NORTH);
		                        	//this.biomeDoor().getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.NORTH).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER).withMirror(Mirror.LEFT_RIGHT);
		                            
		                	    }
		                		break;
		                		case CharBlockTypes.NULL:
		                		{
		                			continue;
		                		}
		                		case CharBlockTypes.LOG_SIDE:
		                		{
		                			this.setBlockState(worldIn, iblockstate2, x, y, z, structureBoundingBoxIn);
		                		}
		                		break;
		                		case CharBlockTypes.GLASS:
		                		{
		                			this.setBlockState(worldIn, iblockstate6, x, y, z, structureBoundingBoxIn);
		                		}
		                		break;
		                		case CharBlockTypes.WATER:
		                		{
		                			this.setBlockState(worldIn, Blocks.WATER.getDefaultState(), x, y, z, structureBoundingBoxIn);
		                		}
		                		break;
		                		case CharBlockTypes.OAK_WOOD:
		                		{
		                			this.setBlockState(worldIn, iblockstate5, x, y, z, structureBoundingBoxIn);
		                		}
		                		break;
		                		case CharBlockTypes.STAIRS_LEFT:
		                		{
		                			this.setBlockState(worldIn, iblockstate3, x, y, z, structureBoundingBoxIn);
		                		}
		                		break;
		                		case CharBlockTypes.STAIRS_RIGHT:
		                		{
		                			this.setBlockState(worldIn, iblockstate4, x, y, z, structureBoundingBoxIn);
		                		}
		                		break;
		                		case CharBlockTypes.STAIRS_NORTH:
		                		{
		                			this.setBlockState(worldIn, iblockstate7, x, y, z, structureBoundingBoxIn);
		                		}
		                		break;
		                		case CharBlockTypes.STAIRS_SOUTH:
		                		{
		                			this.setBlockState(worldIn, iblockstate9, x, y, z, structureBoundingBoxIn);
		                		}
		                		break;
		                		case CharBlockTypes.S_STAIRS_LEFT:
		                		{
		                			this.setBlockState(worldIn, iblockstate11, x, y, z, structureBoundingBoxIn);
		                		}
		                		break;
		                		case CharBlockTypes.S_STAIRS_RIGHT:
		                		{
		                			this.setBlockState(worldIn, iblockstate12, x, y, z, structureBoundingBoxIn);
		                		}
		                		break;
		                		case CharBlockTypes.S_STAIRS_NORTH:
		                		{
		                			this.setBlockState(worldIn, iblockstate13, x, y, z, structureBoundingBoxIn);
		                		}
		                		break;
		                		case CharBlockTypes.S_STAIRS_SOUTH:
		                		{
		                			this.setBlockState(worldIn, iblockstate14, x, y, z, structureBoundingBoxIn);
		                		}
		                		break;
		                		case CharBlockTypes.TORCH_NORTH:
		                		{
		                			this.placeTorch(worldIn, EnumFacing.NORTH, x, y, z, structureBoundingBoxIn);
		                		}
		                		break;
		                		case CharBlockTypes.CHEST:
		                		{
		                		      this.generateChest(worldIn, structureBoundingBoxIn, randomIn, x, y, z, LootTableList.CHESTS_VILLAGE_BLACKSMITH);
		                	    }
		                		break;
		                		case CharBlockTypes.TORCH_SOUTH:
		                		{
		                			//this.setBlockState(worldIn, iblockstate8, x, y, z, structureBoundingBoxIn);
		                			this.placeTorch(worldIn, EnumFacing.SOUTH, x, y, z, structureBoundingBoxIn);
		                			/*if(z == 0)
		                			{
		                				this.placeTorch(worldIn, EnumFacing.SOUTH, x, y, 0, structureBoundingBoxIn);
		                	//			System.out.println("Z Coord 1: "+z+" X Coord 1: "+x+" Y Coord 1: "+y);
		                			}
		                			if(z == 9)
		                			{
		                				this.placeTorch(worldIn, EnumFacing.SOUTH, x, y, 9, structureBoundingBoxIn);
		                //				System.out.println("Z Coord 2: "+z+" X Coord 2: "+x+" Y Coord 2: "+y);
		                			}*/
		       
		              //  			System.out.println("Z Coord: "+z+" X Coord : "+x+" Y Coord : "+y);
		                			//this.placeTorch(worldIn, EnumFacing.SOUTH, x, y, z-2, structureBoundingBoxIn);
		                			//this.placeTorch(worldIn, EnumFacing.SOUTH, x, y, z-1, structureBoundingBoxIn);
		                			//this.placeTorch(worldIn, EnumFacing.SOUTH, x, y, z, structureBoundingBoxIn);
		                			//this.placeTorch(worldIn, EnumFacing.SOUTH, x, y, z+1, structureBoundingBoxIn);
		                			//this.placeTorch(worldIn, EnumFacing.SOUTH, x, y, z+2, structureBoundingBoxIn);
		                			
		                			
		                			//this.placeTorch(worldIn, EnumFacing.SOUTH, x, y, 0, structureBoundingBoxIn);
		                			//this.placeTorch(worldIn, EnumFacing.SOUTH, x, y, 9, structureBoundingBoxIn);
		                			//this.placeTorch(worldIn, EnumFacing.EAST, x, y, z + 1, structureBoundingBoxIn);
		                			//this.placeTorch(worldIn, EnumFacing.WEST, x + 1, y, z + 1, structureBoundingBoxIn);
		                			//this.placeTorch(worldIn, EnumFacing.NORTH, x + 2, y, z + 1, structureBoundingBoxIn);
		                		}
		                		break;
	                		}
	                	}
                	}
                }
                
                for (int i = 0; i <= length - 1; ++i)
                {
                    for (int jj = 0; jj <= width - 1; ++jj)
                    {
                        this.clearCurrentPositionBlocksUpwards(worldIn, jj, height + 1, i, structureBoundingBoxIn);
                        this.replaceAirAndLiquidDownwards(worldIn, iblockstate10, jj, -1, i, structureBoundingBoxIn);
                    }
                }

			}
        }

    public static class Well extends LatinStructurePieces.Village
        {
    	 public Well()
         {
         }

         public Well(LatinStructurePieces.Start start, int type, Random rand, int x, int z)
         {
             super(start, type);
             this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(rand));

             if (this.getCoordBaseMode().getAxis() == EnumFacing.Axis.Z)
             {
                 this.boundingBox = new StructureBoundingBox(x, 64, z, x + 5 - 1, 78, z + 5 - 1);
             }
             else
             {
                 this.boundingBox = new StructureBoundingBox(x, 64, z, x + 5 - 1, 78, z + 5 - 1);
             }
         }

         /**
          * Initiates construction of the Structure Component picked, at the current Location of StructGen
          */
         public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
         {
             LatinStructurePieces.generateAndAddRoadPiece((LatinStructurePieces.Start)componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.WEST, this.getComponentType());
             LatinStructurePieces.generateAndAddRoadPiece((LatinStructurePieces.Start)componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.EAST, this.getComponentType());
             LatinStructurePieces.generateAndAddRoadPiece((LatinStructurePieces.Start)componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
             LatinStructurePieces.generateAndAddRoadPiece((LatinStructurePieces.Start)componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
         }

         /**
          * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
          * Mineshafts at the end, it adds Fences...
          */
         public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
         {
             if (this.averageGroundLvl < 0)
             {
                 this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

                 if (this.averageGroundLvl < 0)
                 {
                     return true;
                 }

                 this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 3, 0);
             }

             IBlockState iblockstate = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
             IBlockState iblockstate1 = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());
             this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 3, 12, 3, iblockstate, Blocks.FLOWING_WATER.getDefaultState(), false);
             this.setBlockState(worldIn, Blocks.LADDER.getDefaultState(), 2, 12, 2, structureBoundingBoxIn);
             /*this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 3, 12, 2, structureBoundingBoxIn);
             this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 2, 12, 3, structureBoundingBoxIn);
             this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 3, 12, 3, structureBoundingBoxIn);*/
             this.setBlockState(worldIn, iblockstate1, 1, 13, 1, structureBoundingBoxIn);
             this.setBlockState(worldIn, iblockstate1, 1, 14, 1, structureBoundingBoxIn);
             this.setBlockState(worldIn, iblockstate1, 3, 13, 1, structureBoundingBoxIn);
             this.setBlockState(worldIn, iblockstate1, 3, 14, 1, structureBoundingBoxIn);
             this.setBlockState(worldIn, iblockstate1, 1, 13, 3, structureBoundingBoxIn);
             this.setBlockState(worldIn, iblockstate1, 1, 14, 3, structureBoundingBoxIn);
             this.setBlockState(worldIn, iblockstate1, 3, 13, 3, structureBoundingBoxIn);
             this.setBlockState(worldIn, iblockstate1, 3, 14, 3, structureBoundingBoxIn);
             this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 15, 1, 3, 15, 3, iblockstate, iblockstate, false);

             
             for (int i = 0; i <= 4; ++i)
             {
                 for (int j = 0; j <= 4; ++j)
                 {
                     if (j == 0 || j == 4 || i == 0 || i == 4)
                     {
                         this.setBlockState(worldIn, iblockstate, j, 11, i, structureBoundingBoxIn);
                         this.clearCurrentPositionBlocksUpwards(worldIn, j, 12, i, structureBoundingBoxIn);
                     }
                 }
             }
             
             /*int radius = 25 * 4;
             BlockPos center = new BlockPos(this.getXWithOffset(0, 0), this.getYWithOffset(0), this.getZWithOffset(0, 0));
             BlockPos northStart = center.add(0, 0, radius);
             BlockPos southStart = center.add(0, 0, -radius);
             BlockPos westStart = center.add(radius, 0, 0);
             BlockPos eastStart = center.add(-radius, 0, 0);
             
             for(int xx = -radius; xx < radius; xx++)
             {
             	BlockPos posE = new BlockPos(eastStart.add(xx, 0, 0).getX(), eastStart.getY(), eastStart.getZ());
             	BlockPos posW = new BlockPos(westStart.add(xx, 0, 0).getX(), westStart.getY(), westStart.getZ());
             	
             	for(int y = -10; y < 30; y++)
             	{
                 	if(posE.getX() >= -2 && 2 <= posE.getX() && y > 0 && y < 2)
                 	{
                 		continue;
                 	}
                 	
                 	if(posW.getX() >= -2 && 2 <= posE.getX() && y > 0 && y < 2)
                 	{
                 		continue;
                 	} 
                 	
                 	worldIn.setBlockState(posE.add(0, y, 0), iblockstate);
                 	worldIn.setBlockState(posW.add(0, y, 0), iblockstate);
             	}
             }
             
             for(int zz = -radius; zz < radius; zz++)
             {
             	BlockPos posE = new BlockPos(northStart.getX(), northStart.getY(), northStart.add(0, 0, zz).getZ());
             	BlockPos posW = new BlockPos(southStart.getX(), southStart.getY(), southStart.add(0, 0, zz).getZ());
             	
             	for(int y = -10; y < 10; y++)
             	{
                 	worldIn.setBlockState(posE.add(0, y, 0), iblockstate);
                 	worldIn.setBlockState(posW.add(0, y, 0), iblockstate);
             	}
             }*/
             this.spawnVillagers(worldIn, structureBoundingBoxIn, 2, 13, 2, 1, AttributeVocation.CLASS_RULER, 0, false);
             return true;
         }
        }

    public static class SmallHut extends LatinStructurePieces.Village
        {
            private boolean isTallHouse;
            private int tablePosition;

            public SmallHut()
            {
            }

            public SmallHut(LatinStructurePieces.Start start, int type, Random rand, StructureBoundingBox structurebb, EnumFacing facing)
            {
                super(start, type);
                this.setCoordBaseMode(facing);
                this.front = facing;
                this.boundingBox = structurebb;
                this.isTallHouse = rand.nextBoolean();
                this.tablePosition = rand.nextInt(3);
            }

            /**
             * (abstract) Helper method to write subclass data to NBT
             */
            protected void writeStructureToNBT(NBTTagCompound tagCompound)
            {
                super.writeStructureToNBT(tagCompound);
                tagCompound.setInteger("T", this.tablePosition);
                tagCompound.setBoolean("C", this.isTallHouse);
            }

            /**
             * (abstract) Helper method to read subclass data from NBT
             */
            protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
            {
                super.readStructureFromNBT(tagCompound, p_143011_2_);
                this.tablePosition = tagCompound.getInteger("T");
                this.isTallHouse = tagCompound.getBoolean("C");
            }

            public static LatinStructurePieces.SmallHut createPiece(LatinStructurePieces.Start start, List<StructureComponent> p_175853_1_, Random rand, int p_175853_3_, int p_175853_4_, int p_175853_5_, EnumFacing facing, int p_175853_7_)
            {
                StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175853_3_, p_175853_4_, p_175853_5_, 0, 0, 0, LatinBuildingsArray.SMALLHUT_WIDTH, LatinBuildingsArray.SMALLHUT_HEIGHT, LatinBuildingsArray.SMALLHUT_LENGTH, facing);
                return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175853_1_, structureboundingbox) == null ? new LatinStructurePieces.SmallHut(start, p_175853_7_, rand, structureboundingbox, facing) : null;
            }

            /**
             * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
             * Mineshafts at the end, it adds Fences...
             */
            public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
            {
                if (this.averageGroundLvl < 0)
                {
                    this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

                    if (this.averageGroundLvl < 0)
                    {
                        return true;
                    }

                    this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + LatinBuildingsArray.SMALLHUT_HEIGHT, 0);
                }

                this.placeStructureBase(worldIn, structureBoundingBoxIn, randomIn, LatinBuildingsArray.smallhut_1, LatinBuildingsArray.SMALLHUT_LENGTH, LatinBuildingsArray.SMALLHUT_HEIGHT, LatinBuildingsArray.SMALLHUT_WIDTH, front);

                this.spawnVillagers(worldIn, structureBoundingBoxIn, 1, 1, 2, 1, AttributeVocation.CLASS_VILLAGER, 0, false);
                return true;
            }
        }
}