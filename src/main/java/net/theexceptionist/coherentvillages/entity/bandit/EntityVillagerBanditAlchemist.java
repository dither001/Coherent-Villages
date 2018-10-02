package net.theexceptionist.coherentvillages.entity.bandit;

import java.util.List;
import java.util.UUID;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.theexceptionist.coherentvillages.entity.ai.EntityAIHealAllies;
import net.theexceptionist.coherentvillages.main.Main;

public class EntityVillagerBanditAlchemist extends AbstractVillagerBandit implements IRangedAttackMob{
	protected static final UUID MODIFIER_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
    protected static final AttributeModifier MODIFIER = (new AttributeModifier(MODIFIER_UUID, "Drinking speed penalty", -0.25D, 0)).setSaved(false);
    protected static final DataParameter<Boolean> IS_AGGRESSIVE = EntityDataManager.<Boolean>createKey(EntityVillagerBanditAlchemist.class, DataSerializers.BOOLEAN);
   
    protected int attackTimer;
    protected int coolDown = 0;
    protected EntityLivingBase client;
    
	public EntityVillagerBanditAlchemist(World worldIn)
	{
		super(worldIn);
		this.canSpawn = Main.villager_spawn.get(Main.Soldier.Bandit_Alchemist.ordinal()).spawn;
	}
	
    public boolean isDrinkingPotion()
    {
        return ((Boolean)this.getDataManager().get(IS_AGGRESSIVE)).booleanValue();
    }
    
    public void setAggressive(boolean aggressive)
    {
        this.getDataManager().set(IS_AGGRESSIVE, Boolean.valueOf(aggressive));
    }

	protected void initEntityAI()
    {
		super.initEntityAI();
        //this.tasks.addTask(3, new EntityAIHealAllies(this, 1.0D, 60, 10.0F, EntityVillager.class));
        this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0D, 60, 10.0F));
        
   	 for(Object task : this.tasks.taskEntries.toArray())
		{
			 EntityAIBase ai = ((EntityAITaskEntry) task).action;
			 if(ai instanceof EntityAIAttackMelee)
				 this.tasks.removeTask(ai);	
			 //System.out.println("Removed");
		}
		//this.areAdditionalTasksSet = true;
       // this.tasks.addTask(6, new EntityAIHarvestFarmland(this, 0.6D));
    }
    public void onLivingUpdate()
    {
        if (!this.world.isRemote)
        {
        	//this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.POTIONITEM));
            if (this.isDrinkingPotion())
            {
                if (this.attackTimer-- <= 0)
                {
                    this.setAggressive(false);
                    ItemStack itemstack = this.getHeldItemMainhand();
                    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);

                    if (itemstack.getItem() == Items.POTIONITEM)
                    {
                        List<PotionEffect> list = PotionUtils.getEffectsFromStack(itemstack);

                        if (list != null)
                        {
                            for (PotionEffect potioneffect : list)
                            {
                                this.addPotionEffect(new PotionEffect(potioneffect));
                            }
                        }
                    }

                    this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(MODIFIER);
                }
            }
            else
            {
                PotionType potiontype = null;

                if (this.rand.nextFloat() < 0.15F && this.isInsideOfMaterial(Material.WATER) && !this.isPotionActive(MobEffects.WATER_BREATHING))
                {
                    potiontype = PotionTypes.WATER_BREATHING;
                }
                else if (this.rand.nextFloat() < 0.15F && (this.isBurning() || this.getLastDamageSource() != null && this.getLastDamageSource().isFireDamage()) && !this.isPotionActive(MobEffects.FIRE_RESISTANCE))
                {
                    potiontype = PotionTypes.FIRE_RESISTANCE;
                }
                else if (this.rand.nextFloat() < 0.05F && this.getHealth() < this.getMaxHealth())
                {
                    potiontype = PotionTypes.HEALING;
                }
                else if (this.rand.nextFloat() < 0.5F && this.getAttackTarget() != null && !this.isPotionActive(MobEffects.SPEED) && this.getAttackTarget().getDistanceSq(this) > 121.0D)
                {
                    potiontype = PotionTypes.SWIFTNESS;
                    //PotionTypes.
                }

                if (potiontype != null)
                {
                    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), potiontype));
                    this.attackTimer = this.getHeldItemMainhand().getMaxItemUseDuration();
                    this.setAggressive(true);
                    this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_DRINK, this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
                    IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
                    iattributeinstance.removeModifier(MODIFIER);
                    iattributeinstance.applyModifier(MODIFIER);
                }
            }

            if (this.rand.nextFloat() < 7.5E-4F)
            {
                this.world.setEntityState(this, (byte)15);
            }
        }

        super.onLivingUpdate();
    }
    
    protected void entityInit()
    {
        super.entityInit();
        this.getDataManager().register(IS_AGGRESSIVE, Boolean.valueOf(false));
    }
    
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor)
    {
        if (!this.isDrinkingPotion())
        {
            double d0 = target.posY + (double)target.getEyeHeight() - 1.100000023841858D;
            double d1 = target.posX + target.motionX - this.posX;
            double d2 = d0 - this.posY;
            double d3 = target.posZ + target.motionZ - this.posZ;
            float f = MathHelper.sqrt(d1 * d1 + d3 * d3);
            PotionType potiontype = PotionTypes.HARMING;
            
            if(world.rand.nextInt(100) < 10)
            	potiontype = PotionTypes.STRONG_HARMING;

           if (f >= 8.0F && !target.isPotionActive(MobEffects.SLOWNESS))
            {
                potiontype = PotionTypes.SLOWNESS;
            }/*else if(target instanceof EntityPigZombie){
            	 potiontype = PotionTypes.HARMING;
            }*/
          /*  else if (target. >= 8.0F && !target.isPotionActive(MobEffects.POISON))
            {
                potiontype = PotionTypes.REGENERATION;
            }*/
            else if (target.isEntityUndead())
            {
                potiontype = PotionTypes.HEALING;
                if(world.rand.nextInt(100) < 10)
                	potiontype = PotionTypes.STRONG_HEALING;
            }else if (target.getHealth() >= 8.0F && !target.isPotionActive(MobEffects.POISON) && !(target instanceof EntitySpider))
            {
                potiontype = PotionTypes.POISON;
                if(world.rand.nextInt(100) < 10)
                	potiontype = PotionTypes.STRONG_POISON;
            }
           

            EntityPotion entitypotion = new EntityPotion(this.world, this, PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potiontype));
            entitypotion.rotationPitch -= -20.0F;
            entitypotion.shoot(d1, d2 + (double)(f * 0.2F), d3, 0.75F, 8.0F);
            this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_THROW, this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
            this.world.spawnEntity(entitypotion);
        }
    }
    

	public void healEntityWithRangedAttack(EntityLivingBase target,
			float lvt_5_1_) {
		 if (!this.isDrinkingPotion())
	        {
	            double d0 = target.posY + (double)target.getEyeHeight() - 1.100000023841858D;
	            double d1 = target.posX + target.motionX - this.posX;
	            double d2 = d0 - this.posY;
	            double d3 = target.posZ + target.motionZ - this.posZ;
	            float f = MathHelper.sqrt(d1 * d1 + d3 * d3);
	            PotionType potiontype = PotionTypes.REGENERATION;
	            
	            if(world.rand.nextInt(100) < 10)
                	potiontype = PotionTypes.STRONG_REGENERATION;

	           /* if (f >= 8.0F && !target.isPotionActive(MobEffects.SLOWNESS))
	            {
	                potiontype = PotionTypes.SLOWNESS;
	            }
	            else*/ if (target.getHealth() <= 8.0F && !target.isPotionActive(MobEffects.POISON))
	            {
	                potiontype = PotionTypes.HEALING;
	                if(world.rand.nextInt(100) < 10)
	                	potiontype = PotionTypes.STRONG_HEALING;
	               
	            }
	         /*   else if (f <= 3.0F && this.rand.nextFloat() < 0.25F)
	            {
	                potiontype = PotionTypes.HEALING;
	            }*/

	            EntityPotion entitypotion = new EntityPotion(this.world, this, PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potiontype));
	            entitypotion.rotationPitch -= -20.0F;
	            entitypotion.shoot(d1, d2 + (double)(f * 0.2F), d3, 0.75F, 8.0F);
	            this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_THROW, this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
	            this.world.spawnEntity(entitypotion);
	        }
		
	}
	
	
	 protected void applyEntityAttributes()
	    {
	        super.applyEntityAttributes();
	        
	        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
	        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.50D);
	        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.3D);
	        getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(0.3D);
	        getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(16.0D);
	        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
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


		@Override
		public void setSwingingArms(boolean swingingArms) {
			// TODO Auto-generated method stub
			
		}
}
