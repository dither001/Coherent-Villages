package net.theexceptionist.coherentvillages.main.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.theexceptionist.coherentvillages.main.entity.EntityHumanVillager;

public class EntityModLargeFireball  extends EntityLargeFireball
{
    public int explosionPower = 1;

    public EntityModLargeFireball(World worldIn)
    {
        super(worldIn);
    }

    @SideOnly(Side.CLIENT)
    public EntityModLargeFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
    {
        super(worldIn, x, y, z, accelX, accelY, accelZ);
    }

    public EntityModLargeFireball(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
    {
        super(worldIn, shooter, accelX, accelY, accelZ);
    }
    
    
    public void setShooter(EntityLivingBase shooter)
    {
    	this.shootingEntity = shooter;
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected void onImpact(RayTraceResult result)
    {
        if (!this.world.isRemote)
        {	

        	EntityHumanVillager villager = null;
        	boolean flag = false;
            if (result.entityHit != null)
            {

	        	if(this.shootingEntity instanceof EntityHumanVillager)
	        	{
	        		villager = (EntityHumanVillager) shootingEntity;
	    			//System.out.println("Shooting Entity: "+(this.shootingEntity != null)+" Fireball: "+(this != null)+" Hit: "+(result.entityHit != null));
	                flag = result.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), (float) villager.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
	            	if(villager.isDestructive())
	            	{
	            		this.world.newExplosion((Entity)null, this.posX, this.posY, this.posZ, (float)this.explosionPower, flag, flag);
	            		result.entityHit.setFire(5);	
	            	}
	        	}
	        	else
	        	{
	    			//System.out.println("Shooting Entity: "+(this.shootingEntity != null)+" Fireball: "+(this != null)+" Hit: "+(result.entityHit != null));
	        		flag = result.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 5.0f);
	        	}
        	
            }
            
        	if(this.shootingEntity instanceof EntityHumanVillager)
        	{
        		villager = (EntityHumanVillager) shootingEntity;
            	if(villager.isDestructive())
            	{
            		this.world.newExplosion((Entity)null, this.posX, this.posY, this.posZ, (float)this.explosionPower, true, true);            	
            	}
        	}

        	
            this.setDead();
        }
    }

    public static void registerFixesLargeFireball(DataFixer fixer)
    {
        EntityFireball.registerFixesFireball(fixer, "Fireball");
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("ExplosionPower", this.explosionPower);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        if (compound.hasKey("ExplosionPower", 99))
        {
            this.explosionPower = compound.getInteger("ExplosionPower");
        }
    }
}