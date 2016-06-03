package com.mygdx.ai.leaf;

import com.mygdx.ai.functional.RoutineSurvivalable;


class Idle implements RoutineSurvivalable
{
	/*
	 * does nothing.
	 * Allows the actor to not do anything
	 * or in other cases, act upon the actor in other ways than Routines
	 */
	Idleable actor;
	
	public Idle(Idleable i) 
	{
		actor = i;
	}


	@Override
	public boolean conditionUpheld() // fails once the actor meets the criteria for idling
	{
		return actor.canIdle();
	}

	@Override
	public boolean succeeded() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean failed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean instaSucceeded() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean instaFailed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void startSequence() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean sequenceIsComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void completeSequence() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelSequence() {
		// TODO Auto-generated method stub
		
	}
}
interface Idleable
{
	public boolean canIdle();
}
