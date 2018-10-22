package net.theexceptionist.coherentvillages.main.events;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.theexceptionist.coherentvillages.main.entity.EntityHumanVillager;
import net.theexceptionist.coherentvillages.main.entity.attributes.AttributeRace;

public class EventBanditSkirmish extends Event {
	public EventBanditSkirmish(Random rand, boolean startInDay, int chance, int range, int raceID) {
		super(rand, startInDay, chance, range, raceID);
		this.style = new Style();
		this.style.setColor(TextFormatting.GREEN);
		this.eventMessage = new TextComponentString("You hear a battle going on somewhere nearby....");
		this.eventMessage.setStyle(style);
	}

	@Override
	public void execute() {
		int count = this.rand.nextInt(4) + 2;
		if(this.player != null)
		{
			double x = this.player.posX + range;
			double z = this.player.posZ + range;
			
			Vec3d vec = this.polarRotate(x, z, this.rand.nextInt(360));
			
			double y = this.player.world.getTopSolidOrLiquidBlock(new BlockPos(vec.x, 80, vec.z)).getY();
			BlockPos spawn = new BlockPos(x, y, z);
			World worldIn = this.player.world;
			
			for(int i = 0; i < count; i++)
			{
				EntityHumanVillager soldier = new EntityHumanVillager(worldIn, raceID, AttributeRace.getFromIDRace(raceID).getRandomBandit(worldIn), EntityHumanVillager.getRandomGender(worldIn), false);                            
            	soldier.setLocationAndAngles((double)x + 0.5D, (double)y, (double)z + 0.5D, 0.0F, 0.0F);
            	worldIn.spawnEntity(soldier);
            	
				EntityHumanVillager bandit = new EntityHumanVillager(worldIn, raceID, AttributeRace.getFromIDRace(raceID).getRandomBandit(worldIn), EntityHumanVillager.getRandomGender(worldIn), false);                            
            	bandit.setLocationAndAngles((double)x + 0.5D, (double)y, (double)z + 0.5D, 0.0F, 0.0F);
            	worldIn.spawnEntity(bandit);
			}
			
			this.player.sendMessage(eventMessage);
			
			this.spawned = true;
		}
		else 
		{
			return;
		}
	}

	@Override
	public void reset() {
		if(this.player != null && this.player.world.isDaytime() != startInDay)
		{
			this.spawned = false;
		}
	}

}
