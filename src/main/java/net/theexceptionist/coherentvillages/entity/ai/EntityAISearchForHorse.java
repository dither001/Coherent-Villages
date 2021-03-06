package net.theexceptionist.coherentvillages.entity.ai;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.math.MathHelper;
import net.theexceptionist.coherentvillages.main.entity.EntityHumanVillager;

public class EntityAISearchForHorse extends EntityAIBase{
    private final EntityHumanVillager host;
    private final double entityMoveSpeed;
    private final double maxDistance;
    private final double radius;
	private EntityLivingBase target;
	private boolean moving = false;
    
    public EntityAISearchForHorse(EntityHumanVillager host, double movespeed, double radius, double maxDistanceIn)
    {
        this.host = host;
        this.radius = radius;
        this.entityMoveSpeed = movespeed;
        this.maxDistance = maxDistanceIn;
        this.setMutexBits(1);
        
    }
    
    public void resetTask()
    {
    	super.resetTask();
    	moving = false;
    }


	/**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
    	double d0 = radius * 2;
    	//System.out.println("Working 1");
        List<AbstractHorse> list = this.host.world.getEntitiesWithinAABB(AbstractHorse.class, this.host.getEntityBoundingBox().expand(d0, 8.0D, d0).offset(-d0/2, -4, -d0/2));
   
        if (list.isEmpty() || host.isRiding())
        {
        	//System.out.println("empty");
            return false;
        }
        else
        {
        	for(int i = 0; i < list.size(); i++){
        		//list = this.host.world.getEntitiesWithinAABB(EntityVillager.class, this.host.getEntityBoundingBox().expand(d0, 4.0D, d0));
	            this.target = (EntityLivingBase)list.get(i);
	            AbstractHorse horse = null;
	            
	            //System.out.println("Target: "+this.target.getCustomNameTag()+" Health "+this.target.getHealth()+"/"+this.target.getMaxHealth());
	            
	        	if(this.target instanceof AbstractHorse)
            	{
            		horse = (AbstractHorse) this.target;
            		return !horse.isBeingRidden(); 
            	}
        	}
        }
		return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return this.shouldExecute() || !this.host.getNavigator().noPath();
    }
    /**
     * Updates the task
     */
    public void updateTask()
    {
    	if(this.target != null){
	        double d0 = this.host.getDistanceSq(this.target.posX, this.target.getEntityBoundingBox().minY, this.target.posZ);
	        boolean flag = true;//this.host.getEntitySenses().canSee(this.attackTarget);
	       //Unused for now
	        //System.out.println(this.host.getCustomNameTag()+" healing start");
	        //this.host.setClient(target);
	
	        /*if (d0 <= (double)this.maxAttackDistance)
	        {
	            this.host.getNavigator().clearPath();
	            System.out.println("Time: "+this.seeTime+" Distance: "+d0+" Max Distance: "+this.maxAttackDistance);
	        }
	        else
	        {
	            this.host.getNavigator().tryMoveToEntityLiving(this.target, this.entityMoveSpeed);
	            System.out.println("moving start");
	        }*/
	        this.host.getNavigator().tryMoveToEntityLiving(this.target, this.entityMoveSpeed);
	
	        this.host.getLookHelper().setLookPositionWithEntity(this.target, 30.0F, 30.0F);
	
	        if (d0 < this.maxDistance)
	        {
	
	            double f = MathHelper.sqrt(d0) / this.radius;
	            float lvt_5_1_ = (float)MathHelper.clamp(f, 0.1F, 1.0F);
	            
	            if(!moving)
	            { 	            
	            	this.host.getNavigator().tryMoveToEntityLiving(this.target, 1D);
	            	moving = true;
	            }
	            
	            if(d0 <= 5 && target instanceof AbstractHorse)
	            {
	            	this.host.setRidingHorse((AbstractHorse) target);
	            	//System.out.println("Riding");
	            }
	            //System.out.println("Healing: "+this.target);
	           // this.rangedAttackTime = MathHelper.floor(f * (float)(this.maxRangedAttackTime - this.attackIntervalMin) + (float)this.attackIntervalMin);
	        }
	    }
    }
}

