package net.theexceptionist.coherentvillages.main.entity.spells;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.theexceptionist.coherentvillages.main.entity.EntityHumanVillager;

public class SpellExtendLife extends Spell {
	private int coolDown = 0, coolDownSet;
	private int health;
	private int duration;
	
	
	public SpellExtendLife(String name, int type, int health, int coolDown, int duration) {
		super(name, type);
		this.health = health;
		this.coolDownSet = coolDown;
		this.duration = duration;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void execute(EntityLivingBase caster) {
		// TODO Auto-generated method stub
		if(caster instanceof EntityHumanVillager)
		{
			if(this.coolDown <= 0)
			{
				EntityHumanVillager villager = (EntityHumanVillager) caster;
				
				if(villager.inCombat())
				{
					villager.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, duration, 3/*health*/));
					
					//System.out.println("Absorb!");
					
					this.coolDown = coolDownSet;
				}
				
				//villager.spawnCloudParticle();
				//System.out.println("Absorb: "+(villager.getActivePotionEffect(MobEffects.ABSORPTION) != null));
			}
			else
			{
				this.coolDown--;
			}
		}
	}
}
