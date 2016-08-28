package com.mygdx.ai.blackboard;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.utils.Array;
import com.mygdx.entity.soldier.SoldierBattle;

final class EnemyTracker
{
	private SoldierBattle owner;
	private boolean inSight;
	private List<SoldierBattle> user;
	private boolean valid;
	
	EnemyTracker(SoldierBattle tracked,SoldierBattle tracker)
	{
		owner =  tracked;
		inSight = true;
		user = new LinkedList<SoldierBattle>();
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
	void pendAsUser(SoldierBattle h) 
	{
		user.add(h);
	}
	void userSignOut(SoldierBattle h) 
	{
		user.remove(h);
	}
}
