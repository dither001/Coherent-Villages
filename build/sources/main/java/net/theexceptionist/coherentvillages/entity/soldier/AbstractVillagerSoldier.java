package net.theexceptionist.coherentvillages.entity.soldier;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHarvestFarmland;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.village.Village;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.theexceptionist.coherentvillages.entity.ai.EntityAIAttackBackExclude;
import net.theexceptionist.coherentvillages.entity.ai.EntityAIStayInBorders;
import net.theexceptionist.coherentvillages.entity.bandit.AbstractVillagerBandit;
import net.theexceptionist.coherentvillages.entity.followers.EntitySkeletonMinion;
import net.theexceptionist.coherentvillages.entity.followers.IEntityFollower;
import net.theexceptionist.coherentvillages.main.Main;
import net.theexceptionist.coherentvillages.main.NameGenerator;

public abstract class AbstractVillagerSoldier extends EntityVillager implements IEntityFollower{
	protected String className = "Soldier";
	protected int faction = 0;
	public boolean wasSpawned = false, attacking = false;
	public BlockPos spawnPos;
	protected int homeCheckTimer; 
	protected Village village;
	protected Object buyingList;
	protected boolean isHostile, canSpawn;
	protected boolean creeperHunter, undeadHunter, livingHunter;
	
	public AbstractVillagerSoldier(World worldIn) {
		super(worldIn);
		
		//Friendly by default
		this.faction = Main.SOLDIER_FACTION;
	}
	
	public AbstractVillagerSoldier(World worldIn, boolean hostile) {
		super(worldIn);
		this.isHostile = hostile;
	}
	
	public AbstractVillagerSoldier(World worldIn, boolean hostile, boolean creeperHunter) {
		super(worldIn);
		this.isHostile = hostile;
		this.creeperHunter = creeperHunter;
	}
	
	public AbstractVillagerSoldier(World worldIn, boolean hostile, boolean creeperHunter, boolean undeadHunter, boolean livingHunter) {
		super(worldIn);
		this.isHostile = hostile;
		/*this.creeperHunter = creeperHunter;
		this.creeperHunter = undeadHunter;
		this.creeperHunter = livingHunter;*/
	}
	

	public boolean isCanSpawn() {
		return canSpawn;
	}

	public void setCanSpawn(boolean canSpawn) {
		this.canSpawn = canSpawn;
	}

	public void onStruckByLightning(EntityLightningBolt lightningBolt)
    {

    }
	
	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
		return false;
    }
	
	@Override
	public MerchantRecipeList getRecipes(EntityPlayer player)
    {
        if (this.buyingList == null)
        {
            //this.populateBuyingList();
        }

        return null;
    }
	
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setEnchantmentBasedOnDifficulty(difficulty);
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * difficulty.getClampedAdditionalDifficulty());

		 for(Object task : this.tasks.taskEntries.toArray())
			{
				 EntityAIBase ai = ((EntityAITaskEntry) task).action;
				 if(ai instanceof EntityAIHarvestFarmland)
					 this.tasks.removeTask(ai);	
				 //System.out.println("Removed");
				 
			}
		 
        if (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty())
        {
            Calendar calendar = this.world.getCurrentDate();

            /*if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25F)
            {
                this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
                this.inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.0F;
            }*/
        }
        
		this.setCustomNameTag(getTrueName(world.rand));
		this.setAlwaysRenderNameTag(Main.useNametags);

        return livingdata;
    }
    
    protected String getTrueName(Random rand)
    {
    	return NameGenerator.generateRandomName(rand)+" - "+className;
    }
    
    protected String getClassName()
    {
    	return className;
    }
	
	protected void initEntityAI()
    {
		this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(2, new EntityAIStayInBorders(this, 1.0D));
        this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(5, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.6D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        
        this.targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, EntityLiving.class, 1, true, true, new Predicate<EntityLiving>()
        {
            public boolean apply(@Nullable EntityLiving p_apply_1_)
            {
            	int faction = getFaction();
            	
            	if(p_apply_1_ instanceof AbstractVillagerSoldier)
            	{
            		AbstractVillagerSoldier soldier = (AbstractVillagerSoldier) p_apply_1_;
            		int soldierFaction = soldier.getFaction();
            		
            		//System.out.println(getCustomNameTag()+" - "+faction+" | "+soldier.getCustomNameTag()+" - "+soldierFaction);
            		
            		if(faction != soldierFaction)
            		{
            			//System.out.println("True");
            			return true;
            		}
            		else if(faction == soldierFaction)
            		{
            			//System.out.println("False");
            			return false;
            		}
            	}
            	//ystem.out.println(getCustomNameTag()+" - "+getFaction());
        		return p_apply_1_ != null && (IMob.VISIBLE_MOB_SELECTOR.apply(p_apply_1_) && !(p_apply_1_ instanceof EntityCreeper) && !(p_apply_1_ instanceof EntityTameable) && !(p_apply_1_ instanceof EntitySkeletonMinion));
            }
        }));
        this.targetTasks.addTask(1, new EntityAIAttackBackExclude(this, true, new Class[0]));  
		//this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        //if(this.isHostile) this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        
		/*this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, true));
       // this.tasks.addTask(5, new EntityAIHangAroundFence(this, this.world));
        
        //this.tasks.addTask(2, new EntityAIMoveIndoors(this));
        this.tasks.addTask(2, new EntityAIRestrictOpenDoor(this));
        this.tasks.addTask(3, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(4, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
        this.tasks.addTask(5, new EntityAIMoveThroughVillage(this, 0.6D, true));
        this.tasks.addTask(6, new EntityAISearchHouse(this, 50));
        this.tasks.addTask(7, new EntityAIMoveTowardsRestriction(this, 1.0D));
        // this.tasks.addTask(8, new EntityAIGuardPost(this, true));
        //this.Stasks.addTask(5, new EntityAILookAtVillager(this));
        this.tasks.addTask(8, new EntityAIGuardArea(this));
        this.tasks.addTask(9, new EntityAIWanderAvoidWater(this, 0.6D));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(11, new EntityAILookIdle(this));
       // this.tasks.addTask(6, new EntityAIHarvestFarmland(this, 0.6D));
        //this.areAdditionalTasksSet = true;
        
        
        
        this.targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, EntityLiving.class, 1, false, true, new Predicate<EntityLiving>()
        {
            public boolean apply(@Nullable EntityLiving p_apply_1_)
            {
                return p_apply_1_ != null && IMob.VISIBLE_MOB_SELECTOR.apply(p_apply_1_) && !(p_apply_1_ instanceof EntityCreeper);
            }
        }));
        this.targetTasks.addTask(1, new EntityAIAttackBackExclude(this, true, new Class[0]));*/  
        
		
		//this.getName();
    }
	
	public boolean isValidSpawn(World world, BlockPos pos)
	{
		BlockPos posNew = pos.add(0, 1, 0);
		return world.getBlockState(posNew).getBlock() == Blocks.AIR;
	}
	
	
	 public void setFaction(int faction) {
		this.faction = faction;
	}

	protected void applyEntityAttributes()
	    {
	        super.applyEntityAttributes();
	        
	        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24.0D);
	        getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
	    }
	 
	 public void onLivingUpdate()
	    {
	        super.onLivingUpdate();
	    }
	 
	 protected void damageEntity(DamageSource damageSrc, float damageAmount)
	    {
		 //Maybe have faction idea
		 if(damageSrc.getTrueSource() instanceof AbstractVillagerSoldier){
			 AbstractVillagerSoldier soldier = (AbstractVillagerSoldier) damageSrc.getTrueSource();
			 
			 //System.out.println("Working");
			 
			 if(soldier.getFaction() == this.getFaction())
			 {
				 //System.out.println("Work");
				 return;
			 }
			 else
			 {
				 super.damageEntity(damageSrc, damageAmount);
			 }
		 }
		 else
		 {
			 super.damageEntity(damageSrc, damageAmount);
		 }
		// System.out.println("Working");
	    }
	 
	 private int getFaction() {
		// TODO Auto-generated method stub
		return this.faction;
	}

	protected void updateAITasks()
	    {
		
		 super.updateAITasks();
		 
		 //Make sure a villager isn't the target
		 if(this.getAttackTarget() instanceof EntityVillager){
			 if(this.getAttackTarget() instanceof AbstractVillagerSoldier)
			 {
				 AbstractVillagerSoldier soldier = (AbstractVillagerSoldier) this.getAttackTarget();
				 if(soldier.getFaction() == this.getFaction())
				 {
					 this.setAttackTarget(null);
				 }
			 }
			 else
			 {
				 if(this.faction == Main.BANDIT_FACTION)
				 {
					 this.setAttackTarget(null); 
				 }
			 }
		 }
		 
		 if (--this.homeCheckTimer <= 0)
	        {
	            this.homeCheckTimer = 70 + this.rand.nextInt(50);
	            this.village = this.world.getVillageCollection().getNearestVillage(new BlockPos(this), 32);

	            if (this.village == null)
	            {
	                this.detachHome();
	            }
	            else
	            {
	                BlockPos blockpos = this.village.getCenter();
	                this.setHomePosAndDistance(blockpos, (int)((float)this.village.getVillageRadius() * 0.6F));
	            }
	        }
	    }
	 
	

	 public boolean attackEntityAsMob(Entity entityIn)
	    {
		 float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
	        int i = 0;

	        if (entityIn instanceof EntityLivingBase)
	        {
	            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)entityIn).getCreatureAttribute());
	            i += EnchantmentHelper.getKnockbackModifier(this);
	           }
	        
	        this.attacking = true;
	        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

	        if (flag)
	        {
	            if (i > 0 && entityIn instanceof EntityLivingBase)
	            {
	                ((EntityLivingBase)entityIn).knockBack(this, (float)i * 0.5F, (double)MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
	                this.motionX *= 0.6D;
	                this.motionZ *= 0.6D;
	            }

	            int j = EnchantmentHelper.getFireAspectModifier(this);

	            if (j > 0)
	            {
	                entityIn.setFire(j * 4);
	            }

	            if (entityIn instanceof EntityPlayer)
	            {
	                EntityPlayer entityplayer = (EntityPlayer)entityIn;
	                ItemStack itemstack = this.getHeldItemMainhand();
	                ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

	                if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem() instanceof ItemAxe && itemstack1.getItem() == Items.SHIELD)
	                {
	                    float f1 = 0.25F + (float)EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

	                    if (this.rand.nextFloat() < f1)
	                    {
	                        entityplayer.getCooldownTracker().setCooldown(Items.SHIELD, 100);
	                        this.world.setEntityState(entityplayer, (byte)30);
	                    }
	                }
	            }

	            this.applyEnchantments(this, entityIn);
	        }

	        return flag;
	    }

	 protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
	    {
	        super.setEquipmentBasedOnDifficulty(difficulty);
	        
	        //Main.logger.info("Gave Equipment");//, message, p0, p1, p2, p3, p4, p5, p6, p7);

			this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
			this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
			this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
	    }
	 
	public void setSpawnPoint(double d, double k, double e) {
		this.spawnPos = new BlockPos(d, k, e);
	}


	public Village getVillage() {
		// TODO Auto-generated method stub
		return this.village;
	}
	
	@Override
	public boolean isAIDisabled()
	{
	   return false;
	}

	public boolean isShouldFollow() {
		return true;
	}
	
	@Override
	public EntityLiving getLiving() {
		// TODO Auto-generated method stub
		return this;
	}
	
	   /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
       /* super.writeEntityToNBT(compound);
        compound.setInteger("Profession", this.getProfession());
        compound.setString("ProfessionName", this.getProfessionForge().getRegistryName().toString());
        compound.setInteger("Riches", this.);
        compound.setInteger("Career", this.careerId);
        compound.setInteger("CareerLevel", this.careerLevel);
        compound.setBoolean("Willing", this.isWillingToMate);

        if (this.buyingList != null)
        {
            compound.setTag("Offers", this.buyingList.getRecipiesAsTags());
        }
*/
        NBTTagList nbttaglist = new NBTTagList();
        
        nbttaglist.appendTag(this.getHeldItemMainhand().writeToNBT(new NBTTagCompound()));
        Iterator<ItemStack> equip = this.getEquipmentAndArmor().iterator();

       while(equip.hasNext())
       {
    	   ItemStack item = equip.next();
    	   nbttaglist.appendTag(item.writeToNBT(new NBTTagCompound()));
    	//   System.out.println(this.getName()+" equip: - "+item.getDisplayName());
       }

        compound.setTag("Equipment", nbttaglist);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        /*super.readEntityFromNBT(compound);
        this.setProfession(compound.getInteger("Profession"));
        if (compound.hasKey("ProfessionName"))
        {
            net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession p =
                net.minecraftforge.fml.common.registry.ForgeRegistries.VILLAGER_PROFESSIONS.getValue(new net.minecraft.util.ResourceLocation(compound.getString("ProfessionName")));
            if (p == null)
                p = net.minecraftforge.fml.common.registry.ForgeRegistries.VILLAGER_PROFESSIONS.getValue(new net.minecraft.util.ResourceLocation("minecraft:farmer"));
            this.setProfession(p);
        }
        this.wealth = compound.getInteger("Riches");
        this.careerId = compound.getInteger("Career");
        this.careerLevel = compound.getInteger("CareerLevel");
        this.isWillingToMate = compound.getBoolean("Willing");

        if (compound.hasKey("Offers", 10))
        {
            NBTTagCompound nbttagcompound = compound.getCompoundTag("Offers");
            this.buyingList = new MerchantRecipeList(nbttagcompound);
        }*/

        NBTTagList nbttaglist = compound.getTagList("Equipment", 10);
        
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            ItemStack itemstack = new ItemStack(nbttaglist.getCompoundTagAt(i));

            if(itemstack.getItem() instanceof ItemSword || itemstack.getItem() instanceof ItemAxe || itemstack.getItem() instanceof ItemSpade|| itemstack.getItem() instanceof ItemHoe)
            {
            	 this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(nbttaglist.getCompoundTagAt(i)));
            }
            else if(itemstack.getItem().isValidArmor(itemstack, EntityEquipmentSlot.HEAD, this))
            {
    			this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(nbttaglist.getCompoundTagAt(i)));
            }
            else if(itemstack.getItem().isValidArmor(itemstack, EntityEquipmentSlot.CHEST, this))
            {
    			this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(nbttaglist.getCompoundTagAt(i)));
            }
            else if(itemstack.getItem().isValidArmor(itemstack, EntityEquipmentSlot.LEGS, this))
            {
    			this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(nbttaglist.getCompoundTagAt(i)));
            }
            else if(itemstack.getItem().isValidArmor(itemstack, EntityEquipmentSlot.FEET, this))
            {
    			this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(nbttaglist.getCompoundTagAt(i)));
            }
        }

        /*this.setCanPickUpLoot(true);
        this.setAdditionalAItasks();*/
    }

	@Override
	public void setMaster(AbstractVillagerSoldier villager) {
		// TODO Auto-generated method stub
		
	}

}
