package com.mygdx.entity.soldier;

import com.badlogic.gdx.math.Vector2;

class SoldierBattleState 
{
	private SoldierBattle owner;
	
	private Direction directionPrev;
	private State statePrev;
	private Height heightPrev;
	private Direction direction;
	private State state;
	private Height height;
	
	private final Weapon weapon;
	private final Armor armor;
	private final Identification id;
	private final Allegiance allegiance;
	
	private int unsteadiness;// the inaccuracy of the gun due to a multitude of factors such as psychological pressure or having ran
	private int ammo;
	private int gunHeight;
	private int currentHp;
	
	private SoldierBattleState(Weapon weapon,Armor armor,Identification id, Allegiance allegiance)
	{
		// initializing the state
		direction = Direction.down;
		directionPrev = Direction.down;
		state = State.still;
		statePrev = State.still;
		height = Height.stand;
		heightPrev = Height.stand;
		/////////////////
		
		this.weapon = weapon;
		this.armor = armor;
		this.id = id;
		this.allegiance = allegiance;
		
	}
	static SoldierBattleState createProtectorState()
	{
		return new SoldierBattleState(Weapon.TSOKOS,Armor.FEDARMOR,Identification.auxiliary,Allegiance.epeirot);
	}
	
	void update()
	{
		checkStateChange();
	}
	
	private void checkStateChange()
	{
		boolean switchState = false;
		if(!statePrev.equals(state))
		{
			statePrev = state;
			switchState = true;
		}
		if(!directionPrev.equals(direction))
		{
			directionPrev = direction;
			switchState = true;
		}
		if(!heightPrev.equals(height))
		{
			heightPrev = height;
			switchState = true;
		}
		if(switchState)
		{
			switchAnimation();
		}
	}
	private void switchAnimation()
	{
		StringBuilder animePath = new StringBuilder();
		StringBuilder dataPath = new StringBuilder();

		// unidirectional
		if(state.equals(State.move) && height.equals(Height.crouch))
		{
			state = State.still;
		}
		if(state.equals(State.reload) || state.equals(State.dead))
		{
			//animePath = id.toString().concat(state.toString());
		}
		else
		{
			animePath.append("animation/soldier/");
			animePath.append(id.toString());
			animePath.append("/");
			animePath.append(state.toString());
			animePath.append("/");
			animePath.append(height.toString());
			animePath.append("/");
			animePath.append(direction.toString());
			animePath.append(".png");
			dataPath.append("animation/data/soldier");
			dataPath.append(state.toString());
			dataPath.append(height.toString());
			dataPath.append(".txt");

		}	
		owner.switchAnimation(animePath.toString(), dataPath.toString());
	}	
	
	private enum Allegiance
	{
		epeirot,
		dalmati;	
	}	
	private enum State
	{
		// common

			// 8 directional
		still,
		move,
		shoot,
		reload,
		hurt,
		

			// unidirectional 
		wounded,
		dead,
		
		// unique
		
		;
		
	}
	private enum Direction 
	{
		up(0),
		upright(1),
		right(2),
		downright(3),
		down(4),
		downleft(5),
		left(6),
		upleft(7);
		
		
		private final int position;
		
		Direction(int position)
		{
			this.position = position;
		}
		
		private static int getDif(Direction one,Direction two)
		{
			int counter = one.position;
			int diff = 0;
			while(counter != two.position)
			{
				if(counter == 7)
				{
					counter = 0;
					diff++;
				}
				else
				{
					counter ++;
					diff ++;
				}						
			}
			if(diff >=5)
			{
				diff = 8-diff;
			}
			return diff;
		}
	}
	private enum Height
	{
		stand,
		crouch,
		lay,		
		;
	}
	private enum Weapon
	{
		GAUSSRIFLE(2,5,1,1,6,150,10,2,1), // Fed Assault Rifle. Ayane's weapon
		TSOKOS(7,9,7,2,14,170,10,3,0), // Fed fully automatic shotgun. Chanion's weapon
		
		 // Zan automatic rifle
		 // Zan semi automatic shotgun
		 // Zan light machine gun
		 // Zan anti-infantry bazooka
		
		 // Zan civilian issue derringer shotgun
		 // Zan civilian issue automatic rifle
		;
		
		private final int pierce;
		private final int damage;
		private final float accuracy;
		private final int radius;
		private final int rate;
		private final int reload;
		private final int capacity;
		private final int burst;
		private final int burstDev;
		
		
		Weapon(int pierce,int damage,float accuracy,int radius,int rate,int reload,int capacity,int burst,int burstDev)
		{
			this.pierce = pierce;
			this.damage = damage;
			this.accuracy = accuracy;
			this.radius = radius;
			this.rate = rate;
			this.reload = reload;
			this.capacity = capacity;
			this.burst = burst;
			this.burstDev = burstDev;
		}
		
		private Vector2 getDeviation()
		{
			Vector2 impact = new Vector2();
			/*
			 * this needs filling
			 */
			
			
			
			return null;
		}
		
	}
	private enum Identification
	{
		controller,
		auxiliary,
		rifleman,
		shotgunner,
		machinegunner,
		bazooka,
		man,
		woman,
		child;
	}
	private enum Armor
	{
		FLAKJACKET(3),
		FEDARMOR(10),
		ZANARMOR(8),
		CIVILIAN(1);
		
		private final int armor;
		
		Armor(int armor)
		{
			this.armor = armor;
		}
	}
	
}
