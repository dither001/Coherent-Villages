package net.theexceptionist.coherentvillages.main.entity.render;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.theexceptionist.coherentvillages.main.Resources;
import net.theexceptionist.coherentvillages.main.entity.EntityHumanVillager;
import net.theexceptionist.coherentvillages.main.entity.model.ModelHumanVillager;

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
	
	public RenderHumanVillager(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelHumanVillager(), 0.5f);
		/*LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this)
        {
            protected void initArmor()
            {
                this.modelLeggings = new ModelHumanVillager(0.5F, true);
                this.modelArmor = new ModelHumanVillager(1.0F, true);
            }
        };*/
        this.addLayer(new LayerBipedArmor(this)/*layerbipedarmor*/);
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

        if (!itemstack1.isEmpty())
        {
            modelbiped$armpose1 = ModelBiped.ArmPose.ITEM;

            if (villager.getItemInUseCount() > 0)
            {
                EnumAction enumaction1 = itemstack1.getItemUseAction();

                if (enumaction1 == EnumAction.BLOCK)
                {
                    modelbiped$armpose1 = ModelBiped.ArmPose.BLOCK;
                }
                // FORGE: fix MC-88356 allow offhand to use bow and arrow animation
                else if (enumaction1 == EnumAction.BOW)
                {
                    modelbiped$armpose1 = ModelBiped.ArmPose.BOW_AND_ARROW;
                }
            }
            
            modelhuman.rightArmPose = modelbiped$armpose;
            modelhuman.leftArmPose = modelbiped$armpose1;
        }
    }

       /* if (villager.isSpectator())
        {
            modelplayer.setVisible(false);
            modelplayer.bipedHead.showModel = true;
            modelplayer.bipedHeadwear.showModel = true;
        }
        else
        {
            ItemStack itemstack = villager.getHeldItemMainhand();
            ItemStack itemstack1 = villager.getHeldItemOffhand();
            modelplayer.setVisible(true);
            /*modelplayer.bipedHeadwear.showModel = villager.isWearing(EnumPlayerModelParts.HAT);
            modelplayer.bipedBodyWear.showModel = villager.isWearing(EnumPlayerModelParts.JACKET);
            modelplayer.bipedLeftLegwear.showModel = villager.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG);
            modelplayer.bipedRightLegwear.showModel = villager.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG);
            modelplayer.bipedLeftArmwear.showModel = villager.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
            modelplayer.bipedRightArmwear.showModel = villager.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
            modelplayer.isSneak = villager.isSneaking();
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

            if (!itemstack1.isEmpty())
            {
                modelbiped$armpose1 = ModelBiped.ArmPose.ITEM;

                if (villager.getItemInUseCount() > 0)
                {
                    EnumAction enumaction1 = itemstack1.getItemUseAction();

                    if (enumaction1 == EnumAction.BLOCK)
                    {
                        modelbiped$armpose1 = ModelBiped.ArmPose.BLOCK;
                    }
                    // FORGE: fix MC-88356 allow offhand to use bow and arrow animation
                    else if (enumaction1 == EnumAction.BOW)
                    {
                        modelbiped$armpose1 = ModelBiped.ArmPose.BOW_AND_ARROW;
                    }
                }
            }

            if (villager.getPrimaryHand() == EnumHandSide.RIGHT)
            {
                modelplayer.rightArmPose = modelbiped$armpose;
                modelplayer.leftArmPose = modelbiped$armpose1;
            }
            else
            {
                modelplayer.rightArmPose = modelbiped$armpose1;
                modelplayer.leftArmPose = modelbiped$armpose;
            }
        //}
    }*/

	@Override
	protected ResourceLocation getEntityTexture(EntityHumanVillager entity) {
		return entity.getVillagerSkin();
	}

}
