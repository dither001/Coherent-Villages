package net.theexceptionist.coherentvillages.main.entity;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class EntityBjornserker extends EntityPolarBear implements IMob{

	private int coolDown, healthDown;

	public EntityBjornserker(World worldIn) {
		super(worldIn);
		// TODO Auto-generated constructor stub
	}
	
	protected void initEntityAI()
    {
		super.initEntityAI();
	        
	    this.targetTasks.addTask(6, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	    this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityVillager.class, true));
	    this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityGolem.class, true));
	    this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityCow.class, true));
	    this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPig.class, true));
	    this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityWolf.class, true));
	    
	    this.targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, EntityLiving.class, 1, true, true, new Predicate<EntityLiving>()
	    {
	       public boolean apply(@Nullable EntityLiving p_apply_1_)
	            {
	            	return p_apply_1_ != null && ((p_apply_1_ instanceof IAnimals) && !(p_apply_1_ instanceof EntityTameable)) && !(p_apply_1_ instanceof IMob);// /*|| (p_apply_1_ instanceof IAnimal)*/);
	            }
	    }));
	    this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
    }
	
	protected void applyEntityAttributes()
    {
    	super.applyEntityAttributes();
    	this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.22D);
    	this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(15.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0D);
    }
	
	protected void damageEntity(DamageSource damageSrc, float damageAmount)
    {
		DamageSource newSrc = handleInWall(damageSrc);
		if(newSrc == null) return;
		super.damageEntity(newSrc, damageAmount -= this.getEntityAttribute(SharedMonsterAttributes.ARMOR).getBaseValue());
    }
	
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		if(healthDown > 0)
		{
			healthDown--;
		}
		else
		{
			if(!this.world.isRemote && this.getHealth() < this.getMaxHealth())
			{
				this.heal(5);
				healthDown = 100;
			}
		}
		
		if(coolDown > 0)
		{
			coolDown--;
		}
		else
		if (!this.world.isRemote && this.hurtTime == 0 && this.coolDown <= 0)
        {
            this.collideWithEntities(this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(2.0D, 2.0D, 2.0D).offset(0.0D, -2.0D, 0.0D)));
            this.spawnExplosionParticle();
            this.coolDown = 100;
        }
	}
	
	public DamageSource handleInWall(DamageSource source)
	{
		if(source.damageType.contains("inWall"))
		{
			this.posX += 1;
			this.posY += 1;
			this.posZ += 1;
			return null;
		}
		else
		{
			return source;
		}
	}
	
	public void onDeath(DamageSource cause)
    {
        super.onDeath(cause);

        if(!this.world.isRemote)
        {
	        this.dropItem(Items.LEATHER, world.rand.nextInt(3) + 1);
        }
    }
	
	private void collideWithEntities(List<Entity> p_70970_1_)
    {
        double d0 = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0D;
        double d1 = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0D;

        for (Entity entity : p_70970_1_)
        {
            if (entity instanceof EntityLivingBase)
            {
                double d2 = entity.posX - d0;
                double d3 = entity.posZ - d1;
                double d4 = d2 * d2 + d3 * d3;
                entity.addVelocity(d2 / d4 * 4.0D, 0.20000000298023224D, d3 / d4 * 4.0D);

                entity.attackEntityFrom(DamageSource.causeMobDamage(this), 5.0F);
            }
        }
    }
	
	protected boolean isValidLightLevel()
    {
        BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);

        if (this.world.getLightFor(EnumSkyBlock.SKY, blockpos) > this.rand.nextInt(32))
        {
            return false;
        }
        else
        {
            int i = this.world.getLightFromNeighbors(blockpos);

            if (this.world.isThundering())
            {
                int j = this.world.getSkylightSubtracted();
                this.world.setSkylightSubtracted(10);
                i = this.world.getLightFromNeighbors(blockpos);
                this.world.setSkylightSubtracted(j);
            }

            return i <= this.rand.nextInt(8);
        }
    }
	
	 public boolean getCanSpawnHere()
	 {
	     return this.world.getDifficulty() != EnumDifficulty.PEACEFUL &&  (this.isValidLightLevel() && this.world.rand.nextInt(100) <= 2)  && super.getCanSpawnHere() /*&& this.isCanSpawn() && rand.nextInt(100) < Main.bandit_spawn*/;
	 }
	

}