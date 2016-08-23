package com.mygdx.ai.leaf;

import com.mygdx.ai.functional.Repeater;
import com.mygdx.ai.functional.RoutineSequencialable;
import com.mygdx.script.Sequencialable;

class BurstShoot implements RoutineSequencialable
{
	private final Repeater repeater;
	private final Shoot shoot;
	private final BurstShootable actor;
	private float x;
	private float y;
	private float z;
	
	BurstShoot(BurstShootable actor,Shoot shoot)
	{
		repeater = new Repeater(shoot);
		this.actor = actor;
		this.shoot = shoot;
	}
	void designateTarget(float x,float y,float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public void startSequence() 
	{
		shoot.designateTarget(x, y, z);
		repeater.setTimes(actor.getBurstAmount());
		repeater.startSequence();
	}

	@Override
	public void update(float dt) 
	{
		repeater.update(dt);
	}

	@Override
	public boolean sequenceIsComplete()
	{
		return repeater.sequenceIsComplete();
	}

	@Override
	public void completeSequence() 
	{
		repeater.completeSequence();
	}

	@Override
	public void cancelSequence() 
	{
		repeater.cancelSequence();
	}

	@Override
	public boolean succeeded()
	{
		return repeater.succeeded();
	}

	@Override
	public boolean failed() 
	{
		return false;
	}
	
	@Override
	public boolean instaSucceeded() 
	{
		return false;
	}
	@Override
	public boolean instaFailed() 
	{
		return false;
	}

	interface BurstShootable
	{
		public int getBurstAmount();
	}
	
}

