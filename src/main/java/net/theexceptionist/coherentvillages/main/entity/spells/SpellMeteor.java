package net.theexceptionist.coherentvillages.main.entity.spells;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.theexceptionist.coherentvillages.main.entity.EntityHumanVillager;
import net.theexceptionist.coherentvillages.main.entity.projectile.EntityModFireballSmall;
import net.theexceptionist.coherentvillages.main.entity.projectile.EntityModLargeFireball;

public class SpellMeteor extends Spell{
	private int count, power;
	private boolean isVolley;
	private boolean largeFireball;
	private int burst, burstSet;
	private int coolDown, coolDownSet;
	
	public SpellMeteor(String name, int type, int count, int power, int burst, int coolDown, boolean isVolley, boolean isGreater) {
		super(name, type);
		this.count = count;
		this.isVolley = isVolley;
		this.largeFireball = isGreater;
		this.power = power;
		this.burstSet = burst;
		this.burst = this.burstSet;
		this.coolDownSet = coolDown;
	}

	@Override
	public void execute(EntityLivingBase caster) {
		if(this.coolDown <= 0 && caster instanceof EntityLiving)
		{
			double d1 = 4.0D;
	        Vec3d vec3d = caster.getLook(1.0F);
	        EntityLivingBase entitylivingbase = ((EntityLiving)caster).getAttackTarget();
	        World world = caster.world;
	        
	        double x = entitylivingbase.posX;// - (caster.posX + vec3d.x * 4.0D);
	        double y = entitylivingbase.posY + 10;//getEntityBoundingBox().minY + (double)(entitylivingbase.height / 2.0F) - (0.5D + caster.posY + (double)(caster.height / 2.0F));
	        double z = entitylivingbase.posZ;// - (caster.posZ + vec3d.z * 4.0D);
	        
	        world.playSound((EntityPlayer)null, caster.posX, caster.posY, caster.posZ, SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.NEUTRAL, 2.0F, 0.5F + world.rand.nextFloat() * 0.2F);
		    
	        int numFireballs = count;
	        if(isVolley) numFireballs = world.rand.nextInt(count) + 1;
	
	        for(int i = 0; i < numFireballs; i++)
	        {
	        	EntityFireball entitylargefireball = null;
		        
	        	if(largeFireball)
	        	{
	        		entitylargefireball = new EntityModLargeFireball(world, x, y, z, 0, -0.2, 0);
	        		
	        		if(caster instanceof EntityHumanVillager)
	        		{
	        			int rank = ((EntityHumanVillager)caster).getVocation().getRank();
	        			((EntityModLargeFireball)entitylargefireball).explosionPower = rank;
	        		}
	        		else ((EntityModLargeFireball)entitylargefireball).explosionPower = power;
	        		
	        		((EntityModLargeFireball)entitylargefireball).setShooter(caster);
	        	}
	        	else
	        	{
	        		entitylargefireball = new EntityModFireballSmall(world, x, y, z, 0, -0.2, 0);
	        		((EntityModFireballSmall)entitylargefireball).setShooter(caster);
	        	}
	        	
	        	this.spawnExplosionParticle(world, caster, x, y, z);
	        	
		        //entitylargefireball.posX = x;//caster.posX + vec3d.x * 4.0D;
		        //entitylargefireball.posY = y;//caster.posY + (double)(caster.height / 2.0F) + 0.5D;
		        //entitylargefireball.posZ = z;//caster.posZ + vec3d.z * 4.0D;
		        
		      //  System.out.println("Fireball Motion - X: ("+entitylargefireball.motionX+", "+entitylargefireball.accelerationX+") Y: ("+entitylargefireball.motionY+", "+entitylargefireball.accelerationY+") Z: ("+entitylargefireball.motionZ+", "+entitylargefireball.accelerationZ+")");
		        
		        world.spawnEntity(entitylargefireball);
		        this.burst--;
	        }
	        
	        if(burst <= 0)
	        {
	        	this.coolDown = this.coolDownSet;
	        	this.burst = this.burstSet;
	        }
		}
		else
		{
			this.coolDown--;
		}
	}

}
