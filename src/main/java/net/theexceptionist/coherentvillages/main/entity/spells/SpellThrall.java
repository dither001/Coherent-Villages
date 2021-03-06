package net.theexceptionist.coherentvillages.main.entity.spells;

import net.minecraft.entity.EntityLivingBase;
import net.theexceptionist.coherentvillages.main.entity.EntityHumanVillager;

public class SpellThrall extends Spell {

	private int duration;
	private int coolDown, coolDownSet;

	public SpellThrall(String name, int type, int coolDown,
			int duration) {
		super(name, type);
		this.duration = duration;
		this.coolDownSet = coolDown;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(EntityLivingBase caster) {
		if(coolDown <= 0 && caster instanceof EntityHumanVillager)
		{
			EntityHumanVillager human = (EntityHumanVillager)caster;
			EntityLivingBase target = human.getAttackTarget();
			
			if(target != null && target instanceof EntityHumanVillager)
			{
				EntityHumanVillager villager = (EntityHumanVillager) target;
				
				villager.setMaster(caster);
				villager.setControlTime(duration);
				human.setLiege(villager);
				
				//System.out.println(villager.getName()+" is a thrall! At: ("+villager.posX+","+villager.posY+","+villager.posZ+")");
				coolDown = coolDownSet;
				
				human.setAttackTarget(null);
			}
		}
		else
		{
			coolDown--;
		}
		
		//if(caster instanceof EntityHumanVillager) ((EntityHumanVillager)caster).spawnEndParticle();

	}

}
