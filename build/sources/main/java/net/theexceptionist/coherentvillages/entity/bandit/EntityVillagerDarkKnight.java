package net.theexceptionist.coherentvillages.entity.bandit;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.theexceptionist.coherentvillages.entity.EntityVillagerLighting;
import net.theexceptionist.coherentvillages.entity.ai.EntityAIAttackWithMagic;
import net.theexceptionist.coherentvillages.entity.followers.EntitySkeletonMinion;
import net.theexceptionist.coherentvillages.main.Main;

public class EntityVillagerDarkKnight extends AbstractVillagerBandit implements IRangedAttackMob{
	protected int coolDown = 0;
	protected List currentSpawns;
	protected int coolDownNecro = 0;
	protected int critChance;
	protected int burstCount;
	
	public EntityVillagerDarkKnight(World worldIn) {
		super(worldIn);
		this.burstCount = worldIn.rand.nextInt(5) + 5;
		this.critChance = 5;
		this.className = "Dark Knight";
		this.canSpawn = Main.villager_spawn.get(Main.Soldier.Necromancer.ordinal()).spawn;
		
		//this.isHostile = true;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void setUpgrade() {
		this.upgrade = null;//new EntityVillagerSergeant(world);
	}
	
	protected void initEntityAI()
    {
		super.initEntityAI();
		//this.areAdditionalTasksSet = true;
        this.tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6D, true));
        this.tasks.addTask(1, new  EntityAIAttackWithMagic(this, 1.0D, 20, 15.0F));
        
   	 for(Object task : this.tasks.taskEntries.toArray())
		{
			 EntityAIBase ai = ((EntityAITaskEntry) task).action;
			 if(ai instanceof EntityAIAttackMelee)
				 this.tasks.removeTask(ai);	
			 //System.out.println("Removed");
		}
       // this.tasks.addTask(6, new EntityAIHarvestFarmland(this, 0.6D));
    }
	 protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
	    {
	        super.setEquipmentBasedOnDifficulty(difficulty);
	        
	        
			//this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
	        this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
			this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
			this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
			this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
			this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
	    }
	 
	 protected void applyEntityAttributes()
	    {
	        super.applyEntityAttributes();
	        
	        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60.0D);
	        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30D);
	        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.8D);
	        getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(8.0D);
	        getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(0.0D);
	        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
	    }
	 
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		// TODO Auto-generated method stub
currentSpawns = this.world.getEntities(EntitySkeletonMinion.class, EntitySelectors.IS_ALIVE);
EntitySkeletonMinion skeleton = null;
        boolean crit = world.rand.nextInt(100) < critChance;
		if(coolDownNecro > 0)
		{
			coolDownNecro--;
		}
		if(coolDownNecro == 0 || (crit && this.getHealth() > 5)){
				for(int i = 0; i < currentSpawns.size(); i++){
					EntitySkeletonMinion g = (EntitySkeletonMinion) currentSpawns.get(i);
				}
				// TODO Auto-generated method stub
			      this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GHAST_SCREAM, SoundCategory.NEUTRAL, 2.0F, 0.5F + this.rand.nextFloat() * 0.2F);
			       
				int amount = 1 + this.world.rand.nextInt(4);
				BlockPos spawn = new BlockPos(this.posX, this.posY, this.posZ);
				
				
				for(int i =0; i < amount; i++){
					EntitySkeletonMinion entityvillager = new EntitySkeletonMinion(this.world, this);
		          entityvillager.setLocationAndAngles(spawn.getX() + rand.nextInt(5),spawn.getY()  + rand.nextInt(5),spawn.getZ()  + rand.nextInt(5), 0.0F, 0.0F);
		          entityvillager.onInitialSpawn(this.world.getDifficultyForLocation(getPosition()), null);
		          //entityvillager.setSpawnPoint(spawn.getX()  + rand.nextInt(5),spawn.getY()  + rand.nextInt(5),spawn.getZ()  + rand.nextInt(5));
		          //entityvillager.setProfession(null); 
		          //entityvillager.finalizeMobSpawn(this.world.getDifficultyForLocation(new BlockPos(entityvillager)), (IEntityLivingData)null, false);
		          this.world.spawnEntity(entityvillager);
		          
		          if(skeleton == null) skeleton = entityvillager;
				}
				
				if(crit)
				{
					this.heal(-5);
				}
				else
				{
					coolDownNecro = 5;
				}
		}
				

		if(!this.world.isRemote){
			if(coolDown > 0)
			{
				coolDown--;
			}else
			
			if(coolDown <= 0 && skeleton != null){
				
				this.world.addWeatherEffect(new EntityVillagerLighting(this.world, skeleton.posX, skeleton.posY, skeleton.posZ, true));
				coolDown = 3;
			}
		}
		
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {
		// TODO Auto-generated method stub
		
	}

}