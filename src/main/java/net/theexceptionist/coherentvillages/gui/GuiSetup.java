package net.theexceptionist.coherentvillages.gui;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSetup extends GuiScreen
{

	public static final int ID = 0;
	
    private GuiButton buttonDone;
    
    public GuiSetup()
    {
    	//System.out.println("Working");
    }
    
	@Override
    public void initGui() 
    {
		buttonList.clear();
        Keyboard.enableRepeatEvents(true);

        buttonDone = new GuiButton(0, width / 2 + 2, 4, 
              98, 20, I18n.format("gui.done", new Object[0]));
  
        buttonList.add(buttonDone);
    }

}
