package net.theexceptionist.coherentvillages.main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Items;
import net.minecraftforge.common.MinecraftForge;
import net.theexceptionist.coherentvillages.events.EventModTick;
import net.theexceptionist.coherentvillages.events.EventOverrideVillages;
import net.theexceptionist.coherentvillages.events.PlayerConnectionEvent;
import net.theexceptionist.coherentvillages.gui.GuiSetup;
import net.theexceptionist.coherentvillages.main.entity.EntityBjornserker;
import net.theexceptionist.coherentvillages.main.entity.EntityBloodBat;
import net.theexceptionist.coherentvillages.main.entity.EntityDrachen;
import net.theexceptionist.coherentvillages.main.entity.EntityHumanVillager;
import net.theexceptionist.coherentvillages.main.entity.EntityLemure;
import net.theexceptionist.coherentvillages.main.entity.EntitySkeletonSummon;
import net.theexceptionist.coherentvillages.main.entity.EntityWarg;
import net.theexceptionist.coherentvillages.main.entity.EntityWraith;
import net.theexceptionist.coherentvillages.main.entity.render.RenderBjornserker;
import net.theexceptionist.coherentvillages.main.entity.render.RenderBloodBat;
import net.theexceptionist.coherentvillages.main.entity.render.RenderDrachen;
import net.theexceptionist.coherentvillages.main.entity.render.RenderHumanVillager;
import net.theexceptionist.coherentvillages.main.entity.render.RenderLemure;
import net.theexceptionist.coherentvillages.main.entity.render.RenderSkeletonSummon;
import net.theexceptionist.coherentvillages.main.entity.render.RenderWarg;
import net.theexceptionist.coherentvillages.main.entity.render.RenderWraith;
import net.theexceptionist.coherentvillages.main.items.EntityWeaponThrowable;
import net.theexceptionist.coherentvillages.main.items.ModItems;
import net.theexceptionist.coherentvillages.main.items.RenderWeaponThrowable;
import net.theexceptionist.coherentvillages.main.items.ShieldTileStackRenderer;

public class ClientProxy extends CommonProxy {
	public void registerRenderInformation(){
		for(int i = 0; i < ModItems.nordShield.length; i++)
		{
			ModItems.nordShield[i].setTileEntityItemStackRenderer(new ShieldTileStackRenderer());
		}
		ModItems.romanShield.setTileEntityItemStackRenderer(new ShieldTileStackRenderer());
	}
	
	public void registerRenderers(){
		RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
		fixer = Minecraft.getMinecraft().getDataFixer();
		//Humans
		renderManager.entityRenderMap.put(EntityHumanVillager.class, new RenderHumanVillager(renderManager));
		
		//Creatures
		renderManager.entityRenderMap.put(EntityWarg.class, new RenderWarg(renderManager));
		renderManager.entityRenderMap.put(EntityBjornserker.class, new RenderBjornserker(renderManager));	
		renderManager.entityRenderMap.put(EntityWraith.class, new RenderWraith(renderManager));
		renderManager.entityRenderMap.put(EntityLemure.class, new RenderLemure(renderManager));
		renderManager.entityRenderMap.put(EntityDrachen.class, new RenderDrachen(renderManager));
		renderManager.entityRenderMap.put(EntitySkeletonSummon.class, new RenderSkeletonSummon(renderManager));
		renderManager.entityRenderMap.put(EntityBloodBat.class, new RenderBloodBat(renderManager));

		renderManager.entityRenderMap.put(EntityWeaponThrowable.class, new RenderWeaponThrowable(renderManager, Items.IRON_AXE, Minecraft.getMinecraft().getRenderItem()));
		
		/*
		//Soldiers
        renderManager.entityRenderMap.put(EntityVillagerGuard.class, new RenderVillagerGuard(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerPeasant.class, new RenderVillagerPeasant(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerSergeant.class, new RenderVillagerSergeant(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerWarrior.class, new RenderVillagerWarrior(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerManAtArms.class, new RenderVillagerManAtArms(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerMilitia.class, new RenderVillagerMilitia(renderManager));
        
        //Archers
        renderManager.entityRenderMap.put(EntityVillagerArcher.class, new RenderVillagerArcher(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerHunter.class, new RenderVillagerHunter(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerMageArcher.class, new RenderVillagerMageArcher(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerMarksman.class, new RenderVillagerMarksman(renderManager));
        
        //Mages
        renderManager.entityRenderMap.put(EntityVillagerMage.class, new RenderVillagerMage(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerGrandMage.class, new RenderVillagerGrandMage(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerConjurer.class, new RenderVillagerConjurer(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerNecromancer.class, new RenderVillagerNecromancer(renderManager));
        
        //Alchemists
        renderManager.entityRenderMap.put(EntityVillagerAlchemist.class, new RenderVillagerAlchemist(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerUndeadHunter.class, new RenderVillagerUndeadHunter(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerHealer.class, new RenderVillagerHealer(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerPotionMaster.class, new RenderVillagerPotionMaster(renderManager));
        
        //Knights
        renderManager.entityRenderMap.put(EntityVillagerKnight.class, new RenderVillagerKnight(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerCavalier.class, new RenderVillagerCavalier(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerMageKnight.class, new RenderVillagerMageKnight(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerPaladin.class, new RenderVillagerPaladin(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerHorseArcher.class, new RenderVillagerHorseArcher(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerApothecary.class, new RenderVillagerApothecary(renderManager));
        

        renderManager.entityRenderMap.put(EntityVillagerMerchant.class, new RenderVillagerMerchant(renderManager));
        
        renderManager.entityRenderMap.put(EntityVillagerGuardian.class, new RenderVillagerGuardian(renderManager));
        renderManager.entityRenderMap.put(EntitySkeletonMinion.class, new RenderSkeleton(renderManager));
        renderManager.entityRenderMap.put(EntityMerchantGuard.class, new RenderMerchantGuard(renderManager));
        
        renderManager.entityRenderMap.put(EntityVillagerHorse.class, new RenderVillagerHorse(renderManager));
        
        //Bandits
        renderManager.entityRenderMap.put(EntityVillagerBandit.class, new RenderVillagerBandit(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerBanditArcher.class, new RenderVillagerBanditArcher(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerBanditMage.class, new RenderVillagerBanditMage(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerBanditHorseman.class, new RenderVillagerBanditHorseman(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerBanditAlchemist.class, new RenderVillagerBanditAlchemist(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerDarkKnight.class, new RenderVillagerDarkKnight(renderManager));
        
        //renderManager.entityRenderMap.put(EntityVillagerEvilMage.class, new RenderVillagerEvilMage(renderManager));*/
        
        //renderManager.entityRenderMap.put(EntityVillagerArrow.class, new RenderVillagerArrow(renderManager));
	}
	
	@Override
	public void openGUI(int id)
	{
		//System.out.println("^%$%#@!#$%^&%$#%^&* Open");
		if(id == GuiSetup.ID)Minecraft.getMinecraft().displayGuiScreen(new GuiSetup());
	}
	
	public void initEvents(){
		MinecraftForge.EVENT_BUS.register(new EventModTick());	
		MinecraftForge.EVENT_BUS.register(new PlayerConnectionEvent());	
		MinecraftForge.TERRAIN_GEN_BUS.register(new EventOverrideVillages());
	
	}
}
