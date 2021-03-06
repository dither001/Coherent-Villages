package net.theexceptionist.coherentvillages.main.entity.spells;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.theexceptionist.coherentvillages.main.entity.EntityHumanVillager;

public class SpellThunderStorm extends Spell {

	public SpellThunderStorm(String name, int type) {
		super(name, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(EntityLivingBase caster) {
		World world = null;
		if(caster instanceof EntityHumanVillager) 
		{
			world  = ((EntityHumanVillager)caster).world;
				
			//if(caster instanceof EntityPlayer)  world  = ((EntityPlayer)caster).world;
			
			WorldInfo info = world.getWorldInfo();
			
			if(!world.isRaining() && caster.isBurning())
			{
				info.setCleanWeatherTime(0);
				info.setRainTime(0);
				info.setThunderTime(120000);
				info.setRaining(true);
				info.setThundering(true);
			}
		}
	}

}
