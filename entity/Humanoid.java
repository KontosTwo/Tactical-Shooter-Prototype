package com.mygdx.entity;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.ai.RiflemanRoutineable;
import com.mygdx.ai.RoutineFactory;
import com.mygdx.ai.RoutineManager;
import com.mygdx.handler.Auxiliarable;
import com.mygdx.handler.ControlManager;
import com.mygdx.handler.ControlManagerable;
import com.mygdx.handler.Controllable;
import com.mygdx.misc.Differentable;
import com.mygdx.misc.Point;
import com.mygdx.misc.TimeCapsule;
import com.mygdx.misc.Tuple;
import com.mygdx.script.ScriptManager;

public class Humanoid extends Hurtboxable implements Controllable, Auxiliarable,Differentable <Humanoid>,RiflemanRoutineable,ControlManagerable
{
	// states of the Humanoid. Used in determining which animation to use. 
	// the prev stats exist so that the humanoid can compare its current state with its previous states. if the two differ, then the animation must update
	// this system prevents a new animation from being created every second. That would suck 
	private Direction directionPrev;
	private State statePrev;
	private Height heightPrev;
	private Direction direction;
	private State state;
	private Height height;
		
	// identification enumerations
	private Weapon weapon;
	private Armor armor;
	private Identification id;
	private Allegiance allegiance;// which "side" the humanoid is on
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
	private ScriptManager scriptManager;
	private ControlManager controlManager;
	private EnemyManager enemyManager;
	//////////////////////////////

	// these fields are to serve the Riflemanaiable interface
	private int reloadProgress;
	private int shootProgress;
	//private int burstProgress;
	//private int burstAmount;
	private float flankSuppressiveRatio; // the chance that a humanoid will chose to engage in flanking maneuvers over laying down suppressive fire
	private float shootGrenadeRatio; // the chance that a humanoid will
	///////////////////////////////////////
	
	private Humanoid(Vector2 position,Weapon weapon,Armor armor,Identification id,Allegiance a,String debugName)
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
		enemyManager = new EnemyManager(this);
		scriptManager = new ScriptManager();
		controlManager = new ControlManager(this);
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
	public static Humanoid createProtector(Vector2 position)
	{
		Humanoid ret = new Humanoid(position,Weapon.TSOKOS,Armor.FEDARMOR,Identification.protector,Allegiance.epeirot,"protector");
		return ret;
	}
	public static Humanoid createHuman(Vector2 position)
	{
		Humanoid ret = new Humanoid(position,Weapon.TSOKOS,Armor.FEDARMOR,Identification.protector,Allegiance.epeirot,"human");
		return ret;
	}
	public static Humanoid createRifleman(Vector2 position)
	{
		Humanoid ret = new Humanoid(position,Weapon.TSOKOS,Armor.FEDARMOR,Identification.protector,Allegiance.dalmati,"rifleman");
		ret.routineManager.startRiflemanRoutine(ret);
		return ret;
	}
	public void update(float dt)
	{
		super.update(dt);
		if(currentHp < 0)
		{
			// die
		}
		
		

		/*
		 * this prevents the creation of new animations every tick
		 * a new animation will be created only if the state is changed
		 */
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
		
		/*if(!statePrev.equals(state) || !directionPrev.equals(direction) || !heightPrev.equals(height))
		{
			
			checkState();
			System.out.println(height + " " + heightPrev);
		}*/

		//faceUnit((int)getVelocity().x,(int)getVelocity().y);
	
		if(unsteadiness > 0)
		{
			unsteadiness --;
		}
		
		
		
		
		// updating both states and directions
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
		enemyManager.update();
		
		
		// managing the interplay between control, scirpt, and routine managers
		

		
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
			//System.out.println(reloadProgress);
			if(state.equals(State.shoot))
			{
				//System.out.println("shooting");
				
			}
		}
		
		/*
		if(!routineManager.active() && controller)
		{
			controlManager.update(dt);
		}
		else
		{
			routineManager.update();
		}*/
	}	
	public void damage(Humanoid target)// this damage formula is even more unbalanced than ur mum
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
	public boolean sameAs(Humanoid comparer) 
	{
		return this.allegiance.equals(comparer.allegiance);
	}
	public static boolean areEnemies(Humanoid a,Humanoid b)
	{
		return !a.allegiance.equals(b.allegiance);// will not work properly if there are more than 3 allegiances
	}
	public static boolean areAllies(Humanoid a,Humanoid b)
	{
		return a.allegiance.equals(b.allegiance);// will not work properly if there are more than 3 allegiances
	}
	
	
	public boolean senseShot(HitMarker ht)
	{
		return  Math.abs(ht.getPosition().x - this.center.x) < SHOTSENSINGDISTANCE && Math.abs(ht.getPosition().y - this.center.y) < SHOTSENSINGDISTANCE;
	}
	public void recieveObscuredEnemyMessage(Humanoid target)
	{
		enemyManager.recieveObscuredEnemyMessage(target);
	}
	public void spotEnemy(Humanoid h)// will not add the new EnemyMarker if too close to existing one
	{
		enemyManager.spotEnemy(h);		
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

	
	// processing all the input
	/*private void processInput()
	{
		state = State.move;
		boolean moved = true;
		if(moveUp)
		{
			if(moveRight)
			{
				setUnitVelocityInput(.707f,.707f);
				direction = Direction.upright;
			}
			else if(moveLeft)
			{
				setUnitVelocityInput(-.707f,.707f);
				direction = Direction.upleft;
			}
			else
			{
				setUnitVelocityInput(0,1);
				direction = Direction.up;
			}
		}
		else if(moveDown)
		{
			if(moveRight)
			{
				setUnitVelocityInput(.707f,-.707f);
				direction = Direction.downright;
			}
			else if(moveLeft)
			{
				setUnitVelocityInput(-.707f,-.707f);
				direction = Direction.downleft;
			}
			else
			{
				setUnitVelocityInput(0,-1);
				direction = Direction.down;
			}
		}
		else
		{
			if(moveRight)
			{
				setUnitVelocityInput(1,0);
				direction = Direction.right;
			}
			else if(moveLeft)
			{
				setUnitVelocityInput(-1,0);
				direction = Direction.left;
			}
			else
			{
				setUnitVelocityInput(0,0);
				state = State.still;
				moved = false;
			}
		}
		if(moved)
		{
			if(height.equals(Height.crouch))
			{
				stand();
			}
		}
	}*/
	/////////////////////////////
	
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
	public void cShoot(double x,double y,double z) 
	{			
		//scriptManager.pushSequence(RoutineFactory.createShootSeq(x, y, z, this));
		scriptManager.pushSequence(RoutineFactory.createBurstShootSeq(x, y, z, this));

		//routineManager.queueShoot(x,y,z);					
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
	public Vector2 provideCenter() 
	{
		return new Vector2(this.center);
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
	public void aFollow(Controllable c) 
	{
		scriptManager.pushSequence(RoutineFactory.createPathToSeq(c.provideCenter().x, c.provideCenter().y, this));
	}
	@Override
	public void aTurn(double x,double y) 
	{
		
	}
	////////////
	
	// PathDiagnostic
	@Override
	public Tuple<Boolean, LinkedList<Point>> calculatePath(double x, double y) 
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

	// can shoot enemy
	@Override
	public boolean enemyAvailableToShoot() 
	{
		return enemyManager.trackerInMind();
	}
	////////////
	
	// return the coordinates for shot
	@Override
	public Vector3 getProjectedEnemyLocation() 
	{
		Humanoid h = enemyManager.acquireEnemyTracker().getHumanoid();
		return new Vector3(h.center,h.getHeight());
	}
	//////////////////////////////////
	
	//enemyManager
	private interface Trackable
	{
		public Humanoid getHumanoid();
	}
	private interface Targetable
	{
		
	}
	private class EnemyManager
	{
		/*
		 * this class managed all the enemies that a humanoid class is aware of
		 * the humanoid class will access both recognizedEnemies, which are enemies that are not visible
		 * due to hiding behind obstacles, and sightedEnemies, which are visible enemies, and act upon them
		 * both the arrays recognizedEnemy and sightedEnemy will add and drop enemy humanoids based on the methods
		 * defined below
		 */
		private LinkedList<EnemyMarker> recognizedEnemy;		
		private Array<EnemyTracker> sightedEnemy;
		private static final int ENEMYGUESSRADIUS = 60;// radius of each enemyMarker. if another enemymarker attempts to spawn within this radius, it won't
		private static final int ENEMYASSUMERADIUS = 30; // radius of you when you expose yourself. The enemy will assum that you were one of the nearby enemyMarkers that revealed themselves. 
		private static final int ENEMYREVEALRADIUS = 50; // radius of your EnemyMarker when the enemy sees it. If the distance between you or chanion and that enemyMArker is beyond that range, then the enemy gets rid of that EnemyMarker
		//private static final int ENEMYMARKERRADIUS = 60; // the "radius" of each enemyMArker. EAch enemyMArker is entitled to this much space. No toher enemyMarker can be in this space. This is used for many checks and stuff
		private static final int MAXENEMYGUESSES = 5;// this exists to prevent you from abusing the whole "duplicate yourself needlessly to make the enemy feel grossly outnumbered"  trick, you bastard
		private static final int TIMETONOTIFYALLIES = 50;// time for a soldier to notify other soldiers of an enemy
		private Humanoid soldier;
		
		private EnemyTracker targetVisible;
		private EnemyMarker targetObscured;

		private Array<TimeCapsule<Tuple<Humanoid,Vector2>>> enemyMarkerMessage;// list of notifications of newly discovered enemies
		// the tuple exists so that both a shallow copy of the humanoid and a deep copy of the position is retained
		
		private EnemyManager(Humanoid soldier)
		{
			recognizedEnemy = new LinkedList<EnemyMarker>();
			sightedEnemy = new Array<EnemyTracker>();
			this.soldier = soldier;
			enemyMarkerMessage = new Array<TimeCapsule<Tuple<Humanoid,Vector2>>>();
		}
		private void recieveObscuredEnemyMessage(Humanoid target)// recieve a message that an enemyMarker is at location X
		{
			enemyMarkerMessage.add(new TimeCapsule<Tuple<Humanoid,Vector2>>(new Tuple<Humanoid,Vector2>(target,new Vector2(target.center)),TIMETONOTIFYALLIES));// the new Vector makes a deep copy of the position
		}
		private boolean trackerInMind()
		{
			return !(sightedEnemy.size == 0);
		}
		private Trackable acquireEnemyTracker() // must be called after trackerInMind
		{
			EnemyTracker closest = null;
			double distanceMin = Integer.MAX_VALUE;// near guarentee that SOMETHING is returned
			for(EnemyTracker et : sightedEnemy)
			{
				double distanceFrom = soldier.distanceFrom(et.owner.center.x, et.owner.center.y);
				if(distanceFrom < distanceMin)
				{
					distanceMin = distanceFrom;
					closest = et;
				}
			}
			return closest;
		}
		private boolean markerInMind()
		{
			return !(recognizedEnemy.size() == 0);
		}
		private Targetable acquireEnemyMarker() // must be called after markerInMind
		{
			EnemyMarker closest = null;
			double distanceMin = Integer.MAX_VALUE;// near guarentee that SOMETHING is returned
			for(EnemyMarker em : recognizedEnemy)
			{
				double distanceFrom = soldier.distanceFrom(em.owner.center.x, em.owner.center.y);
				if(distanceFrom < distanceMin)
				{
					distanceMin = distanceFrom;
					closest = em;
				}
			}
			return closest;
		}
		private void spotEnemy(Humanoid h)
		{
			// condition to make sure that no duplicates are made
			checkToAddToSighted(h);
		}
		private boolean coveredAgainstUnseenEnemy()
		{
			boolean ret = true;
			// searches through all visible "allied" and see if they are favorably positioned against them
			for(EnemyMarker e : recognizedEnemy)
			{
				if(!Humanoid.this.coveredAgainst(standHeight,e.position.x,e.position.y))// this needs verification
				{
					ret = false;
				}
			}
			return ret;
		}
		private boolean coveredAgainstSeenEnemy()
		{
			boolean ret = true;
			// searches through all visible "allied" and see if they are favorably positioned against them
			for(EnemyTracker e : sightedEnemy)
			{
				if(!Humanoid.this.coveredAgainst(standHeight,e.owner.center.x,e.owner.center.y))// this needs edit
				{
					ret = false;
				}
			}
			return ret;
		}
		private void update()
		{
			// any other humanoid can discover an enemyMarker and render it invalid. Next, this code signs this current soldier out, After everyone has signed out, it's removed
			for(int i = 0; i < recognizedEnemy.size(); i ++)
			{
				EnemyMarker em = recognizedEnemy.get(i);
				if(!em.isStillValid())
				{
					em.userSignOut(soldier);
				}
			}
			//////////////////////////////////////////////////////////////////////////
			
			// if this humanoid can no longer see one of the enemy, the enemytracker gets converted into an enemymarker, bnasically
			for(int i = 0; i < sightedEnemy.size; i ++)
			{
				EnemyTracker et = sightedEnemy.get(i);
				if(!soldier.see(et.owner))
				{
					/*if(soldier.allegiance.equals(Allegiance.dalmati))
						System.out.println(soldier.verifyGroundOf(et.owner.center));*/

					// make sure that no duplicates
					checkToAddToRecognized(et.owner);
					// do not invalidate. Other humanoids can see this et, probablty
					et.userSignOut(soldier);
					
					entityListener.foundObscuredEnemy(et.owner, soldier);// notify all allies that this enemy has gone out of sight!
				}			
			}
			///////////////////////////////////////////////////////
						
			// checking if the soldier has seen an enemyMarker's ground.
			for(int i = 0; i < recognizedEnemy.size(); i ++)  
			{
				EnemyMarker em = recognizedEnemy.get(i);
				if(soldier.revealEnemyLocationAt(em.position))// any other humanoid can discover an enemyMarker and render it invalid. Next, this code signs this current soldier out, After everyone has signed out, it's removed
				{
					em.discoveredAndRevealed();// this will eventually have other soldiers using this enemyMarkers to sign out themselves
					em.userSignOut(soldier); // this soldier itself will instantly sign out
				}
			}
			/////////////////////////////////////
			

			if(recognizedEnemy.size() > MAXENEMYGUESSES)
			{				
				EnemyMarker em = recognizedEnemy.peekLast();
				em.userSignOut(soldier);
			}
			for(int i = 0; i < recognizedEnemy.size(); i ++)
			{
				recognizedEnemy.get(i).update();
			}
			for(int i = 0; i < sightedEnemy.size; i ++)
			{
				sightedEnemy.get(i).update();
			}
			
			//getting rid of unused markers
			Iterator <EnemyMarker> iterator = recognizedEnemy.iterator();
			while(iterator.hasNext())
			{
				EnemyMarker em = iterator.next();
				if(em.noLongerInUse())
				{
					iterator.remove();
				}
			}
			Iterator <EnemyTracker> iterator2 = sightedEnemy.iterator();
			while(iterator2.hasNext())
			{
				EnemyTracker et = iterator2.next();
				if(et.noLongerInUse())
				{
					iterator2.remove();
				}
			}
			/////////////////////////////////////////////////////////
			
			//updating the messages
			for(int i = 0; i < enemyMarkerMessage.size; i ++)
			{
				enemyMarkerMessage.get(i).update();
			}
			/////////////////////////////////////////
			
			// once a message if fully recieved, add it and then remove it
			Iterator <TimeCapsule<Tuple<Humanoid,Vector2>>> iterator3 = enemyMarkerMessage.iterator();
			while(iterator3.hasNext())
			{
				TimeCapsule<Tuple<Humanoid,Vector2>> current = iterator3.next();
				if(current.isRipe())
				{
					/*for(int i = 0; i < 3; i ++)
					{
						System.out.println("CHECKINGCHECKINGCHECKINGCHECKINGCHECKINGCHECKINGCHECKINGCHECKINGCHECKINGCHECKING");
					}
					System.out.println(recognizedEnemy);*/
					checkToAddToRecognized(current.retrieveCapsule().x,current.retrieveCapsule().y);
					//System.out.println(recognizedEnemy);
					/*
					 * 
					 * 
					 * 
					 * 
					 * the other enemy rifleman actually recieves the enemyMarker into recognizedEnemy for a split second, but something is removing it
					 * 
					 * 
					 * f
					 */
					iterator3.remove();
				}
			}	
			////////////////////////////////////////////////////////////
			//if(soldier.debugName.equals("rifleman"))
			//System.out.println(soldier.toString() + sightedEnemy);
		}
		private void checkToAddToRecognized(Humanoid h)
		{
			for(EnemyMarker em : recognizedEnemy)
			{
				if(em.tooCloseTo(h))// if any existing enemy marker is too close to the humanoid, nothing new is added. THIS IS ESSENTIAL. Even if you've managed to pile copies of yourself onto a single area 10 times, they enemy must still periceve you as only one foe. The enemy only attacks you when your visible. If you have enemyMArkers, he'll "suppress" them.
				{
					return;
				}
			}
			recognizedEnemy.addFirst(new EnemyMarker(h,soldier));
		}
		private void checkToAddToRecognized(Humanoid h,Vector2 delayedCenter)// in case a delayed centr is needed
		{
			for(EnemyMarker em : recognizedEnemy)
			{
				if(em.tooCloseTo(delayedCenter))// this is the bug. Instead of comparing to h, which may change values, compare it with THE DELAYED CENTER
				{
					return;
				}
			}
			
			recognizedEnemy.addFirst(new EnemyMarker(h,soldier,delayedCenter));

		}
		private void checkToAddToSighted(Humanoid h)// We don't have to add in an enemyTracker every time the enemytracker moves. since we already have a reference to the enemyrtacker's position, that's all we need
		{
			boolean duplicateFound = false;
			search:
			for(EnemyTracker et : sightedEnemy)// this prevents making copies of enemies already being tracked
			{
				if(h == et.owner)
				{
					duplicateFound = true;
					break search;
				}
			}
			if(!duplicateFound)// an existing enemyMarker can move around. If this new one's memory address does not match any existing ones, then it's assumed to have instantly come into view. Gameplay-wise, you reared your head
			{
				sightedEnemy.add(new EnemyTracker(h,soldier));
				search:
				for(int i = 0; i < recognizedEnemy.size(); i ++)
				{
					EnemyMarker current = recognizedEnemy.get(i);
					if(current.assumedCloseTo(h))// remove one of the enemyMarkers that are near the sighted enemy. Remember, this only happens when the you appear out of nowhere. Running into an existing enemyMarker won't make it disappear. 
					{
						current.userSignOut(soldier);
						// now the soldier will notify all comrades that the enemyMArker near the sightedEnemy is invalid
						current.discoveredAndRevealed();						
						break search;// only allows one nearby enemyMarker to be removed
					}
				}
			}
		}
		private boolean enemyInMind()
		{
			return (recognizedEnemy.size() != 0);
		}
		private Targetable selectEnemyUnknown() // onyl call when enemyInMind() is true
		{
			EnemyMarker ret;
			PriorityQueue<EnemyMarker> pq = new PriorityQueue<EnemyMarker>(new EnemyMarkerComparator());
			pq.addAll(recognizedEnemy); // make sure that this ensures order
			// first whichever enemy is visible
			
			// if not, choose nearest enemyMarker to engage
			EnemyMarker chosen = pq.peek();
			return chosen;
		}
		
		private  class EnemyMarkerComparator implements Comparator <EnemyMarker>
		{
			@Override
			public int compare(EnemyMarker o1, EnemyMarker o2) // head elements are the ones with the least compare value, or the ones closest
			{
				
				double distance1 = Math.hypot(o1.position.x - Humanoid.this.center.x,o1.position.y - Humanoid.this.center.y);
				double distance2 = Math.hypot(o2.position.x - Humanoid.this.center.x,o2.position.y - Humanoid.this.center.y);
				return (int) (distance2 - distance1);
			}

			/*
			 * WHAT THE FUCK IS THIS ANIME FILLER SHIT BELOW
			 * JAVA 8 IS A HORRIBLE ANIME
			 * I CANT GET RID OF IT
			 */
			@Override
			public Comparator<EnemyMarker> reversed() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<EnemyMarker> thenComparing(
					Comparator<? super EnemyMarker> other) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <U> Comparator<EnemyMarker> thenComparing(
					Function<? super EnemyMarker, ? extends U> keyExtractor,
					Comparator<? super U> keyComparator) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <U extends Comparable<? super U>> Comparator<EnemyMarker> thenComparing(
					Function<? super EnemyMarker, ? extends U> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<EnemyMarker> thenComparingInt(
					ToIntFunction<? super EnemyMarker> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<EnemyMarker> thenComparingLong(
					ToLongFunction<? super EnemyMarker> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<EnemyMarker> thenComparingDouble(
					ToDoubleFunction<? super EnemyMarker> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			public  <T extends Comparable<? super T>> Comparator<T> reverseOrder() {
				// TODO Auto-generated method stub
				return null;
			}

			public  <T extends Comparable<? super T>> Comparator<T> naturalOrder() {
				// TODO Auto-generated method stub
				return null;
			}

			public  <T> Comparator<T> nullsFirst(
					Comparator<? super T> comparator) {
				// TODO Auto-generated method stub
				return null;
			}

			public  <T> Comparator<T> nullsLast(
					Comparator<? super T> comparator) {
				// TODO Auto-generated method stub
				return null;
			}

			public  <T, U> Comparator<T> comparing(
					Function<? super T, ? extends U> keyExtractor,
					Comparator<? super U> keyComparator) {
				// TODO Auto-generated method stub
				return null;
			}

			public  <T, U extends Comparable<? super U>> Comparator<T> comparing(
					Function<? super T, ? extends U> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			public  <T> Comparator<T> comparingInt(
					ToIntFunction<? super T> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			public  <T> Comparator<T> comparingLong(
					ToLongFunction<? super T> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			public  <T> Comparator<T> comparingDouble(
					ToDoubleFunction<? super T> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}	
		}
		
		private class EnemyMarker implements Targetable
		{
			private Vector2 position;
			private Humanoid owner;
			private int age;
			private final static int  MAXAGE = 200000;// time for an EnemyMarker to expire
			private final static int REVEALTIME = 300; // time to notify other soldiers that this enemyMarker is moot
			//private boolean valid;
			
			private Array<Humanoid> user;// when a humanoid creates an enemyMarker out of you, it notifies everyone else in 3 seconds. everyone else will be added to user. Only when all users are removed can this be deleted without causing null pointer
			
			private int timeUntilQuit;
						
			private EnemyMarker(Humanoid tracked,Humanoid tracker)
			{ 
				owner = tracked;
				this.position = new Vector2(tracked.center);
				age = 0;
				user = new Array<Humanoid>();
				timeUntilQuit = 0;
				pendAsUser(tracker);
			}
			private EnemyMarker(Humanoid tracked,Humanoid tracker,Vector2 delayedCenter)
			{ 
				owner = tracked;
				this.position = new Vector2(delayedCenter);
				age = 0;
				user = new Array<Humanoid>();
				timeUntilQuit = 0;
				pendAsUser(tracker);
			}
			private void discoveredAndRevealed() // this EnemyMarker has been discoered by a soldier. Now in a few moments he will have notified his comrades. 
			{
				if( MAXAGE - REVEALTIME > 0)// just a little check to prevent age from going into the negatives. 
				{
					age = MAXAGE - REVEALTIME;
				}
				else
				{
					age = 0;
				}
			}
			private void update()
			{
				age ++;
			}
			private boolean isStillValid()
			{
				return age < MAXAGE;
			}
			// this and the next two methods prevent nullExceptions. only when all humanoids ahve stopped using this EnemyMarker will it be removed
			private boolean noLongerInUse()
			{
				return user.size == 0;
			}
		
			private boolean tooCloseTo(Humanoid h)// if you expose yourself too close to one of your previous hitmarkers, the enemy will still treat you as one soldier
			{
				return Math.abs(h.center.x - this.position.x) < ENEMYGUESSRADIUS && Math.abs(h.center.y - this.position.y) < ENEMYGUESSRADIUS;
			}
			private boolean tooCloseTo(Vector2 delayedCenter)
			{
				return Math.abs(delayedCenter.x - this.position.x) < ENEMYGUESSRADIUS && Math.abs(delayedCenter.y - this.position.y) < ENEMYGUESSRADIUS;
			}
			private boolean assumedCloseTo(Humanoid h)// if you expose yourself close to where you have previously revealed yourself, the enemy assumes you are the previous revelaed
			{
				return Math.abs(h.center.x - this.position.x) < ENEMYASSUMERADIUS && Math.abs(h.center.y - this.position.y) < ENEMYASSUMERADIUS;
			}
			public Vector2 getLocation() 
			{
				return new Vector2(position);
			}
			private void pendAsUser(Humanoid h) 
			{
				user.add(h);
			}
			private void userSignOut(Humanoid h) 
			{
				user.removeValue(h,true);
			}
			public String toString()
			{
				return "" + position;
			}
		}
		private class EnemyTracker implements Trackable
		{
			private Humanoid owner;
			private boolean inSight;
			private Array<Humanoid> user;
			private boolean valid;
			
			private EnemyTracker(Humanoid tracked,Humanoid tracker)
			{
				owner =  tracked;
				inSight = true;
				user = new Array<Humanoid>();
				pendAsUser(tracker);
				valid = true;
			}
			public boolean isStillValid() 
			{
				// TODO Auto-generated method stub
				return valid;
			}
			private void noLongerSighted()
			{
				inSight = false;
			}
			private void update()
			{
				//System.out.println(user);
			}
			private boolean noLongerInUse()
			{
				return user.size == 0;
			}
			@Override
			public Humanoid getHumanoid() 
			{
				// TODO Auto-generated method stub
				return owner;
			}
			private void pendAsUser(Humanoid h) 
			{
				// TODO Auto-generated method stub
				user.add(h);
			}
			private void userSignOut(Humanoid h) 
			{
				// TODO Auto-generated method stub
				user.removeValue(h,true);
			}
			public String toString()
			{
				return "" + owner.center;
			}
			
		}
	}

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
	public boolean see(Humanoid h)
	{
		int difference = Direction.getDif(getDirection((int)h.center.x,(int)h.center.y), this.direction);
		return (difference <= 1 || difference == 7) && entityListener.see(center.y,center.x,getHeight(), h.center.y,h.center.x,h.getHeight());
	}
	private boolean see(Vector2 center)
	{
		int difference = Direction.getDif(getDirection((int)center.x,(int)center.y), this.direction);
		return (difference <= 1 || difference == 7) && entityListener.see(center.y,center.x,getHeight(), center.y,center.x,0);
	}
	
	private Tuple<Boolean,Humanoid> seeEnemy()
	{
		return entityListener.seeEnemy(this);
	}
	private boolean hitMarkerNear()
	{
		return entityListener.hitMarkerNear(this);
	}

	private boolean seePeripheral(Humanoid h)
	{
		int difference = Direction.getDif(getDirection((int)h.center.x,(int)h.center.y), this.direction);
		return (difference == 1);
	}
	private boolean seeBino(Humanoid h)
	{
		int difference = Direction.getDif(getDirection((int)h.center.x,(int)h.center.y), this.direction);
		return (difference == 0 || difference == 7);
	}
	private boolean seeTerrain(Humanoid h)
	{
		return entityListener.see(center.y,center.x,getHeight(), h.center.y,h.center.x,h.getHeight());
	}
	private Vector2 getWanderPoint()
	{
		Random r = new Random(System.currentTimeMillis());
		double angle = r.nextInt(361);
		return new Vector2((float)(center.x + Math.cos(angle)*WANDERDISTANCE),(float)(center.y + Math.sin(angle)*WANDERDISTANCE));
	}
	private Tuple<Boolean,LinkedList<Point>>findPath(int tx,int ty)
	{
		return entityListener.findPath((int)this.center.x, (int)this.center.y, tx, ty);
	}
	
	private Tuple<Boolean,Vector2> findCover() // if false return the same tile as the player
	{
		return entityListener.findCover(center.x,center.y,SEARCHCOVERDISTANCE);
	}
	private boolean judgeCover(Humanoid foe)
	{
		return entityListener.judgeCover((int)this.center.x,(int)this.center.y,(int)this.getHeight(),(int)foe.center.x,(int)foe.center.y,(int)foe.getHeight()) > COVERTHRESHOLD;
	}
	private boolean judgeCover(Humanoid foe,int x,int y)
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
		String path = "";
		// unidirectional
		if(state.equals(State.move) && height.equals(Height.crouch))
		{
			state = State.still;
		}
		if(state.equals(State.reload) || state.equals(State.dead))
		{
			path = id.toString().concat(state.toString());
		}
		else
		{
			path = id.toString().concat(state.toString().concat(height.toString().concat(direction.toString())));
		}		
		updateAnimation(path);
	}	
	public String toString()
	{
		return debugName;
	}
}