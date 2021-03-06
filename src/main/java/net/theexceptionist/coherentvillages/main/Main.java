package net.theexceptionist.coherentvillages.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.IVillageCreationHandler;
import net.theexceptionist.coherentvillages.commands.CommandCreate;
import net.theexceptionist.coherentvillages.commands.CommandGenerateWeapon;
import net.theexceptionist.coherentvillages.main.block.BlockRegister;
import net.theexceptionist.coherentvillages.main.entity.EntityBjornserker;
import net.theexceptionist.coherentvillages.main.entity.EntityBloodBat;
import net.theexceptionist.coherentvillages.main.entity.EntityDrachen;
import net.theexceptionist.coherentvillages.main.entity.EntityHumanVillager;
import net.theexceptionist.coherentvillages.main.entity.EntityLemure;
import net.theexceptionist.coherentvillages.main.entity.EntitySkeletonSummon;
import net.theexceptionist.coherentvillages.main.entity.EntityWarg;
import net.theexceptionist.coherentvillages.main.entity.EntityWraith;
import net.theexceptionist.coherentvillages.main.entity.attributes.AttributeQuality;
import net.theexceptionist.coherentvillages.main.entity.attributes.AttributeRace;
import net.theexceptionist.coherentvillages.main.entity.attributes.FactionManager;
import net.theexceptionist.coherentvillages.main.items.EntityWeaponThrowable;
import net.theexceptionist.coherentvillages.main.items.ModItems;
import net.theexceptionist.coherentvillages.worldgen.villages.ArabStructurePieces;
import net.theexceptionist.coherentvillages.worldgen.villages.BritonStructurePieces;
import net.theexceptionist.coherentvillages.worldgen.villages.FrankStructurePieces;
import net.theexceptionist.coherentvillages.worldgen.villages.GermanStructurePieces;
import net.theexceptionist.coherentvillages.worldgen.villages.GreekStructurePieces;
import net.theexceptionist.coherentvillages.worldgen.villages.LatinStructurePieces;
import net.theexceptionist.coherentvillages.worldgen.villages.NordStructurePieces;
import net.theexceptionist.coherentvillages.worldgen.villages.SlavStructurePieces;
import net.theexceptionist.coherentvillages.worldgen.villages.WorldGenVillage;

@Mod(modid = Resources.MODID, name = Resources.NAME, version = Resources.VERSION, updateJSON="https://github.com/TheExceptionist/Coherent-Villages/blob/master/UpdateChecker/update.json")
public class Main
{
	public static boolean useNametags = false;

	public static Logger logger;
    
    @SidedProxy(serverSide = "net.theexceptionist.coherentvillages.main.CommonProxy", clientSide = "net.theexceptionist.coherentvillages.main.ClientProxy")
    public static CommonProxy proxy;

    @Mod.Instance(Resources.MODID)
	public static Main instance;
   
    public static final int SOLDIER_FACTION = 0;
    public static final int BANDIT_FACTION = 1;
    
	private static int entityIDStart = 1514;
    
    public static int player_faction = SOLDIER_FACTION;
   public static int nord_zombie_infest_rate = 2;
   public static int nord_bandit_infest_rate = 25;
   public static int bjornserker_rate = 5;
   public static int warg_rate = 10;
   public static int upgrade_chance = 25;
	public static int villager_bjorn_rate = 5;
	
	public static int lemure_rate = 5;
	public static int wraith_rate = 5;
	public static int wraith_turn_rate = 50;
	   public static int latin_zombie_infest_rate = 2;
	   public static int latin_bandit_infest_rate = 25;
	   
	   public static int german_zombie_infest_rate = 2;
	   public static int german_bandit_infest_rate = 25;
	   
	   public static int greek_zombie_infest_rate = 2;
	   public static int greek_bandit_infest_rate = 25;
	   
	   public static int frank_zombie_infest_rate = 2;
	   public static int frank_bandit_infest_rate = 25;
	   
	   public static int briton_zombie_infest_rate = 2;
	   public static int briton_bandit_infest_rate = 25;
   
    private static BufferedWriter writer;
    private static BufferedReader reader;
    private static File config_file;
    private static String[] config_text =
    	{
    			"version "+Resources.VERSION+"\n",
    			"#distance between villages\n",
    			"#do not set below 9!\n",
    			"max_distance=20\n",
    			"#do not set above max_distance or below 3!\n",
    			"min_distance=10\n",
    			"#Tested with values 0, 1... change default size of villages\n",
    			"size=1\n",
    			"#ID start for entities\n",
    			"entity_id_start=1514\n",
    			"#=====NORD CONFIGS=======",
    			"#The chance a Nord NPC has to gain the ability(or curse) to transform into a Bjornserker!\n",
    			"bjorn_turn_rate=5\n",
    			"#The chance a Nord Village will bandit infested!\n",
    			"nord_bandit_infest_rate=25\n",
    			"#The chance a Nord Village will zombie infested!\n",
    			"nord_zombie_infest_rate=2\n",
    			"#The spawnrate of wargs!\n",
    			"warg_spawn_rate=10\n",
    			"#The spawnrate of bjornserkers\n",
    			"bjornserker_spawn_rate=5\n",
    			"#The chance a Nord Village attempt to upgrade their soldiers to a higher rank.\n",
    			"upgrade_chance=25\n",
    			"#=====ROMAN CONFIGS=======",
    			"#The spawnrate of lemure in Latin biomes (DON'T CHANGE RIGHT NOW!)\n",
    			"lemure_spawn_rate=2\n",
    			"#The spawnrate of wraiths in Latin biomes\n",
    			"wraith_spawn_rate=5\n",
    			"#The chance when a Latin NPC dies they become a Wraith\n",
    			"wraith_turn_rate=50\n",
    			"#The chance a Latin Village will bandit infested!\n",
    			"latin_bandit_infest_rate=25\n",
    			"#The chance a Latin Village will zombie infested!\n",
    			"latin_zombie_infest_rate=2\n",
    			"#=====GERMAN CONFIGS=======",
    			"#The chance a German Village will be Mongol infested!\n",
    			"german_bandit_infest_rate=8\n",
    			"#The chance a German Village will zombie infested!\n",
    			"german_zombie_infest_rate=2\n",
    			"#The chance when a German Village will vampire infested!\n",
    			"german_vampire_infest_rate=15\n",
    			"#The chance when a vampire attacks a NPC they will be turned!\n",
    			"german_vampire_turn_rate=5\n",
    			"#The chance when a German NPC spawns, they will be a vampire!\n",
    			"german_vampire_spawn_rate=5\n",
    			"#=====SLAVIC CONFIGS=======",
    			"#The chance a Slavic Village will bandit infested!\n",
    			"slav_bandit_infest_rate=15\n",
    			"#The chance a Slavic Village will zombie infested!\n",
    			"slav_zombie_infest_rate=2\n",
    			"#The chance a Slavic Village will mongol infested!\n",
    			"slav_mongol_infest_rate=8\n",
    			"#=====ARABIC CONFIGS=======",
    			"#The chance a Arab Village will bandit infested!\n",
    			"arab_bandit_infest_rate=23\n",
    			"#The chance a Arab Village will zombie infested!\n",
    			"arab_zombie_infest_rate=2\n",
    			"#=====Event CONFIGS=======",
    			"#Show event messages!\n",
    			"show_event_messages=1\n",
    			"#The chance a village will be raid by bandits!\n",
    			"raid_rate=50\n",
    			"#The chance a village will recieve immigrates!\n",
    			"immigrate_rate=50\n",
    			"#The chance a skirmish will happen out in the wild!\n",
    			"skirmish_rate=50\n",    			
    			"#=====General CONFIGS=======",
    			"#Allows for spells to cause damage to the world\n",
    			"allow_destructive_villagers=1\n",
    			"#Makes all villagers destructive, not just hostile ones\n",
    			"all_villagers_destructive=1\n",
    			
    	};
    
    public static enum Soldier
    {
    	Guard, 
    	Man_At_Arms,
		Sergeant,
		Warrior,
		Militia,
		Peasant,
		Archer,
		Hunter,
		Mage_Archer,
		Marksman,
		Merchant,
		Mage,
		Conjurer,
		Necromancer,
		Grand_Mage,
		Alchemist,
		Healer,
		Undead_Healer,
		Potion_Master,
		Cavalier,
		Knight,
		Apothecary,
		Horse_Archer,
		Mage_Knight,
		Paladin,
		Bandit,
		Bandit_Archer,
		Bandit_Mage,
		Bandit_Alchemist,
		Bandit_Horseman,
		NUM_SOLDIERS;
    }
    
    public static int max_distance = 9;
    public static int min_distance = 3;
    public static int village_size = 0;
    public static int passes = 0;
    
    public static CreativeTabs villagesTab = new VillagesTab(CreativeTabs.getNextID(), Resources.NAME);
    
	private static boolean spawnCreepers = true;

	public static int SMALL_RAID_RATE = 50;

	public static int MEDIUM_RAID_RATE = 50;

	public static int LARGE_RAID_RATE = 50;

	public static int SMALL_IMMIGRATE_RATE = 50;

	public static int MEDIUM_IMMIGRATE_RATE = 50;

	public static int LARGE_IMMIGRATE_RATE = 50;

	public static boolean sendMessage = true;
	public static boolean allowDestructive = true;
	public static boolean allDestructive = true;

	public static int SMALL_SKIRMISH_RATE = 50;
	public static int MEDIUM_SKIRMISH_RATE = 50;
	public static int LARGE_SKIRMISH_RATE = 50;
	
	public static int arab_zombie_infest_rate = 2;
	public static int arab_bandit_infest_rate = 23;

	public static int slav_zombie_infest_rate = 2;

	public static int slav_bandit_infest_rate = 15;

	public static int slav_mongol_infest_rate = 8;

	public static int german_vampire_turn_chance = 5;
	public static int german_vampire_infest_rate = 5;

	public static int german_vampire_spawn_rate = 5;


	@EventHandler
	public void serverLoad(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandCreate());
		event.registerServerCommand(new CommandGenerateWeapon());
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		System.out.println(Resources.NAME + " is loading!");
		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
		System.out.println(Resources.MODID+"| Checking for config file\n"+event.getModConfigurationDirectory().getAbsolutePath());//+"\n"+event.getSuggestedConfigurationFile());
		//List<String> config_text = Arrays.asList("#distance between villages", "#do not set below 9!", "max_distance=9", "#do not set above max_distance or below 3!", "min_distance=3");
		//List<String> config_output = null;
		config_file = new File(event.getSuggestedConfigurationFile().getAbsolutePath());
		
		try {
			writeConfig();
		} catch (IOException e) {
			//config_file.mkdirs();
			e.printStackTrace();
		}
		
		BlockRegister.init();
		ModItems.init();
		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
	}
	
	private void writeConfig() throws IOException
	{
		passes++;
		if(config_file.createNewFile())
		{
			System.out.println(Resources.MODID+"| Config file not found! \nCreating...");	
			try {
				System.out.println("Writing to config file....");
				writer = new BufferedWriter(new FileWriter(config_file));
				
				//writer.
				for(int i = 0; i < config_text.length; i++)
				{
					writer.write(config_text[i]);
				}
				
				System.out.println("Wrote to config file!");
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} finally {
				writer.close();
			}
			
			readConfig();
		}
		else
		{
			readConfig();
		}
	}

	private void readConfig() throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		System.out.println(Resources.MODID+"| Config file found! \nLoading...");
		
		try {
			String line;
			
			System.out.print("Reading from config file.....");
			reader = new BufferedReader(new FileReader(config_file));
			
			boolean readingBiomes = false;
			boolean checkedVersion = false;
			while((line = reader.readLine()) != null)
			{
				if(line.substring(0, 1).compareTo("#") == 0) continue;
				if(!checkedVersion) 
				{
					String[] parts = line.split(" ");

					if((parts[0].contains("version") && !parts[1].contains(Resources.VERSION)) || !parts[0].contains("version")) 
					{
						//Past version delete this
						System.out.println("Past config - deleting");
						config_file.delete();
						break;
					}
					
					checkedVersion = true;
					continue;
				}
				
				String[] parts = line.split("=");
				
				int value = 0;
				try
				{
					value = Integer.parseInt(parts[1]);;
				}
				catch(NumberFormatException e)
				{
					System.out.println("ERROR: Reading value from config - "+parts[0]);
					//e.printStackTrace();
					continue;
				}
				
				if(parts[0].contains("max"))
				{
					max_distance = value;
				}
				else if(parts[0].contains("min"))
				{
					min_distance = value;
				}
				else if(parts[0].contains("show_event"))
				{
					sendMessage = value == 1 ? true : false;
					System.out.println("Show Message Events: "+sendMessage);
				}
				else if(parts[0].contains("allow_destructive"))
				{
					allowDestructive = value == 1 ? true : false;
					System.out.println("Allow Destructive Villagers: "+allowDestructive);
				}
				else if(parts[0].contains("all_villagers"))
				{
					allDestructive  = value == 1 ? true : false;
					System.out.println("All Villagers Destructive: "+allDestructive);
				}
				else if(parts[0].contains("raid_rate"))
				{
					SMALL_RAID_RATE = MEDIUM_RAID_RATE = LARGE_RAID_RATE = value;// == 1 ? true : false;
					System.out.println("New Raid Rate: "+value);
				}
				else if(parts[0].contains("immigrate_rate"))
				{
					SMALL_IMMIGRATE_RATE = MEDIUM_IMMIGRATE_RATE = LARGE_IMMIGRATE_RATE = value;
					System.out.println("New Immigrate Rate: "+value);
				}
				else if(parts[0].contains("skirmish_rate"))
				{
					SMALL_SKIRMISH_RATE = MEDIUM_SKIRMISH_RATE = LARGE_SKIRMISH_RATE = value;
					System.out.println("New Skirmish Rate: "+value);
				}
				else if(parts[0].contains("bjorn_turn_rate"))
				{
					villager_bjorn_rate = value;
					System.out.println("New Bjorn Rate: "+value);
				}
				else if(parts[0].contains("wraith_turn_rate"))
				{
					wraith_turn_rate = value;
					System.out.println("New Wraith Turn Rate: "+value);
				}
				else if(parts[0].contains("nord_zombie_infest"))
				{
					nord_zombie_infest_rate = value;
					System.out.println("New Nord Zombie Infest Rate: "+value);
				}
				else if(parts[0].contains("nord_bandit_infest"))
				{
					nord_bandit_infest_rate = value;
					System.out.println("New Nord Bandit Infest Rate: "+value);
				}
				else if(parts[0].contains("german_zombie_infest"))
				{
					german_zombie_infest_rate = value;
					System.out.println("New German Zombie Infest Rate: "+value);
				}
				else if(parts[0].contains("german_vampire_infest"))
				{
					german_vampire_infest_rate = value;
					System.out.println("New German Bandit Infest Rate: "+value);
				}
				else if(parts[0].contains("german_bandit_infest"))
				{
					german_bandit_infest_rate = value;
					System.out.println("New German Bandit Infest Rate: "+value);
				}//german_vampire_turn
				else if(parts[0].contains("german_vampire_turn"))
				{
					german_vampire_turn_chance = value;
					System.out.println("New German Vampire Turn Rate: "+value);
				}
				else if(parts[0].contains("german_vampire_spawn"))
				{
					german_vampire_spawn_rate = value;
					System.out.println("New German Vampire Turn Rate: "+value);
				}
				else if(parts[0].contains("arab_zombie_infest"))
				{
					arab_zombie_infest_rate = value;
					System.out.println("New Arab Zombie Infest Rate: "+value);
				}
				else if(parts[0].contains("arab_bandit_infest"))
				{
					arab_bandit_infest_rate = value;
					System.out.println("New Arab Bandit Infest Rate: "+value);
				}
				else if(parts[0].contains("slav_zombie_infest"))
				{
					slav_zombie_infest_rate = value;
					System.out.println("New Slavic Zombie Infest Rate: "+value);
				}
				else if(parts[0].contains("slav_bandit_infest"))
				{
					slav_bandit_infest_rate = value;
					System.out.println("New Slavic Bandit Infest Rate: "+value);
				}
				else if(parts[0].contains("slav_mongol_infest"))
				{
					slav_mongol_infest_rate = value;
					System.out.println("New Slavic Mongol Infest Rate: "+value);
				}
				else if(parts[0].contains("latin_zombie_infest"))
				{
					latin_zombie_infest_rate = value;
					System.out.println("New Zombie Infest Rate: "+value);
				}
				else if(parts[0].contains("latin_bandit_infest"))
				{
					latin_bandit_infest_rate = value;
					System.out.println("New Bandit Infest Rate: "+value);
				}
				else if(parts[0].contains("warg_spawn"))
				{
					warg_rate = value;
					System.out.println("New Warg Spawnrate: "+value);
				}
				else if(parts[0].contains("wraith_spawn"))
				{
					wraith_rate = value;
					System.out.println("New Wraith Spawnrate: "+value);
				}
				else if(parts[0].contains("lemure_spawn"))
				{
					lemure_rate = value;
					System.out.println("New Lemure Spawnrate: "+value);
				}
				else if(parts[0].contains("bjornserker_spawn"))
				{
					bjornserker_rate = value;
					System.out.println("New Bjornserker Spawnrate: "+value);
				}
				else if(parts[0].contains("upgrade"))
				{
					upgrade_chance = value;
					System.out.println("New Upgrade Chance: "+value);
				}
				else if(parts[0].contains("creep"))
				{
					spawnCreepers = value == 1;
					System.out.println("New Creeper Spawn: "+spawnCreepers);
				}
				else if(parts[0].contains("size"))
				{
					village_size = value;
				}
				else if(parts[0].contains("name"))
				{
					useNametags = value == 1 ? true : false;
					System.out.println("New Nametag value: "+useNametags);
				}
				else
				{
					/*if(parts[0].contains("Ocean")) readingBiomes = true;
						
					if(!readingBiomes)
					{
						new VillagerSpawn(parts[0], value);
					}
					else
					{
						new BiomeSpawn(parts[0], value);
					}*/
				}
			}
			
			if(!checkedVersion && passes < 10)
			{
				writeConfig();
				return;
			}
			else if(passes > 10)
			{
				System.out.println("Config couldn't load! Please check to make sure your config file includes a version at the top.");
			}
			
			/*while(villager_spawn.size() < Soldier.NUM_SOLDIERS.ordinal())
			{
				new VillagerSpawn("Placeholder", 1);
			}
			
			for(VillagerSpawn v : villager_spawn)
			{
				System.out.println("Spawns: "+v.name+" "+v.spawn);
			}*/
			
			System.out.println("Read the config file! New Max: "+max_distance+" New Min: "+min_distance+" New Size: "+village_size);//+" Guard Spawn: "+Main.villager_spawn.get(0).spawn);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.initEvents();
    	proxy.registerRenderers();
    	proxy.registerRenderInformation();
    	instance = this;
    	
    	AttributeRace.init();
       	FactionManager.init();
       	AttributeQuality.init();
    	
    	List<Biome> villageBiomes = Arrays.<Biome>asList(new Biome[] {/*Biomes.PLAINS, Biomes.DESERT, Biomes.SAVANNA, Biomes.TAIGA, Biomes.BEACH, Biomes.BIRCH_FOREST, Biomes.BIRCH_FOREST_HILLS, Biomes.COLD_BEACH, Biomes.COLD_TAIGA, Biomes.COLD_TAIGA_HILLS, Biomes.DEEP_OCEAN,
    		Biomes.DESERT, Biomes.DESERT_HILLS, Biomes.EXTREME_HILLS, Biomes.EXTREME_HILLS_EDGE, Biomes.EXTREME_HILLS_WITH_TREES, Biomes.FOREST, Biomes.FOREST_HILLS, Biomes.FROZEN_RIVER, Biomes.FROZEN_OCEAN, Biomes.ICE_MOUNTAINS, Biomes.ICE_PLAINS,
    		Biomes.JUNGLE, Biomes.JUNGLE_EDGE, Biomes.JUNGLE_HILLS, Biomes.MESA, Biomes.MESA_CLEAR_ROCK, Biomes.MESA_ROCK, Biomes.MUSHROOM_ISLAND, Biomes.MUSHROOM_ISLAND_SHORE, Biomes.MUTATED_BIRCH_FOREST, Biomes.PLAINS, Biomes.DESERT, Biomes.SAVANNA, Biomes.TAIGA, Biomes.OCEAN,
    		Biomes.MUTATED_BIRCH_FOREST, Biomes.MUTATED_BIRCH_FOREST_HILLS, Biomes.MUTATED_DESERT, Biomes.MUTATED_EXTREME_HILLS, Biomes.MUTATED_EXTREME_HILLS_WITH_TREES, Biomes.MUTATED_FOREST, Biomes.MUTATED_ICE_FLATS, Biomes.MUTATED_JUNGLE, Biomes.MUTATED_MESA, Biomes.MUTATED_MESA, Biomes.MUTATED_MESA_CLEAR_ROCK,
    		Biomes.MUTATED_MESA_ROCK, Biomes.MUTATED_PLAINS, Biomes.MUTATED_REDWOOD_TAIGA, Biomes.MUTATED_REDWOOD_TAIGA_HILLS, Biomes.MUTATED_ROOFED_FOREST, Biomes.MUTATED_SAVANNA, Biomes.MUTATED_SAVANNA_ROCK, Biomes.MUTATED_SWAMPLAND, Biomes.MUTATED_TAIGA, Biomes.MUTATED_TAIGA_COLD, Biomes.REDWOOD_TAIGA, Biomes.REDWOOD_TAIGA_HILLS, Biomes.RIVER
    		,Biomes.RIVER, Biomes.ROOFED_FOREST, Biomes.SAVANNA_PLATEAU, Biomes.STONE_BEACH, Biomes.SWAMPLAND, Biomes.TAIGA_HILLS*/});
    	
    	List<Biome> villageNordBiomes = Arrays.<Biome>asList(new Biome[] { 
    			Biomes.DEEP_OCEAN, 
        		Biomes.FOREST, Biomes.FOREST_HILLS, 
        		Biomes.FROZEN_RIVER, Biomes.FROZEN_OCEAN, 
        		Biomes.MUTATED_FOREST, 
        		Biomes.RIVER,     			
    			Biomes.FROZEN_RIVER, Biomes.COLD_TAIGA_HILLS,
        	});
        	
    	List<Biome> villageLatinBiomes = Arrays.<Biome>asList(new Biome[] {
    			Biomes.PLAINS, Biomes.SAVANNA, 
        		 Biomes.MUTATED_PLAINS,  
        		Biomes.MUTATED_SAVANNA, Biomes.MUTATED_SAVANNA_ROCK, 
        		Biomes.MUTATED_SWAMPLAND,  Biomes.SWAMPLAND,
        		Biomes.SAVANNA_PLATEAU 
    			
        	});
        	
    	List<Biome> villageGermanBiomes = Arrays.<Biome>asList(new Biome[] {
        		Biomes.EXTREME_HILLS, Biomes.EXTREME_HILLS_EDGE, Biomes.EXTREME_HILLS_WITH_TREES, 
        		Biomes.MUTATED_EXTREME_HILLS, Biomes.MUTATED_EXTREME_HILLS_WITH_TREES, Biomes.STONE_BEACH,
        		Biomes.MUTATED_ROOFED_FOREST, Biomes.ROOFED_FOREST
    	});
        	
    	List<Biome> villageGreekBiomes = Arrays.<Biome>asList(new Biome[] {
    			Biomes.MUTATED_MESA, Biomes.MUTATED_MESA, Biomes.MUTATED_MESA_CLEAR_ROCK,
    			Biomes.MESA, Biomes.MESA_CLEAR_ROCK, Biomes.MESA_ROCK, Biomes.MUTATED_MESA_ROCK,
    			Biomes.JUNGLE, Biomes.JUNGLE_EDGE, Biomes.JUNGLE_HILLS
    	});
    	
    	List<Biome> villageSlavBiomes = Arrays.<Biome>asList(new Biome[] {
    		Biomes.TAIGA, Biomes.COLD_TAIGA, 
    		Biomes.MUTATED_REDWOOD_TAIGA, Biomes.MUTATED_REDWOOD_TAIGA_HILLS, Biomes.MUTATED_TAIGA, 
    		Biomes.MUTATED_TAIGA_COLD, Biomes.REDWOOD_TAIGA, Biomes.REDWOOD_TAIGA_HILLS,
    		Biomes.TAIGA_HILLS
    	});
    	
    	List<Biome> villageBritonBiomes = Arrays.<Biome>asList(new Biome[] {
    		Biomes.MUTATED_ICE_FLATS, Biomes.COLD_BEACH, Biomes.EXTREME_HILLS_EDGE,
    		Biomes.ICE_MOUNTAINS, Biomes.ICE_PLAINS
    	});
    	
    	List<Biome> villageFrankBiomes = Arrays.<Biome>asList(new Biome[] { 
    			Biomes.BIRCH_FOREST_HILLS, Biomes.MUTATED_BIRCH_FOREST_HILLS, 
    			Biomes.BIRCH_FOREST, Biomes.BEACH, 
    			Biomes.MUTATED_BIRCH_FOREST, Biomes.MUTATED_BIRCH_FOREST_HILLS,
    	});
        	
    	List<Biome> villageArabBiomes = Arrays.<Biome>asList(new Biome[] {Biomes.DESERT, 
        		Biomes.DESERT, Biomes.DESERT_HILLS, 
        		Biomes.MUTATED_DESERT
        	});
        	
    	/*addVillagePiece(VillageComponentBarrackSmall.class, "ViGb"); 
    	addVillageCreationHandler(new VillageHandlerBarracksSmall()); 
    	addVillagePiece(VillageComponentBarracks.class, "ViBR"); 
    	addVillageCreationHandler(new VillageHandlerBarracks()); 
    	addVillagePiece(VillageComponentFence.class, "ViFE"); 
    	addVillageCreationHandler(new VillageHandlerFence()); 
    	addVillagePiece(VillageComponentWall.class, "ViWA"); 
    	addVillageCreationHandler(new VillageHandlerWall()); 
    	addVillagePiece(VillageComponentGuardTower.class, "ViTW"); 
    	addVillageCreationHandler(new VillageHandlerGuardTower()); 
    	addVillagePiece(VillageComponentStable.class, "ViST"); 
    	addVillageCreationHandler(new VillageHandlerStable()); 
    	addVillagePiece(VillageComponentSmallHouseWithDoor.class, "ViSHD"); 
    	addVillageCreationHandler(new VillageHandlerSmallHouseWithDoor()); 
    	
    	addVillagePiece(VillageComponentWizardTower.class, "ViWW"); 
    	addVillageCreationHandler(new VillageHandlerWizardTower()); 
    	addVillagePiece(VillageComponentInn.class, "ViIN"); 
    	addVillageCreationHandler(new VillageHandlerInn()); 
    	addVillagePiece(VillageComponentAlchemyHut.class, "ViAL"); 
    	addVillageCreationHandler(new VillageHandlerAlchemyHut()); 
    	addVillagePiece(VillageComponentVillageFort.class, "ViVF"); 
    	addVillageCreationHandler(new VillageHandlerFort()); 
    	addVillagePiece(VillageComponentBigFarm.class, "ViBF"); 
    	addVillageCreationHandler(new VillageHandlerBigFarm());
    	addVillagePiece(VillageComponentHunterHut.class, "ViHH"); 
    	addVillageCreationHandler(new VillageHandlerHunterHut()); 
    	addVillagePiece(VillageComponentCaneFarm.class, "ViSC"); 
    	addVillageCreationHandler(new VillageHandlerCaneFarm()); 
    	addVillagePiece(VillageComponentShop.class, "ViSS"); 
    	addVillageCreationHandler(new VillageHandlerShop()); 
    	addVillagePiece(VillageComponentSanctuary.class, "ViGS"); 
    	addVillageCreationHandler(new VillageHandlerSanctuary()); */
    	
    	//addVillagePiece(ModMapVillageGen.Start.class, "ViS");
    	MapGenStructureIO.registerStructure(WorldGenVillage.Start.class, "Mod Village");
    	
    	NordStructurePieces.registerVillagePieces();
    	MapGenStructureIO.registerStructure(WorldGenVillage.NordStart.class, "Nord Village");
    	
    	LatinStructurePieces.registerVillagePieces();
    	MapGenStructureIO.registerStructure(WorldGenVillage.LatinStart.class, "Latin Village");
    	
    	GermanStructurePieces.registerVillagePieces();
    	MapGenStructureIO.registerStructure(WorldGenVillage.GermanStart.class, "German Village");
    	
    	SlavStructurePieces.registerVillagePieces();
    	MapGenStructureIO.registerStructure(WorldGenVillage.SlavStart.class, "Slav Village");
    	
    	ArabStructurePieces.registerVillagePieces();
    	MapGenStructureIO.registerStructure(WorldGenVillage.ArabStart.class, "Arab Village");
    	
    	GreekStructurePieces.registerVillagePieces();
    	MapGenStructureIO.registerStructure(WorldGenVillage.GreekStart.class, "German Village");
    	
    	BritonStructurePieces.registerVillagePieces();
    	MapGenStructureIO.registerStructure(WorldGenVillage.BritonStart.class, "Slav Village");
    	
    	FrankStructurePieces.registerVillagePieces();
    	MapGenStructureIO.registerStructure(WorldGenVillage.FrankStart.class, "Arab Village");
    	
    	for(Biome b : villageNordBiomes)
    	{
    		WorldGenVillage.NORD_VILLAGE_SPAWN_BIOMES.add(b);
    	}
    	
    	for(Biome b : villageLatinBiomes)
    	{
    		WorldGenVillage.LATIN_VILLAGE_SPAWN_BIOMES.add(b);
    	}
    	
    	for(Biome b : villageGermanBiomes)
    	{
    		WorldGenVillage.GERMAN_VILLAGE_SPAWN_BIOMES.add(b);
    	}
    	
    	for(Biome b : villageSlavBiomes)
    	{
    		WorldGenVillage.SLAV_VILLAGE_SPAWN_BIOMES.add(b);
    	}
    	
    	for(Biome b : villageArabBiomes)
    	{
    		WorldGenVillage.ARAB_VILLAGE_SPAWN_BIOMES.add(b);
    	}
    	
    	for(Biome b : villageGreekBiomes)
    	{
    		WorldGenVillage.GREEK_VILLAGE_SPAWN_BIOMES.add(b);
    	}
    	
    	for(Biome b : villageBritonBiomes)
    	{
    		WorldGenVillage.BRITON_VILLAGE_SPAWN_BIOMES.add(b);
    	}
    	
    	for(Biome b : villageFrankBiomes)
    	{
    		WorldGenVillage.FRANK_VILLAGE_SPAWN_BIOMES.add(b);
    	}
    	
    	/*
    	int i = 0;
    	for(Biome b : villageBiomes)
    	{
    		for(BiomeSpawn s : biomes_spawn)
    		{
    			if(FMLCommonHandler.instance().getSide() == Side.CLIENT){
	    			if(b.getBiomeName().compareTo(s.name) == 0 && s.spawn)
	    			{
	    				ModMapVillageGen.VILLAGE_SPAWN_BIOMES.add(b);
	    	    		/*if(b != Biomes.MUSHROOM_ISLAND || b != Biomes.MUSHROOM_ISLAND_SHORE)
	    	    		{
	        	    		
	    	    			biomes[i] = b;
	    	    			//System.out.println(biomes[i]);
	    	    		}
	    	    		break;
	    			}
	    			else if(b.getBiomeName().compareTo(s.name) == 0 && !s.spawn)
	    			{
	    				ModMapVillageGen.VILLAGE_SPAWN_BIOMES.remove(b);
	    				System.out.println("Preventing: "+s.name+" from spawning.");
	    				break;
	    			}
    			} 
    			else
    			{
    				//Must ignore config
    				BiomeManager.addVillageBiome(b, true);
    			}
    		}

    		

    		i++;
    	}
    	
    	Biome[] biomes = new Biome[villageBiomes.size()];
    	
    	if(!spawnCreepers)
    	{
    		EntityRegistry.removeSpawn(EntityCreeper.class, EnumCreatureType.MONSTER, villageBiomes.toArray(biomes));
    	}*/
    	

    	Biome[] nBiomes = new Biome[villageNordBiomes.size()];
    	Biome[] lBiomes = new Biome[villageLatinBiomes.size()];
    	
    	createEntity(EntityHumanVillager.class, Main.entityIDStart  , "human_villager", 0x101010, 0xFF0000, false);
    	createEntity(EntityWarg.class, Main.entityIDStart + 1, "warg", 0x101010, 0xFF0000, true);
    	createEntity(EntityBjornserker.class, Main.entityIDStart + 2, "bjornserker", 0x101010, 0x808080, true);
    	createEntity(EntityWraith.class, Main.entityIDStart + 3, "wraith", 0x101010, 0x00FF00, true);
    	createEntity(EntityLemure.class, Main.entityIDStart + 4, "lemure", 0x404040, 0x00AA00, true);
    	createEntity(EntityDrachen.class, Main.entityIDStart + 5, "drachen", 0x808080, 0x005500, true);
    	createEntity(EntitySkeletonSummon.class, Main.entityIDStart + 6, "minion", 0x404040, 0x00AA00, false);
    	createEntity(EntityBloodBat.class, Main.entityIDStart + 7, "blood_bat", 0xA0A080, 0x005500, true);

    	
    	//Soldiers
    	/*createEntity(EntityVillagerGuard.class, 1513, "villager_guard", 161425, 1582224);
    	createEntity(EntityVillagerManAtArms.class, 1514, "villager_man_at_arms", 261425, 1582224);
    	createEntity(EntityVillagerSergeant.class, 1515, "villager_sergeant", 361425, 1582224);
    	createEntity(EntityVillagerWarrior.class, 1516, "villager_warrior", 461425, 1382224);
    	createEntity(EntityVillagerPeasant.class, 1517, "villager_peasant", 561425, 1582224);
    	createEntity(EntityVillagerMilitia.class, 1518, "villager_militia", 661425, 1582224);
    	
    	
    	createEntity(EntityVillagerArcher.class, 1520, "villager_archer", 345895, 1985323);
    	createEntity(EntityVillagerHunter.class, 1521, "villager_hunter", 225395, 2015567);
    	createEntity(EntityVillagerMageArcher.class, 1522, "villager_mage_archer", 345895, 3985323);
    	createEntity(EntityVillagerMarksman.class, 1523, "villager_marksman", 225395, 4015567);
    	

    	createEntity(EntityVillagerMage.class, 1524, "villager_mage", 705895, 8985323);
    	createEntity(EntityVillagerGrandMage.class, 1525, "villager_grand_mage", 416395, 8115567);
    	createEntity(EntityVillagerConjurer.class, 1526, "villager_conjurer", 426395, 8215567);
    	createEntity(EntityVillagerNecromancer.class, 1527, "villager_necromancer", 436395, 8315567);
    	
    	createEntity(EntityVillagerAlchemist.class, 1528, "villager_alchemist", 505895, 3985323);
    	createEntity(EntityVillagerUndeadHunter.class, 1529, "villager_undeadhunter", 515895, 6485323);
    	createEntity(EntityVillagerHealer.class, 1540, "villager_healer", 245895, 6585323);
    	createEntity(EntityVillagerPotionMaster.class, 1541, "villager_potion_master", 545895, 2585323);
    	
    	createEntity(EntityVillagerKnight.class, 1545, "villager_knight", 385895, 3685111);
    	createEntity(EntityVillagerMageKnight.class, 1546, "villager_mage_knight", 385895, 0000111);
    	createEntity(EntityVillagerPaladin.class, 1547, "villager_paladin", 385895, 2222111);
    	createEntity(EntityVillagerHorseArcher.class, 1548, "villager_horse_archer", 385895, 4444111);
    	createEntity(EntityVillagerApothecary.class, 1549, "villager_apothecary", 385895, 5555111);
    	createEntity(EntityVillagerCavalier.class, 1550, "villager_cavalier", 385895, 3685111);
    	
    	createEntity(EntityVillagerGuardian.class, 1551, "villager_guardian", 615895, 7285323);
    	createEntity(EntityVillagerMerchant.class, 1552, "villager_merchant", 805890, 9105323);
    	createEntity(EntityMerchantGuard.class, 1553, "villager_merchant_guard", 525895, 4785000);
    	createEntity(EntityVillagerHorse.class, 1554, "villager_horse", 345895, 5505567);

    	//Bandit
    	createEntity(EntityVillagerBandit.class, 1555, "villager_bandit", 155895, 5505567);
    	createEntity(EntityVillagerBanditArcher.class, 1556, "villager_bandit_archer", 246895, 5505567);
    	createEntity(EntityVillagerBanditMage.class, 1557, "villager_bandit_mage", 245995, 5505567);
    	createEntity(EntityVillagerBanditHorseman.class, 1558, "villager_bandit_horseman", 346895, 5505567);
    	createEntity(EntityVillagerBanditAlchemist.class, 1559, "villager_bandit_alchemist", 455895, 5505567);
    	createEntity(EntityVillagerDarkKnight.class, 1560, "villager_dark_knight", 855895, 5505567);



    	createEntity(EntitySkeletonMinion.class, 1561, "skeleton_minion", 926395, 1015567);*/
    	//Humans
    	//if(biomes != null)
    	//{
    		//System.out.println(biomes[0]);
    	/*if(merchant_spawn > 0){
    		EntityRegistry.addSpawn(EntityVillagerMerchant.class, 1, 1, 2, EnumCreatureType.MONSTER, villageBiomes.toArray(biomes));//weightedProb, min, max, typeOfCreature, biomes);
    	}*/
    	//}
    	
    	/*
    	EntityRegistry.addSpawn(EntityVillagerBandit.class, 5, 1, 2, EnumCreatureType.MONSTER, villageBiomes.toArray(biomes));//weightedProb, min, max, typeOfCreature, biomes);
    	EntityRegistry.addSpawn(EntityVillagerBanditArcher.class, 5, 1, 2, EnumCreatureType.MONSTER, villageBiomes.toArray(biomes));//weightedProb, min, max, typeOfCreature, biomes);
    	EntityRegistry.addSpawn(EntityVillagerBanditMage.class, 2, 1, 2, EnumCreatureType.MONSTER, villageBiomes.toArray(biomes));//weightedProb, min, max, typeOfCreature, biomes);
    	EntityRegistry.addSpawn(EntityVillagerBanditHorseman.class, 5, 1, 2, EnumCreatureType.MONSTER, villageBiomes.toArray(biomes));//weightedProb, min, max, typeOfCreature, biomes);
    	EntityRegistry.addSpawn(EntityVillagerBanditAlchemist.class, 5, 1, 2, EnumCreatureType.MONSTER, villageBiomes.toArray(biomes));//weightedProb, min, max, typeOfCreature, biomes);
    	*/
    	
    	EntityRegistry.addSpawn(EntityWarg.class, Main.warg_rate, 2, 6, EnumCreatureType.MONSTER, villageNordBiomes.toArray(nBiomes));//weightedProb, min, max, typeOfCreature, biomes);
    	EntityRegistry.addSpawn(EntityBjornserker.class, Main.bjornserker_rate, 1, 2, EnumCreatureType.MONSTER, villageNordBiomes.toArray(nBiomes));//weightedProb, min, max, typeOfCreature, biomes);
    	EntityRegistry.addSpawn(EntityWraith.class, Main.wraith_rate, 1, 2, EnumCreatureType.MONSTER, villageLatinBiomes.toArray(lBiomes));//weightedProb, min, max, typeOfCreature, biomes);
    	EntityRegistry.addSpawn(EntityLemure.class, Main.lemure_rate, 1, 2, EnumCreatureType.MONSTER, villageLatinBiomes.toArray(lBiomes));//weightedProb, min, max, typeOfCreature, biomes);
    	
    	//EntityRegistry.registerModEntity(new ResourceLocation(Resources.MODID, "villager_arrow"), EntityVillagerArrow.class, "entity_villager_arrow", 1, instance,1, 1, false);
       	EntityRegistry.registerModEntity(new ResourceLocation(Resources.MODID, "throwing_axe"), EntityWeaponThrowable.class, "throwing_axe", 1, instance, 64, 10, true);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}
	
	public static void createEntity(Class entityClass, int ID, String entityName, int solidColor, int spotColor, boolean withEgg){
		ResourceLocation loc = new ResourceLocation(Resources.MODID+":"+entityName);
    	EntityRegistry.registerModEntity(loc, entityClass, entityName, ID, instance, 128, 1, true);
    	if(withEgg) 
    	{
    		EntityRegistry.registerEgg(new ResourceLocation(Resources.MODID+":"+entityName),  solidColor, spotColor);
    	}
    }
	
	public static void addVillagePiece(Class c, String s) 
    { 
	    try 
	    { 
	    MapGenStructureIO.registerStructureComponent(c, s);
	    } 
	    catch (Exception localException) {} 
	} 
	
	    public static void addVillageCreationHandler(IVillageCreationHandler v) 
	    { 
	    VillagerRegistry.instance().registerVillageCreationHandler(v); 
	    //VillagerRegistry.instance().
	    
    }
	    
	    /*public static void registerEgg(ResourceLocation name, int primary, int secondary)
	    {
	        EntityVillagerEntry entry = ForgeRegistries.ENTITIES.getValue(name);
	        if (entry == null)
	        {
	            FMLLog.bigWarning("Attempted to registry a entity egg for entity ({}) that is not in the Entity Registry", name);
	            return;
	        }
	        entry.setEgg(new EntityEggInfo(name, primary, secondary));
	    }*/
}


