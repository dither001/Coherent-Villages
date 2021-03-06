package net.theexceptionist.coherentvillages.main.entity.spells;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.theexceptionist.coherentvillages.main.entity.EntityHumanVillager;

public class SpellTransformAura extends Spell {

	private int radius;
	private float percent;
	private int changeID;
	
	public static final int ZOMBIE = 0;
	public static final int PIG = 1;

	public SpellTransformAura(String name, int type, int radius, float percent, int changeID) {
		super(name, type);
		this.radius = radius;
		this.percent = percent;
		this.changeID = changeID;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(EntityLivingBase caster) {
		// TODO Auto-generated method stub
		if(caster instanceof EntityHumanVillager)
		{
			EntityHumanVillager villager = (EntityHumanVillager) caster;
			
			List<Entity> entities = caster.world.getEntitiesWithinAABBExcludingEntity(caster, caster.getEntityBoundingBox().grow(radius).offset(-1, 0, -1));
				
			for(Entity entity : entities)
			{
				if(entity instanceof EntityLiving)
				{
					if(((EntityLiving) entity).getHealth() < ((EntityLiving) entity).getMaxHealth() * percent)
					{	
						if(entity instanceof EntityVillager)
						{						
							EntityVillager entityvillager = (EntityVillager)entity;
							
							switch(changeID)
							{
								case ZOMBIE:
								{
									 EntityZombieVillager entityzombievillager = new EntityZombieVillager(caster.world);
							            entityzombievillager.copyLocationAndAnglesFrom(entityvillager);
							            caster.world.removeEntity(entityvillager);
							            entityzombievillager.onInitialSpawn(caster.world.getDifficultyForLocation(new BlockPos(entityzombievillager)), null);
							            entityzombievillager.setProfession(entityvillager.getProfession());
							            entityzombievillager.setChild(entityvillager.isChild());
							            entityzombievillager.setNoAI(entityvillager.isAIDisabled());
	
							            if (entityvillager.hasCustomName())
							            {
							                entityzombievillager.setCustomNameTag(entityvillager.getCustomNameTag());
							                entityzombievillager.setAlwaysRenderNameTag(entityvillager.getAlwaysRenderNameTag());
							            }
	
							            caster.world.spawnEntity(entityzombievillager);
							            caster.world.playEvent((EntityPlayer)null, 1026, new BlockPos(caster), 0);
								}
								break;
							}
							
					//		entityvillager.spawnExplosionParticle();
							entityvillager.setDead();
						}
					}
				}
			}
			
		}
	}

}
