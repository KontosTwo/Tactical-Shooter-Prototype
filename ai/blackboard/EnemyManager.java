package com.mygdx.ai.blackboard;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.misc.TimeCapsule;
import com.mygdx.misc.Pair;
import com.mygdx.physics.PrecisePoint;
import com.mygdx.whyishouldlearntocodebeforestartingaproject.SoldierBattleConcrete;

public final class EnemyManager
{
	/*private List<EnemyMarker> recognizedEnemy;		
	private List<EnemyTracker> sightedEnemy;
	
	private static final int ENEMYREVEALRADIUS = 50; 
	private static final int ENEMYMARKERRADIUS = 60; 
	private static final int MAXENEMYGUESSES = 5;
	private static final int TIMETONOTIFYALLIES = 50;
	private final EnemyCognizable soldier;
	
	private EnemyTracker targetVisible;
	private EnemyMarker targetObscured;

	private List<TimeCapsule<PrecisePoint>> enemyMarkerMessage;// list of notifications of newly discovered enemies
	// the tuple exists so that both a shallow copy of the humanoid and a deep copy of the position is retained
	
	public EnemyManager(EnemyCognizable soldier)
	{
		recognizedEnemy = new LinkedList<>();
		sightedEnemy = new LinkedList<>();
		this.soldier = soldier;
		enemyMarkerMessage = new LinkedList<>();
	}
	public void spotEnemy(TrackableEnemy enemy)
	{
		checkToAddToSighted(enemy);
	}
	private void checkToAddToSighted(TrackableEnemy enemy)// We don't have to add in an enemyTracker every time the enemytracker moves. since we already have a reference to the enemyrtacker's position, that's all we need
	{
		boolean duplicateFound = false;
		search:
		for(EnemyTracker enemyTracker : sightedEnemy)// this prevents making copies of enemies already being tracked
		{
			if(enemyTracker. == enemy)
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
	public void delayedNotifyEnemyAt(PrecisePoint location)
	{
		enemyMarkerMessage.add(new TimeCapsule<PrecisePoint>(new PrecisePoint(location),TIMETONOTIFYALLIES));
	}
	public boolean coveredAgainstUnseenEnemy()
	{
		boolean ret = true;
		// searches through all visible "allied" and see if they are favorably positioned against them
		for(EnemyMarker e : recognizedEnemy)
		{
			if(!Soldier.this.coveredAgainst(standHeight,e.position.x,e.position.y))// this needs verification
			{
				ret = false;
			}
		}
		return ret;
	}
	public boolean coveredAgainstSeenEnemy()
	{
		boolean ret = true;
		// searches through all visible "allied" and see if they are favorably positioned against them
		for(EnemyTracker e : sightedEnemy)
		{
			if(!Soldier.this.coveredAgainst(standHeight,e.owner.center.x,e.owner.center.y))// this needs edit
			{
				ret = false;
			}
		}
		return ret;
	}
	public void update()
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
				if(soldier.allegiance.equals(Allegiance.dalmati))
					System.out.println(soldier.verifyGroundOf(et.owner.center));

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
		Iterator <TimeCapsule<Tuple<Soldier,Vector2>>> iterator3 = enemyMarkerMessage.iterator();
		while(iterator3.hasNext())
		{
			TimeCapsule<Tuple<Soldier,Vector2>> current = iterator3.next();
			if(current.ready())
			{
				for(int i = 0; i < 3; i ++)
				{
					System.out.println("CHECKINGCHECKINGCHECKINGCHECKINGCHECKINGCHECKINGCHECKINGCHECKINGCHECKINGCHECKING");
				}
				System.out.println(recognizedEnemy);
				checkToAddToRecognized(current.retrieve().x,current.retrieve().y);
				//System.out.println(recognizedEnemy);
				
				 * 
				 * 
				 * 
				 * 
				 * the other enemy rifleman actually recieves the enemyMarker into recognizedEnemy for a split second, but something is removing it
				 * 
				 * 
				 * f
				 
				iterator3.remove();
			}
		}	
		////////////////////////////////////////////////////////////
		//if(soldier.debugName.equals("rifleman"))
		//System.out.println(soldier.toString() + sightedEnemy);
	}
	private void checkToAddToRecognized(Soldier h)
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
	private void checkToAddToRecognized(Soldier h,Vector2 delayedCenter)// in case a delayed centr is needed
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
	*/
}
