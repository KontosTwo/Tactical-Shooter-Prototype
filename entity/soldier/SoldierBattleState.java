package com.mygdx.entity.soldier;

import com.badlogic.gdx.math.Vector2;

class SoldierBattleState 
{
	private Direction directionPrev;
	private State statePrev;
	private Height heightPrev;
	private Direction direction;
	private State state;
	private Height height;
	
	private Weapon weapon;
	private Armor armor;
	private Identification id;
	private Allegiance allegiance;
	
	private int unsteadiness;// the inaccuracy of the gun due to a multitude of factors such as psychological pressure or having ran
	private int ammo;
	private int gunHeight;
	private int currentHp;
	
	SoldierBattleState()
	{
		// initializing the state
		direction = Direction.down;
		directionPrev = Direction.down;
		state = State.still;
		statePrev = State.still;
		height = Height.stand;
		heightPrev = Height.stand;
		/////////////////
		
		
	}
	
	void update()
	{
		
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
		human,
		protector,
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
