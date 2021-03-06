package net.theexceptionist.coherentvillages.main.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.theexceptionist.coherentvillages.entity.ai.EntityAIAttackWithMelee;
import net.theexceptionist.coherentvillages.main.entity.spells.Spell;

/**
 * 
 * @author kennethstepney
 * @suggested by: Sunconure11
 */


public class EntityLemure extends EntityMob implements IMob, IRangedAttackMob{
	private EntityAIAttackWithMelee melee;// = new EntityAIAttackWithMelee(this, 1.0, true);
	private EntityAIAttackRanged ranged;
	private Spell spell;
	private int combatType = 0;
	
	public EntityLemure(World worldIn) {
		super(worldIn);
		// TODO Auto-generated constructor stub
	}
	
	protected void applyEntityAttributes()
    {
    	super.applyEntityAttributes();
    	this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.40000001192092896D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        
        spell = Spell.fireball;
    }

	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if(((this.getAttackTarget() != null && this.getAttackTarget().getDistance(this) < 4) 
				|| (this.getAttackingEntity() != null && this.getAttackingEntity().getDistance(this) < 4)) && combatType == 0)
		{
		    this.setCombatTask(1);
		}
		else if ((this.getAttackTarget() != null || this.getAttackingEntity() != null) && combatType == 1)
		{
			this.setCombatTask(0);
		}
		
		 if (this.world.isDaytime() && !this.world.isRemote && !this.isChild() && this.shouldBurnInDay())
	        {
	            float f = this.getBrightness();

	            if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world.canSeeSky(new BlockPos(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ)))
	            {
	                boolean flag = true;
	                ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

	                if (!itemstack.isEmpty())
	                {
	                    if (itemstack.isItemStackDamageable())
	                    {
	                        itemstack.setItemDamage(itemstack.getItemDamage() + this.rand.nextInt(2));

	                        if (itemstack.getItemDamage() >= itemstack.getMaxDamage())
	                        {
	                            this.renderBrokenItemStack(itemstack);
	                            this.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
	                        }
	                    }

	                    flag = false;
	                }

	                if (flag)
	                {
	                    this.setFire(8);
	                }
	            }
	        }
		this.world.spawnParticle(EnumParticleTypes.CLOUD, this.posX + this.rand.nextGaussian() * 0.12999999523162842D, this.getEntityBoundingBox().maxY - 0.5D - this.rand.nextGaussian() * 0.12999999523162842D, this.posZ + this.rand.nextGaussian() * 0.12999999523162842D, 0.0D, -0.12999999523162842D, 0.0D, new int[0]);
	}
	
	private boolean shouldBurnInDay() {
		// TODO Auto-generated method stub
		return true;
	}

	protected void initEntityAI()
    {
		melee = new EntityAIAttackWithMelee(this, 1.0, true);
		ranged = new EntityAIAttackRanged(this, 1.0D, 60,  (float)getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue() + 16.0F);
		
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIRestrictSun(this));
        this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(6, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        
        
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] {EntityPigZombie.class}));
	    this.targetTasks.addTask(6, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	    this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityVillager.class, true));
	    this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityGolem.class, true));
	    
	    this.setCombatTask(0);
    }
	
	private void setCombatTask(int type) {
		this.tasks.removeTask(melee);
		this.tasks.removeTask(ranged);
		
		if(type == 0)
		{
			this.tasks.addTask(0, ranged);
			combatType = 0;
		}
		else
		{
			this.tasks.addTask(0, melee);
			combatType = 1;
		}
	}

	public boolean attackEntityAsMob(Entity entityIn)
    {
		float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);
		
		if(entityIn instanceof EntityLivingBase)
		{
			((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, (world.rand.nextInt(3) + 1) * 20, 0));
		}
//        float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
//        int i = 0;
//
//        if (entityIn instanceof EntityLivingBase)
//        {
//            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)entityIn).getCreatureAttribute());
//            i += EnchantmentHelper.getKnockbackModifier(this);
//        }
//
//        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);
//        this.swingArm(EnumHand.MAIN_HAND);
//        
//        if (flag)
//        {
//            if (i > 0 && entityIn instanceof EntityLivingBase)
//            {
//                ((EntityLivingBase)entityIn).knockBack(this, (float)i * 0.5F, (double)MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
//                this.motionX *= 0.6D;
//                this.motionZ *= 0.6D;
//                this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, this.getSoundCategory(), 1.0F, 1.0F);
//                ((WorldServer)this.world).spawnParticle(EnumParticleTypes.DAMAGE_INDICATOR, entityIn.posX, entityIn.posY + (double)(entityIn.height * 0.5F), entityIn.posZ, (int)f, 0.1D, 0.0D, 0.1D, 0.2D);
//            }
//
//            int j = EnchantmentHelper.getFireAspectModifier(this);
//
//            if (j > 0)
//            {
//                entityIn.setFire(j * 4);
//            }
//
//            if (entityIn instanceof EntityPlayer)
//            {
//                EntityPlayer entityplayer = (EntityPlayer)entityIn;
//                ItemStack itemstack = this.getHeldItemMainhand();
//                ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;
//
//                if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem().canDisableShield(itemstack, itemstack1, entityplayer, this) && itemstack1.getItem().isShield(itemstack1, entityplayer))
//                {
//                    float f1 = 0.25F + (float)EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;
//
//                    if (this.rand.nextFloat() < f1)
//                    {
//                        entityplayer.getCooldownTracker().setCooldown(itemstack1.getItem(), 100);
//                        this.world.setEntityState(entityplayer, (byte)30);
//                    }
//                }
//            }
//
//            this.applyEnchantments(this, entityIn);
//        }
 
        return flag;
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

		@Override
		public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
			if(spell != null && !this.world.isRemote)
			{
				spell.execute(this);
			}
		}

		@Override
		public void setSwingingArms(boolean swingingArms) {
			// TODO Auto-generated method stub
			
		}
}
