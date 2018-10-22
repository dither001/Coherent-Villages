package net.theexceptionist.coherentvillages.main.events;

import java.util.ArrayList;
import java.util.Random;

import net.theexceptionist.coherentvillages.main.entity.attributes.AttributeRace;

public class EventManager {
	public ArrayList<Event> events;
	
	public static final int EVENT_BANDIT_SKIRMISH = 0;
	
	private Random rand;
	private int tickRate = 10;
	
	public EventManager()
	{
		events = new ArrayList<Event>();
		this.rand = new Random();
		this.initEvents();
	}
	
	public void tick()
	{
		//System.out.println("Ticking");
		//events.get(0).execute();
		if(rand.nextInt(100) <= tickRate) return;
		
		for(Event event : events)
		{
			if(event.shouldStart())
			{
				event.execute();
			}
			else if(event.needsReset())
			{
				event.reset();
			}
		}
	}
	
	public void initEvents()
	{
		EventBanditSkirmish banditSkirmish = new EventBanditSkirmish(rand, true, 20, 40, AttributeRace.RACE_TYPE_NORD);
		addEvent(banditSkirmish);
	}
	
	public Event getEvent(int i)
	{
		return events.get(i);
	}
	
	public void addEvent(Event e)
	{
		events.add(e);
	}
}
