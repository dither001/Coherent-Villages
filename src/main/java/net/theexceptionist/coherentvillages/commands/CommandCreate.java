package net.theexceptionist.coherentvillages.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.theexceptionist.coherentvillages.main.entity.EntityBjornserker;
import net.theexceptionist.coherentvillages.main.entity.EntityHumanVillager;
import net.theexceptionist.coherentvillages.main.entity.EntityWraith;
import net.theexceptionist.coherentvillages.main.entity.attributes.AttributeRace;
import net.theexceptionist.coherentvillages.main.entity.attributes.AttributeVocation;
import net.theexceptionist.coherentvillages.math.PolarCoord;

public class CommandCreate implements ICommand{
	private final List aliases;
	
	protected String entityName;
	protected Entity conjuredEntity;
	
	public CommandCreate()
	{
		aliases = new ArrayList<>();
		aliases.add("create");
		aliases.add("crt");
	}

	@Override
	public int compareTo(ICommand o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "create";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "create <villager_race> <villager_class> <number_to_spawn>\nUsed /create <villager_race> list\nto list options";
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return this.aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		World world = sender.getEntityWorld(); 
	    
        if (world.isRemote) 
        { 
            //System.out.println("Not processing on Client side"); 
        } 
        else 
        { 
           // System.out.println("Processing on Server side");
        	/*
        	PolarCoord player = new PolarCoord(sender.getEntityWorld().rand, sender.getPosition().getX() - 10, sender.getPosition().getZ() - 5, sender.getPosition().getX(), sender.getPosition().getZ());
        	player.printStats();
        	player.mirror();
        	player.printStats();
        	player.setRandomAdjacentCoord();
        	player.printStats();*/
        	
        	if(args.length == 0) 
            { 
                sender.sendMessage(new TextComponentString("Invalid argument\nUsage: "+this.getUsage(sender)+"\nEx: /create nord warrior")); 
                return; 
            } 
            
            if(args[0].equals("list"))
            {
            	sender.sendMessage(new TextComponentString(AttributeRace.listRaceNames()));
            	return;
            }
            
            AttributeRace race = AttributeRace.getRaceFromString(args[0]);
            
            if(race == null)
            {
            	 sender.sendMessage(new TextComponentString("Invalid Race")); 
                 return;
            }
            
            if(args.length > 1 && args[1].equals("list"))
            {
            	sender.sendMessage(new TextComponentString(race.listClassNames()));
            }
            else
            {
            	int count = 1;
            	boolean vampire = false, bjornserker = false, wraith = false;
	            AttributeVocation vocation = race.getVocationFromString(args[1]);
	            EntityHumanVillager villager = null;//new EntityHumanVillager(world, race.getID(), vocation, 0, false);
            	AttributeVocation job = race.getRandomSoldier(world);            	
            	if(args.length > 2) count = Integer.parseInt(args[2]);
            	if(args.length > 3) 
            	{
            		vampire = Integer.parseInt(args[3]) == 0;
            		bjornserker = Integer.parseInt(args[3]) == 1;
            		wraith = Integer.parseInt(args[3]) == 2;
            	}
            
	            for(int i = 0; i < count; i++)
	            {
	            	if(vocation != null) 
	            	{
	            		villager = new EntityHumanVillager(world, race.getID(), vocation, 0, false);
	            		sender.sendMessage(new TextComponentString("Creating... " + race.getName() + " " + vocation.getName()));  
	            	}
	            	else
	            	{
	            		sender.sendMessage(new TextComponentString("Vocation: "+args[1]+" not found or specified")); 
		            	sender.sendMessage(new TextComponentString("Creating... " + race.getName())); 
		            	if(job != null)
		            	{
		            		villager = new EntityHumanVillager(world, race.getID(), job, 0, false);
		            	}
		            	else
		            	{
		            		sender.sendMessage(new TextComponentString("Couldn't get random job... Try again.")); 
		            		return;
		            	}
	            	}
	 
		            villager.setLocationAndAngles((double)sender.getPosition().getX() + 0.5D, (double)sender.getPosition().getY(), (double)sender.getPosition().getZ() + 0.5D, 0.0F, 0.0F);
	            	int faction = villager.getFactionNumber();
	            	
	               // System.out.println("2 - Bandit? :"+vocation.getType() == AttributeVocation.CLASS_BANDIT+" Faction Number: "+faction);
	            	
	            	if(args.length > 4) 
	            	{
	            		faction = Integer.parseInt(args[4]);
	            	}
	            	
		            if(vampire)
		            {
		            	villager.setVampire(true);
		            }
		            else if(bjornserker)
		            {
                    	villager.setShifter(true, new EntityBjornserker(world), true, -1);
		            }
		            else if(wraith)
		            {
                    	villager.setShifter(true, new EntityWraith(world), false, 1);	
		            }
		            
		            villager.setFactionNumber(faction);
		            
		            world.spawnEntity(villager);
		            sender.sendMessage(new TextComponentString("Successfully spawned new "+villager.getRace().getName()+" "+villager.getVocation().getName()+": "+villager.getTitle()));
		       }
            }
       } 
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		// TODO Auto-generated method stub
		return false;
	}
}
