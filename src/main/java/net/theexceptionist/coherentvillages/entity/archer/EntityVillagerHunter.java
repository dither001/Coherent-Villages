package net.theexceptionist.coherentvillages.entity.archer;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.theexceptionist.coherentvillages.entity.ai.EntityAIAttackBackExclude;
import net.theexceptionist.coherentvillages.entity.ai.EntityAIAttackWithBow;
import net.theexceptionist.coherentvillages.entity.ai.EntityAIStayInBorders;
import net.theexceptionist.coherentvillages.entity.followers.EntitySkeletonMinion;
import net.theexceptionist.coherentvillages.entity.soldier.AbstractVillagerSoldier;
import net.theexceptionist.coherentvillages.main.Main;

public class EntityVillagerHunter extends AbstractVillagerArcher{

	public EntityVillagerHunter(World worldIn) {
		super(worldIn);
		this.hunter = true;
		this.className = "Hunter";
		this.canSpawn = Main.villager_spawn.get(Main.Soldier.Hunter.ordinal()).spawn;

	}
	
	public EntityVillagerHunter(World worldIn, boolean hunter) {
		super(worldIn, hunter);
		// TODO Auto-generated constructor stub
	}
	
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.40D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.3D);
        getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(0.0D);
        getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(0.0D);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
//        EntityHorse horse = new EntityHorse(this.world);
//        horse.setPosition(this.posX, this.posY, this.posZ);
//        this.world.spawnEntity(horse);
//        this.startRiding(horse);
		
    }
	
	 protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
	    {
	        super.setEquipmentBasedOnDifficulty(difficulty);
	        
			this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
			this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
	    }
	
	protected void initEntityAI()
    {
		//super.initEntityAI();
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIAttackWithBow(this, 1.0D, 60, 10.0F));
        this.tasks.addTask(2, new EntityAIStayInBorders(this, 1.0D));
        this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(5, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.6D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
		this.tasks.addTask(7, new EntityAIMoveTowardsRestriction(this, 1.0D));
        
        this.targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, EntityLiving.class, 1, true, true, new Predicate<EntityLiving>()
        {
            public boolean apply(@Nullable EntityLiving p_apply_1_)
            {
            	//ystem.out.println(getCustomNameTag()+" - "+getFaction());
        		return p_apply_1_ != null && ((p_apply_1_ instanceof IAnimals) && !(p_apply_1_ instanceof EntityTameable));
            }
        }));
        this.targetTasks.addTask(1, new EntityAIAttackBackExclude(this, true, new Class[0]));
    }
	
	@Override
	protected EntityArrow getArrow(float p_190726_1_) {
		EntityTippedArrow entitytippedarrow = new EntityTippedArrow(this.world, this);
        entitytippedarrow.setEnchantmentEffectsFromEntity(this, p_190726_1_);
        return entitytippedarrow;
	}


}