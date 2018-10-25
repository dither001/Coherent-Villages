package net.theexceptionist.coherentvillages.main.items;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.theexceptionist.coherentvillages.main.Main;
import net.theexceptionist.coherentvillages.main.Resources;

public class ItemModSword extends ItemSword{
	public ItemModSword(String name, ToolMaterial material) {
		super(material);
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setCreativeTab(Main.villagesTab);
	}

}