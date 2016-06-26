package com.mygdx.ai.blackboard;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.utils.Array;
import com.mygdx.entity.Soldier;

final class EnemyTracker
{
	private Soldier owner;
	private boolean inSight;
	private List<Soldier> user;
	private boolean valid;
	
	EnemyTracker(Soldier tracked,Soldier tracker)
	{
		owner =  tracked;
		inSight = true;
		user = new LinkedList<Soldier>();
		pendAsUser(tracker);
		valid = true;
	}
	boolean isStillValid() 
	{
		return valid;
	}
	void noLongerSighted()
	{
		inSight = false;
	}
	void update()
	{
		//System.out.println(user);
	}
	boolean noLongerInUse()
	{
		return user.isEmpty();
	}
	void pendAsUser(Soldier h) 
	{
		user.add(h);
	}
	void userSignOut(Soldier h) 
	{
		user.remove(h);
	}
}
