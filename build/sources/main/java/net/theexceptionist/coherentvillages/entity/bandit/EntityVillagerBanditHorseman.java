package net.theexceptionist.coherentvillages.entity.bandit;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.theexceptionist.coherentvillages.entity.EntityVillagerHorse;
import net.theexceptionist.coherentvillages.entity.ai.EntityAIAttackBackExclude;
import net.theexceptionist.coherentvillages.entity.ai.EntityAIStayInBorders;
import net.theexceptionist.coherentvillages.entity.followers.EntitySkeletonMinion;
import net.theexceptionist.coherentvillages.entity.knight.ai.EntityAIKnightMoveToTarget;
import net.theexceptionist.coherentvillages.entity.soldier.EntityVillagerManAtArms;
import net.theexceptionist.coherentvillages.main.Main;

public class EntityVillagerBanditHorseman extends AbstractVillagerBandit {
	protected EntityVillagerHorse horse;
	protected boolean isRiding = false;
	public static final double TROT = 0.8;
	public static final double RUNNING = 1.6;
	public static final double SPRINT = 2.4;
	
	protected float horseWidth;
	protected float horseHeight;
	
	public EntityVillagerBanditHorseman(World worldIn)
	{
		super(worldIn);
		this.canSpawn = Main.villager_spawn.get(Main.Soldier.Bandit_Horseman.ordinal()).spawn;
	}
	
	 protected void updateAITasks()
	    {
		 super.updateAITasks();
		 
		 if(!isRiding){
			 this.setRidingHorse();
		 }
		 if(this.horse == null || this.horse.isDead || this.inWater){
			 this.isRiding = false;
			 
			 EntityVillagerManAtArms entityvillager = new EntityVillagerManAtArms(this.world);
			  entityvillager.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(this.posX, this.posY, this.posZ)), null);
    	
			  entityvillager.setLocationAndAngles(this.posX + 0.5D, this.posY, this.posZ + 0.5D, 0.0F, 0.0F);
		       entityvillager.setSpawnPoint(this.posX + 0.5D, this.posY, this.posZ + 0.5D);
		       //entityvillager.setProfession(null);
		       
		       entityvillager.finalizeMobSpawn(this.world.getDifficultyForLocation(new BlockPos(entityvillager)), (IEntityLivingData)null, false);
		       this.world.spawnEntity(entityvillager);
		       this.setDead();
			 
		 }
	  }
	
	public void setRidingHorse(){
		this.horse = new EntityVillagerHorse(this.world);
        horse.setPosition((double)this.posX, (double)this.posY, (double)this.posZ);
        horse.setHorseTamed(true);
    	this.horseHeight = this.horse.height;
    	this.horseWidth  = this.horse.width; 
    	this.height += this.horseHeight;
    	this.width += this.horseWidth;
        
       /* if(world.rand.nextInt(100) < 50){
        	
        	horse.setHorseArmorStack(new ItemStack(Items.IRON_HORSE_ARMOR));
        }else if(world.rand.nextInt(100) < 20){
        	horse.setHorseArmorStack(new ItemStack(Items.GOLDEN_HORSE_ARMOR));
        	
        }else if(world.rand.nextInt(100) < 5){
        	horse.setHorseArmorStack(new ItemStack(Items.DIAMOND_HORSE_ARMOR));
        	
        	  
        this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
		this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
		this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
		this.targetTasks.addTask(2, new EntityAIGuardArea(this));
        }*/
        this.world.spawnEntity(horse);
        this.startRiding(horse);
        this.isRiding = true;
	}
	
	 protected void applyEntityAttributes()
	    {
	        super.applyEntityAttributes();
	        
	        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
	        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.50D);
	        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.8D);
	        getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(0.8D);
	        getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(16.0D);
	        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
	    }
	 
		@Override
		 protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
		    {
		        super.setEquipmentBasedOnDifficulty(difficulty);
		        
		        //Main.logger.info("Gave Equipment");//, message, p0, p1, p2, p3, p4, p5, p6, p7);

				this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.STONE_SWORD));
				this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
				this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
				this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
				this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
		    }
}
