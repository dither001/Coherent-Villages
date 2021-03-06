package net.theexceptionist.coherentvillages.main.items;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.theexceptionist.coherentvillages.main.Resources;

@SideOnly(Side.CLIENT)
public class RenderWeaponThrowable<T extends Entity> extends Render<T>
	{
		//private ResourceLocation texture = new ResourceLocation(Resources.MODID, "textures/entity/projectiles/throwing_axe.png");
		private Item weapon;
	    private final RenderItem itemRenderer;
	    private float rotateZ;

	    public RenderWeaponThrowable(RenderManager renderManagerIn, Item itemIn, RenderItem itemRendererIn)
	    {
	        super(renderManagerIn);
	        this.weapon = itemIn;
	        this.itemRenderer = itemRendererIn;
	        rotateZ = 0.0f;
	    }

	    /**
	     * Renders the desired {@code T} type Entity.
	     */
	    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
	    {
	        GlStateManager.pushMatrix();
	        GlStateManager.translate((float)x, (float)y, (float)z);
	        GlStateManager.enableRescaleNormal();
	        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
	        GlStateManager.rotate((float)(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
	        GlStateManager.rotate(180.0F + rotateZ, 0.0F, 0.0F, 1.0f);
	        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

	        if (this.renderOutlines)
	        {
	            GlStateManager.enableColorMaterial();
	            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
	        }

	        this.itemRenderer.renderItem(this.getStackToRender(entity), ItemCameraTransforms.TransformType.GROUND);

	        if (this.renderOutlines)
	        {
	            GlStateManager.disableOutlineMode();
	            GlStateManager.disableColorMaterial();
	        }

	        GlStateManager.disableRescaleNormal();
	        GlStateManager.popMatrix();
	        rotateZ += 1f;
	        //System.out.println("Rotate: "+rotateZ);
	        super.doRender(entity, x, y, z, entityYaw, partialTicks);
	    }

	    public ItemStack getStackToRender(T entityIn)
	    {
	        return new ItemStack(this.weapon);
	    }

	    /**
	     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	     */
	    protected ResourceLocation getEntityTexture(Entity entity)
	    {
	    	return TextureMap.LOCATION_BLOCKS_TEXTURE;
	    }
	
}
	/*public void doRender(EntityWeaponThrowable entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
		/*GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float)(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        this.bindTexture(texture);

        if (this.renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        this.itemRenderer.renderItem(this.getStackToRender(entity), ItemCameraTransforms.TransformType.GROUND);

        if (this.renderOutlines)
        {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
	
    public ItemStack getStackToRender(EntityWeaponThrowable entityIn)
    {
        //return new ItemStack(this.item);
    }*/
