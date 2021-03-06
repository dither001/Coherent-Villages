package net.theexceptionist.coherentvillages.main.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelLemure extends ModelBase
{
	public ModelRenderer head;
	public ModelRenderer leftArm;
	public ModelRenderer rightArm;
	
	public ModelLemure()
	{
		this.head =  new ModelRenderer(this, 0, 0);
		this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0);
		this.head.setRotationPoint(0.0F, 0.0F , 0.0F);
		
		this.rightArm =  new ModelRenderer(this, 40, 16);
		this.rightArm.addBox(-3.0F, -2.0F, -3.0F, 4, 12, 4, 0);
		this.rightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		
		this.leftArm =  new ModelRenderer(this, 40, 16);
		this.leftArm.mirror = true;
		this.leftArm.addBox(-1.0F, -2.0F, -1.0F, 4, 12, 4, 0);
		this.leftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
	}
	
	 public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	    {
	        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
	        GlStateManager.pushMatrix();

	        this.head.render(scale);
	        this.leftArm.render(scale);
	        this.rightArm.render(scale);

	        GlStateManager.popMatrix();
	    }
	 
	 public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	    {
		 float f2 =  (float) -(Math.PI / 1.5);
		 this.leftArm.rotateAngleX = f2;
		 this.rightArm.rotateAngleX = f2;
	    }
}
