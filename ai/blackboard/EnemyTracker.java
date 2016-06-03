package com.mygdx.ai.blackboard;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.utils.Array;
import com.mygdx.entity.Humanoid;

final class EnemyTracker
{
	private Humanoid owner;
	private boolean inSight;
	private List<Humanoid> user;
	private boolean valid;
	
	EnemyTracker(Humanoid tracked,Humanoid tracker)
	{
		owner =  tracked;
		inSight = true;
		user = new LinkedList<Humanoid>();
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
	void pendAsUser(Humanoid h) 
	{
		user.add(h);
	}
	void userSignOut(Humanoid h) 
	{
		user.remove(h);
	}
}
