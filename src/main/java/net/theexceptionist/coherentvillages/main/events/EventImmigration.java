package net.theexceptionist.coherentvillages.main.events;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.theexceptionist.coherentvillages.main.entity.EntityHumanVillager;
import net.theexceptionist.coherentvillages.main.entity.attributes.AttributeFaction;
import net.theexceptionist.coherentvillages.main.entity.attributes.AttributeRace;
import net.theexceptionist.coherentvillages.main.entity.attributes.AttributeVocation;

public class EventImmigration extends Event {
	protected int limit;
	
	public EventImmigration(String name, boolean startInDay, int range, int count, int change, int limit) {
		super(name, startInDay, range, count, change);
		this.limit = limit;
		this.style = new Style();
		this.style.setColor(TextFormatting.GREEN);
		this.eventMessage = "Immigrants have just arrived at &f!";
	}

	@Override
	public boolean execute(World world, boolean day, BlockPos spawn, AttributeRace race, AttributeFaction faction) {
		if(race == null || faction == null) return false;
		
		boolean executed = true;	
		int roll = world.rand.nextInt(100);
		if(roll > this.chance) return (executed = false);
		
		int x = spawn.getX() + (world.rand.nextInt(100) < 50 ? range : -range);
		int z = spawn.getZ() + (world.rand.nextInt(100) < 50 ? range : -range);
		int y = world.getTopSolidOrLiquidBlock(new BlockPos(x, 80, z)).getY();
		int count = world.rand.nextInt(this.count) + 1;
		int raceID = race.getID();
		Village village = faction.getRuler().getVillage();
		
		if(village != null && village.getNumVillagers() < limit 
				&& village.getNumVillageDoors() * 2 > village.getNumVillagers() + count)
		{
			if(day == startInDay)
			{		
				for(int i = 0; i < count; i++)
				{
					AttributeVocation job = AttributeRace.getFromIDRace(raceID).getRandomVillager(world);
					if(job == null) continue;
					
					EntityHumanVillager villager = new EntityHumanVillager(world, raceID, job, EntityHumanVillager.getRandomGender(world), false);                            
	            	villager.setLocationAndAngles((double)x + 0.5D, (double)y, (double)z + 0.5D, 0.0F, 0.0F);
	            	world.spawnEntity(villager);
				}
				
				this.setEventMessage(faction);
			}
			else
			{
				executed = false;
			}
		}
		else
		{
			executed = false;
		}
		
		return executed;
	}

	@Override
	public void reset(boolean day) {
		// TODO Auto-generated method stub
		
	}
	

}
