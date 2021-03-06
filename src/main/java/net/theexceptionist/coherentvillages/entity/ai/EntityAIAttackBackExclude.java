package net.theexceptionist.coherentvillages.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.math.AxisAlignedBB;
import net.theexceptionist.coherentvillages.main.entity.EntityHumanVillager;
import net.theexceptionist.coherentvillages.main.entity.attributes.AttributeVocation;

public class EntityAIAttackBackExclude extends EntityAITarget
{
    private final boolean entityCallsForHelp;
    /** Store the previous revengeTimer value */
    private int revengeTimerOld;
    private final Class<?>[] excludedReinforcementTypes;
    private EntityHumanVillager creatureIn;

    public EntityAIAttackBackExclude(EntityHumanVillager creatureIn, boolean entityCallsForHelpIn, Class<?>... excludedReinforcementTypes)
    {
        super(creatureIn, true);
        this.entityCallsForHelp = entityCallsForHelpIn;
        this.excludedReinforcementTypes = excludedReinforcementTypes;
        this.creatureIn = creatureIn;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
    	int villagerHostile = -1;
        int i = this.taskOwner.getRevengeTimer();
        EntityLivingBase entitylivingbase = this.taskOwner.getRevengeTarget();
        
        if(entitylivingbase != null && entitylivingbase instanceof EntityHumanVillager)
        {
        	EntityHumanVillager potentialBandit = (EntityHumanVillager) entitylivingbase;
        	
        	if(creatureIn.getVocation().getType() != AttributeVocation.CLASS_BANDIT)
        	{
	        	if(potentialBandit.getVocation().getType() == AttributeVocation.CLASS_BANDIT)
	        	{
	        		villagerHostile = 1;
	        	}
	        	else
	        	{
	        		villagerHostile = 0;
	        	}
        	}
        	else
        	{
        		villagerHostile = 1;
        	}
        }
        //return entitylivingbase != null;
        return i != this.revengeTimerOld && entitylivingbase != null && this.isSuitableTarget(entitylivingbase, false) && villagerHostile != 0 ? true : false ;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
    	 this.taskOwner.setAttackTarget(this.taskOwner.getRevengeTarget());
    	// System.out.println(taskOwner.getName()+" "+this.taskOwner.getAttackTarget().getName()+" "+this.taskOwner.getRevengeTarget().getName());
         this.target = this.taskOwner.getAttackTarget();
         this.revengeTimerOld = this.taskOwner.getRevengeTimer();
         this.unseenMemoryTicks = 300;

         if (this.entityCallsForHelp)
         {
             this.alertOthers();
         }

         super.startExecuting();
    }

    protected void alertOthers()
    {
        double d0 = this.getTargetDistance();

        for (EntityCreature entitycreature : this.taskOwner.world.getEntitiesWithinAABB(this.taskOwner.getClass(), (new AxisAlignedBB(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + 1.0D, this.taskOwner.posY + 1.0D, this.taskOwner.posZ + 1.0D)).expand(d0, 10.0D, d0)))
        {
            if (this.taskOwner != entitycreature && entitycreature.getAttackTarget() == null && (!(this.taskOwner instanceof EntityTameable) || ((EntityTameable)this.taskOwner).getOwner() == ((EntityTameable)entitycreature).getOwner()) && !entitycreature.isOnSameTeam(this.taskOwner.getAttackTarget()))
            {
                boolean flag = false;

                for (Class<?> oclass : this.excludedReinforcementTypes)
                {
                    if (entitycreature.getClass() == oclass)
                    {
                        flag = true;
                        break;
                    }
                }

                if (!flag)
                {
                    this.setEntityAttackTarget(entitycreature, this.taskOwner.getAttackTarget());
                }
            }
        }
    }

    protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn)
    {
        creatureIn.setAttackTarget(entityLivingBaseIn);
    }
}
