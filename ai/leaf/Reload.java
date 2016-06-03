package com.mygdx.ai.leaf;

import java.util.function.BinaryOperator;

import com.mygdx.ai.functional.RoutineSequencialable;

class Reload implements RoutineSequencialable
{
	/*
	 * this routine has the actor complete its reload sequence
	 */
	private final Reloadable actor;
	
	public Reload(Reloadable r) 
	{
		actor = r;
	}

	@Override
	public void startSequence() 
	{
		actor.beginReload();
	}

	@Override
	public void update(float dt) 
	{
		
	}

	@Override
	public boolean sequenceIsComplete() 
	{
		return actor.finishedReloading();
	}

	@Override
	public void completeSequence() 
	{
		actor.completeReload();		
	}

	@Override
	public void cancelSequence() 
	{
		actor.cancelReload();
	}

	@Override
	public boolean succeeded() 
	{
		return actor.finishedReloading();
	}

	@Override
	public boolean failed() 
	{
		return false;
	}
	interface Reloadable
	{
		public void beginReload();// start the reload sequence
		public boolean finishedReloading(); // whether or not the reload sequence is completed
		public void completeReload(); // reseting everything
		public void cancelReload(); // interrupt the reload sequence
		public boolean hasAmmo();
	}
	@Override
	public boolean instaSucceeded() 
	{
		// TODO Auto-generated method stub
		return actor.hasAmmo();
	}

	@Override
	public boolean instaFailed() 
	{
		// TODO Auto-generated method stub
		return false;
	}
}

