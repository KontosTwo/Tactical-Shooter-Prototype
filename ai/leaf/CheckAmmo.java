package com.mygdx.ai.leaf;

import java.util.function.BinaryOperator;

import com.mygdx.ai.functional.RoutineSurvivalable;

class CheckAmmo implements RoutineSurvivalable
{
	/*
	 * this decorator will make the actor reload only if the actor is in need of ammunition. it can also serve as a survivalRoutine, and make other routines
	 * fail if the actor does not have enough ammunition
	 * CheckAmmo can fail before it starts if the actor never needed to reload in the first place
	 */
	private final Reload reload;
	private final CheckAmmoable actor;
	
	public CheckAmmo(CheckAmmoable ca,Reload r) 
	{
		actor = ca;
		reload = r;
	}
	
	@Override
	public boolean conditionUpheld() 
	{
		return !actor.needToReload();
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


}
interface CheckAmmoable
{
	public boolean needToReload();// whether or not the actor needs to reload and thus begin the reload sequence
}
