package net.theexceptionist.coherentvillages.main.entity.render;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.theexceptionist.coherentvillages.main.Resources;
import net.theexceptionist.coherentvillages.main.entity.EntityDrachen;
import net.theexceptionist.coherentvillages.main.entity.model.ModelDrachen;
import net.theexceptionist.coherentvillages.main.entity.model.ModelDrachenOld;

@SideOnly(Side.CLIENT)
public class RenderDrachen extends RenderLiving<EntityDrachen>
{
	public static final ResourceLocation TEXTURE = new ResourceLocation(Resources.MODID, "textures/entity/drachen/drachen.png");
	
	public RenderDrachen(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelDrachen(), 0);
		// TODO Auto-generated constructor stub
	}
	
	 public void doRender(EntityDrachen entity, double x, double y, double z, float entityYaw, float partialTicks)
	 {
	     super.doRender(entity, x, y, z, entityYaw, partialTicks);
	    
	 }
	 
	public ModelDrachen getMainModel()
	 {
	        return (ModelDrachen)super.getMainModel();
	 }

	@Override
	protected ResourceLocation getEntityTexture(EntityDrachen entity) {
		// TODO Auto-generated method stub
		return TEXTURE;
	}

}
