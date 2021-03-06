package net.theexceptionist.coherentvillages.main.items;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityWeaponThrowable extends EntityThrowable
{
		private Item weapon = null;
		private double damage = 0;
		
	    public EntityWeaponThrowable(World worldIn)
	    {
	        super(worldIn);
	    }

	    public EntityWeaponThrowable(World worldIn, EntityLivingBase throwerIn)
	    {
	        super(worldIn, throwerIn);
	    }

	    public EntityWeaponThrowable(World worldIn, double x, double y, double z)
	    {
	        super(worldIn, x, y, z);
	    }

	    /*public static void registerFixesSnowball(DataFixer fixer)
	    {
	        EntityThrowable.registerFixesThrowable(fixer, "Snowball");
	    }*/

	    public EntityWeaponThrowable(World worldIn, EntityLivingBase throwerIn, Item weapon, int damage) {
			// TODO Auto-generated constructor stub
	    	 super(worldIn, throwerIn);
	    	 this.weapon = weapon;
	    	 this.damage = throwerIn.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue() + damage;
		}

		/**
	     * Handler for {@link World#setEntityState}
	     */
	    @SideOnly(Side.CLIENT)
	    public void handleStatusUpdate(byte id)
	    {
	        if (id == 3)
	        {
	            for (int i = 0; i < 8; ++i)
	            {
	                this.world.spawnParticle(EnumParticleTypes.CRIT, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
	            }
	        }
	    }

	    /**
	     * Called when this EntityThrowable hits a block or entity.
	     */
	    protected void onImpact(RayTraceResult result)
	    {
	    	if(result.entityHit != thrower)
	    	{
		    	if(result.entityHit instanceof EntityLivingBase) this.damage += EnchantmentHelper.getModifierForCreature(new ItemStack(weapon), ((EntityLivingBase) result.entityHit).getCreatureAttribute());
		        //System.out.println(damage);
		    	if (result.entityHit != null && this.weapon != null)
		        {
		        	
		        	float i = (float) damage;
		        	
		            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), i);
		        }
	
		        if (!this.world.isRemote)
		        {
		            this.world.setEntityState(this, (byte)3);
		            this.setDead();
		        }
	    	}
	    }
}
