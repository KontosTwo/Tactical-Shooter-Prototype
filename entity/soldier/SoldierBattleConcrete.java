package com.mygdx.entity.soldier;

import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.ai.blackboard.EnemyCognizable;
import com.mygdx.ai.blackboard.EnemyManager;
import com.mygdx.ai.functional.RoutineManager;
import com.mygdx.ai.leaf.RiflemanRoutineable;
import com.mygdx.ai.leaf.RoutineFactory;
import com.mygdx.control.Auxiliarable;
import com.mygdx.control.Control;
import com.mygdx.control.Steerable;
import com.mygdx.control.PlayerControllable;
import com.mygdx.entity.HitMarker;
import com.mygdx.entity.Hurtboxable;
import com.mygdx.misc.Differentable;
import com.mygdx.misc.Pair;
import com.mygdx.physics.Point;
import com.mygdx.physics.PrecisePoint;
import com.mygdx.script.Scripter;

public class SoldierBattleConcrete extends Hurtboxable implements PlayerControllable, Auxiliarable,
Differentable <SoldierBattleConcrete>,RiflemanRoutineable,Steerable,EnemyCognizable
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
	private String debugName;
	
	private boolean controller;// whether this humanoid is controlled by the player as per Controllable

	// some constants
	private static final int walkSpeed = 3;
	private static final int crawlSpeed = 1;
	private static final int standHeight = 70;
	private static final int crouchHeight = 40;
	private static final int crawlHeight = 10;
	private static final int standGunHeight = 70;
	private static final int crouchGunHeight = 40;
	private static final int crawlGunHeight = 10;
	private static final int headHeight = 50;
	private static final int WANDERDISTANCE = 50; // radius of the area centered on the humanoid that it can choose to go to
	private static final int SEARCHCOVERDISTANCE = 1000; // maximum distance from the point that the humanoid chooses to be cover
	private static final int COVERTHRESHOLD = 10; // the comparative value that denotes the minimal cover advantage the humanoid must have against an adversary
	private static final int SHOTSENSINGDISTANCE = 150; // a shot must be within this range for a humanoid to sense and react to
	private static final float MINAMMOTHRESHOLD = .2f; // percentage of the ammo left to warrant reload
	private static final int VERIFYHIDDENENEMYRADIUS = 20; // the distance from a target point that a humanoid must check before making sure no one's there.
	private static final int MAXHP = 10;
	////////////////////////////////////
	
	// these fields constitute the variable state of the humanoid. 
	private int unsteadiness;// the inaccuracy of the gun due to a multitude of factors such as psychological pressure or having ran
	private int ammo;
	private int gunHeight;
	private int currentHp;
	/////////////////////////////////
	
	// some classes that encapsulate mentioned behavior
	private RoutineManager routineManager;
	private Scripter scriptManager;
	private Control controlManager;
	private EnemyManager enemyManager;
	//////////////////////////////

	// these fields are to serve the Riflemanaiable interface
	private int reloadProgress;
	private int shootProgress;
	//private int burstProgress;
	//private int burstAmount;
	private double flankSuppressiveRatio; // the chance that a humanoid will chose to engage in flanking maneuvers over laying down suppressive fire
	private double shootGrenadeRatio; // the chance that a humanoid will
	///////////////////////////////////////
	
	private SoldierBattleConcrete(PrecisePoint position,Weapon weapon,Armor armor,Identification id,Allegiance a,String debugName)
	{
		super(position);
		// initializing the state
		direction = Direction.down;
		directionPrev = Direction.down;
		state = State.still;
		statePrev = State.still;
		height = Height.stand;
		heightPrev = Height.stand;
		/////////////////
		
		// designating the id
		this.weapon = weapon;
		this.armor = armor;
		this.id = id;
		this.debugName = debugName;
		controller = false;
		////////////////////////
		
		// the encapsulation classes
		routineManager = new RoutineManager();
		//enemyManager = new EnemyManager(this);
		scriptManager = new Scripter();
		controlManager = new Control(this);
		/////////////////
		
		unsteadiness = 0;
		setHeight(standHeight);
		currentHp = MAXHP;
		setSpeed(walkSpeed);
		checkState();// this initializes the animation
		stand();
		ammo = weapon.capacity;

		// these adjust the location of the animation relative to the humanoid's center
		setSizeAll(70,70);
		doodadify();
		setHitBoxSize(10,10);
		////////////
		
		
		this.allegiance = a;
	}
	public static SoldierBattleConcrete createProtector(PrecisePoint position)
	{
		SoldierBattleConcrete ret = new SoldierBattleConcrete(position,Weapon.TSOKOS,Armor.FEDARMOR,Identification.auxiliary,Allegiance.epeirot,"protector");
		return ret;
	}
	public static SoldierBattleConcrete createHuman(PrecisePoint position)
	{
		SoldierBattleConcrete ret = new SoldierBattleConcrete(position,Weapon.TSOKOS,Armor.FEDARMOR,Identification.auxiliary,Allegiance.epeirot,"human");
		return ret;
	}
	public static SoldierBattleConcrete createRifleman(PrecisePoint position)
	{
		SoldierBattleConcrete ret = new SoldierBattleConcrete(position,Weapon.TSOKOS,Armor.FEDARMOR,Identification.auxiliary,Allegiance.dalmati,"rifleman");
		ret.routineManager.startRiflemanRoutine(ret);
		return ret;
	}
	public void update(float dt)
	{
		super.update(dt);
		checkForAnimationChange();	
		statePrev = state;
		directionPrev = direction;
		if(state.equals(State.reload))
		{
			reloadProgress ++;
		}
		if(state.equals(State.shoot))
		{
			shootProgress ++;
		}		
		entityListener.scanBattleField(this);
		//enemyManager.update();
		controlManager.update(dt);
		routineManager.update(dt);
		if(controlManager.isActive())
		{

			if(scriptManager.isActive())
			{
				scriptManager.cancelScript();
			}
		}
		else
		{
			scriptManager.update(dt);
		}
		
		if(debugName.equals("human"))
		{

		}
	}	
	private void checkForAnimationChange()
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
			checkState();
		}
	}
	public void damage(SoldierBattleConcrete target)
	{
		int attack = this.weapon.damage;
		int defense = target.armor.armor;

		double random = Math.random();
		if(random < .30)
		{
			target.currentHp -= attack * 2;
		}
		else if(random < .60)
		{
			target.currentHp -= attack*2/defense;
		}
		else
		{
			target.currentHp -= attack/defense;
		}
	}	
	
	public boolean isEnemy()
	{
		return this.allegiance.equals(Allegiance.dalmati);
	}
	public boolean isFriendly()
	{
		return this.allegiance.equals(Allegiance.epeirot); // this should become more generalized
	}
	@Override
	public boolean sameAs(SoldierBattleConcrete comparer) 
	{
		return this.allegiance.equals(comparer.allegiance);
	}
	public static boolean areEnemies(SoldierBattleConcrete a,SoldierBattleConcrete b)
	{
		return !a.allegiance.equals(b.allegiance);// will not work properly if there are more than 3 allegiances
	}
	public static boolean areAllies(SoldierBattleConcrete a,SoldierBattleConcrete b)
	{
		return a.allegiance.equals(b.allegiance);// will not work properly if there are more than 3 allegiances
	}
	
	
	public boolean senseShot(HitMarker ht)
	{
		return  Math.abs(ht.getPosition().x - this.center.x) < SHOTSENSINGDISTANCE && Math.abs(ht.getPosition().y - this.center.y) < SHOTSENSINGDISTANCE;
	}
	public void recieveObscuredEnemyMessage(PrecisePoint location)
	{
		//enemyManager.delayedNotifyEnemyAt(location);
	}
	public void spotEnemy(SoldierBattleConcrete h)// will not add the new EnemyMarker if too close to existing one
	{
		//enemyManager.spotEnemy(h);		
	}
	
	public boolean isEnemyDebug()
	{
		return allegiance.equals(Allegiance.dalmati);
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
	private enum Height
	{
		stand,
		crouch,
		lay,		
		;
	}
	private enum Identification
	{
		player,
		auxiliary,
		rifleman,
		shotgunner,
		machinegunner,
		bazooka,
		man,
		woman,
		child;
	}
	private enum Allegiance
	{
		epeirot,
		dalmati;	
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
	
	// ControlManager
	@Override
	public void moveLeft() 
	{
		setUnitVelocityInput(-1,0);
		direction = Direction.left;
	}
	@Override
	public void moveRight() 
	{
		setUnitVelocityInput(1,0);
		direction = Direction.right;
	}
	@Override
	public void moveUp() 
	{
		setUnitVelocityInput(0,1);
		direction = Direction.up;
	}
	@Override
	public void moveDown() 
	{
		setUnitVelocityInput(0,-1);
		direction = Direction.down;
	}
	@Override
	public void moveUpLeft() 
	{
		setUnitVelocityInput(-.707f,.707f);
		direction = Direction.upleft;
	}
	@Override
	public void moveUpRight() 
	{
		setUnitVelocityInput(.707f,.707f);
		direction = Direction.upright;
	}
	@Override
	public void moveDownLeft() 
	{
		setUnitVelocityInput(-.707f,-.707f);
		direction = Direction.downleft;
	}
	@Override
	public void moveDownRight() 
	{
		setUnitVelocityInput(.707f,-.707f);
		direction = Direction.downright;
	}
	@Override
	public void moveAction()
	{
		// if the humanoid was crouching, make it stand up
		state = State.move;
		if(height.equals(Height.crouch))
		{
			stand();
		}
	}
	@Override
	public void notMoveAction()
	{
		/*
		 * must make sure that this does not conflict with script or routineManager
		 */
		if(!scriptManager.isActive())
		{
			state = State.still;
			setUnitVelocityInput(0,0);
		}
	}
	//////////////////////////////////
	
	// Controller
	@Override
	public void initiateControllable() 
	{
		controller = true;
	}
	@Override
	public void cMoveRight(boolean b) 
	{		
		controlManager.pendRight(b);
	}
	@Override
	public void cMoveLeft(boolean b) 
	{
		controlManager.pendLeft(b);
	}
	@Override
	public void cMoveUp(boolean b) 
	{
		controlManager.pendUp(b);
	}
	@Override
	public void cMoveDown(boolean b)
	{		
		controlManager.pendDown(b);
	}
	@Override
	public void cShoot(int x,int y,int z) 
	{			
		scriptManager.pushSequence(RoutineFactory.createBurstShootSeq(x, y, z, this));				
	}
	@Override
	public void cReload() 
	{
		scriptManager.pushSequence(RoutineFactory.createReloadSeq(this));
	}
	@Override
	public void cFace(double x,double y) 
	{
		faceControl(x,y);
	}
	@Override
	public void cStand() 
	{
		stand();
	}
	@Override
	public void cCrouch() 
	{
		crouch();
	}
	@Override
	public void cLay() 
	{
		lay();
	}
	@Override
	public void cGrenade(double x, double y) 
	{
		grenade(x,y);
	}
	@Override
	public PrecisePoint provideCenterForDebugger() 
	{
		return new PrecisePoint(this.center);
	}
	/////////////
	
	// Auxiliary
	@Override
	public void aMoveTo(double x,double y) 
	{
		scriptManager.pushSequence(RoutineFactory.createPathToSeq(x,y, this));
	}
	@Override
	public void aAttackMoveTo(double x,double y) 
	{
		
	}
	@Override
	public void aFollow(PlayerControllable c) 
	{
		scriptManager.pushSequence(RoutineFactory.createPathToSeq(c.provideCenterForDebugger().x, c.provideCenterForDebugger().y, this));
	}
	@Override
	public void aTurn(double x,double y) 
	{
		
	}
	////////////
	
	// PathDiagnostic
	@Override
	public Pair<Boolean, LinkedList<Point>> calculatePath(double x, double y) 
	{
		return entityListener.findPath((int)this.center.x, (int)this.center.y, (int)x, (int)y);
	}
	///
	
	//PathTo
	@Override
	public void completePathTo()
	{
		idle();
	}
	///
	
	// MoveTo
	@Override
	public void beginMoveTo(double x, double y) 
	{
		orientVelocity((float)x,(float)y);
		faceAi(x,y);
		move();
	}
	@Override
	public boolean finishMoveTo(double x, double y) 
	{
		return  reachTarget(x,y) || distanceFrom(x, y) < 50;
	}
	@Override
	public void completeMoveTo() 
	{
		stopVelocity();
	}
	@Override
	public void stopMoveTo() 
	{
		stopVelocity();
		idle();// the idle is in here instead of compelteMoveTo because an idle in completeMoveTo would interrupt the animation
	}
	//////////////////////////////////////////
	
	// Shoot
	@Override
	public void beginShoot(double x, double y,double z) 
	{
		entityListener.shoot(center.y, center.x, gunHeight, y, x ,z,this,weapon.accuracy);// do deviation y when trajectories are reworked
		faceAi(x,y);
		ammo --;
		state = State.shoot;	
		shootProgress = 0;
		stopVelocity();
		stopVelocityInput();
	}
	@Override
	public boolean hasAmmo() 
	{
		return ammo > 0;
	}
	@Override
	public boolean finishedShooting()
	{
		return shootProgress == weapon.rate;
	}
	@Override
	public void completeShoot() 
	{
		shootProgress = 0;
	}
	@Override
	public void failShoot() 
	{
		shootProgress = 0;
	}
	///////////////////////////////////////////////
	
	// Reload
	@Override
	public void beginReload()
	{
		state = State.reload;
		reloadProgress = 0;
		stopVelocity();
	}
	@Override
	public boolean finishedReloading()
	{
		return reloadProgress == weapon.reload;
	}
	@Override
	public void completeReload()
	{
		reloadProgress = 0;
		ammo = weapon.capacity;
		idle();
	}
	@Override
	public void cancelReload()
	{
		reloadProgress = 0;
		state = State.still;
		idle();
	}
	/////////////////////////////////////////////////
	
	//BurstShoot
	@Override
	public int getBurstAmount()
	{
		return weapon.burst;
	}
	/////////////////////////////////////////////////////
	
	// Idle
	@Override
	public boolean canIdle() 
	{
		return true;
	}
	//////////////////////////////////////////

	// private comands
	private void stand()
	{
		setHeight(standHeight);
		setSpeed(walkSpeed);
		height = Height.stand;
		gunHeight = standGunHeight;
	}
	private void move()
	{
		state = State.move;
	}
	private void crouch()
	{
		setHeight(crouchHeight);
		setSpeed(0);
		height = Height.crouch;
		gunHeight = crouchGunHeight;
	}
	private void lay()
	{
		setHeight(crawlHeight);
		setSpeed(crawlSpeed);
		height = Height.lay;
		gunHeight = crawlGunHeight;
	}
	private void idle()
	{
		state = State.still;
	}
	private void dead()// rest in pepperoni
	{
		state = State.dead;
	}
	private void faceControl(double x,double y) 
	{
		/*
		 * this is the orientation function used by the player's humanoid
		 * since the point that the humanoid will be facing needs to be adjusted
		 * because the point is aligned to the humanoid's face, not its center,
		 * headHeight is added
		 */
		direction = getDirection(x,y - (int)headHeight);
	}
	private void faceAi(double x,double y)
	{
		/*
		 * this is the face used by ai. No need for adjusting 
		 * the facing coordinates
		 */
		direction = getDirection(x,y);
	}
	private void grenade(double x,double y)
	{
		entityListener.grenade((int)center.x, (int)center.y,(int)x,(int)y);
	}
	public boolean see(SoldierBattleConcrete h)
	{
		int difference = Direction.getDif(getDirection((int)h.center.x,(int)h.center.y), this.direction);
		return (difference <= 1 || difference == 7) && entityListener.see(center.y,center.x,getHeight(), h.center.y,h.center.x,h.getHeight());
	}
	private boolean see(Vector2 center)
	{
		int difference = Direction.getDif(getDirection((int)center.x,(int)center.y), this.direction);
		return (difference <= 1 || difference == 7) && entityListener.see(center.y,center.x,getHeight(), center.y,center.x,0);
	}
	
	private Pair<Boolean,SoldierBattleConcrete> seeEnemy()
	{
		return entityListener.seeEnemy(this);
	}
	private boolean hitMarkerNear()
	{
		return entityListener.hitMarkerNear(this);
	}

	private boolean seePeripheral(SoldierBattleConcrete h)
	{
		int difference = Direction.getDif(getDirection((int)h.center.x,(int)h.center.y), this.direction);
		return (difference == 1);
	}
	private boolean seeBino(SoldierBattleConcrete h)
	{
		int difference = Direction.getDif(getDirection((int)h.center.x,(int)h.center.y), this.direction);
		return (difference == 0 || difference == 7);
	}
	private boolean seeTerrain(SoldierBattleConcrete h)
	{
		return entityListener.see(center.y,center.x,getHeight(), h.center.y,h.center.x,h.getHeight());
	}
	private Vector2 getWanderPoint()
	{
		Random r = new Random(System.currentTimeMillis());
		double angle = r.nextInt(361);
		return new Vector2((float)(center.x + Math.cos(angle)*WANDERDISTANCE),(float)(center.y + Math.sin(angle)*WANDERDISTANCE));
	}
	private Pair<Boolean,LinkedList<Point>>findPath(int tx,int ty)
	{
		return entityListener.findPath((int)this.center.x, (int)this.center.y, tx, ty);
	}
	
	private Pair<Boolean,Vector2> findCover() // if false return the same tile as the player
	{
		return entityListener.findCover(center.x,center.y,SEARCHCOVERDISTANCE);
	}
	private boolean judgeCover(SoldierBattleConcrete foe)
	{
		return entityListener.judgeCover((int)this.center.x,(int)this.center.y,(int)this.getHeight(),(int)foe.center.x,(int)foe.center.y,(int)foe.getHeight()) > COVERTHRESHOLD;
	}
	private boolean judgeCover(SoldierBattleConcrete foe,int x,int y)
	{
		return entityListener.judgeCover(x,y,(int)this.getHeight(),(int)foe.center.x,(int)foe.center.y,(int)foe.getHeight()) > COVERTHRESHOLD;
		// this has yet to consider the fact that the the cover location can be appropriate whether the humanoid is crawling, crouching, or standing
	}
	private boolean coveredAgainst(float z1,float x2,float y2)// z is standing, crouching, etc
	{
		return !entityListener.seePhysical(this.center.x, this.center.y, z1, x2, y2, standHeight);
		// assume that this humanoid assumes that the enemy will be standing
	}
	private boolean revealEnemyLocationAt(Vector2 center)
	{
		// returns true if this humanoid is close enough to see and verify this location
		boolean ret = false;
		return this.see(center) && this.distanceFrom(center.x, center.y) < VERIFYHIDDENENEMYRADIUS;
	}
	private Direction getDirection(double x,double y)
	{
		Direction ret = null;
		if(Math.abs((y-center.y)/(x-center.x)) < .41)
		{
			if(x-center.x > 0)
			{
				ret = Direction.right;
			}
			else
			{
				ret = Direction.left;
			}
		}
		else if(Math.abs((x-center.x)/(y-center.y)) < .41)
		{
			if(y-center.y > 0)
			{
				ret = Direction.up;
			}
			else
			{
				ret = Direction.down;
			}
		}	
		else if(x-center.x > 0)
		{
			if(y-center.y > 0)
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
			if(y-center.y > 0)
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
	private void checkState()
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
		updateAnimation(animePath.toString(),dataPath.toString());
	}	
	public String toString()
	{
		return debugName;
	}
	@Override
	public boolean see(double x, double y, double z) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public double proximityFrom(double x, double y) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean verifyEnemyMarkerAt(double x, double y) {
		// TODO Auto-generated method stub
		return false;
	}
}