package net.theexceptionist.coherentvillages.main.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWaterFlying;
import net.minecraft.entity.ai.EntityFlyHelper;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityFlying;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.theexceptionist.coherentvillages.entity.ai.EntityAIAttackWithMelee;

public class EntityWraith extends EntityMob implements IMob, EntityFlying{

	
	public EntityWraith(World worldIn) {
		super(worldIn);
		this.moveHelper = new EntityFlyHelper(this);
		//this.moveHelper = new EntityWraith.GhastMoveHelper(this);
	}
	
	 public EnumCreatureAttribute getCreatureAttribute()
	    {
	        return EnumCreatureAttribute.UNDEAD;
	    }

	

	@Override
    public void fall(float distance, float damageMultiplier)
    {
		
    }

	@Override
    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos)
    {
		
    }
	
	protected void initEntityAI()
    {
		this.tasks.addTask(0, new EntityAIAttackWithMelee(this, 1.0, true));
		this.tasks.addTask(2, new EntityAIWanderAvoidWaterFlying(this, 1.0D));
		
	    this.targetTasks.addTask(6, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	    this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityVillager.class, true));
	    this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityGolem.class, true));
	    this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] {EntityPigZombie.class}));
    }
	
	 protected void applyEntityAttributes()
	    {
	        super.applyEntityAttributes();
	        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
	        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
	        this.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.4000000059604645D);
	        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.40000000298023224D);
	        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1D);
	        getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(1D);
	        getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(32.0D);
	        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
	        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
	        
	    }
	 
	 protected PathNavigate createNavigator(World worldIn)
	    {
	        PathNavigateFlying pathnavigateflying = new PathNavigateFlying(this, worldIn);
	        pathnavigateflying.setCanOpenDoors(false);
	        pathnavigateflying.setCanFloat(true);
	        pathnavigateflying.setCanEnterDoors(true);
	        return pathnavigateflying;
	    }
	 
	 protected void damageEntity(DamageSource damageSrc, float damageAmount)
	    {
			DamageSource newSrc = handleSource(damageSrc);
			float damage = damageAmount;
			if(newSrc == null) return;
			super.damageEntity(newSrc, damage);
	    }
	 
		public DamageSource handleSource(DamageSource source)
		{
			if(source.isExplosion() || source.isFireDamage() || source.isMagicDamage())
			{
				return source;
			}
			else
			{
				return null;
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
