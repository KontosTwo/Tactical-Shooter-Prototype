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

	private final Armor armor;
	private final Identification id;
	private final Allegiance allegiance;

	final WeaponState weaponState;

	// dimensions of the body
	private static final int BODYX = 10;
	private static final int BODYY = 10;
	private static final int BODYZ = 50;

	private static final int CRAWLSPEED = 1;
	private static final int STANDSPEED = 4;
	private static final int STANDHEIGHT = 60;
	private static final int CROUCHHEIGHT = 30;
	private static final int CRAWLHEIGHT = 10;
	private static final int HEADHEIGHT = 50;

	// how wide in (degrees/45) the field of vision is. 
	private static final int FIELDOFVISION = 1;

	private SoldierBattleState(WeaponState weaponSTate,Armor armor,Identification id, Allegiance allegiance)
	{
		centerPrevious = new PrecisePoint();
		center = new MovablePoint();
		centerFuture = new PrecisePoint();
		body = new MovableBox(center.getCenterReference(),BODYX,BODYY,BODYZ);
		

		// initializing the state
		direction = Direction.down;
		directionPrev = Direction.down;
		state = State.still;
		statePrev = State.still;
		height = Height.stand;
		heightPrev = Height.stand;
		/////////////////
		
		this.weaponState = weaponSTate;
		this.armor = armor;
		this.id = id;
		this.allegiance = allegiance;
		
		stand();
	}
	static SoldierBattleState createProtectorState()
	{
		return new SoldierBattleState(WeaponState.createTsokos(),Armor.FEDARMOR,Identification.auxiliary,Allegiance.epeirot);
	}

	void update()
	{
		centerPrevious.set(center.getCenterReference());
		center.update();
		centerFuture.set(center.createProjectedLocation());
		weaponState.update();
	}
	float getCurrentAccuracy()
	{
		return weaponState.getAccuracy();
	}
	boolean facingTowards(final PrecisePoint observerLocation,final PrecisePoint targetLocation)
	{
		Direction otherDirection = getDirectionBetweenTwoPoints(observerLocation,targetLocation);
		return direction.bearingDifference(otherDirection) <= FIELDOFVISION;
	}
	MyVector3 getSides() {
		return new MyVector3(BODYX,BODYY,body.getHeight());
	}

	PrecisePoint getCenter() {
		return new PrecisePoint(center.getCenterReference().x,center.getCenterReference().y);
	}
	
	PrecisePoint3 getVantagePoint(){
		return new PrecisePoint3(center.getCenterReference().x,center.getCenterReference().y,body.getHeight());
	}
	
	HitBoxable getBody(){
		return new HitBoxable(){
			@Override
			public MyVector3 getSides() {
				return new MyVector3(BODYX,BODYY,body.getHeight());
			}

			@Override
			public PrecisePoint getBottomLeftCorner() {
				return new PrecisePoint(center.getCenterReference().x - BODYX/2,center.getCenterReference().y - BODYY/2);
			}
			
			@Override
			public boolean equals(Object other){
				if(other == null){
					return false;
				}
				if(!(other instanceof HitBoxable)){
					return false;
				}
				HitBoxable otherHitbox = (HitBoxable)other;
				return getBottomLeftCorner().equals(otherHitbox.getBottomLeftCorner())
						&& getSides().equals(otherHitbox.getSides());
			}
			
			@Override
			public int hashCode(){
				int hash = 7;
			    hash = (int) (71 * hash + getSides().hashCode());
			    hash = (int) (71 * hash + getBottomLeftCorner().hashCode());
			    return hash;
			}
		};
	}
	
	void stand()
	{
		body.setHeight(STANDHEIGHT);
		center.setSpeed(STANDSPEED);
		height = Height.stand;
		weaponState.stand();
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
		weaponState.crouch();
	}
	void lay()
	{
		body.setHeight(CRAWLHEIGHT);
		center.setSpeed(CRAWLSPEED);
		height = Height.lay;
		weaponState.lay();
	}
	void setToShooting(){
		state = State.shoot;
	}
	void setToReloading(){
		state = State.reload;
		crouch();
	}
	void setToIdle(){
		state = State.still;
	}
	void face(PrecisePoint location){
		direction = getDirectionBetweenTwoPoints(center.getCenterReference(),location);
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
			animePathBuiler.append("animation/soldier/");
			animePathBuiler.append(id.toString());
			animePathBuiler.append("/unidirectional/");
			animePathBuiler.append(state.toString());
			animePathBuiler.append(".png");
			dataPathBuilder.append("animation/data/soldier");
			dataPathBuilder.append(state.toString());
			dataPathBuilder.append(".txt");
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
		Pair<String,String> animationFilePath = new Pair<>();
		animationFilePath.setFirst(animePathBuiler.toString());
		animationFilePath.setSecond(dataPathBuilder.toString());
		return animationFilePath;
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
	public String toString(){
		return id.toString();
	}
}
