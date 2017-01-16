package com.mygdx.ai.blackboardold;

import java.util.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.ai.blackboard.Markable;
import com.mygdx.physics.PrecisePoint;
/*
final class EnemyMarker
{
	private PrecisePoint position;
	private Markable target;
	private int age;
	private final static int  MAXAGE = 200000;// time for an EnemyMarker to expire
	private final static int REVEALTIME = 300; // time to notify other soldiers that this enemyMarker is moot	
	private static final int ENEMYGUESSRADIUS = 60;// radius of each enemyMarker. if another enemymarker attempts to spawn within this radius, it won't
	private static final int ENEMYASSUMERADIUS = 30; // when the Markable is exposed, it is assumed to be all Markables in this radius
	private HashSet<Object> tracker;// these are all the objects that are tracking the Markable
	private int timeUntilQuit;
		
	EnemyMarker(Markable tracked,Object seeker)
	{ 
		target = tracked;
		this.position = new PrecisePoint(tracked.getPosition());
		age = 0;
		tracker = new HashSet<>();
		timeUntilQuit = 0;
		pendAsUser(seeker);
	}
	/*EnemyMarker(MarkableEnemy tracked,SoldierBattleConcrete seeker,PrecisePoint delayedCenter)
	{ 
		target = tracked;
		this.position = new PrecisePoint(delayedCenter);
		age = 0;
		tracker = new HashSet<>();
		timeUntilQuit = 0;
		pendAsUser(seeker);
	}
	PrecisePoint getPosition()
	{
		return target.getPosition();
	}
	void discoveredAndRevealed() // this EnemyMarker has been discoered by a soldier. Now in a few moments he will have notified his comrades. 
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
	void update()
	{
		age ++;
	}
	boolean isStillValid()
	{
		return age < MAXAGE;
	}
	// this and the next two methods prevent nullExceptions. only when all humanoids ahve stopped using this EnemyMarker will it be removed
	boolean noLongerInUse()
	{
		return tracker.size() == 0;
	}

	boolean tooCloseTo(Markable m)// if you expose yourself too close to one of your previous hitmarkers, the enemy will still treat you as one soldier
	{
		return Math.abs(m.getPosition().x - this.position.x) < ENEMYGUESSRADIUS && Math.abs(m.getPosition().y - this.position.y) < ENEMYGUESSRADIUS;
	}
	boolean tooCloseTo(Vector2 delayedCenter)
	{
		return Math.abs(delayedCenter.x - this.position.x) < ENEMYGUESSRADIUS && Math.abs(delayedCenter.y - this.position.y) < ENEMYGUESSRADIUS;
	}
	boolean assumedCloseTo(Markable m)// if you expose yourself close to where you have previously revealed yourself, the enemy assumes you are the previous revelaed
	{
		return Math.abs(m.getPosition().x - this.position.x) < ENEMYASSUMERADIUS && Math.abs(m.getPosition().y - this.position.y) < ENEMYASSUMERADIUS;
	}
	void pendAsUser(Object o) 
	{
		tracker.add(0);
	}
	void userSignOut(Object o) 
	{
		tracker.remove(o);
	}
	public String toString()
	{
		return "" + position;
	}
	static class EnemyMarkerComparator implements Comparator<EnemyMarker>
	{
		private PrecisePoint observer;
		
		EnemyMarkerComparator(PrecisePoint observer)
		{
			this.observer = new PrecisePoint(observer);
		}
		
		@Override
		public int compare(EnemyMarker o1, EnemyMarker o2) {
			return (int) (Math.sqrt(Math.pow(Math.abs(o1.position.x - observer.x),2) + Math.pow(Math.abs(o1.position.y - observer.y),2)) - 
					Math.sqrt(Math.pow(Math.abs(o2.position.x - observer.x),2) + Math.pow(Math.abs(o2.position.y - observer.y),2)));
		}	
	}
}*/
