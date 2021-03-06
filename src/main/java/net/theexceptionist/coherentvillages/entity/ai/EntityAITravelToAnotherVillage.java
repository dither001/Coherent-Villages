package net.theexceptionist.coherentvillages.entity.ai;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.theexceptionist.coherentvillages.main.entity.EntityHumanVillager;

public class EntityAITravelToAnotherVillage extends EntityAIBase {
	private EntityHumanVillager villager;
	private Village targetVillage;
    private double movePosX;
    private double movePosY;
    private double movePosZ;
	
	public EntityAITravelToAnotherVillage(EntityHumanVillager villager)
	{
		this.villager = villager;
	}
	
	@Override
	public boolean shouldExecute() {
		// TODO Auto-generated method stub
		if(targetVillage != null) return false;
		
		World world = villager.world;
		int numVillages = world.villageCollection.getVillageList().size();
		
		if(numVillages > 0) targetVillage = world.villageCollection.getVillageList().get(world.rand.nextInt(numVillages));
		
		if(targetVillage == null)
		{
			return false;
		}
		else
		{
			BlockPos pos = targetVillage.getCenter();
			Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.villager, 16, 7, new Vec3d((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()));
    		
			System.out.println("["+this.villager.getTitle()+"] Traveling to - X: "+pos.getX()+" Y: "+pos.getY()+" Z: "+pos.getZ()+" From - X: "+this.villager.posX+" Y: "+this.villager.posY+" Z: "+this.villager.posZ);
			
			if (vec3d == null)
            {
                return false;
            }
            else
            {
            	this.movePosX = vec3d.x;
                this.movePosY = vec3d.y;
                this.movePosZ = vec3d.z;
                return true;
            }
		}
	}
	
	 /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
    	
    	if(!this.villager.getNavigator().noPath())
    	{
        	System.out.println("["+this.villager.getTitle()+"] has no path!");
    		this.targetVillage = null;
    		return false;
    	}
    	
    	return true;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.villager.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.villager.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue());
    }

}
