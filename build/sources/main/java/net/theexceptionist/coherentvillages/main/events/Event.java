package net.theexceptionist.coherentvillages.main.events;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;

public abstract class Event {
	protected int startChance;
	protected boolean spawned = false;
	protected boolean startInDay;
	protected EntityPlayer player;
	protected Random rand;
	protected int range, raceID;
	protected Style style;
	protected ITextComponent eventMessage;
	
	public Event(Random rand, boolean startInDay, int chance, int range, int raceID)
	{
		this.rand = rand;
		this.startInDay = startInDay;
		this.startChance = chance;
		this.range = range;
		this.raceID = raceID;
	}
	
	protected boolean shouldStart()
	{
		boolean correctTime = false;
		if(player != null) correctTime = player.world.isDaytime() == startInDay;
		return !spawned && rand.nextInt(100) < startChance && correctTime;
	}
	
	public void setPlayer(EntityPlayer player)
	{
		this.player = player;
	}
	
	abstract public void execute();
	abstract public void reset();

	public boolean needsReset() {
		return spawned;
	}
	

	protected Vec3d polarRotate(double x, double z, double theta) {
		Vec3d vec = new Vec3d(0, 0, 0);
		
		double r = Math.sqrt((x * x) + (z * z));
		double rad = Math.toRadians(theta);
		
		vec.add(new Vec3d(r * Math.cos(rad), 0, r * Math.sin(rad)));
		return vec;
	}
}
