package com.mygdx.entity.soldier;

import com.mygdx.map.GameMap.HitBoxable;
import com.mygdx.misc.Pair;
import com.mygdx.physics.MovableBox;
import com.mygdx.physics.MovablePoint;
import com.mygdx.physics.MyVector3;
import com.mygdx.physics.PrecisePoint;
import com.mygdx.physics.PrecisePoint3;

final class SoldierBattleState
{		
	final PrecisePoint centerPrevious;
	final MovablePoint center;
	final PrecisePoint centerFuture;

	private final MovableBox body;

	private Direction directionPrev;
	private State statePrev;
	private Height heightPrev;
	Direction direction;
	State state;
	Height height;

	private final Weapon weapon;
	private final Armor armor;
	private final Identification id;
	private final Allegiance allegiance;

	int unsteadiness;// the inaccuracy of the gun due to a multitude of factors such as psychological pressure or having ran
	int ammo;
	int gunHeight;
	int currentHp;

	private int reloadProgress;
	private int shootingProgress;

	private static final int BODYX = 10;
	private static final int BODYY = 10;
	private static final int BODYZ = 70;

	private static final int CRAWLSPEED = 1;
	private static final int STANDSPEED = 4;
	private static final int STANDHEIGHT = 70;
	private static final int CROUCHHEIGHT = 40;
	private static final int CRAWLHEIGHT = 10;
	private static final int STANDGUNHEIGHT = 70;
	private static final int CROUCHGUNHEIGHT = 40;
	private static final int CRAWLGUNHEIGHT = 10;
	private static final int HEADHEIGHT = 50;

	// how wide in (degrees/45) the field of vision is. 
	private static final int FIELDOFVISION = 1;

	private SoldierBattleState(Weapon weapon,Armor armor,Identification id, Allegiance allegiance)
	{
		centerPrevious = new PrecisePoint();
		center = new MovablePoint();
		centerFuture = new PrecisePoint();
		body = new MovableBox(center.getCenterReference(),BODYX,BODYY,BODYZ);
		stand();

		// initializing the state
		direction = Direction.down;
		directionPrev = Direction.down;
		state = State.still;
		statePrev = State.still;
		height = Height.stand;
		heightPrev = Height.stand;
		/////////////////

		reloadProgress = 0;
		shootingProgress = 0;
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
		centerPrevious.set(center.getCenterReference());
		center.update();
		centerFuture.set(center.createProjectedLocation());
	}
	float getCurrentAccuracy()
	{
		return weapon.accuracy;
	}
	boolean facingTowards(final PrecisePoint observerLocation,final PrecisePoint targetLocation)
	{
		Direction otherDirection = getDirectionBetweenTwoPoints(observerLocation,targetLocation);
		return direction.bearingDifference(otherDirection) <= FIELDOFVISION;
	}
	MyVector3 getSides() {
		return new MyVector3(BODYX,BODYY,BODYZ);
	}

	PrecisePoint getCenter() {
		return new PrecisePoint(center.getCenterReference().x,center.getCenterReference().y);
	}
	
	PrecisePoint3 getVantagePoint(){
		return new PrecisePoint3(center.getCenterReference().x,center.getCenterReference().y,BODYZ);
	}
	
	HitBoxable getBody(){
		return new HitBoxable(){
			@Override
			public MyVector3 getSides() {
				return new MyVector3(BODYX,BODYY,BODYZ);
			}

			@Override
			public PrecisePoint getCenter() {
				return new PrecisePoint(center.getCenterReference().x,center.getCenterReference().y);
			}
		};
	}
	
	void stand()
	{
		body.setHeight(STANDHEIGHT);
		center.setSpeed(STANDSPEED);
		height = Height.stand;
		gunHeight = STANDGUNHEIGHT;
	}
	void move()
	{
		state = State.move;
	}
	void crouch()
	{
		body.setHeight(CROUCHHEIGHT);
		center.setSpeed(0);
		height = Height.crouch;
		gunHeight = CROUCHGUNHEIGHT;
	}
	void lay()
	{
		body.setHeight(CRAWLHEIGHT);
		center.setSpeed(CRAWLSPEED);
		height = Height.lay;
		gunHeight = CRAWLGUNHEIGHT;
	}
	void idle()
	{
		state = State.still;
	}
	void face(double x,double y){
		direction = getDirectionBetweenTwoPoints(center.getCenterReference(),new PrecisePoint(x,y));
	}

	private static Direction getDirectionBetweenTwoPoints(PrecisePoint origin,PrecisePoint target)
	{
		Direction ret = null;
		if(Math.abs((target.y-origin.y)/(target.x-origin.x)) < .41)
		{
			if(target.x-origin.x > 0)
			{
				ret = Direction.right;
			}
			else
			{
				ret = Direction.left;
			}
		}
		else if(Math.abs((target.x-origin.x)/(target.y-origin.y)) < .41)
		{
			if(target.y-origin.y > 0)
			{
				ret = Direction.up;
			}
			else
			{
				ret = Direction.down;
			}
		}	
		else if(target.x-origin.x > 0)
		{
			if(target.y-origin.y > 0)
			{
				ret = Direction.upright;
			}
			else
			{
				ret = Direction.downright;
			}
		}
		else
		{
			if(target.y-origin.y > 0)
			{
				ret = Direction.upleft;
			}
			else
			{
				ret = Direction.downleft;
			}
		}
		return ret;
	}

	boolean stateHasChanged()
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
		return switchState;
	}
	Pair<String,String> createAnimationFilePath()
	{
		StringBuilder animePathBuiler = new StringBuilder();
		StringBuilder dataPathBuilder = new StringBuilder();

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
			animePathBuiler.append("animation/soldier/");
			animePathBuiler.append(id.toString());
			animePathBuiler.append("/");
			animePathBuiler.append(state.toString());
			animePathBuiler.append("/");
			animePathBuiler.append(height.toString());
			animePathBuiler.append("/");
			animePathBuiler.append(direction.toString());
			animePathBuiler.append(".png");
			dataPathBuilder.append("animation/data/soldier");
			dataPathBuilder.append(state.toString());
			dataPathBuilder.append(height.toString());
			dataPathBuilder.append(".txt");
		}	
		return new Pair<String,String>(animePathBuiler.toString(),dataPathBuilder.toString());
	}	

	private enum Allegiance
	{
		epeirot,
		dalmati;	
	}	
	enum State
	{
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
	enum Direction 
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

		private int bearingDifference(Direction other)
		{
			int rawDifference = Math.abs(position - other.position);
			return rawDifference > 4 ? 8 - rawDifference : rawDifference;
		}
	}
	enum Height
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
