package net.theexceptionist.coherentvillages.main.entity.attributes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.theexceptionist.coherentvillages.main.entity.EntityHumanVillager;

public class AttributeFaction {
	private static final String[] n_titles = 
		{
				"%n's Kingdom of the %rs",
				"%n's %r Village",
				"%n's %r Jarledom",
		};
	
	private static final String[] n_b_titles = 
		{
				"%n's Pillagers of the %rs",
				"%n's %r Viking Den",
				"The %n's %r Sea Raiders",
		};
	
	private static final String[] l_titles = 
		{
				"%n's %r Empire",
				"%n's %r Town",
				"%n's %r Village",
		};
	
	private static final String[] l_b_titles = 
		{
				"%n's Pillagers of the %rs",
				"%n's %r Viking Den",
				"The %n's %r Sea Raiders",
		};
	
	private static final String[] g_titles = 
		{
				"%n's Kingdom of the %rs",
				"%n's %r Village",
				"%n's %r Fiefdom",
		};
	
	private static final String[] g_b_titles = 
		{
				"%n's Pillagers of the %rs",
				"%n's %r Viking Den",
				"The %n's %r Sea Raiders",
		};
	
	private static final String[] s_titles = 
		{
				"%n's Kingdom of the %rs",
				"%n's %r Village",
				"%n's %r States",
		};
	
	private static final String[] s_b_titles = 
		{
				"%n's Pillagers of the %rs",
				"%n's %r Viking Den",
				"The %n's %r Sea Raiders",
		};
	
	private static final String[] a_titles = 
		{
				"%n's Sultanate of the %rs",
				"%n's %r Village",
				"%n's %r Caliphate",
		};
	
	private static final String[] a_b_titles = 
		{
				"%n's Pillagers of the %rs",
				"%n's %r Viking Den",
				"The %n's %r Sea Raiders",
		};
	
	private int radius = 50;
	private BlockPos center = null;
	private World world;
	
	protected String name;
	protected String titleName;
	protected AttributeRace race;
	protected boolean isBandit = false;
	
	public static int END_ID = 0;
	protected final int ID;

	private boolean messageSend = false;
	private EntityHumanVillager ruler;
	//private ArrayList<EntityHumanVillager> members;
	
	public AttributeFaction(World world, BlockPos center, AttributeRace race, String name, EntityHumanVillager entityHumanVillager, String factionName)
	{
		this.center = center;
		this.world = world;
		this.titleName = name;
		this.race = race;
		this.ID = END_ID++;
		this.ruler = entityHumanVillager;
		
		if(!world.isRemote)
		{
			if(factionName.contains("None"))
			{
				this.setName();
			}
			else
			{
				this.name = factionName;
			}
			
			FactionManager.addFaction(this);
		}
		
		
	//this.members = new ArrayList<EntityHumanVillager>();
	}
	
	public EntityHumanVillager getRuler() {
		return ruler;
	}

	public int getID() {
		return ID;
	}

	public String getTitleName() {
		return this.name;
	}

	public void setName()
	{
		switch(race.getType())
		{
			case AttributeRace.RACE_TYPE_NORD:
			{
				if(!isBandit) this.name = n_titles[world.rand.nextInt(n_titles.length)].replace("%n", titleName).replace("%r", race.getName());
				else this.name = n_b_titles[world.rand.nextInt(n_b_titles.length)].replace("%n", titleName).replace("%r", race.getName());
			}
			break;
			case AttributeRace.RACE_TYPE_SLAV:
			{
				if(!isBandit) this.name = s_titles[world.rand.nextInt(s_titles.length)].replace("%n", titleName).replace("%r", race.getName());
				else this.name = s_b_titles[world.rand.nextInt(s_b_titles.length)].replace("%n", titleName).replace("%r", race.getName());
			}
			break;
			case AttributeRace.RACE_TYPE_LATIN:
			{
				if(!isBandit) this.name = l_titles[world.rand.nextInt(l_titles.length)].replace("%n", titleName).replace("%r", race.getName());
				else this.name = l_b_titles[world.rand.nextInt(l_b_titles.length)].replace("%n", titleName).replace("%r", race.getName());
			}
			break;
			case AttributeRace.RACE_TYPE_GERMAN:
			{
				if(!isBandit) this.name = g_titles[world.rand.nextInt(g_titles.length)].replace("%n", titleName).replace("%r", race.getName());
				else this.name = g_b_titles[world.rand.nextInt(g_b_titles.length)].replace("%n", titleName).replace("%r", race.getName());
			}
			break;
			case AttributeRace.RACE_TYPE_ARAB:
			{
				if(!isBandit) this.name = a_titles[world.rand.nextInt(a_titles.length)].replace("%n", titleName).replace("%r", race.getName());
				else this.name = a_b_titles[world.rand.nextInt(a_b_titles.length)].replace("%n", titleName).replace("%r", race.getName());
			}
			case AttributeRace.RACE_TYPE_GREEK:
			{
			//	if(!isBandit) this.name = a_titles[world.rand.nextInt(a_titles.length)].replace("%n", titleName).replace("%r", race.getName());
			//	else this.name = a_b_titles[world.rand.nextInt(a_b_titles.length)].replace("%n", titleName).replace("%r", race.getName());
			}
			case AttributeRace.RACE_TYPE_FRANK:
			{
			//	if(!isBandit) this.name = a_titles[world.rand.nextInt(a_titles.length)].replace("%n", titleName).replace("%r", race.getName());
			//	else this.name = a_b_titles[world.rand.nextInt(a_b_titles.length)].replace("%n", titleName).replace("%r", race.getName());
			}
			case AttributeRace.RACE_TYPE_BRITON:
			{
			//	if(!isBandit) this.name = a_titles[world.rand.nextInt(a_titles.length)].replace("%n", titleName).replace("%r", race.getName());
			//	else this.name = a_b_titles[world.rand.nextInt(a_b_titles.length)].replace("%n", titleName).replace("%r", race.getName());
			}
			break;
		}
	}
	
	public void setBandit(boolean bandit)
	{
		isBandit = bandit;
	}
	
	public boolean getBandit()
	{
		return isBandit;// = bandit;
	}
	
	public Vec3i getCenter() {
		// TODO Auto-generated method stub
		return center;
	}

	//return a constant from now
	public int getVillageRadius() {
		// TODO Auto-generated method stub
		return radius;
	}

	public boolean messageSent() {
		// TODO Auto-generated method stub
		return messageSend ;
	}
	
	public void setMessageSent(boolean sent) {
		// TODO Auto-generated method stub
		messageSend = sent;
	}

	public void addRuler(EntityHumanVillager ruler) {
		this.ruler = ruler;
	}

	public void update() {
		// TODO Auto-generated method stub
		
	}

	/*public void addMember(EntityHumanVillager entityHumanVillager) {
		members.add(entityHumanVillager);
	}*/

}
