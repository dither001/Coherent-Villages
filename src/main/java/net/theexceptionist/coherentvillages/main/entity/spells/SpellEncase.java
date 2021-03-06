package net.theexceptionist.coherentvillages.main.entity.spells;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.theexceptionist.coherentvillages.main.entity.EntityHumanVillager;

public class SpellEncase extends Spell {

	private int coolDown, setCoolDown;
	private Block encasingBlock;
	private BlockPos castSpot = null;
	private int radius;
	private boolean casted = false;
	private int duration, setDuration;

	
	public SpellEncase(String name, int type, int coolDown, int duration, int radius, Block encasingBlock) {
		super(name, type);
		this.coolDown = coolDown;
		this.setCoolDown = coolDown;
		this.duration = duration;
		this.setDuration = duration;
		this.radius = radius;
		this.encasingBlock = encasingBlock;
	}

	@Override
	public void execute(EntityLivingBase caster) {
		// TODO Auto-generated method stub
		if(coolDown <= 0 && caster instanceof EntityLiving && duration >= setDuration)
		{
			EntityLivingBase target = ((EntityLiving)caster).getAttackTarget();
			castSpot = target.getPosition().up();
			
			for(int x = -radius; x < radius; x++)
			{
				for(int y = -radius; y < radius; y++)
				{
					for(int z = -radius; z < radius; z++)
					{
						BlockPos spawnPos = target.getPosition().up().add(x, y, z);
						
						if(caster.world.isAirBlock(spawnPos))
						{
							caster.world.setBlockState(spawnPos, encasingBlock.getDefaultState());
						}
					}
				}
			}
			
			coolDown = this.setCoolDown;
			duration = 0;
			casted  = true;	
			
			if(target instanceof EntityHumanVillager) ((EntityHumanVillager)target).spawnExplosionParticle();
		}
		else
		{
			coolDown--;
		}
	}
	
	public void reset(EntityLivingBase caster)
	{
		if(duration < setDuration)
		{
			duration++;
		}
		else if(castSpot != null)
		{
			for(int x = -radius; x < radius; x++)
			{
				for(int y = -radius; y < radius; y++)
				{
					for(int z = -radius; z < radius; z++)
					{
						BlockPos spawnPos = castSpot.add(x, y, z);
						
						if(caster.world.getBlockState(spawnPos).getBlock() == encasingBlock)
						{
							caster.world.destroyBlock(spawnPos, false);//.setBlockState(spawnPos, encasingBlock.getDefaultState());
						}
					}
				}
			}
			
			castSpot = null;
		}
	}

}
