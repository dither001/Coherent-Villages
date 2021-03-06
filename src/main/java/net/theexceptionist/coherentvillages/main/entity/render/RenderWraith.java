package net.theexceptionist.coherentvillages.main.entity.render;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.theexceptionist.coherentvillages.main.Resources;
import net.theexceptionist.coherentvillages.main.entity.EntityWraith;

public class RenderWraith extends RenderBiped<EntityWraith>
{
	public static final String GHOST = "textures/entity/villager/human/latin_m/ghost_0.png";

	public RenderWraith(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelZombie(), 0.0f);

	}
	
	public void doRender(EntityWraith entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
    	super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
	
	@Override
	protected ResourceLocation getEntityTexture(EntityWraith entity) {
		return new ResourceLocation(Resources.MODID, GHOST);
	}

}
