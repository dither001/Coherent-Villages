package net.theexceptionist.coherentvillages.main.entity.spells;

import net.minecraft.entity.EntityLivingBase;

public abstract class Spell {
	public static final SpellFireball fireball = new SpellFireball("Fireball", Spell.SPELL_TYPE_ATTACK, 1, 0, 5, 10,  false, false);
	public static final SpellFireball fireball_volley = new SpellFireball("Fireball Volley", Spell.SPELL_TYPE_ATTACK, 5, 0, 1, 10, true, false);
	public static final SpellFireball greater_fireball = new SpellFireball("Greater Fireball", Spell.SPELL_TYPE_ATTACK, 1, 0, 2, 10, false, true);
	public static final SpellFireball greater_fireball_volley = new SpellFireball("Greater Fireball Volley", Spell.SPELL_TYPE_ATTACK, 3, 0, 1, 20, true, true);
	public static final SpellFireball firestorm = new SpellFireball("Firestorm", Spell.SPELL_TYPE_ATTACK, 3, 1, 1, 40, true, true);
	
	public static final SpellSummonAncient summon_ancient_warror = new SpellSummonAncient("Summon Ancient Warrior", Spell.SPELL_TYPE_SUMMON, 40, 3, 1000);
	
	public static final SpellThunder thunderbolt = new SpellThunder("Thunderbolt", Spell.SPELL_TYPE_ATTACK, 10);
	
	public static final SpellThunderStorm storm = new SpellThunderStorm("Storm", Spell.SPELL_TYPE_WEATHER);
	//Active
	public static final int SPELL_TYPE_ATTACK = 0;
	public static final int SPELL_TYPE_SUMMON = 1;
	
	//Passive
	public static final int SPELL_TYPE_WEATHER = 2;
	public static final int SPELL_TYPE_TRANSMUTE = 3;
	
	protected String name;
	protected int type;
	protected boolean isActive = false;
	
	public Spell(String name, int type)
	{
		this.name = name;
		this.type = type;

		if(type == SPELL_TYPE_ATTACK || type == SPELL_TYPE_SUMMON) isActive = true;
	}	
	
	public abstract void execute(EntityLivingBase caster);

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
