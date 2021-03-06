package net.theexceptionist.coherentvillages.main.entity.render;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.theexceptionist.coherentvillages.main.entity.EntityHumanVillager;
import net.theexceptionist.coherentvillages.main.entity.model.ModelHumanVillager;
import net.theexceptionist.coherentvillages.main.items.ItemModShield;

@SideOnly(Side.CLIENT)
public class RenderHumanVillager extends RenderBiped<EntityHumanVillager>
{
	public static final String LATIN_SKIN_M = "textures/entity/villager/human/latin_m/";
	
	public static final String LATIN_SKIN_F = "textures/entity/villager/human/latin_f/";

	public static final String NORD_SKIN_M = "textures/entity/villager/human/nord_m/";

	public static final String NORD_SKIN_F = "textures/entity/villager/human/nord_f/";
	
	public static final String GERMAN_SKIN_M = "textures/entity/villager/human/german_m/";

	public static final String GERMAN_SKIN_F = "textures/entity/villager/human/german_f/";

	public static final String SLAV_SKIN_M = "textures/entity/villager/human/slav_m/";

	public static final String SLAV_SKIN_F = "textures/entity/villager/human/slav_f/";
	
	public static final String ARAB_SKIN_M = "textures/entity/villager/human/arab_m/";
	
	public static final String ARAB_SKIN_F = "textures/entity/villager/human/arab_f/";

	public static final String GREEK_SKIN_M = "textures/entity/villager/human/greek_m/";
	public static final String GREEK_SKIN_F = "textures/entity/villager/human/greek_f/";
	
	public static final String FRANK_SKIN_M = "textures/entity/villager/human/frank_m/";
	public static final String FRANK_SKIN_F = "textures/entity/villager/human/frank_f/";
	
	public static final String BRITON_SKIN_M = "textures/entity/villager/human/briton_m/";
	public static final String BRITON_SKIN_F = "textures/entity/villager/human/briton_f/";

		public static final String MONGOL_SKIN_M = "textures/entity/villager/human/mongol_m/";
	public static final String MONGOL_SKIN_F = "textures/entity/villager/human/mongol_f/";

	public static final String VAMPIRE_SKIN_M = "textures/entity/villager/human/vampire_m/";
	public static final String VAMPIRE_SKIN_F = "textures/entity/villager/human/vampire_f/";
	
	public RenderHumanVillager(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelHumanVillager(), 0.5f);
        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerArrow(this));
        this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
	}
	
    public void doRender(EntityHumanVillager entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
    	this.setModelVisibilities(entity);
    	
    	if(entity.isRiding())
    	{
    		super.doRender(entity, x, y - 0.5D, z, entityYaw, partialTicks);
    	}
    	else
    	{
    		super.doRender(entity, x, y, z, entityYaw, partialTicks);
    	}
    }
	
    public ModelHumanVillager getMainModel()
    {
        return (ModelHumanVillager)super.getMainModel();
    }

	
    private void setModelVisibilities(EntityHumanVillager villager)
    {
        ModelHumanVillager modelhuman = this.getMainModel();
        ItemStack itemstack = villager.getHeldItemMainhand();
        ItemStack itemstack1 = villager.getHeldItemOffhand();
        ModelBiped.ArmPose modelbiped$armpose = ModelBiped.ArmPose.EMPTY;
        ModelBiped.ArmPose modelbiped$armpose1 = ModelBiped.ArmPose.EMPTY;
        
        if (!itemstack.isEmpty())
        {
            modelbiped$armpose = ModelBiped.ArmPose.ITEM;

            if (villager.getItemInUseCount() > 0)
            {
                EnumAction enumaction = itemstack.getItemUseAction();

                if (enumaction == EnumAction.BLOCK)
                {
                    modelbiped$armpose = ModelBiped.ArmPose.BLOCK;
                }
                else if (enumaction == EnumAction.BOW)
                {
                    modelbiped$armpose = ModelBiped.ArmPose.BOW_AND_ARROW;
                }
            }
        }

        //System.out.println("Empty: "+itemstack1.isEmpty());
        if (!itemstack1.isEmpty())
        {
            modelbiped$armpose1 = ModelBiped.ArmPose.ITEM;
            
            //System.out.println("In use: "+villager.getItemInUseCount());
            if (villager.getItemInUseCount() > 0)
            {
                EnumAction enumaction1 = itemstack1.getItemUseAction();
                
                if (enumaction1 == EnumAction.BLOCK)
                {
                    modelbiped$armpose1 = ModelBiped.ArmPose.BLOCK;
                    //System.out.println("In use: "+villager.getItemInUseCount());
                }
                // FORGE: fix MC-88356 allow offhand to use bow and arrow animation
                else if (enumaction1 == EnumAction.BOW)
                {
                    modelbiped$armpose1 = ModelBiped.ArmPose.BOW_AND_ARROW;
                }
            }
        }

        modelhuman.rightArmPose = modelbiped$armpose;
        modelhuman.leftArmPose = modelbiped$armpose1;
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityHumanVillager entity) {
		return entity.getVillagerSkin();
	}

}
