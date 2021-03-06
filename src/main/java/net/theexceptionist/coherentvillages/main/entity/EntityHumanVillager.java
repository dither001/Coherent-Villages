package net.theexceptionist.coherentvillages.main.entity;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIFollowGolem;
import net.minecraft.entity.ai.EntityAIHarvestFarmland;
import net.minecraft.entity.ai.EntityAILookAtTradePlayer;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.ai.EntityAITradePlayer;
import net.minecraft.entity.ai.EntityAIVillagerInteract;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.theexceptionist.coherentvillages.entity.ai.EntityAIAttackBackExclude;
import net.theexceptionist.coherentvillages.entity.ai.EntityAIAttackRangedMod;
import net.theexceptionist.coherentvillages.entity.ai.EntityAIAttackWithBow;
import net.theexceptionist.coherentvillages.entity.ai.EntityAIAttackWithMagic;
import net.theexceptionist.coherentvillages.entity.ai.EntityAIAttackWithMelee;
import net.theexceptionist.coherentvillages.entity.ai.EntityAIBlock;
import net.theexceptionist.coherentvillages.entity.ai.EntityAIFollowEntity;
import net.theexceptionist.coherentvillages.entity.ai.EntityAIHealAllies;
import net.theexceptionist.coherentvillages.entity.ai.EntityAIHideFromHarm;
import net.theexceptionist.coherentvillages.entity.ai.EntityAIPatrolEntireVillage;
import net.theexceptionist.coherentvillages.entity.ai.EntityAIRest;
import net.theexceptionist.coherentvillages.entity.ai.EntityAISearchForHorse;
import net.theexceptionist.coherentvillages.entity.ai.EntityAIStayInBorders;
import net.theexceptionist.coherentvillages.entity.followers.IEntityFollower;
import net.theexceptionist.coherentvillages.main.Main;
import net.theexceptionist.coherentvillages.main.NameGenerator;
import net.theexceptionist.coherentvillages.main.entity.attributes.AttributeFaction;
import net.theexceptionist.coherentvillages.main.entity.attributes.AttributeQuality;
import net.theexceptionist.coherentvillages.main.entity.attributes.AttributeRace;
import net.theexceptionist.coherentvillages.main.entity.attributes.AttributeVocation;
import net.theexceptionist.coherentvillages.main.entity.spells.Spell;
import net.theexceptionist.coherentvillages.main.items.EntityWeaponThrowable;
import net.theexceptionist.coherentvillages.main.items.ItemModShield;
import net.theexceptionist.coherentvillages.main.items.ItemWeaponThrowable;

public class EntityHumanVillager extends EntityVillager implements IEntityFollower, IEntityVillager, IRangedAttackMob{
	public final static int GENDER_MALE = 0;
	public final static int GENDER_FEMALE = 1;
	
	//USED to store shifting data
	private static final int BJORN_ID = 0;
	private static final int WRAITH_ID = 1;
	
	public static int END_ID = 0;

	protected String firstName, lastName;
	protected int gender = -1;
	protected AttributeRace race;
	protected AttributeFaction faction;
	protected AttributeVocation vocation;
	protected AttributeQuality quality;
	protected Village village;
	private int[] hostiles;
	protected int factionNumber = -1;
	private String factionName = "None";
	
	private EntityHumanVillager liege;
	protected EntityLiving creatureShiftTo = null;
	private EntityLivingBase master = null;
	private EntityHumanVillager servant = null;
	private AbstractHorse horse;
	private float horseHeight;
	private float horseWidth;
	private boolean horseSpawned = false;
	
	private EntityAIAttackWithMelee melee;
	private EntityAIAttackRangedMod ranged;
	private EntityAIAttackWithBow rangedTrained;
	private EntityAIBlock block;
	private EntityAIHealAllies heal;
	private EntityAIAttackRanged potions;
	private EntityAIAttackWithMagic spells;
	private EntityAIHarvestFarmland farm;
	
	private int homeCheckTimer = 0;
	private int swingProgressTicks;
	private boolean isSwinging = false;
	
	private int kills = 0;
	
	//
	private boolean spawnAttempt = false;
	private boolean patrolShift = true;
	private boolean ruler = false;
	protected boolean shifter = false;
	protected boolean destructive = false;
	private boolean dropLoot = true;
	
	protected PotionType[] ARROW_POTIONS = new PotionType[2];
	protected int A_PASSIVE_1 = 0;
	protected int A_PASSIVE_2 = 1;
	
	protected PotionType[] PASSIVE_POTIONS = new PotionType[8];
	protected int PASSIVE_1 = 0;
	protected int PASSIVE_2 = 1;
	protected int PASSIVE_3 = 2;
	protected int PASSIVE_4 = 3;
	
	//Additional Potion Slots
	protected int PASSIVE_5 = 4;
	protected int PASSIVE_6 = 5;
	protected int PASSIVE_7 = 6;
	protected int PASSIVE_8 = 7;
	
	protected PotionType[] ACTIVE_POTIONS = new PotionType[8];
	protected int ACTIVE_1 = 0;
	protected int ACTIVE_2 = 1;
	protected int ACTIVE_3 = 2;
	protected int ACTIVE_4 = 3;
	
	//Additional Potion Slots
	protected int ACTIVE_5 = 4;
	protected int ACTIVE_6 = 5;
	protected int ACTIVE_7 = 6;
	protected int ACTIVE_8 = 7;
	private boolean canUsePotions = false;
	private boolean additionalPotionSlots = false;
	
	protected Spell[] PASSIVE_SPELLS = new Spell[4];
	protected int S_PASSIVE_1 = 0;
	protected int S_PASSIVE_2 = 1;
	//Only for master wizards
	protected int S_PASSIVE_3 = 2;
	protected int S_PASSIVE_4 = 3;
	
	protected Spell[] ACTIVE_SPELLS = new Spell[4];
	protected int S_ACTIVE_1 = 0;
	protected int S_ACTIVE_2 = 1;
	//Only for master wizards
	protected int S_ACTIVE_3 = 2;
	protected int S_ACTIVE_4 = 3;
	private boolean isMagic = false;
	private boolean additionalMagicSlots = false;
	
	protected ItemWeaponThrowable thrownWeapon = null;
	protected Item meleeWeapon = null;
	protected Item rangedWeapon = null;
	protected ItemStack held;
	
	private static final DataParameter<Integer> PROFESSION = EntityDataManager.<Integer>createKey(EntityHumanVillager.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> RACE = EntityDataManager.<Integer>createKey(EntityHumanVillager.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> GENDER = EntityDataManager.<Integer>createKey(EntityHumanVillager.class, DataSerializers.VARINT);
	private static final DataParameter<String> FIRST_NAME = EntityDataManager.<String>createKey(EntityHumanVillager.class, DataSerializers.STRING);
	private static final DataParameter<String> LAST_NAME = EntityDataManager.<String>createKey(EntityHumanVillager.class, DataSerializers.STRING);
	private static final DataParameter<Boolean> SWINGING_ARMS = EntityDataManager.<Boolean>createKey(EntityHumanVillager.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> BLOCKING = EntityDataManager.<Boolean>createKey(EntityHumanVillager.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Boolean> IS_AGGRESSIVE = EntityDataManager.<Boolean>createKey(EntityHumanVillager.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Boolean> IS_VAMPIRE = EntityDataManager.<Boolean>createKey(EntityHumanVillager.class, DataSerializers.BOOLEAN);

	protected static final UUID MODIFIER_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
    protected static final AttributeModifier MODIFIER = (new AttributeModifier(MODIFIER_UUID, "Drinking speed penalty", -0.25D, 0)).setSaved(false);
	
    protected int attackTimer;
    protected int coolDown = 0, coolDownDrinking = 0;
	private boolean isHealer = false;
	private int lifespan = -1;
	private boolean hasLifespan = false;
	
	private boolean canAttackWithPotions = false;

	private int healthChange = -1;
	private boolean nervous = false;
	private int combatType = 0;
	private int patrolType = 0;
	private boolean withDoors, useHomePosition;
	
	private boolean magicRanged = false;
	private boolean magicMelee = false;
	
	private boolean strafingRanged = false;
	protected boolean breaksBlocksOnCollide = false;
	private int reduction = 0;
	private boolean arrowProof = false;
	private int controlDuration = -1;
	private boolean usesLingering = false;
	
	protected Block targetBlock;
	private boolean ironFoot = false;
	private boolean tookArmorOff = false;
	ItemStack helmet;
	ItemStack chestplate;
	ItemStack leggings;
	ItemStack boots;
	

    
	public EntityHumanVillager(World worldIn) {
		super(worldIn);
	}
	
	public EntityHumanVillager(World worldIn, final int ID)
	{
		super(worldIn);
		
		this.race = AttributeRace.getFromIDRace((ID/2) % AttributeRace.races.size());
		this.gender = (ID/2) % (GENDER_FEMALE + 1);
		
		this.applyAttributes();
		this.applyEquipment();
		
		this.initEntityClass();
		this.initEntityCharacteristics();
		this.initEntityAI();
		
		this.initEntityName();
		this.initValues();
		this.setCombatTask();
	}
	
	public EntityHumanVillager(World worldIn, final int ID, final int vocation, final int rank, final int gender)
	{
		super(worldIn);
		
		this.race = AttributeRace.getFromIDRace(ID);
		this.gender = gender;
		this.initVocation(vocation, rank);
		this.initEntityCharacteristics();
		
		this.applyAttributes();
		this.applyEquipment();
		this.initEntityAI();
		
		this.initEntityName();
		this.initValues();
		this.setCombatTask();
		
	}
	
	public EntityHumanVillager(World worldIn, final int ID, final AttributeVocation vocation, final int gender, boolean shifter, EntityLiving creature)
	{
		super(worldIn);
		
		this.race = AttributeRace.getFromIDRace(ID);
		this.gender = gender;
		this.vocation = vocation;
		this.shifter = shifter;
		this.creatureShiftTo = creature;
		
		this.initEntityCharacteristics();
		
		this.applyAttributes();
		this.applyEquipment();
		this.initEntityAI();
		
		this.initEntityName();
		this.initValues();
		this.setCombatTask();
		
	}
	
	public EntityHumanVillager(World worldIn, final int ID, final AttributeVocation vocation, final int gender, final String firstName, final String lastName)
	{
		super(worldIn);
		
		this.race = AttributeRace.getFromIDRace(ID);
		this.gender = gender;
		this.vocation = vocation;
		this.firstName = firstName;
		this.lastName = lastName;
		
		this.applyAttributes();
		this.applyEquipment();
		this.initEntityAI();
		
		this.initEntityName();
		this.initValues();
		this.setCombatTask();
	}

	public EntityHumanVillager(World worldIn, final int ID, final AttributeVocation vocation, final int gender, final boolean ruler)
	{
		super(worldIn);
		this.race = AttributeRace.getFromIDRace(ID);
		this.gender = gender;
		this.vocation = vocation;
		this.ruler = ruler;
		
		this.applyAttributes();
		this.applyEquipment();	
		this.initEntityAI();
		this.initEntityCharacteristics();
		
		this.initEntityName();
		this.initValues();
		this.setCombatTask();
		
		if(ruler) this.createFaction();
	}
	
	public EntityHumanVillager(EntityHumanVillager toSpawn) {
		this(toSpawn.world, toSpawn.getRace().getID(), toSpawn.vocation, toSpawn.getGender(), false);
	}

	public void reInit(final int ID, final AttributeVocation vocation, final int gender, final String firstName, final String lastName)
	{
		this.race = AttributeRace.getFromIDRace(ID);
		this.gender = gender;
		this.vocation = vocation;
		this.firstName = firstName;
		this.lastName = lastName;
		
		this.applyAttributes();
		this.applyEquipment();
		this.initEntityAI();
		
		this.initEntityName();
		this.initValues();
		this.setCombatTask();
	}
	
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();

        getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        getAttributeMap().registerAttribute(AttributeRace.MAGIC_DAMAGE);
        getAttributeMap().registerAttribute(AttributeRace.HORSE_HEALTH);
    }
	
	private void initEntityClass() 
	{
		this.vocation = race.getRecruitVocation(AttributeVocation.CLASS_SOLDIER);		
	}
	
	protected void applyAttributes()
    {
		if(this.world.isRemote) return;
		this.quality = AttributeQuality.getRandomQuality(this.rand);
		
		int healthBonus = race.getHealthBonus();
		int attackBonus = race.getAttackBonus();
		int magicBonus = race.getMagicBonus();
		int detectBonus = race.getDetectBonus();
		int speedBonus = race.getSpeedBonus();
		int knockbackBonus = race.getKnockbackBonus();
		int horseHealth = race.getHorseHealthBonus();
		float scale = vocation.getScale();
		
		this.setScale(scale);
		
		int difficulty = 1;
		
		if(this.getRace().getType() == AttributeRace.RACE_TYPE_VAMPIRE || this.getVocation().getID() == AttributeVocation.CLASS_BANDIT)
		{
			difficulty = -1 * (this.world.getDifficulty().getDifficultyId() - 4);
		}
		
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D + (((healthBonus * vocation.getRank()) + this.vocation.getHealthOffest())/difficulty) + this.quality.getHealthBonus());
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D + (((speedBonus * vocation.getRank())/20) + this.vocation.getSpeedOffest()) + this.quality.getSpeedBonus()/32);
        getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue((knockbackBonus * (vocation.getRank() + this.vocation.getKnockBackOffest() + this.quality.getKnockbackBonus())/10));
        getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(0.2D);
        getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(32.0D);
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D + ((detectBonus * vocation.getRank()) + this.vocation.getDetectOffest()) +  this.quality.getDetectBonus());
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D + (((attackBonus * vocation.getRank()) + this.vocation.getDamageOffest())/difficulty) + this.quality.getAttackBonus());
        if(getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue() < 1) getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1);//4.0D + (((attackBonus * vocation.getRank()) + this.vocation.getDamageOffest())/difficulty) + this.quality.getAttackBonus());

        
        getEntityAttribute(AttributeRace.MAGIC_DAMAGE).setBaseValue(2.0D + (((magicBonus * vocation.getRank()) + this.vocation.getDamageOffest())/difficulty) + this.quality.getMagicBonus());
        getEntityAttribute(AttributeRace.HORSE_HEALTH).setBaseValue(2.0D + ((horseHealth * vocation.getRank())/difficulty) + this.quality.getHorseHealth());

        this.setHealth((float) getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue());
        
        this.factionNumber = this.race.getType();
        if(faction != null) this.factionNumber = this.faction.getFactionID();
        if(vocation.getType() == AttributeVocation.CLASS_BANDIT) this.factionNumber += 10;
        this.hostiles = AttributeFaction.getHostileID(race, vocation.getType() == AttributeVocation.CLASS_BANDIT);    
    }
	
	protected void applyEquipment()
	{
		if(vocation == null || this.world.isRemote) return;
		
		meleeWeapon = vocation.getMeleeWeapon();
		rangedWeapon = vocation.getRangedWeapon();
	
		this.additionalMagicSlots = vocation.usesAdditionalMagicSlots();
		this.additionalPotionSlots = vocation.usesAdditionalPotionSlots();

		
		if(rangedWeapon != null)
		{
			this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(rangedWeapon));
		}
		else
		{
			this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(meleeWeapon));
		}
		
		this.setHeldItem(EnumHand.OFF_HAND, new ItemStack(vocation.getShield()));
		
		this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(vocation.getHelmet()));
		this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(vocation.getChestplate()));
		this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(vocation.getLeggings()));
		this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(vocation.getBoots()));
		
		ARROW_POTIONS[A_PASSIVE_1] = vocation.getArrowPotions(AttributeVocation.PA_SLOT_PASSIVE_1);
		ARROW_POTIONS[A_PASSIVE_2] = vocation.getArrowPotions(AttributeVocation.PA_SLOT_PASSIVE_2);
		
		PASSIVE_POTIONS[PASSIVE_1] = vocation.getPotions(AttributeVocation.POTION_SLOT_PASSIVE_1);
		PASSIVE_POTIONS[PASSIVE_2] = vocation.getPotions(AttributeVocation.POTION_SLOT_PASSIVE_2);
		PASSIVE_POTIONS[PASSIVE_3] = vocation.getPotions(AttributeVocation.POTION_SLOT_PASSIVE_3);
		PASSIVE_POTIONS[PASSIVE_4] = vocation.getPotions(AttributeVocation.POTION_SLOT_PASSIVE_4);
		
		ACTIVE_POTIONS[ACTIVE_1] = vocation.getPotions(AttributeVocation.POTION_SLOT_ACTIVE_1);
		ACTIVE_POTIONS[ACTIVE_2] = vocation.getPotions(AttributeVocation.POTION_SLOT_ACTIVE_2);
		ACTIVE_POTIONS[ACTIVE_3] = vocation.getPotions(AttributeVocation.POTION_SLOT_ACTIVE_3);
		ACTIVE_POTIONS[ACTIVE_4] = vocation.getPotions(AttributeVocation.POTION_SLOT_ACTIVE_4);
		
		if(this.additionalPotionSlots)
		{
			PASSIVE_POTIONS[PASSIVE_5] = vocation.getPotions(AttributeVocation.POTION_SLOT_PASSIVE_5);
			PASSIVE_POTIONS[PASSIVE_6] = vocation.getPotions(AttributeVocation.POTION_SLOT_PASSIVE_6);
			PASSIVE_POTIONS[PASSIVE_7] = vocation.getPotions(AttributeVocation.POTION_SLOT_PASSIVE_7);
			PASSIVE_POTIONS[PASSIVE_8] = vocation.getPotions(AttributeVocation.POTION_SLOT_PASSIVE_8);
			ACTIVE_POTIONS[ACTIVE_5] = vocation.getPotions(AttributeVocation.POTION_SLOT_ACTIVE_5);
			ACTIVE_POTIONS[ACTIVE_6] = vocation.getPotions(AttributeVocation.POTION_SLOT_ACTIVE_6);
			ACTIVE_POTIONS[ACTIVE_7] = vocation.getPotions(AttributeVocation.POTION_SLOT_ACTIVE_7);
			ACTIVE_POTIONS[ACTIVE_8] = vocation.getPotions(AttributeVocation.POTION_SLOT_ACTIVE_8);
		}
		
		
		PASSIVE_SPELLS[S_PASSIVE_1] = vocation.getSpell(AttributeVocation.SPELL_SLOT_PASSIVE_1);
		PASSIVE_SPELLS[S_PASSIVE_2] = vocation.getSpell(AttributeVocation.SPELL_SLOT_PASSIVE_2);
		ACTIVE_SPELLS[S_ACTIVE_1] = vocation.getSpell(AttributeVocation.SPELL_SLOT_ACTIVE_1);
		ACTIVE_SPELLS[S_ACTIVE_2] = vocation.getSpell(AttributeVocation.SPELL_SLOT_ACTIVE_2);
		
		if(this.additionalMagicSlots)
		{
			PASSIVE_SPELLS[S_PASSIVE_3] = vocation.getSpell(AttributeVocation.SPELL_SLOT_PASSIVE_3);
			PASSIVE_SPELLS[S_PASSIVE_4] = vocation.getSpell(AttributeVocation.SPELL_SLOT_PASSIVE_4);
			ACTIVE_SPELLS[S_ACTIVE_3] = vocation.getSpell(AttributeVocation.SPELL_SLOT_ACTIVE_3);
			ACTIVE_SPELLS[S_ACTIVE_4] = vocation.getSpell(AttributeVocation.SPELL_SLOT_ACTIVE_4);
			
			//System.out.println("Has Additional Slots: "+ACTIVE_SPELLS[S_ACTIVE_3]+" | "+
			//ACTIVE_SPELLS[S_ACTIVE_4] );
		}
		
		/*for(int i = 0; i < ACTIVE_SPELLS.length; i++)
		{
			System.out.println("Active Spell: "+ACTIVE_SPELLS[i]);
		}*/
		
		thrownWeapon = vocation.getThrown();
		this.breaksBlocksOnCollide = vocation.getBreakBlocks();
		this.usesLingering  = vocation.isUsesLingering();
		
		this.isHealer = vocation.isHealer();
		
		/*if(isHealer)
		{
			System.out.println("Potion:"+PASSIVE_POTIONS[PASSIVE_1]);
		}*/
		
		if(PASSIVE_POTIONS[PASSIVE_1] != null || PASSIVE_POTIONS[PASSIVE_2] != null || PASSIVE_POTIONS[PASSIVE_3] != null || PASSIVE_POTIONS[PASSIVE_4] != null)
		{
			if(ACTIVE_POTIONS[ACTIVE_1] != null || ACTIVE_POTIONS[ACTIVE_2] != null || ACTIVE_POTIONS[ACTIVE_3] != null || ACTIVE_POTIONS[ACTIVE_4] != null) canAttackWithPotions  = true;
			canUsePotions  = true;
			//System.out.println("Use Potions");
		}
		if(PASSIVE_SPELLS[S_PASSIVE_1] != null || PASSIVE_SPELLS[S_PASSIVE_2] != null || ACTIVE_SPELLS[S_ACTIVE_1] != null || ACTIVE_SPELLS[S_ACTIVE_2] != null) isMagic  = true;
		
		this.setEnchantments();		
		//Reset equipment for next guy
		vocation.setEquipment();
	}
	
	public void setEnchantments()
	{
		{
			Enchantment meleeEnchant = vocation.getSwordEnchantments(AttributeVocation.SWORD_ENCHANT_1);
			if(vocation.getSwordEnchantments(AttributeVocation.SWORD_ENCHANT_2) != null && this.getRNG().nextInt(100) < 50) vocation.getSwordEnchantments(AttributeVocation.SWORD_ENCHANT_2);
			
			if(this.getRNG().nextInt(100) < (this.vocation.getEnchantmentChance() + this.quality.getEnchantmentBonus()) && meleeEnchant != null) 
			{
				if(meleeWeapon != null && meleeWeapon.canApplyAtEnchantingTable(this.getHeldItemMainhand(), meleeEnchant))
				{
					this.getHeldItemMainhand().addEnchantment(meleeEnchant, vocation.getRank());
				}
			}
		}
		
		{
			Enchantment bowEnchant = vocation.getSwordEnchantments(AttributeVocation.BOW_ENCHANT_1);
			if(vocation.getBowEnchantments(AttributeVocation.BOW_ENCHANT_2) != null && this.getRNG().nextInt(100) < 50) vocation.getBowEnchantments(AttributeVocation.BOW_ENCHANT_2);
			
			if(this.getRNG().nextInt(100) < (this.vocation.getEnchantmentChance() + this.quality.getEnchantmentBonus()) && bowEnchant != null) 
			{
				if(rangedWeapon != null && rangedWeapon.canApplyAtEnchantingTable(this.getHeldItemMainhand(), bowEnchant))
				{
					this.getHeldItemMainhand().addEnchantment(bowEnchant, vocation.getRank());
				}
			}
		}
		
		{
			Enchantment headEnchant = vocation.getHeadEnchantments(AttributeVocation.HEAD_ENCHANT_1);
			if(vocation.getHeadEnchantments(AttributeVocation.HEAD_ENCHANT_2) != null && this.getRNG().nextInt(100) < 50) vocation.getHeadEnchantments(AttributeVocation.HEAD_ENCHANT_2);
			
			if(this.getRNG().nextInt(100) < (this.vocation.getEnchantmentChance() + this.quality.getEnchantmentBonus()) && headEnchant != null) 
			{
				if(this.getItemStackFromSlot(EntityEquipmentSlot.HEAD) != null 
						&& this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem().canApplyAtEnchantingTable(this.getItemStackFromSlot(EntityEquipmentSlot.HEAD), headEnchant))
				{
					this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).addEnchantment(headEnchant, vocation.getRank());
				}
			}
		}
		
		{
			Enchantment chestEnchant = vocation.getChestEnchantments(AttributeVocation.CHEST_ENCHANT_1);
			if(vocation.getChestEnchantments(AttributeVocation.CHEST_ENCHANT_2) != null && this.getRNG().nextInt(100) < 50) vocation.getChestEnchantments(AttributeVocation.CHEST_ENCHANT_2);
			
			if(this.getRNG().nextInt(100) < (this.vocation.getEnchantmentChance() + this.quality.getEnchantmentBonus()) && chestEnchant != null) 
			{
				if(this.getItemStackFromSlot(EntityEquipmentSlot.CHEST) != null
						&& this.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem().canApplyAtEnchantingTable(this.getItemStackFromSlot(EntityEquipmentSlot.CHEST), chestEnchant))
				{
					this.getItemStackFromSlot(EntityEquipmentSlot.CHEST).addEnchantment(chestEnchant, vocation.getRank());
				}
			}
		}
	
		{
			Enchantment legsEnchant = vocation.getLegsEnchantments(AttributeVocation.LEGS_ENCHANT_1);
			if(vocation.getLegsEnchantments(AttributeVocation.LEGS_ENCHANT_2) != null && this.getRNG().nextInt(100) < 50) vocation.getLegsEnchantments(AttributeVocation.LEGS_ENCHANT_2);
			
			if(this.getRNG().nextInt(100) < (this.vocation.getEnchantmentChance() + this.quality.getEnchantmentBonus()) && legsEnchant != null) 
			{
				if(this.getItemStackFromSlot(EntityEquipmentSlot.LEGS) != null
						&& this.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem().canApplyAtEnchantingTable(this.getItemStackFromSlot(EntityEquipmentSlot.LEGS), legsEnchant))
				{
					this.getItemStackFromSlot(EntityEquipmentSlot.LEGS).addEnchantment(legsEnchant, vocation.getRank());
				}
			}
		}
		
		{
			Enchantment bootsEnchant = vocation.getBootsEnchantments(AttributeVocation.BOOTS_ENCHANT_1);
			if(vocation.getBootsEnchantments(AttributeVocation.BOOTS_ENCHANT_2) != null && this.getRNG().nextInt(100) < 50) vocation.getBootsEnchantments(AttributeVocation.BOOTS_ENCHANT_2);
			
			if(this.getRNG().nextInt(100) < (this.vocation.getEnchantmentChance() + this.quality.getEnchantmentBonus()) && bootsEnchant != null) 
			{
				if(this.getItemStackFromSlot(EntityEquipmentSlot.FEET) != null
						&& this.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem().canApplyAtEnchantingTable(this.getItemStackFromSlot(EntityEquipmentSlot.FEET), bootsEnchant))
				{
					this.getItemStackFromSlot(EntityEquipmentSlot.FEET).addEnchantment(bootsEnchant, vocation.getRank());
				}
			}
		}
	}
	
	public void attackWithThrown(EntityLivingBase target, float distanceFactor, ItemWeaponThrowable thrown)
	{
		double d0 = target.posY + (double)target.getEyeHeight() - 1.100000023841858D;
        double d1 = target.posX + target.motionX - this.posX;
        double d2 = d0 - this.posY;
        double d3 = target.posZ + target.motionZ - this.posZ;
        float f = MathHelper.sqrt(d1 * d1 + d3 * d3);
            
	    EntityWeaponThrowable entitypotion = new EntityWeaponThrowable(world, this, thrown.getWeapon(), 0);
	    entitypotion.rotationPitch -= -20.0F;
	    entitypotion.shoot(d1, d2 + (double)(f * 0.2F), d3, 0.75F, 8.0F);
	    this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_THROW, this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
	    this.world.spawnEntity(entitypotion);
		
	}
	
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		if(!this.world.isRemote)
			if (this.world != null)
	        {
	            ItemStack itemstack = this.getHeldItemMainhand();
	
	            if (itemstack.getItem() == Items.BOW)
	            {
	                this.attackWithBow(target, distanceFactor);
	                if(this.magicRanged) this.forceCombatTask(0);
	            }
	        }
		
			if (!this.isDrinkingPotion() && this.canAttackWithPotions)
	        {
	            double d0 = target.posY + (double)target.getEyeHeight() - 1.100000023841858D;
	            double d1 = target.posX + target.motionX - this.posX;
	            double d2 = d0 - this.posY;
	            double d3 = target.posZ + target.motionZ - this.posZ;
	            float f = MathHelper.sqrt(d1 * d1 + d3 * d3);
	            boolean isLingering = world.rand.nextInt(100) < 50 && this.usesLingering;
	            
	            
	            boolean undeadEffective = false;
	            boolean spiderEffective = false;
	          
	            PotionType potiontype = getEffectivePotion(target); 
	            
	            /*if(world.rand.nextInt(100) < 50 && ACTIVE_2 != null) potiontype = ACTIVE_2;
	            if(world.rand.nextInt(100) < 50 && ACTIVE_3 != null) potiontype = ACTIVE_3;
	            if(world.rand.nextInt(100) < 50 && ACTIVE_4 != null) potiontype = ACTIVE_4;
	           
	            if(this.additionalPotionSlots)
	            {
		            if(world.rand.nextInt(100) < 50 && ACTIVE_5 != null) potiontype = ACTIVE_5;
		            if(world.rand.nextInt(100) < 50 && ACTIVE_6 != null) potiontype = ACTIVE_6;
		            if(world.rand.nextInt(100) < 50 && ACTIVE_7 != null) potiontype = ACTIVE_7;
		            if(world.rand.nextInt(100) < 50 && ACTIVE_8 != null) potiontype = ACTIVE_8;
	            }
	            
	            
	            for (PotionEffect potioneffect : potiontype.getEffects())
	            {
	            	if(targetUndead && (potioneffect.getPotion() == MobEffects.INSTANT_HEALTH)) undeadEffective = true;
	            	else if(targetSpider && (potioneffect.getPotion() != MobEffects.POISON)) spiderEffective = true;
	            }*/
	            
	            if (potiontype != null)
	            {
		            EntityPotion entitypotion = new EntityPotion(this.world, this, PotionUtils.addPotionToItemStack(isLingering ? new ItemStack(Items.SPLASH_POTION) : new ItemStack(Items.LINGERING_POTION), potiontype));
		            entitypotion.rotationPitch -= -20.0F;
		            entitypotion.shoot(d1, d2 + (double)(f * 0.2F), d3, 0.75F, 8.0F);
		            this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_THROW, this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
		            this.world.spawnEntity(entitypotion);
	            }
	        }
		}
	
	private void goHome()
	{
		BlockPos home = this.getHomePosition();
		
		if(home != null)
		{
			BlockPos blockpos = home;
			
			int i = blockpos.getX();
            int j = blockpos.getY();
            int k = blockpos.getZ();
            
			if (this.getDistanceSq(blockpos) > 256.0D)
            {
                Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this, 14, 3, new Vec3d((double)i + 0.5D, (double)j, (double)k + 0.5D));

                if (vec3d != null)
                {
                    this.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 1.0D);
                }
            }
            else
            {
                this.getNavigator().tryMoveToXYZ((double)i + 0.5D, (double)j, (double)k + 0.5D, 1.0D);
            }
		}
		else
		{
			Village village = this.getVillage();
	
	    	BlockPos blockpos = new BlockPos(this);
	    	
	        if (village != null)
	        
	        {
	            VillageDoorInfo doorInfo = village.getDoorInfo(blockpos);
	            
	            if(doorInfo != null)
	            {
	            	BlockPos targetPos = doorInfo.getInsideBlockPos();
	            	
	            	int i = blockpos.getX();
	                int j = blockpos.getY();
	                int k = blockpos.getZ();
	
	                if (this.getDistanceSq(blockpos) > 256.0D)
	                {
	                    Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this, 14, 3, new Vec3d((double)i + 0.5D, (double)j, (double)k + 0.5D));
	
	                    if (vec3d != null)
	                    {
	                        this.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 1.0D);
	                    }
	                }
	                else
	                {
	                    this.getNavigator().tryMoveToXYZ((double)i + 0.5D, (double)j, (double)k + 0.5D, 1.0D);
	                }
	            }
	        }
	        else
	        {      
	        	return;
	        }
		}
	}
		
	private PotionType getEffectivePotion(EntityLivingBase target) {
		// TODO Auto-generated method stub
		boolean targetUndead = target.isEntityUndead();
        boolean targetSpider = target.getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD;
        PotionType potion = ACTIVE_POTIONS[ACTIVE_1];
        int i = ACTIVE_1;
        
        for(PotionType p : ACTIVE_POTIONS)
        {
        	if(p == null) continue;
        	
	        for (PotionEffect potioneffect : p.getEffects())
	        {
	        	if(target.getActivePotionEffect(potioneffect.getPotion()) != null) continue;

	        	//System.out.println("Target Undead? "+targetUndead);
	        	if(targetUndead && (potioneffect.getPotion() == MobEffects.INSTANT_HEALTH || potioneffect.getPotion() == MobEffects.SLOWNESS || potioneffect.getPotion() == MobEffects.WEAKNESS))
	        	{
	        		return p;
	        	}
	        	else if(targetUndead)
	        	{
	        		this.goHome();
	        		return null;
	        	}
	        	
	        	if(targetSpider && (potioneffect.getPotion() != MobEffects.POISON))
	        	{
	        		return p;
	        	}
	        	else if(targetSpider)
	        	{
	        		this.goHome();
	        		return null;
	        	}
	    
	        	if(target.getHealth() > target.getMaxHealth() / 2 && potioneffect.getPotion() == MobEffects.POISON) return p;
	        	if(this.getRNG().nextInt(100) < 50)
	 	        {
	 	    	   if(!this.additionalPotionSlots && i > ACTIVE_4) break;
	 	    	   potion = p;
	 	        }
	        }
	        
	       i++;
        }
		
        
		return potion;
	}
	
	protected void attackWithBow(EntityLivingBase target,
			float distanceFactor)
	{
		
		EntityArrow entityarrow = this.getArrow(distanceFactor);
		entityarrow.setDamage((float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
		entityarrow.setIsCritical(true);
		
       double d0 = target.posX - this.posX;
       double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - entityarrow.posY;
       double d2 = target.posZ - this.posZ;
       double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
       entityarrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float)(14 - this.world.getDifficulty().getDifficultyId() * 4));
      
       this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
       this.world.spawnEntity(entityarrow);
	}
	
	protected EntityArrow getArrow(float p_190726_1_) {
		if(this.magicRanged)
		{
			EntitySpectralArrow entityspectralarrow = new EntitySpectralArrow(this.world, this);
            entityspectralarrow.setEnchantmentEffectsFromEntity(this, p_190726_1_);
            return entityspectralarrow;
		}
		
		ItemStack itemstack = new ItemStack(Items.TIPPED_ARROW);
		PotionType potion = ARROW_POTIONS[A_PASSIVE_1];
		EntityTippedArrow entitytippedarrow = new EntityTippedArrow(this.world, this);
		
		if(ARROW_POTIONS[A_PASSIVE_2] != null && this.getRNG().nextInt(100) < 50) potion = ARROW_POTIONS[A_PASSIVE_2];

		if(potion != null)
		{
	        PotionUtils.addPotionToItemStack(itemstack, potion);
	        PotionUtils.appendEffects(itemstack, potion.getEffects());
			entitytippedarrow.setPotionEffect(itemstack);
		}
		
        entitytippedarrow.setEnchantmentEffectsFromEntity(this, p_190726_1_);
        return entitytippedarrow;
	}

	public void healEntityWithRangedAttack(EntityLivingBase target, float lvt_5_1_) {
		if(coolDown > 0)
		{
			coolDown--;
		}
		else
		{
			 if (!this.isDrinkingPotion())
		        {
		            double d0 = target.posY + (double)target.getEyeHeight() - 1.100000023841858D;
		            double d1 = target.posX + target.motionX - this.posX;
		            double d2 = d0 - this.posY;
		            double d3 = target.posZ + target.motionZ - this.posZ;
		            float f = MathHelper.sqrt(d1 * d1 + d3 * d3);
		            boolean isLingering = world.rand.nextInt(100) < 50 && this.usesLingering;
		            
		            PotionType potiontype = getEffectiveHealing(target);
		            
		            if(potiontype == null) return;
		            
		            EntityPotion entitypotion = new EntityPotion(this.world, this, PotionUtils.addPotionToItemStack(isLingering ? new ItemStack(Items.SPLASH_POTION) : new ItemStack(Items.LINGERING_POTION), potiontype));
		            entitypotion.rotationPitch -= -20.0F;
		            entitypotion.shoot(d1, d2 + (double)(f * 0.2F), d3, 0.75F, 8.0F);
		            this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_THROW, this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
		            this.world.spawnEntity(entitypotion);

	                coolDown = 10;
		        }
			
		}
	}

	private PotionType getEffectiveHealing(EntityLivingBase target) {
		PotionType potion = null;
		int i = PASSIVE_1;
		
		for(PotionType p : PASSIVE_POTIONS)
        {
        	if(p == null) continue;
        	
	        for (PotionEffect potioneffect : p.getEffects())
	        {
	        	
	        	if(target.getActivePotionEffect(potioneffect.getPotion()) != null) continue;
	        	else if(target.isBurning() && potioneffect.getPotion() == MobEffects.FIRE_RESISTANCE) return p;
	        	else if(target.getHealth() > target.getMaxHealth() / 2 && potioneffect.getPotion() == MobEffects.REGENERATION) return p;
	        	else if(potioneffect.getPotion() == MobEffects.INSTANT_HEALTH)
	        	{
	        		if(target.getHealth() < target.getMaxHealth() / 2) return p;
	        	}
	        	else
	        	{
		        	if(this.getRNG().nextInt(100) < 50)
		 	        {
		 	    	   if(!this.additionalPotionSlots && i > ACTIVE_4) break;
		 	    	   potion = p;
		 	    	   //System.out.println("Effect Healing: "+potioneffect.getEffectName());
		 	        }
	        	}
	        }
	       
	       i++;
        }
		
		return potion;
	}

	public void attackEntityWithMagicAttack(EntityLivingBase attackTarget, float f1) 
	{
		//System.out.println("Attacking");
		if(!world.isRemote)
		{
			Spell usedSpell = ACTIVE_SPELLS[S_ACTIVE_1];
			
			if(ACTIVE_SPELLS[S_ACTIVE_2] !=  null && world.rand.nextInt(100) < 50) usedSpell = ACTIVE_SPELLS[S_ACTIVE_2];
			
			if(this.additionalMagicSlots)
			{
				if(ACTIVE_SPELLS[S_ACTIVE_3] !=  null && world.rand.nextInt(100) < 50) usedSpell = ACTIVE_SPELLS[S_ACTIVE_3];
				if(ACTIVE_SPELLS[S_ACTIVE_4] !=  null && world.rand.nextInt(100) < 50) usedSpell = ACTIVE_SPELLS[S_ACTIVE_4];
			}
			
			if(usedSpell != null)
			{
				usedSpell.execute(this);
			}
		
            if(this.magicRanged) this.forceCombatTask(2);
		}
	}
	
	public void drinkPotion()
	{
		if (!this.world.isRemote && this.canUsePotions)
        {
        	//this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.POTIONITEM));
            if (this.isDrinkingPotion())
            {
                if (this.attackTimer-- <= 0)
                {
                    this.setAggressive(false);
                    ItemStack itemstack = this.getHeldItemMainhand();
                    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, held);

                    if (itemstack.getItem() == Items.POTIONITEM)
                    {
                        List<PotionEffect> list = PotionUtils.getEffectsFromStack(itemstack);

                        if (list != null)
                        {
                            for (PotionEffect potioneffect : list)
                            {
                           // 	System.out.println("Effect: "+potioneffect.getEffectName());
                                this.addPotionEffect(new PotionEffect(potioneffect));
                                
                                if(this.horse != null)
                                {
                                	this.horse.addPotionEffect(new PotionEffect(potioneffect));
                                }
                            }
                        }
                    }

                    this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(MODIFIER);
                    coolDownDrinking = 160 / this.vocation.getRank();
                }
            }
            else 
            {
            	PotionType potiontype = this.getEffectiveHealing(this);
            	
            	if(potiontype != null && coolDownDrinking <= 0)
                {
                	held = this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
                	
                	this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), potiontype));
	                this.attackTimer = this.getHeldItemMainhand().getMaxItemUseDuration();
	                this.setAggressive(true);
	                this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_DRINK, this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
	                IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
	                iattributeinstance.removeModifier(MODIFIER);
	                iattributeinstance.applyModifier(MODIFIER);
                }
            	else if(coolDownDrinking > 0)
            	{
            		coolDownDrinking--;
            	}
            	
            	/*if(((this.getHealth() < this.getMaxHealth() && !this.inCombat()) || this.getHealth() <= this.getMaxHealth()/4) && coolDownDrinking <= 0)
            	{
	                PotionType potiontype = PASSIVE_1;
	                
	                if(this.rand.nextInt(100) < 1/8 && PASSIVE_2 != null) potiontype = PASSIVE_2;// && !this.isPotionActive(MobEffects.REGENERATION))
	                if(this.rand.nextInt(100) < 2/8 && PASSIVE_3 != null) potiontype = PASSIVE_3;// && !this.isPotionActive(MobEffects.REGENERATION))
	                if(this.rand.nextInt(100) < 3/8 && PASSIVE_4 != null) potiontype = PASSIVE_4;// && !this.isPotionActive(MobEffects.REGENERATION))
	               
	                if(this.additionalPotionSlots)
	                {
		                if(this.rand.nextInt(100) < 4/8 && PASSIVE_5 != null) potiontype = PASSIVE_5;// && !this.isPotionActive(MobEffects.REGENERATION))
		                if(this.rand.nextInt(100) < 5/8 && PASSIVE_6 != null) potiontype = PASSIVE_6;// && !this.isPotionActive(MobEffects.REGENERATION))
		                if(this.rand.nextInt(100) < 6/8 && PASSIVE_7 != null) potiontype = PASSIVE_7;// && !this.isPotionActive(MobEffects.REGENERATION))
		                if(this.rand.nextInt(100) < 7/8 && PASSIVE_8 != null) potiontype = PASSIVE_8;// && !this.isPotionActive(MobEffects.REGENERATION))
	                }
	                
	                
            	}
            	else if(coolDownDrinking > 0)
            	{
            		coolDownDrinking--;
            	}*/
            }

            if (this.rand.nextFloat() < 7.5E-4F)
            {
                this.world.setEntityState(this, (byte)15);
            }
        }
	}

	public void setRidingHorse(AbstractHorse horse) {
		if(!this.world.isRemote)
		{
	    	this.horse = horse;
	        this.horse.setPosition((double)this.posX, (double)this.posY, (double)this.posZ);    
			
	        if(!this.horse.isTame())
	        {
		        this.horse.setHorseTamed(true);
				this.horse.setGrowingAge(0);
				this.horse.enablePersistence();
				this.horse.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.horse.getMaxHealth() + this.getEntityAttribute(AttributeRace.HORSE_HEALTH).getBaseValue());
				this.horse.heal(1000);
				//this.getCollisionBox(entityIn);
				//System.out.println("2 - Width and Height: "+this.width+" "+this.height);
				this.horse.replaceItemInInventory(400, new ItemStack(Items.SADDLE));
			//	System.out.println("Success: "+success);
				
				if(this.horse instanceof EntityHorse)
				{
					EntityHorse armorHorse = (EntityHorse)this.horse;
					Item armor = this.vocation.getHorseArmor();
					armorHorse.setHorseArmorStack(new ItemStack(armor));
					this.horse.replaceItemInInventory(401, new ItemStack(armor));
					//System.out.println("Success: "+success);
				}
				
				//this.horse.sets
				this.horse.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D + this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue());
	        }
	        
	        this.horseWidth = this.horse.width;
			this.horseHeight = this.horse.height;
			//System.out.println("1 - Width and Height: "+this.width+" "+this.height);
			this.setSize2(this.width + this.horseWidth, this.height + this.horseHeight);
			
			
			//System.out.println("");
			
			if(this.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.BOW)
			{
	        	this.tasks.removeTask(this.ranged);
	        	this.tasks.addTask(1, this.rangedTrained);
			}
			
	    	this.startRiding(this.horse);
		}
	}
	
	
	protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier)
    {
		if(!world.isRemote)
		{
			//System.out.println("Dropping");
	        for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values())
	        {
	            ItemStack itemstack = this.getItemStackFromSlot(entityequipmentslot);
	
	            if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack) && this.rand.nextInt(100) < 25)
	            {
	                if (itemstack.isItemStackDamageable())
	                {
	                    itemstack.setItemDamage(itemstack.getMaxDamage() - this.rand.nextInt(1 + this.rand.nextInt(Math.max(itemstack.getMaxDamage() - 3, 1))));
	                }
	
	                this.entityDropItem(itemstack, 0.0F);
	            }
	        }
	        
	        int count = rand.nextInt(5);
	        
	        for(int i = 0; i < count; i++)
	        {
	        	ItemStack itemstack = new ItemStack(Items.EMERALD);
	        	this.entityDropItem(itemstack, 0.0F);
	        }
		}
    }
	
	@Override
	protected void initEntityAI()
	{
		if(vocation == null || this.world.isRemote) return;
		//this.getVillage();
		melee = new EntityAIAttackWithMelee(this, 1.0, true);
		ranged = new EntityAIAttackRangedMod(this, 1.0D, 60,  (float)getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue() + 16.0F);
		rangedTrained = new EntityAIAttackWithBow(this, 1.0D, 60, 10.0F);
		block = new EntityAIBlock(this, 2.0D, this.vocation.isAlwaysBlocking());
		heal = new EntityAIHealAllies(this, 1.0D, 60, 10.0F, EntityVillager.class);
        potions = new EntityAIAttackRanged(this, 1.0D, 60, 10.0F);
        spells =  new  EntityAIAttackWithMagic(this, 1.0D, 20, (float) this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getBaseValue(), 6.0f);  
        farm = new EntityAIHarvestFarmland(this, 0.6D);
		patrolShift = vocation.getType() == AttributeVocation.CLASS_BANDIT ? this.world.rand.nextInt(100) < 75 : this.world.rand.nextInt(100) < 25;
        patrolType = this.world.rand.nextInt(2);
        withDoors = this.world.rand.nextInt(2) == 0;
        boolean useHomePosition = this.world.rand.nextInt(2) == 0;
        
        this.targetBlock = vocation.getTargetBlock();
        
        if(this.race.getType() == AttributeRace.RACE_TYPE_VAMPIRE)
        {
        	if(!this.isVampire()) this.setVampire(true);
        		
        	this.tasks.addTask(1, new EntityAISwimming(this));
            this.tasks.addTask(2, new EntityAIRestrictSun(this));
            this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
	        this.tasks.addTask(4, new EntityAIRestrictOpenDoor(this));
	        this.tasks.addTask(5, new EntityAIOpenDoor(this, true));
            this.tasks.addTask(6, new EntityAIMoveTowardsRestriction(this, 1.0D));
            this.tasks.addTask(7, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
            this.tasks.addTask(8, new EntityAIWanderAvoidWater(this, 1.0D));
            this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
            this.tasks.addTask(10, new EntityAILookIdle(this));
	        
	        this.targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, EntityLiving.class, 1, true, true, new Predicate<EntityLiving>()
	        {
	            public boolean apply(@Nullable EntityLiving p_apply_1_)
	            {
	            	if(p_apply_1_ instanceof EntityHumanVillager)
	            	{
	            		EntityHumanVillager villager = (EntityHumanVillager) p_apply_1_;
	            		
	            		return isHostileFaction(villager);
	            	}
	            	
	        		return p_apply_1_ != null && ((IMob.VISIBLE_MOB_SELECTOR.apply(p_apply_1_) || (p_apply_1_ instanceof EntityVillager)) && !(p_apply_1_ instanceof EntityCreeper)) && !(p_apply_1_ instanceof EntityWarg);
	            }
	        }));
	        this.targetTasks.addTask(1, new EntityAIAttackBackExclude(this, true, new Class[0])); 
	        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
			this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityGolem.class, true));
	
	        if(vocation.getType() == AttributeVocation.CLASS_HYBRID_MAGE_SOLDER)
	        {
		        this.magicMelee = true;
	        }
	        
	        this.destructive = true;
        }
        else
        {
			switch(vocation.getType())
			{
				case AttributeVocation.CLASS_SOLDIER:
				{
					if(patrolType == 1) this.tasks.addTask(3, new EntityAIPatrolEntireVillage(this, 0.6D, patrolShift, withDoors, useHomePosition));
					else this.tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6D, patrolShift));
	
					this.tasks.addTask(0, new EntityAISwimming(this));
			       	this.tasks.addTask(1, new EntityAIStayInBorders(this, 1.0D));
			        this.tasks.addTask(2, new EntityAIRestrictOpenDoor(this));
			        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
			        this.tasks.addTask(5, new EntityAIHideFromHarm(this));
			        this.tasks.addTask(6, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
			        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 0.6D));
			        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
			        this.tasks.addTask(9, new EntityAILookIdle(this));
			        
			        this.defaultTargets();
				}
				break;
				case AttributeVocation.CLASS_ARCHER:
				{
					if(patrolType == 1) this.tasks.addTask(3, new EntityAIPatrolEntireVillage(this, 0.6D, patrolShift, withDoors, useHomePosition));
					else this.tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6D, patrolShift));
	
					this.tasks.addTask(0, new EntityAISwimming(this));
			        this.tasks.addTask(1, new EntityAIRest(this, true));
			        this.tasks.addTask(2, new EntityAIStayInBorders(this, 1.0D));
			        this.tasks.addTask(4, new EntityAIRestrictOpenDoor(this));
			        this.tasks.addTask(5, new EntityAIOpenDoor(this, true));
			        this.tasks.addTask(6, new EntityAIHideFromHarm(this));
			        this.tasks.addTask(7, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
			        this.tasks.addTask(8, new EntityAIWanderAvoidWater(this, 0.6D));
			        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
			        this.tasks.addTask(10, new EntityAILookIdle(this));
			        
			        this.defaultTargets();
				}
				break;
				case AttributeVocation.CLASS_MAGE:
				{
					if(patrolType == 1) this.tasks.addTask(3, new EntityAIPatrolEntireVillage(this, 0.6D, patrolShift, withDoors, useHomePosition));
					else this.tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6D, patrolShift));
	
					this.tasks.addTask(0, new EntityAISwimming(this));
			    	this.tasks.addTask(1, new EntityAIStayInBorders(this, 1.0D));
			      	this.tasks.addTask(2, new EntityAIRestrictOpenDoor(this));
			        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
			        this.tasks.addTask(5, new EntityAIHideFromHarm(this));
			        this.tasks.addTask(6, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
			        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 0.6D));
			        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
			        this.tasks.addTask(9, new EntityAILookIdle(this));
			        
			        this.defaultTargets();
				}
				break;
				case AttributeVocation.CLASS_ALCHEMIST:
				{
					if(patrolType == 1) this.tasks.addTask(3, new EntityAIPatrolEntireVillage(this, 0.6D, patrolShift, withDoors, useHomePosition));
					else this.tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6D, patrolShift));
	
					this.tasks.addTask(0, new EntityAISwimming(this));
			        this.tasks.addTask(1, new EntityAIStayInBorders(this, 1.0D));
			        this.tasks.addTask(2, new EntityAIRestrictOpenDoor(this));
			        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
			        this.tasks.addTask(5, new EntityAIHideFromHarm(this));
			        this.tasks.addTask(6, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
			        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 0.6D));
			        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
			        this.tasks.addTask(9, new EntityAILookIdle(this));
			        
			        this.defaultTargets();
				}
				break;
				case AttributeVocation.CLASS_VILLAGER:
				{
					int subType = vocation.getSubType();
					
					this.tasks.addTask(0, new EntityAISwimming(this));
			        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
			        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityEvoker.class, 12.0F, 0.8D, 0.8D));
			        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityVindicator.class, 8.0F, 0.8D, 0.8D));
			        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityVex.class, 8.0F, 0.6D, 0.6D));
			        this.tasks.addTask(1, new EntityAITradePlayer(this));
			        this.tasks.addTask(1, new EntityAILookAtTradePlayer(this));
			        this.tasks.addTask(2, new EntityAIMoveIndoors(this));
			        this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
			        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
			        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
			       	this.tasks.addTask(7, new EntityAIFollowGolem(this));
			        this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
			        this.tasks.addTask(9, new EntityAIVillagerInteract(this));
			        this.tasks.addTask(9, new EntityAIWanderAvoidWater(this, 0.6D));
			        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
	
			        this.targetTasks.addTask(1, new EntityAIAttackBackExclude(this, true, new Class[0])); 
			        
			        switch(subType)
			        {
				        case AttributeVocation.SUBCLASS_FARMER:
				        {
				        	this.tasks.addTask(6, farm);
				        }
				        break;
				        case AttributeVocation.SUBCLASS_CRAFTER:
				        {
				        	
				        }
				        break;
				        case AttributeVocation.SUBCLASS_MERCHANT:
				        {
				        }
				        break;
				        case AttributeVocation.SUBCLASS_TRAINER:
				        {
				        	
				        }
				        break;
				        case AttributeVocation.SUBCLASS_WORKER:
				        {
				        	if(this.targetBlock !=  null)
				        	{
				        		if(this.targetBlock == Blocks.WATER)
				        		{
				        		}
				        	}
				        }
				        break;
				        case AttributeVocation.SUBCLASS_HUNTER:
				        {
					        this.targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, EntityLiving.class, 1, true, true, new Predicate<EntityLiving>()
					        {
					            public boolean apply(@Nullable EntityLiving p_apply_1_)
					            {
					            	if(p_apply_1_ instanceof EntityHumanVillager)
					            	{
					            		return false;
					            	}
					            	
					        		return p_apply_1_ != null && (p_apply_1_ instanceof IAnimals) && !(p_apply_1_ instanceof EntityTameable) && !(p_apply_1_ instanceof AbstractHorse);
					            }
					        }));
					        this.targetTasks.addTask(1, new EntityAIAttackBackExclude(this, true, new Class[0])); 
				        }
				        break;
			        }
				}
				break;
				case AttributeVocation.CLASS_BANDIT:
				{
					if(patrolType == 1) this.tasks.addTask(3, new EntityAIPatrolEntireVillage(this, 0.6D, patrolShift, withDoors, useHomePosition));
					else this.tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6D, patrolShift));
	
					this.tasks.addTask(0, new EntityAISwimming(this));
			        this.tasks.addTask(1, new EntityAIStayInBorders(this, 1.0D));
			        this.tasks.addTask(2, new EntityAIRestrictOpenDoor(this));
			        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
			        this.tasks.addTask(5, new EntityAIHideFromHarm(this));
			        this.tasks.addTask(6, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
			        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 0.6D));
			        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
			        this.tasks.addTask(9, new EntityAILookIdle(this));
			        
			        this.targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, EntityLiving.class, 1, true, true, new Predicate<EntityLiving>()
			        {
			            public boolean apply(@Nullable EntityLiving p_apply_1_)
			            {
			            	if(p_apply_1_ instanceof EntityHumanVillager)
			            	{
			            		EntityHumanVillager villager = (EntityHumanVillager) p_apply_1_;
			            	
			            		return isHostileFaction(villager);
			            	}
			            	
			        		return p_apply_1_ != null && ((IMob.VISIBLE_MOB_SELECTOR.apply(p_apply_1_) || (p_apply_1_ instanceof EntityVillager)) && !(p_apply_1_ instanceof EntityCreeper)) && !(p_apply_1_ instanceof EntityWarg);
			            }
			        }));
			        this.targetTasks.addTask(1, new EntityAIAttackBackExclude(this, true, new Class[0])); 
			        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
					this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityGolem.class, true));
					
			        this.destructive = true;
				}
				break;
				case AttributeVocation.CLASS_RULER:
				{
					this.tasks.addTask(0, new EntityAISwimming(this));
			        this.tasks.addTask(1, new EntityAIStayInBorders(this, 1.0D));
			        this.tasks.addTask(2, new EntityAIRestrictOpenDoor(this));
			        this.tasks.addTask(3, new EntityAIOpenDoor(this, true));
			        this.tasks.addTask(4, new EntityAIHideFromHarm(this));
			        this.tasks.addTask(5, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
			        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.6D));
			        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
			        this.tasks.addTask(8, new EntityAILookIdle(this));
			        
			        this.defaultTargets();
			        
				}
				break;
				case AttributeVocation.CLASS_MERCENARY:
				{
					this.tasks.addTask(0, new EntityAISwimming(this));
			        this.tasks.addTask(1, new EntityAIRestrictOpenDoor(this));
			        this.tasks.addTask(2, new EntityAIOpenDoor(this, true));
			        this.tasks.addTask(3, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
			        this.tasks.addTask(4, new EntityAIWanderAvoidWater(this, 0.6D));
			        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
			        this.tasks.addTask(6, new EntityAILookIdle(this));
			        
			        this.defaultTargets();
				}
				break;
				case AttributeVocation.CLASS_HYBRID_MAGE_SOLDER:
				{
					if(patrolType == 1) this.tasks.addTask(3, new EntityAIPatrolEntireVillage(this, 0.6D, patrolShift, withDoors, useHomePosition));
					else this.tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6D, patrolShift));
	
					this.tasks.addTask(0, new EntityAISwimming(this));
			        this.tasks.addTask(1, new EntityAIStayInBorders(this, 1.0D));
			        this.tasks.addTask(2, new EntityAIRestrictOpenDoor(this));
			        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
			        this.tasks.addTask(5, new EntityAIHideFromHarm(this));
			        this.tasks.addTask(6, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
			        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 0.6D));
			        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
			        this.tasks.addTask(9, new EntityAILookIdle(this));
			        
			        this.defaultTargets();
			        this.magicMelee = true;
				}
				break;
				case AttributeVocation.CLASS_HYBRID_ARCHER_SOLDIER:
				{
					if(patrolType == 1) this.tasks.addTask(3, new EntityAIPatrolEntireVillage(this, 0.6D, patrolShift, withDoors, useHomePosition));
					else this.tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6D, patrolShift));
	
					this.tasks.addTask(0, new EntityAISwimming(this));
			        this.tasks.addTask(1, new EntityAIStayInBorders(this, 1.0D));
			        this.tasks.addTask(2, new EntityAIRestrictOpenDoor(this));
			        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
			        this.tasks.addTask(5, new EntityAIHideFromHarm(this));
			        this.tasks.addTask(6, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
			        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 0.6D));
			        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
			        this.tasks.addTask(9, new EntityAILookIdle(this));
			        
			        this.defaultTargets();
				}
				break;
				case AttributeVocation.CLASS_HYBRID_MAGE_ARCHER:
				{
					if(patrolType == 1) this.tasks.addTask(3, new EntityAIPatrolEntireVillage(this, 0.6D, patrolShift, withDoors, useHomePosition));
					else this.tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6D, patrolShift));
	
					this.tasks.addTask(0, new EntityAISwimming(this));
			        this.tasks.addTask(1, new EntityAIStayInBorders(this, 1.0D));
			        this.tasks.addTask(2, new EntityAIRestrictOpenDoor(this));
			        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
			        this.tasks.addTask(5, new EntityAIHideFromHarm(this));
			        this.tasks.addTask(6, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
			        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 0.6D));
			        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
			        this.tasks.addTask(9, new EntityAILookIdle(this));
			        
			        this.defaultTargets();
			        this.magicRanged = true;
				}
				break;
			}
			
			if(getRace().getType() == AttributeRace.RACE_TYPE_MONGOL)
			{
		        this.targetTasks.addTask(6, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
			}
        }
	}
	
	public void setCombatTask()
    {
        if (this.world != null && !this.world.isRemote)
        {
            this.tasks.removeTask(this.melee);
            this.tasks.removeTask(this.ranged);
            this.tasks.removeTask(this.rangedTrained);
            this.tasks.removeTask(this.potions);
        	this.tasks.removeTask(this.block);
        	this.tasks.removeTask(this.spells);
        	this.tasks.removeTask(this.heal);

            
            if(this.vocation.getType() != AttributeVocation.CLASS_VILLAGER)
            {
                for(Object task : this.tasks.taskEntries.toArray())
        		{
        			 EntityAIBase ai = ((EntityAITaskEntry) task).action;
        			 if(ai instanceof EntityAIHarvestFarmland)
        				 this.tasks.removeTask(ai);	
        			 
        		}
            }
            	
            ItemStack itemstack = this.getHeldItemMainhand();

            if (itemstack.getItem() == Items.BOW)
            {
                int i = 20;

                if (this.world.getDifficulty() != EnumDifficulty.HARD)
                {
                    i = 40;
                }

               /* if(this.vocation.getType() != AttributeVocation.CLASS_ARCHER) 
                {
                	this.tasks.addTask(1, this.ranged);
                    this.setSwingingArms(true);
                }
                else 
                {
                	this.tasks.addTask(1, this.rangedTrained);
                	this.strafingRanged = true;
                }
                */
                this.tasks.addTask(1, this.ranged);
                this.setSwingingArms(true);
                
                this.combatType = 2;
            }
            else if(isMagic)
            {
               	this.tasks.addTask(1, this.spells);
               	this.combatType = 0;
            }else if(canAttackWithPotions) 
        	{
        		this.tasks.addTask(2, this.potions);
        	}
            else
            {
                this.tasks.addTask(1, this.melee);
                
                if(this.getHeldItemOffhand() != null && (this.getHeldItemOffhand().getItem() == Items.SHIELD || this.getHeldItemOffhand().getItem() instanceof ItemModShield))
                {
                	this.tasks.addTask(2, this.block);
                }
                this.combatType = 1;
            } 
            
            
            if(canUsePotions)
            {         
                	if((PASSIVE_POTIONS[PASSIVE_1] != null || PASSIVE_POTIONS[PASSIVE_2] != null 
                			|| PASSIVE_POTIONS[PASSIVE_3] != null || PASSIVE_POTIONS[PASSIVE_4] != null) && vocation.isHealer()) 
                	{
                		//System.out.println("Is healer! ");
                		this.tasks.addTask(3, this.heal);
                	}
            }
            
            if(this.vocation.isCanRide())
            {
            	this.tasks.addTask(4, new EntityAISearchForHorse(this, 0.5D, 40.0, 100));
            }
        }
    }
	
	private void defaultTargets() {
		this.targetTasks.addTask(0, new EntityAINearestAttackableTarget(this, EntityLiving.class, 1, true, true, new Predicate<EntityLiving>()
        {
            public boolean apply(@Nullable EntityLiving p_apply_1_)
            {
            	if(p_apply_1_ instanceof EntityHumanVillager)
            	{
            		EntityHumanVillager villager = (EntityHumanVillager) p_apply_1_;
            		
            		return isHostileFaction(villager);
            	}
            	
        		return p_apply_1_ != null && ((IMob.VISIBLE_MOB_SELECTOR.apply(p_apply_1_) && !(p_apply_1_ instanceof EntityCreeper) && !(p_apply_1_ instanceof EntityTameable)) || (p_apply_1_ instanceof EntityWarg));
            }
        }));
        this.targetTasks.addTask(1, new EntityAIAttackBackExclude(this, true, new Class[0])); 
	}
	
	public boolean isHostileFaction(EntityHumanVillager villager)
	{
		if(servant != null && villager == servant)
		{
				return false;
		}
		
		if(hostiles[villager.getFactionNumber()] == villager.getFactionNumber())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void onLivingUpdate()
    {
		this.drinkPotion();
		super.onLivingUpdate();
		this.updateArmSwingProgress();
		
		if(this.vocation != null && this.kills > this.vocation.getUpgradeReq() && !this.ruler)
		{
			this.upgrade();
		}
		else if(this.ruler && this.kills > 5 && this.world.isDaytime())
		{
			this.spawnRecruits(world.rand.nextInt(this.kills + 1));
			this.kills = 0;
		}
		
		if(this.shifter && this.getHealth() <= this.healthChange)
		{
			this.transform();
		}
		
		if(this.lifespan > 0)
		{
			lifespan--;
		}
		else if(this.lifespan <= 0 && this.hasLifespan )
		{
			this.setDead();
		}
		
		if(this.faction == null && this.liege != null)
		{
			this.faction = this.liege.getFaction();
		}
		
		if(this.magicMelee && !this.world.isRemote)
		{
			EntityLivingBase target = this.getTarget();
			if(target != null && target.getDistance(this) < 4)
			{
				if(this.combatType == 0) this.forceCombatTask(1);
			}
			else if(target != null)
			{
				if(this.combatType == 1) this.forceCombatTask(0);
			}
		}
		
		if(this.ruler && this.faction != null && !this.world.isRemote)
		{
			if(this.world.isDaytime() && !faction.doneUpdate())
			{
				faction.update(this.world.isDaytime(), this.getPos());
				faction.setUpdate(true);
			}
			else if(!this.world.isDaytime() && faction.doneUpdate())
			{
				faction.update(this.world.isDaytime(), this.getPos());
				faction.setUpdate(false);
			}
		}
				
		if(!this.world.isRemote &&
				this.vocation != null &&
				this.vocation.isCanRide() && 
				!this.horseSpawned)
		{
			if(this.rand.nextInt(100) <= this.vocation.getHorseChance())
			{
		        EntityHorse horse = new EntityHorse(world);
				horse.setLocationAndAngles(this.posX, this.posY, this.posZ, 0, 0);
				horse.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(horse)), (IEntityLivingData)null);
				world.spawnEntity(horse);
	
				this.setRidingHorse(horse);
				
				this.horseSpawned  = true;
			}
			else
			{
				this.horseSpawned  = true;
			}
		}
		
		if(!this.world.isRemote && this.meleeWeapon != null && this.rangedWeapon != null)
		{
			EntityLivingBase target = this.getTarget();
			if(target != null && target.getDistance(this) < 4)
			{
				if(this.combatType == 2) 
				{
					this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(this.meleeWeapon));
					this.forceCombatTask(1);
				}
			}
			else if(target != null)
			{
				if(this.combatType == 1)
				{
					this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(this.rangedWeapon));
					this.forceCombatTask(2);
				}
			}
		}
		
		if(this.horse != null && !this.isRiding() && !this.world.isRemote)
		{
			this.setSize2(this.width - this.horseWidth, this.height - this.horseHeight);
			this.horseWidth = 0;
			this.horseHeight = 0;
			this.horse = null;
		}
		
		if(!this.world.isRemote && this.isVampire() 
				&& this.getRace().getID() != AttributeRace.RACE_TYPE_VAMPIRE
				&& (this.ticksExisted / ((60 * 60) * 12)) >= 1
				&& !this.world.isDaytime())
		{
			EntityHumanVillager vampire = new EntityHumanVillager(world, AttributeRace.vampires.getID(), AttributeRace.vampires.getBandit(1), gender, firstName, lastName);                            
        	vampire.setLocationAndAngles((double)this.posX + 0.5D, (double)this.posY, (double)this.posZ + 0.5D, 0.0F, 0.0F);
            vampire.setVampire(true);
            world.spawnEntity(vampire);
            this.setDead();
		}
		
		if(!this.world.isRemote)
        {
			if(this.horse != null)
			{
				if(this.inCombat() && !this.isStrafing())
				{
					if(!this.horse.isSprinting())
					{
						this.horse.setSprinting(true);
					}
					else
					{
			            this.collideWithEntities(this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()));
					}
				}
				else
				{
					if(this.horse.isSprinting()) this.horse.setSprinting(false);
				}
			}
			
			
			if(this.getRace().getID() == AttributeRace.RACE_TYPE_VAMPIRE)
			{
				if(this.world.isDaytime() && !this.world.getBiome(this.getPos()).isSnowyBiome())
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
				else if(!this.world.isDaytime() && ((this.ticksExisted % (200) == 0 && !this.isBurning())))
				{
					this.heal(4);
				}
				
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
            
            if(this.isVampire())
            {
            	((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 300));
            	
            	if(this.getRNG().nextInt(100) < 5) ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 150));
            
            	
            	if(entityIn instanceof EntityHumanVillager) 
            	{
            		if(this.getRNG().nextInt(100) < Main.german_vampire_turn_chance) ((EntityHumanVillager)entityIn).setVampire(true);
            	}
            }
        }

        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);
        this.swingArm(EnumHand.MAIN_HAND);

        if (flag)
        {
            if (i > 0 && entityIn instanceof EntityLivingBase)
            {
                ((EntityLivingBase)entityIn).knockBack(this, (float)i * 0.5F, (double)MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
                this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, this.getSoundCategory(), 1.0F, 1.0F);
                ((WorldServer)this.world).spawnParticle(EnumParticleTypes.DAMAGE_INDICATOR, entityIn.posX, entityIn.posY + (double)(entityIn.height * 0.5F), entityIn.posZ, (int)f, 0.1D, 0.0D, 0.1D, 0.2D);
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

                if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem().canDisableShield(itemstack, itemstack1, entityplayer, this) && itemstack1.getItem().isShield(itemstack1, entityplayer))
                {
                    float f1 = 0.25F + (float)EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

                    if (this.rand.nextFloat() < f1)
                    {
                        entityplayer.getCooldownTracker().setCooldown(itemstack1.getItem(), 100);
                        this.world.setEntityState(entityplayer, (byte)30);
                    }
                }
            }

            this.applyEnchantments(this, entityIn);
        }
 
        return flag;
    }
	
	@Override
	 public void onStruckByLightning(EntityLightningBolt lightningBolt)
	    {
		
	    }
	 
	public void transform()
	{
		EntityLiving mob = getShifter();
		
		if(mob != null)
		{
			EntityLiving entityShifter = mob;                           
	        entityShifter.setLocationAndAngles((double)this.posX + 0.5D, (double)this.posY, (double)this.posZ + 0.5D, 0.0F, 0.0F);
	        world.spawnEntity(entityShifter);
	        this.setDead();
		}
	}

	@Override
	public void setDead()
	{
		if(!this.world.isRemote && this.horse != null && this.horse.isSprinting())
		{
			this.horse.setSprinting(false);
			this.horse.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() - 0.5D);
			this.horse = null;
		}
		
		this.spawnExplosionParticle();
		super.setDead();
	}
	
	public static int getRandomGender(World worldIn)
	{
		return worldIn.rand.nextInt(100) < 45 ? EntityHumanVillager.GENDER_MALE : EntityHumanVillager.GENDER_FEMALE;
	}
	
	private void initValues() {
		if(!this.world.isRemote)
		{
			if(getRaceFromManager() == -1)
			{
				setRace(this.race);
			}
			if(getGender() == -1)
			{
				setGender(gender);
			}
			if(getProfessionID() == -1)
			{
				setProfessionID(this.vocation);
			}
			if(getLastNameFromManager() == "")
			{
				setLastNameFromManager(this.lastName);
			}
			if(getNameFromManager() == "")
			{
				setNameFromManager(this.firstName);
			}
		}
	}
	
	public boolean processInteract(EntityPlayer player, EnumHand hand)
	{
		System.out.println("Faction Interact: "+faction+" Ruler: "+this.liege);
		if(this.isVampire()) return false;
		if(this.vocation != null && this.vocation.getType() == AttributeVocation.CLASS_MERCENARY )
		{	
			ItemStack item = player.getHeldItem(hand);
			
			Style style2 = new Style();
			style2.setColor(TextFormatting.GREEN);
			ITextComponent itextcomponent1;
			
			if(item.getItem() == Items.EMERALD && this.master == null)
			{
				itextcomponent1 = new TextComponentString(
						player.getDisplayNameString()+", sure I "+this.firstName+" will follow you.");
				itextcomponent1.setStyle(style2);

				//player.setHeldItem(hand, null);
				this.setMaster(player);
			}
			else
			{
				itextcomponent1 = new TextComponentString(
						player.getDisplayNameString()+" you have no emerald!");
				itextcomponent1.setStyle(style2);
			}
			
			player.sendMessage(itextcomponent1);
			
			return false;
		}
		else if(this.vocation != null && this.vocation.getType() == AttributeVocation.CLASS_BANDIT)
		{
			return false;
		}
		else if(this.faction != null && this.vocation != null 
				&& (this.vocation.getType() == AttributeVocation.CLASS_SOLDIER
				|| this.vocation.getType() == AttributeVocation.CLASS_ARCHER
				|| this.vocation.getType() == AttributeVocation.CLASS_MAGE
				|| this.vocation.getType() == AttributeVocation.CLASS_ALCHEMIST
				|| this.vocation.getType() == AttributeVocation.CLASS_RULER))
		{
			Style style2 = new Style();
			style2.setColor(TextFormatting.YELLOW);
			
			ITextComponent itextcomponent1 = new TextComponentString("Hail "+
					player.getDisplayNameString()+". I'm "+this.firstName+" "+this.lastName+", and I'm a "
					+race.getName()+" "+vocation.getName()+" of "+faction.getTitleName()+" with "+this.kills+" kills.");
			itextcomponent1.setStyle(style2);
			player.sendMessage(itextcomponent1);
			return false;
		}
		else if(this.vocation != null && this.vocation.getType() == AttributeVocation.CLASS_VILLAGER)
		{
			return super.processInteract(player, hand);
		}
		else
		{
			return false;
		}
	}

	private void forceCombatTask(int i) {
		if (this.world != null && !this.world.isRemote)
        {
            this.tasks.removeTask(this.melee);
            this.tasks.removeTask(this.ranged);
            this.tasks.removeTask(this.rangedTrained);
            this.tasks.removeTask(this.potions);
        	this.tasks.removeTask(this.block);
        	this.tasks.removeTask(this.spells);
        	this.tasks.removeTask(this.heal);
        	
        	switch(i)
        	{
        	case 0:
        		 this.tasks.addTask(0, this.spells);
        		 this.combatType = 0;
        		 break;
        	case 1:
       		 	this.tasks.addTask(0, this.melee);
       		 	this.combatType = 1;
       		 	break;
        	case 2:
       		 	this.tasks.addTask(0, this.ranged);
       		 	this.combatType = 2;
       		 	break;
        	}
        }
	}

	public void upgrade() {
		if(this.world.isRemote) return;
		
		AttributeVocation upgrade = null;
		if(this.vocation.getUpgradeLeft() != null && this.vocation.getUpgradeRight() != null) upgrade = world.rand.nextInt(50) < 100 ? this.vocation.getUpgradeLeft() : this.vocation.getUpgradeRight();
		else if(this.vocation.getUpgradeLeft() != null) upgrade = this.vocation.getUpgradeLeft();
		else if(this.vocation.getUpgradeRight() != null) upgrade = this.vocation.getUpgradeRight();

		if(upgrade != null)
		{
			EntityHumanVillager entityHumanVillager = new EntityHumanVillager(world, race.getID(), upgrade, gender, firstName, lastName);                            
        	entityHumanVillager.setLocationAndAngles((double)this.posX + 0.5D, (double)this.posY, (double)this.posZ + 0.5D, 0.0F, 0.0F);
            world.spawnEntity(entityHumanVillager);
            this.setDead();
		}
	}
	
	protected void spawnRecruits(int num)
	{
		for(int i = 0; i < num; i++)
		{
			AttributeVocation recruit = race.getRandomRecruitVocation();
			
			if(recruit != null)
			{
				EntityHumanVillager entityHumanVillager = new EntityHumanVillager(world, race.getID(), recruit, gender, firstName, lastName);                            
		    	entityHumanVillager.setLocationAndAngles((double)this.posX + 0.5D, (double)this.posY, (double)this.posZ + 0.5D, 0.0F, 0.0F);
		        world.spawnEntity(entityHumanVillager);
			}
		}
	}
	
	protected void updateAITasks()
    {
	
	 super.updateAITasks();
	 
	 if (--this.homeCheckTimer  <= 0)
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
	if(!this.world.isRemote)
	{
		 if(PASSIVE_SPELLS[S_PASSIVE_1] != null)
		 {
			 PASSIVE_SPELLS[S_PASSIVE_1].execute(this);
		 }
		 if(PASSIVE_SPELLS[S_PASSIVE_2] != null)
		 {
			 PASSIVE_SPELLS[S_PASSIVE_2].execute(this);
		 }
		 
		 if(ACTIVE_SPELLS[S_ACTIVE_1] != null)
		 {
			 ACTIVE_SPELLS[S_ACTIVE_1].reset(this);
		 }
		 if(ACTIVE_SPELLS[S_ACTIVE_2] != null)
		 {
			 ACTIVE_SPELLS[S_ACTIVE_2].reset(this);
		 }
		 
		 if(this.additionalMagicSlots)
		 {
			 if(PASSIVE_SPELLS[S_PASSIVE_3] != null)
			 {
				 PASSIVE_SPELLS[S_PASSIVE_3].execute(this);
			 }
			 if(PASSIVE_SPELLS[S_PASSIVE_4] != null)
			 {
				 PASSIVE_SPELLS[S_PASSIVE_4].execute(this);
			 }
			 
			 if(ACTIVE_SPELLS[S_ACTIVE_3] != null)
			 {
				 ACTIVE_SPELLS[S_ACTIVE_3].reset(this);
			 }
			 if(ACTIVE_SPELLS[S_ACTIVE_4] != null)
			 {
				 ACTIVE_SPELLS[S_ACTIVE_4].reset(this);
			 } 
		 }
		 
		 if(controlDuration != -1)
		 {
			 if(controlDuration > 0)
			 {
				 controlDuration--;
			 }
			 else
			 {
				 if(master != null && master instanceof EntityHumanVillager) ((EntityHumanVillager) master).setLiege(null);
					
				 this.setMaster(null);
				this.setCustomNameTag(this.getTitle());
				this.setAlwaysRenderNameTag(Main.useNametags);
				 controlDuration = -1;
			 }
		 }
		 
		 if(master != null)
		 {
			 EntityLivingBase target1 = master.getAttackingEntity();
			 
			 if(target1 == null && master instanceof EntityLiving)
			 {
				 target1 = ((EntityLiving)master).getAttackTarget();
			 }
			 
			 this.setAttackTarget(target1);
		 }
	}

	if(this.getActivePotionEffect(MobEffects.INVISIBILITY) != null)
	{
		helmet = this.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
		chestplate = this.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		leggings = this.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
		boots = this.getItemStackFromSlot(EntityEquipmentSlot.FEET);

		this.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
		this.setItemStackToSlot(EntityEquipmentSlot.CHEST, ItemStack.EMPTY);
		this.setItemStackToSlot(EntityEquipmentSlot.LEGS, ItemStack.EMPTY);
		this.setItemStackToSlot(EntityEquipmentSlot.FEET,ItemStack.EMPTY);
		this.tookArmorOff  = true;
	}
	else if(this.tookArmorOff)
	{
		this.setItemStackToSlot(EntityEquipmentSlot.HEAD, helmet);
		this.setItemStackToSlot(EntityEquipmentSlot.CHEST, chestplate);
		this.setItemStackToSlot(EntityEquipmentSlot.LEGS, leggings);
		this.setItemStackToSlot(EntityEquipmentSlot.FEET, boots);
		this.tookArmorOff  = false;
	}
	 
	 if((this.getAttackTarget() != null || this.getAttackingEntity() != null) && !this.isSprinting())
	 {
		 this.setSprinting(true);
	 }
	 else if (this.isSprinting())
	 {
		 this.setSprinting(false);
	 }
	 
	 if(this.breaksBlocksOnCollide)
	 {
		 this.handleBreakOnCollide();
	 }
    }
	
	public void handleBreakOnCollide()
	{
		int i1 = MathHelper.floor(this.posY);
	     int l1 = MathHelper.floor(this.posX);
	     int i2 = MathHelper.floor(this.posZ);
	     boolean flag = false;
	
	     for (int j = 0; j <= 1; ++j)
        {
            int i3 = l1;
            int k = i1 + j;
            int l = i2;
            BlockPos blockpos = new BlockPos(i3, k, l);
            IBlockState iblockstate = this.world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();

            if (!block.isAir(iblockstate, this.world, blockpos) && this.canDestroyBlock(iblockstate.getBlock()) && net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this, blockpos, iblockstate))
            {
                flag = this.world.destroyBlock(blockpos, true) || flag;
            }
        }

	
	     if (flag)
	     {
	         this.world.playEvent((EntityPlayer)null, 1022, new BlockPos(this), 0);
	     }
	}
	
	public void handleBreakOnFall(int power)
	{
		int i1 = MathHelper.floor(this.posY);
	     int l1 = MathHelper.floor(this.posX);
	     int i2 = MathHelper.floor(this.posZ);
	     boolean flag = false;
	     
	     if(power < 0)
	     {
	    	 BlockPos blockpos = this.getPosition().down();
	         IBlockState iblockstate = this.world.getBlockState(blockpos);
	         Block block = iblockstate.getBlock();
	            
	    	 if (!block.isAir(iblockstate, this.world, blockpos) && this.canDestroyBlock(iblockstate.getBlock()) && net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this, blockpos, iblockstate))
            {
                flag = this.world.destroyBlock(blockpos, true) || flag;
            }
	    	 
	    	 return;
	     }
	     
	     for(int i = -power; i < power; i++)
	     {
	    	 for(int n = -power; n < power; n++)
	    	 {
	    		 for(int j = -power; j < power; j++)
	    		 {
	    	 
			    	BlockPos blockpos = this.getPosition().add(n, i, j);
		            IBlockState iblockstate = this.world.getBlockState(blockpos);
		            Block block = iblockstate.getBlock();
		
		            if (!block.isAir(iblockstate, this.world, blockpos) && this.canDestroyBlock(iblockstate.getBlock()) && net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this, blockpos, iblockstate))
		            {
		                flag = this.world.destroyBlock(blockpos, true) || flag;
		            }
	    		 }
	    	 }
	     }

	
	     if (flag)
	     {
	         this.world.playEvent((EntityPlayer)null, 1022, new BlockPos(this), 0);
	     }
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		//if(this.world.isRemote) return;
		//System.out.println("Name: "+this.getTitle()+"Vocation: "+compound.getInteger("Profession")+" | Name: "+AttributeVocation.getVocationFromID(compound.getInteger("Profession")).getName());
		AttributeVocation vocation = AttributeVocation.getVocationFromID(compound.getInteger("Profession"));
        AttributeRace race = AttributeRace.getFromIDRace(compound.getInteger("Race"));
        int gender = compound.getInteger("Gender");
        String name1 = compound.getString("First Name");
        String name2 = compound.getString("Last Name");
        
        this.setFactionNumber(compound.getInteger("Faction Number"));
        
        this.shifter = compound.getBoolean("Shifter");
        this.ruler = compound.getBoolean("Ruler");
        if(race.getID() != AttributeRace.RACE_TYPE_VAMPIRE) this.setVampire(compound.getBoolean("Vampire"), false);
        int shiftCreatureID = -1;
        if(this.shifter) shiftCreatureID = compound.getInteger("creature");
        if(shiftCreatureID == BJORN_ID) this.creatureShiftTo = new EntityBjornserker(world);
        if(shiftCreatureID == WRAITH_ID) this.creatureShiftTo = new EntityWraith(world);
        
        this.reInit(race.getID(), vocation, gender, name1, name2);
        
        this.factionName = compound.getString("Faction Name");
        
        int radius = 0;
        radius = compound.getInteger("Radius");
        
        if(ruler) 
        {
        	this.createFaction();
        	
        	List<EntityHumanVillager> list = this.world.getEntitiesWithinAABB(EntityHumanVillager.class, this.getEntityBoundingBox().expand(radius, 8.0D, radius).offset(-radius/2, -4, -radius/2));
        	
        	if (!list.isEmpty())
             {
        		for(int i = 0; i < list.size(); i++){
             		//list = this.rangedAttackEntityHost.world.getEntitiesWithinAABB(EntityVillager.class, this.rangedAttackEntityHost.getEntityBoundingBox().expand(d0, 4.0D, d0));
     	            EntityHumanVillager potentialMember = (EntityHumanVillager)list.get(i);
     	        	
     	            if((potentialMember.vocation.getType() == AttributeVocation.CLASS_SOLDIER
     	   				|| potentialMember.vocation.getType() == AttributeVocation.CLASS_ARCHER
     					|| potentialMember.vocation.getType() == AttributeVocation.CLASS_MAGE
     					|| potentialMember.vocation.getType() == AttributeVocation.CLASS_ALCHEMIST
     					|| potentialMember.vocation.getType() == AttributeVocation.CLASS_RULER))
     	            {
     	            	potentialMember.setRuler(this);
     	            	if(faction != null) faction.addVillager();
     	            }
             	}
             }
            
        }
        
        this.kills = compound.getInteger("Kills");
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		if(this.world.isRemote) return;
		//System.out.println("Name: "+this.getTitle()+"Vocation: "+AttributeVocation.getVocationFromID(this.getProfessionID()).getID()+" | Name: "+AttributeVocation.getVocationFromID(this.getProfessionID()).getName());
        compound.setInteger("Profession", this.getProfessionID());
        compound.setInteger("Race", this.getRaceFromManager());
        compound.setInteger("Gender", this.getGender());
        compound.setInteger("Kills", this.kills);
        compound.setInteger("Faction Number", this.factionNumber);
        compound.setString("First Name", this.getNameFromManager());
        compound.setString("Last Name", this.getLastNameFromManager());
        compound.setBoolean("Shifter", this.isShifter());
        compound.setBoolean("Ruler", this.ruler);
        if(race.getID() != AttributeRace.RACE_TYPE_VAMPIRE)   compound.setBoolean("Vampire", this.isVampire());
        compound.setString("Faction Name", this.factionName);
        
        if(this.getVillage() != null) compound.setInteger("Radius", this.getVillage().getVillageRadius());
        
        if(this.isShifter())
        {
        	if(this.creatureShiftTo instanceof EntityBjornserker) compound.setInteger("creature", BJORN_ID);
        	if(this.creatureShiftTo instanceof EntityBjornserker) compound.setInteger("creature", WRAITH_ID);
        }

        NBTTagList nbttaglist = new NBTTagList();
        
        nbttaglist.appendTag(this.getHeldItemMainhand().writeToNBT(new NBTTagCompound()));
        Iterator<ItemStack> equip = this.getEquipmentAndArmor().iterator();

       while(equip.hasNext())
       {
    	   ItemStack item = equip.next();
    	   nbttaglist.appendTag(item.writeToNBT(new NBTTagCompound()));
    	//   System.out.println(this.getName()+" equip: - "+item.getDisplayName());
       }
       
       if(ACTIVE_SPELLS[S_ACTIVE_1] != null) ACTIVE_SPELLS[S_ACTIVE_1].reset(this);
       if(ACTIVE_SPELLS[S_ACTIVE_2] != null) ACTIVE_SPELLS[S_ACTIVE_2].reset(this);
       if(ACTIVE_SPELLS[S_ACTIVE_3] != null) ACTIVE_SPELLS[S_ACTIVE_3].reset(this);
       if(ACTIVE_SPELLS[S_ACTIVE_4] != null) ACTIVE_SPELLS[S_ACTIVE_4].reset(this);

        compound.setTag("Equipment", nbttaglist);
	}
	
	public void initVocation(int type, int id)
	{
		switch(type)
		{
			case AttributeVocation.CLASS_SOLDIER:
			{
				this.vocation = race.getVocation(type, id);
			}
			break;
			case AttributeVocation.CLASS_ARCHER:
			{
				this.vocation = race.getVocation(type, id);
			}
			break;
			case AttributeVocation.CLASS_MAGE:
			{
				this.vocation = race.getVocation(type, id);
			}
			break;
			case AttributeVocation.CLASS_ALCHEMIST:
			{
				this.vocation = race.getVocation(type, id);
			}
			break;
			case AttributeVocation.CLASS_VILLAGER:
			{
				this.vocation = race.getVocation(type, id);
			}
			break;
		}		
	}
	
	protected void initEntityCharacteristics() {
		if(!world.isRemote)
		{
			this.generateName(rand);
		}
	}
	
	protected void initEntityName()
	{
		if(this.isShifter())
		{ 
			if(this.nervous) this.setCustomNameTag("Nervous "+this.getTitle());
			if(this.ruler && this.vocation.getID() == AttributeVocation.CLASS_BANDIT) this.setCustomNameTag("Leader: "+this.getTitle());
		}
		else
		{
			if(this.isVampire()) this.setCustomNameTag("Sickly "+this.getTitle());
			else this.setCustomNameTag(this.getTitle());
		}
		
		this.setAlwaysRenderNameTag(Main.useNametags);
	}

	private void collideWithEntities(List<Entity> p_70970_1_)
    {
        double d0 = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0D;
        double d1 = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0D;

        for (Entity entity : p_70970_1_)
        {
            if (entity instanceof EntityLivingBase && entity != this && !(entity instanceof AbstractHorse))
            {	
            	if(entity instanceof EntityHumanVillager)
            	{
            		AttributeVocation job = ((EntityHumanVillager)entity).getVocation();
            		
            		if(((EntityHumanVillager)entity).isRiding())
            		{
            			return;
            		}
            		
            		if(!isHostileFaction(((EntityHumanVillager)entity)))
            		{
            			return;
            		}
            	}
            	
                double d2 = entity.posX - d0;
                double d3 = entity.posZ - d1;
                double d4 = d2 * d2 + d3 * d3;
                entity.addVelocity(d2 / d4 * 2.0D, 0.20000000298023224D, d3 / d4 * 2.0D);

                entity.attackEntityFrom(DamageSource.causeMobDamage(this), 5.0F);
            }
        }
    }
	
	public boolean inCombat()
	{
		return (this.getAttackingEntity() != null || this.getAttackTarget() != null);
	}

	public boolean isBlocking()
	{
		return ((Boolean)this.dataManager.get(BLOCKING)).booleanValue();
	}

	@SideOnly(Side.CLIENT)
    public boolean isSwingingArms()
    {
        return ((Boolean)this.dataManager.get(SWINGING_ARMS)).booleanValue();
    }
	
	@Override
	public boolean isActiveItemStackBlocking()
    {
        return this.isBlocking();
    }

	@Override
    public void setSwingingArms(boolean swingingArms)
    {
		//System.out.println("Set swinging!");
        this.dataManager.set(SWINGING_ARMS, Boolean.valueOf(swingingArms));
    }
	
	public void setBlocking(boolean b) {
	      this.dataManager.set(BLOCKING, Boolean.valueOf(b));
	      if(b) this.setActiveHand(EnumHand.OFF_HAND);
	      else this.resetActiveHand();
	}
	
	private void setVampire(boolean vampire, boolean b) {
        if(b) this.getDataManager().set(IS_VAMPIRE, Boolean.valueOf(vampire));
	}

	private void createFaction() {
		this.faction = new AttributeFaction(world, this.getPos(), this.getRace(), this.firstName, this, factionName);
		this.factionName = this.faction.getTitleName();
	}
	
	@Override
	public void setMaster(EntityLivingBase player) {
		// TODO Auto-generated method stub
		this.master = player;
		
		if(master != null)
		{
			this.tasks.addTask(3, new EntityAIFollowEntity(this, player, true));	
		}
		else
		{
			for(Object task : this.tasks.taskEntries.toArray())
			{
				 EntityAIBase ai = ((EntityAITaskEntry) task).action;
				 if(ai instanceof EntityAIFollowEntity)
					 this.tasks.removeTask(ai);	
				 //System.out.println("Removed");
				 
			}
			
		}
		//System.out.println("Setting master!");
	}

	@Override
	public boolean isShouldFollow() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

    protected void generateName(Random rand)
    {
    	this.firstName = NameGenerator.generateRandomName(rand, race, gender);
    	this.lastName = NameGenerator.generateRandomName(rand, race);
    }
    
    public String getTitle()
    {
    	String villagerName = this.firstName;
    	
    	if(villagerName != null && villagerName != "" && villagerName != " ")
    	{
    		villagerName += " - ";
    	}
    	
    	if(quality.displayName()) return villagerName+this.race.getName()+" "+this.quality.getName()+" "+this.vocation.getName();
    	else return villagerName+this.race.getName()+" "+this.vocation.getName();
    }
    
    public String getFullName()
    {
    	return this.firstName+" "+this.lastName;
    }
    
    public AttributeVocation getVocation()
    {
    	return this.vocation;
    }
    
    public AttributeRace getRace()
    {
    	return this.race;
    }
	
	public EntityVillager getLiving()
	{
		return (EntityVillager)this;
	}
	
	public Village getVillage() {
		// TODO Auto-generated method stub
		return this.village;
	}
	
	protected void damageEntity(DamageSource damageSrc, float damageAmount)
    {
		DamageSource newSrc = handleInWall(damageSrc);
		float damage = damageAmount;
		if(newSrc == null) return;
		
		if(this.isVampire())
		{
			if(newSrc.isFireDamage())
			{
				damage *= 2;
			}
		}
		
		if(this.reduction > 0)
		{
			//System.out.println("Old Damage: "+damage);
			damage -= damage * ((double)reduction/100.0D);
            this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.BLOCK_ANVIL_PLACE, this.getSoundCategory(), 1.0F, 1.0F);

            //System.out.println("New Damage: "+damage);
		}
		
		super.damageEntity(newSrc, damage);
    }
	
	public DamageSource handleInWall(DamageSource source)
	{
		if(source.damageType.contains("inWall"))
		{
			this.posX += 1;
			this.posY += 1;
			this.posZ += 1;
			return source;
		}
		else if(source.damageType.contains("fall"))
		{
			if(this.breaksBlocksOnCollide)
			{
				if(Main.allDestructive) this.handleBreakOnFall(-1);
			}
			else if(this.ironFoot)
			{
				if(Main.allDestructive) this.handleBreakOnFall(1);
				return null;
			}
			
			return source;
		}
		else if(this.vocation.getID() == AttributeVocation.CLASS_HYBRID_MAGE_ARCHER ||this.vocation.getID() == AttributeVocation.CLASS_HYBRID_MAGE_SOLDER )
		{
			if(source.isFireDamage() || source.isMagicDamage() || source.isExplosion())
			{
				return null;
			}
			else
			{
				return source;
			}
		}
		else if(source.isProjectile() && this.arrowProof)
		{
			if(source.getImmediateSource() instanceof EntityArrow)
			{
				EntityArrow arrow = (EntityArrow)source.getImmediateSource();
				arrow.setVelocity(-1*arrow.motionX, arrow.motionY, -1*arrow.motionZ);
			}
			
			return null;
		}
		else
		{
			return source;
		}
	}

	public ResourceLocation getVillagerSkin() {
		return this.getSkin(this.getProfessionID(), this.getRaceFromManager(), this.getGender());
	}

	private ResourceLocation getSkin(int profession2, int race2, int gender2) {
		AttributeRace tempRace = AttributeRace.getFromIDRace(race2);
		ResourceLocation skin = null;
		
		if(gender2 == this.GENDER_MALE) skin = tempRace.getRandomSkinM(profession2);
		else skin = tempRace.getRandomSkinF(profession2);	
				
		return skin;
	}

	public void addKills(int i) {
		// TODO Auto-generated method stub
		this.kills  += i;
	}

	/*public int getSupply() {
		// TODO Auto-generated method stub
		return supply;
	}

	public void reSupply(int i) {
		// TODO Auto-generated method stub
		supply += i;
	}*/

	public int getKills() {
		// TODO Auto-generated method stub
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}
	
	 protected SoundEvent getAmbientSound()
	    {
	        return null;
	    }

	    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	    {
	        return SoundEvents.ENTITY_PLAYER_HURT;
	    }

	    protected SoundEvent getDeathSound()
	    {
	        return SoundEvents.ENTITY_PLAYER_DEATH;
	    }
	
	protected boolean canDropLoot()
    {
        return dropLoot ;
    }

	/**
     * Reduces damage, depending on potions
     */
    protected float applyPotionDamageCalculations(DamageSource source, float damage)
    {
        damage = super.applyPotionDamageCalculations(source, damage);

        if (source.getTrueSource() == this)
        {
            damage = 0.0F;
        }

        if (source.isMagicDamage())
        {
            damage = (float)((double)damage * 0.15D);
        }

        return damage;
    }
    
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        if (id == 15)
        {
            for (int i = 0; i < this.rand.nextInt(35) + 10; ++i)
            {
                if(this.isMagic ) this.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX + this.rand.nextGaussian() * 0.12999999523162842D, this.getEntityBoundingBox().maxY + 0.5D + this.rand.nextGaussian() * 0.12999999523162842D, this.posZ + this.rand.nextGaussian() * 0.12999999523162842D, 0.0D, 0.0D, 0.0D, new int[0]);
            }
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }
    
    public void setAggressive(boolean aggressive)
    {
        this.getDataManager().set(IS_AGGRESSIVE, Boolean.valueOf(aggressive));
    }

    public boolean isDrinkingPotion()
    {
        return ((Boolean)this.getDataManager().get(IS_AGGRESSIVE)).booleanValue();
    }

	public EntityLivingBase getHostileEntity() 
	{
		if(this.getAttackTarget() != null)
		{
			return this.getAttackTarget();
		}
		else if(this.getAttackingEntity() != null)
		{
			return this.getAttackingEntity();
		}
		else 
		{
			return null;
		}
	}

	public void setLifespan(int lifespan) {
		this.lifespan  = lifespan;
		this.hasLifespan = true;
	}
	
	/**
     * Sets the width and height of the entity.
     */
    protected void setSize2(float width, float height)
    {
    	//System.out.println(width+" "+height);
        if (width != this.width || height != this.height)
        {
        	//System.out.println("Running");
            float f = this.width;
            this.width = width;
            this.height = height;

            if (this.width < f)
            {
                double d0 = (double)width / 2.0D;
                this.setEntityBoundingBox(new AxisAlignedBB(this.posX - d0, this.posY, this.posZ - d0, this.posX + d0, this.posY + (double)this.height, this.posZ + d0));
                return;
            }

            AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
            this.setEntityBoundingBox(new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + (double)this.width, axisalignedbb.minY + (double)this.height, axisalignedbb.minZ + (double)this.width));

            if (this.width > f && !this.firstUpdate && !this.world.isRemote)
            {
                this.move(MoverType.SELF, (double)(f - this.width), 0.0D, (double)(f - this.width));
            }
        }
    }

	public AttributeFaction getFaction() {
		return faction;
	}

	public void setFaction(AttributeFaction faction) {
		this.faction = faction;
	}

	public void setRuler(EntityHumanVillager ruler2) {
		if(!this.world.isRemote)
		{
			this.liege = ruler2;
			this.faction = ruler2.getFaction();
			//System.out.println(this.getTitle()+" Joined Faction: "+this.faction.getTitleName());
		}
		//this.faction.addMember(this);
	}
	
	public EntityLivingBase getTarget()
	{
		EntityLivingBase target = null;
		
		if(this.getAttackingEntity() != null)
		{
			target = this.getAttackingEntity();
		}
		
		if(this.getAttackTarget() != null)
		{
			target = this.getAttackTarget();
		}
		
		return target;
	}
	
	public EnumCreatureAttribute getCreatureAttribute()
    {
		if(this.isVampire()) return EnumCreatureAttribute.UNDEAD;
        return EnumCreatureAttribute.UNDEFINED;
    }

	public boolean isRuler() {
		// TODO Auto-generated method stub
		return ruler;
	}

	public ItemWeaponThrowable getThrownWeapon() {
		return thrownWeapon;
	}

	public void setThrownWeapon(ItemWeaponThrowable thrownWeapon) {
		this.thrownWeapon = thrownWeapon;
	}

	public boolean isDestructive() {
		if(Main.allowDestructive) 
		{
			if(Main.allDestructive) return true;
			else return destructive;
		}
		else return false;
	}

	public void setDestructive(boolean destructive) {
		this.destructive = destructive;
	}

	public void setReductionAmount(int percent) {
		this.reduction  = percent;
	}

	public void setArrowProof(boolean b) {
		// TODO Auto-generated method stub
		this.arrowProof = b;
	}

	public void setControlTime(int duration) {
		this.controlDuration = duration;
		
		this.setCustomNameTag("[Enthrall] "+this.getTitle());
		this.setAlwaysRenderNameTag(Main.useNametags);

	}
	
	public boolean isEnthralled()
	{
		return controlDuration > 0;
	}

	public void setLiege(EntityHumanVillager villager) {
		this.servant  = villager;
	}
	
	
	public boolean isAdditionalPotionSlots() {
		return additionalPotionSlots;
	}

	public void setAdditionalPotionSlots(boolean additionalPotionSlots) {
		this.additionalPotionSlots = additionalPotionSlots;
	}

	public boolean isAdditionalMagicSlots() {
		return additionalMagicSlots;
	}

	public void setAdditionalMagicSlots(boolean additionalMagicSlots) {
		this.additionalMagicSlots = additionalMagicSlots;
	}

	public EntityLiving getShifter() {
		if(isShifter())return this.creatureShiftTo;
		else return null;
	}
	
	public boolean isShifter() {
		return shifter;
	}

	public void setShifter(boolean shifter, EntityLiving creature, boolean isNervous, int healthChange) {
		this.shifter = shifter;
		this.creatureShiftTo = creature;
		
		this.healthChange = healthChange;
		if(healthChange < 0) this.healthChange = (int) (this.getMaxHealth() / 2);
		
		this.nervous = isNervous;
		
		this.initEntityName();
		//System.out.println(this.getTitle()+" Pos X: "+this.posX+" Pos Y: "+this.posY+" Pos Z: "+this.posZ+" - "+this.shifter);
	}
	
	public int getFactionNumber() {
		return factionNumber;
	}

	public void setFactionNumber(int factionNumber) {
		this.factionNumber = factionNumber;
	}
	
	public static boolean canDestroyBlock(Block blockIn)
    {
		if(Main.allowDestructive) return blockIn != Blocks.BEDROCK && !(blockIn instanceof BlockDoor) && !blockIn.getDefaultState().getMaterial().isLiquid() && blockIn != Blocks.COMMAND_BLOCK && blockIn != Blocks.REPEATING_COMMAND_BLOCK && blockIn != Blocks.CHAIN_COMMAND_BLOCK && blockIn != Blocks.BARRIER && blockIn != Blocks.STRUCTURE_BLOCK && blockIn != Blocks.STRUCTURE_VOID && blockIn != Blocks.PISTON_EXTENSION && blockIn != Blocks.END_GATEWAY;
		else return false;
    }
	

	private boolean isStrafing() {
		return this.strafingRanged;
	}
	
	public void setStrafing(boolean set) {
		this.strafingRanged = set;
	}

	
	protected void entityInit()
    {
        super.entityInit();
		dataManager.register(RACE, -1);
		dataManager.register(GENDER, -1);
		dataManager.register(PROFESSION, -1);
		dataManager.register(FIRST_NAME, "");
		dataManager.register(LAST_NAME, "");
		dataManager.register(IS_AGGRESSIVE, Boolean.valueOf(false));
		dataManager.register(SWINGING_ARMS, Boolean.valueOf(false));
		dataManager.register(BLOCKING, Boolean.valueOf(false));
		dataManager.register(IS_VAMPIRE, Boolean.valueOf(false));
    }
	
	public void setVampire(boolean vampire)
    {
        this.getDataManager().set(IS_VAMPIRE, Boolean.valueOf(vampire));
        
        //System.out.println("Vampire created");
        
		if(vampire) 
		{
			this.initEntityName();
		}
    }

    public boolean isVampire()
    {
        return ((Boolean)this.getDataManager().get(IS_VAMPIRE)).booleanValue();
    }
	
	public void setLastNameFromManager(String name)
	{
		dataManager.set(LAST_NAME, name);
	}

	public String getLastNameFromManager()
	{
		return dataManager.get(LAST_NAME).toString();
	}
	
	public void setNameFromManager(String name)
	{
		dataManager.set(FIRST_NAME, name);
	}

	public String getNameFromManager()
	{
		return dataManager.get(FIRST_NAME).toString();
	}
	
	public void setRace(AttributeRace num)
	{
		dataManager.set(RACE, num.getID());
	}

	public void setGender(int num)
	{
		dataManager.set(GENDER, num);
	}
	
	public void setProfessionID(AttributeVocation num)
	{
		dataManager.set(PROFESSION, num.getID());
	}

	public int getRaceFromManager()
	{
		return dataManager.get(RACE).intValue();
	}

	public int getGender()
	{
		return dataManager.get(GENDER).intValue();
	}
	
	public int getProfessionID()
	{
		return dataManager.get(PROFESSION).intValue();
	}

	public void setIronFeet(boolean b) {
		// TODO Auto-generated method stub
		this.ironFoot = b;
	}

	public boolean hasIronFoot() {
		// TODO Auto-generated method stub
		return ironFoot;
	}
}
