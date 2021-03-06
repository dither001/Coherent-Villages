package net.theexceptionist.coherentvillages.main.entity.spells;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.theexceptionist.coherentvillages.main.entity.EntityHumanVillager;

public class SpellDrop extends Spell {

	private int height;
	private int coolDownSet, coolDown;

	public SpellDrop(String name, int type, int coolDown, int height) {
		super(name, type);
		this.coolDownSet = coolDown;
		
		this.height = height;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(EntityLivingBase caster) {
		// TODO Auto-generated method stub
		if(coolDown <= 0)
		{
			if(caster instanceof EntityLiving)
			{
				EntityLivingBase entity = ((EntityLiving)caster).getAttackTarget();
				
				if(entity instanceof EntityHumanVillager) ((EntityHumanVillager)entity).spawnExplosionParticle();
				entity.setPosition(entity.posX, entity.posY + height, entity.posZ);
				if(entity instanceof EntityHumanVillager) ((EntityHumanVillager)entity).spawnExplosionParticle();
				
				coolDown = coolDownSet;
				
				
			}
		}
		else
		{
			coolDown--;
		}
	}

}
