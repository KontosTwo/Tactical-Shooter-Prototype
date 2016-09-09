package com.mygdx.ai.blackboard;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.utils.Array;
import com.mygdx.entity.soldier.SoldierBattleConcrete;

final class EnemyTracker
{
	private SoldierBattleConcrete owner;
	private boolean inSight;
	private List<SoldierBattleConcrete> user;
	private boolean valid;
	
	EnemyTracker(SoldierBattleConcrete tracked,SoldierBattleConcrete tracker)
	{
		owner =  tracked;
		inSight = true;
		user = new LinkedList<SoldierBattleConcrete>();
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
	void pendAsUser(SoldierBattleConcrete h) 
	{
		user.add(h);
	}
	void userSignOut(SoldierBattleConcrete h) 
	{
		user.remove(h);
	}
}
