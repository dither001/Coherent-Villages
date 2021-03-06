package net.theexceptionist.coherentvillages.main.entity.attributes;

import java.util.ArrayList;
import java.util.Random;

public class AttributeQuality {
	protected String name;
	protected int healthBonus;// = race.getHealthBonus();
	protected int attackBonus;// = race.getAttackBonus();
	protected int magicBonus;// = race.getMagicBonus();
	protected int detectBonus;// = race.getDetectBonus();
	protected int speedBonus;// = race.getSpeedBonus();
	protected int knockbackBonus;// = race.getKnockbackBonus();
	protected int horseHealth;// = race.getHorseHealthBonus();
	protected int chance;
	protected int ID;
	protected int enchantmentBonus;
	private boolean display = true;
	
	public static ArrayList<AttributeQuality> qualities = new ArrayList<AttributeQuality>();
	
	protected static AttributeQuality recruit;
	protected static AttributeQuality levy;
	protected static AttributeQuality trainee;
	protected static AttributeQuality apprentice;
	protected static AttributeQuality adept;
	protected static AttributeQuality novice;
	protected static AttributeQuality inferior;
	protected static AttributeQuality inexperienced;
	
	protected static AttributeQuality regular;
	
	protected static AttributeQuality trained;
	protected static AttributeQuality experienced;
	protected static AttributeQuality expert;
	protected static AttributeQuality captain;
	protected static AttributeQuality general;
	protected static AttributeQuality veteran;
	protected static AttributeQuality legend;
	protected static AttributeQuality master;
	protected static AttributeQuality greater;
	protected static AttributeQuality grand;
	protected static AttributeQuality protege;
	
	protected static AttributeQuality magical;
	protected static AttributeQuality unmagical;
	protected static AttributeQuality weak;
	protected static AttributeQuality strong;
	protected static AttributeQuality unhealthy;
	protected static AttributeQuality vigorous;
	protected static AttributeQuality blind;
	protected static AttributeQuality perceptive;
	protected static AttributeQuality quick;
	protected static AttributeQuality slow;
	protected static AttributeQuality heavy;
	protected static AttributeQuality light;
	protected static AttributeQuality knightly;
	protected static AttributeQuality kniavely;
	
	public static int END_ID = 0;

	
	public AttributeQuality(String name, final int health, final int attack, final int magic,
			final int detect, final int speed, final int knockback, 
			final int horseHealth, final int chance, final int enchantment)
	{
		this.name = name;
		this.healthBonus = health;
		this.attackBonus = attack;
		this.magicBonus = magic;
		this.detectBonus = detect;
		this.speedBonus = speed;
		this.knockbackBonus = knockback;
		this.horseHealth = horseHealth;
		this.chance = chance;
		this.enchantmentBonus = enchantment;
		this.ID = END_ID++;
		qualities.add(this);
		
	}
	
	public static void init()
	{
		recruit = new AttributeQuality("Recruit", -8, -8, -8, -8, -8, -8, -8, 200, -50);
		levy = new AttributeQuality("Levy", -4, -4, -4, -4, -4, -4, -4, 200, -40);
		trainee = new AttributeQuality("Trainee", -2, -2, -2, -2, -2, -2, -2, 400, -30);
		novice = new AttributeQuality("Novice", -1, -1, -1, -1, -1, -1, -1, 600, -25);
		apprentice = new AttributeQuality("Apprentice", 0, 0, -2, -6, 0, -3, -10, 300, -15);
		inferior = new AttributeQuality("Inferior", -3, -3, 0, 0, -2, -3, -5, 100, -5);
		adept = new AttributeQuality("Adept", -6, -5, 0, -3, -2, 0, 0, 100, 0);
		inexperienced = new AttributeQuality("Inexperienced", -5, -5, -3, -2, -2, 0, 0, 200, -10);

		regular = new AttributeQuality("", 0, 0, 0, 0, 0, 0, 0, 0, 0);
		regular.setDisplay(false);
		
		trained = new AttributeQuality("Trained", 1, 1, 1, 1, 1, 1, 1, 100, 5);
		experienced = new AttributeQuality("Experienced", 2, 2, 2, 2, 2, 2, 2, 100, 10);
		expert= new AttributeQuality("Expert", 5, 1, 5, 3, 1, 0, 0, 50, 15);
		veteran = new AttributeQuality("Veteran", 4, 4, 4, 4, 4, 4, 4, 20, 40);
		captain = new AttributeQuality("Captain", 2, 3, 0, 4, 0, 6, 10, 100, 20);
		general = new AttributeQuality("General", 4, 6, 0, 0, 0, 0, 20, 100, 30);
		greater = new AttributeQuality("Greater", 0, 0, 6, 2, 4, 2, 0, 0, 50);
		greater = new AttributeQuality("Grand", 2, 3, 2, 2, 4, 2, 0, 0, 60);
		protege = new AttributeQuality("Protégé", 8, 8, 8, 8, 8, 8, 8, 10, 80);
		master = new AttributeQuality("Master", 10, 10, 10, 10, 10, 10, 10, 10, 90);
		legend = new AttributeQuality("Legendary", 12, 12, 12, 12, 12, 12, 12, 5, 100);
		
		AttributeQuality magical = new AttributeQuality("Magical", 0, 0, 16, 0, 0, 0, 0, 100, 16);
		AttributeQuality unmagical = new AttributeQuality("Unmagical", 0, 0, -16, 0, 0, 0, 0, 100, 16);
		AttributeQuality weak = new AttributeQuality("Weak", 0, -16, 0, 0, 0, 0, 0, 100, 0);
		AttributeQuality strong = new AttributeQuality("Strong", 0, 16, 0, 0, 0, 0, 0, 100, 0);
		AttributeQuality unhealthy = new AttributeQuality("Unhealthy", -16, 0, 0, 0, 0, 0, 0, 100, 0);
		AttributeQuality vigorous = new AttributeQuality("Vigorous", 16, 0, 0, 0, 0, 0, 0, 100, 0);
		AttributeQuality blind = new AttributeQuality("Blind", 0, 0, 0, -16, 0, 0, 0, 100, 0);
		AttributeQuality perceptive = new AttributeQuality("Perceptive", 0, 0, 0, 16, 0, 0, 0, 100, 0);
		AttributeQuality quick = new AttributeQuality("Quick", 0, 0, 0, 0, 16, 0, 0, 100, 0);
		AttributeQuality slow = new AttributeQuality("Slow", 0, 0, 0, 0, -16, 0, 0, 100, 0);
		AttributeQuality heavy = new AttributeQuality("Heavy", 0, 0, 0, 0, 0, 16, 0, 100, 0);
		AttributeQuality light = new AttributeQuality("Light", 0, 0, 0, 0, 0, -16, 0, 100, 0);
		AttributeQuality knightly = new AttributeQuality("Knightly", 0, 0, 0, 0, 0, 0, 16, 100, 0);
		AttributeQuality kniavely  = new AttributeQuality("Kniavely", 0, 0, 0, 0, 0, 0, -16, 100, 0);

	}

	public static AttributeQuality getRandomQuality(Random rand) {
		// TODO Auto-generated method stub
		if(qualities.size() < 1) return null;
		if(rand.nextInt(100) < 50)
		{
			for(AttributeQuality quality : qualities)
			{
				if(rand.nextInt(1000) < quality.getChance())
				{
					return quality;
				}
			}
			
			return regular;
			//return qualities.get(rand.nextInt(qualities.size()));
		}
		else return regular;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHealthBonus() {
		return healthBonus;
	}

	public void setHealthBonus(int healthBonus) {
		this.healthBonus = healthBonus;
	}

	public int getAttackBonus() {
		return attackBonus;
	}

	public void setAttackBonus(int attackBonus) {
		this.attackBonus = attackBonus;
	}

	public int getMagicBonus() {
		return magicBonus;
	}

	public void setMagicBonus(int magicBonus) {
		this.magicBonus = magicBonus;
	}

	public int getDetectBonus() {
		return detectBonus;
	}

	public void setDetectBonus(int detectBonus) {
		this.detectBonus = detectBonus;
	}

	public int getSpeedBonus() {
		return speedBonus;
	}

	public void setSpeedBonus(int speedBonus) {
		this.speedBonus = speedBonus;
	}

	public int getKnockbackBonus() {
		return knockbackBonus;
	}

	public void setKnockbackBonus(int knockbackBonus) {
		this.knockbackBonus = knockbackBonus;
	}

	public int getHorseHealth() {
		return horseHealth;
	}

	public void setHorseHealth(int horseHealth) {
		this.horseHealth = horseHealth;
	}

	public int getChance() {
		return chance;
	}

	public void setChance(int chance) {
		this.chance = chance;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public boolean displayName() {
		// TODO Auto-generated method stub
		return display;
	}
	
	public void setDisplay(boolean display) {
		this.display = display;
	}

	public int getEnchantmentBonus() {
		return enchantmentBonus;
	}

	public void setEnchantmentBonus(int enchantmentBonus) {
		this.enchantmentBonus = enchantmentBonus;
	}
	
	
}