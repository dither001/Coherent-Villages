package net.theexceptionist.coherentvillages.events;

import java.util.Random;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.theexceptionist.coherentvillages.entity.EntityVillagerMerchant;
import net.theexceptionist.coherentvillages.entity.alchemist.EntityVillagerAlchemist;
import net.theexceptionist.coherentvillages.entity.archer.EntityVillagerArcher;
import net.theexceptionist.coherentvillages.entity.bandit.AbstractVillagerBandit;
import net.theexceptionist.coherentvillages.entity.bandit.EntityVillagerBandit;
import net.theexceptionist.coherentvillages.entity.bandit.EntityVillagerBanditAlchemist;
import net.theexceptionist.coherentvillages.entity.bandit.EntityVillagerBanditArcher;
import net.theexceptionist.coherentvillages.entity.bandit.EntityVillagerBanditHorseman;
import net.theexceptionist.coherentvillages.entity.bandit.EntityVillagerBanditMage;
import net.theexceptionist.coherentvillages.entity.knight.EntityVillagerCavalier;
import net.theexceptionist.coherentvillages.entity.mage.EntityVillagerMage;
import net.theexceptionist.coherentvillages.entity.soldier.AbstractVillagerSoldier;
import net.theexceptionist.coherentvillages.entity.soldier.EntityVillagerMilitia;
import net.theexceptionist.coherentvillages.main.Main;

public class EventOverrideMerchantSpawn {
	public static boolean raidInProgress = false;
	@SubscribeEvent
	public void initSpawnEvent(LivingSpawnEvent.CheckSpawn event)
	{
		Random rand = new Random();
		
		if(event.getEntityLiving() instanceof EntityVillagerMerchant)
		{
			EntityVillagerMerchant merchant = new EntityVillagerMerchant(event.getWorld());
			
			if(rand.nextInt(1000) > 1)
			{
				//merchant.
				//merchant.despawnGuards();
				merchant.setDead();
				//System.out.println("Working");
			}
		}
	}
	
	boolean raidHappened = false;
	boolean driveAttempted = false;
	int daysPassed = 0;
	@SubscribeEvent
	public void checkRaid(TickEvent.PlayerTickEvent event)
	{
		EntityPlayer player = event.player;
		World world = event.player.world;
		
		Style style = new Style();
		style.setColor(TextFormatting.DARK_GREEN);
		
		Style style2 = new Style();
		style2.setColor(TextFormatting.DARK_BLUE);
		
		if(world.villageCollection != null){
			Village village = world.villageCollection.getNearestVillage(player.getPosition(), 30);
			
			
			if(!world.isRemote && village != null){
				BlockPos center = village.getCenter();
				if(!world.isDaytime() && !raidInProgress && Main.bandit_spawn > 0){
					int radius = village.getVillageRadius();
					int offX = center.getX() + (world.rand.nextBoolean() == true ? 0 : radius * ((world.rand.nextBoolean() == true) ? -1 : 1));
					int offZ = center.getZ() + (world.rand.nextBoolean() == true ? 0 : radius * ((world.rand.nextBoolean() == true) ? -1 : 1));
					int y = world.getTopSolidOrLiquidBlock(new BlockPos(offX, 60, offZ)).getY();
					BlockPos spawn = new BlockPos(offX, y, offZ);
					VillageDoorInfo door = village.getNearestDoor(spawn);
					BlockPos spawnpoint = door.getDoorBlockPos().add(20, 0, -1);
					
					//System.out.println(radius+" - "+offX+" | "+offZ+" Spawn: "+spawnpoint.getX()+" "+spawnpoint.getY()+" "+spawnpoint.getZ());
					if(world.rand.nextInt(100) < 5)
					{
						int numBandits = world.rand.nextInt(5 * ((int)Math.floor(world.getDifficultyForLocation(spawnpoint).getClampedAdditionalDifficulty() + 1))) + 5;
						AbstractVillagerBandit bandit = null;
						
						ITextComponent itextcomponent1 = new TextComponentString(
								"A village raid has begun "+(int)Math.sqrt(player.getPosition().distanceSq(spawnpoint))+" blocks away!");
						itextcomponent1.setStyle(style);
						player.sendMessage(itextcomponent1);
						
						while(numBandits > 0)
						{
							bandit = new EntityVillagerBandit(world);
							
							if(world.rand.nextInt(100) < 50) bandit = new EntityVillagerBanditArcher(world);
							else if(world.rand.nextInt(100) < 50) bandit = new EntityVillagerBanditHorseman(world);
							else if(world.rand.nextInt(100) < 25) bandit = new EntityVillagerBanditAlchemist(world);
							else if(world.rand.nextInt(100) < 10) bandit = new EntityVillagerBanditMage(world);
							
							if(!bandit.isCanSpawn()) continue;
							
							bandit.onInitialSpawn(world.getDifficultyForLocation(spawnpoint), null);
							bandit.setAlwaysRenderNameTag(true);
		                	bandit.setLocationAndAngles(spawnpoint.getX(), spawnpoint.getY(), spawnpoint.getZ(), 0.0F, 0.0F);
		                    bandit.setSpawnPoint(spawnpoint.getX(), spawnpoint.getY(), spawnpoint.getZ());
		                    
		                    bandit.finalizeMobSpawn(world.getDifficultyForLocation(new BlockPos(bandit)), (IEntityLivingData)null, false);
		                    world.spawnEntity(bandit);
		                    
							spawnpoint = spawnpoint.add(1, 0, 0);
							numBandits--;
							raidHappened = true;
						}
					}
					raidInProgress = true;
				}
				else if(world.isDaytime() && raidInProgress)
				{
					raidInProgress = false;
				}
				
				/*if(world.getTotalWorldTime() % 14500 == 0)
				{
					daysPassed++;
				}
				
				//recruiting drive
				System.out.println(daysPassed+" "+raidHappened);*/
				if(world.isDaytime() && raidHappened && !driveAttempted)
				{
					driveAttempted = true;
					
					if(world.rand.nextInt(100) < 75) return;
					
					int num = village.getNumVillagers() / 4;
					BlockPos spawnpoint = village.getNearestDoor(center).getDoorBlockPos();
					AbstractVillagerSoldier soldier = null;
					
					if(village.getNumVillagers() + num > village.getNumVillageDoors() * 2) return;
					
					ITextComponent itextcomponent1 = new TextComponentString(
							"A village recruiting drive has begun "+(int)Math.sqrt(player.getPosition().distanceSq(center))+" blocks away!");
					itextcomponent1.setStyle(style2);
					player.sendMessage(itextcomponent1);
					
					while(num > 0)
					{
						soldier = new EntityVillagerMilitia(world);
						
						if(world.rand.nextInt(100) < 50) soldier = new EntityVillagerArcher(world);
						else if(world.rand.nextInt(100) < 50) soldier = new EntityVillagerCavalier(world);
						else if(world.rand.nextInt(100) < 25) soldier = new EntityVillagerAlchemist(world);
						else if(world.rand.nextInt(100) < 10) soldier = new EntityVillagerMage(world);
						
						if(!soldier.isCanSpawn()) continue;
						
						//world.spaw
						soldier.onInitialSpawn(world.getDifficultyForLocation(spawnpoint), null);
	                	soldier.setLocationAndAngles(spawnpoint.getX(), spawnpoint.getY(), spawnpoint.getZ(), 0.0F, 0.0F);
	                    soldier.setSpawnPoint(spawnpoint.getX(), spawnpoint.getY(), spawnpoint.getZ());
	                    
	                    soldier.finalizeMobSpawn(world.getDifficultyForLocation(new BlockPos(soldier)), (IEntityLivingData)null, false);
	                    world.spawnEntity(soldier);
	                    
						spawnpoint = spawnpoint.add(1, 0, 0);
						
						num--;
					}
					
					
					daysPassed = 0;
					raidHappened = false;
				}
				else if(!world.isDaytime() && raidHappened && driveAttempted)
				{
					driveAttempted = false;
				}
			}
		}
	}
	
}
