package com.mygdx.ai;

import com.badlogic.gdx.math.Vector3;

class CheckShootPossible implements RoutineSequencialable,RoutineSurvivalable
{

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
	public boolean upheld() {
		// TODO Auto-generated method stub
		return false;
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
interface CheckShootPossibleable
{
	/*
	 * this does not necessarily mean that an enemy is visible
	 * the implementation of this interface could have a soldier
	 * shoot an enemy that he cannot see but can detect 
	 * i.e. an enemy hiding behind a flimsy bush
	 */
	public boolean enemyAvailableToShoot();
	public Vector3 getProjectedEnemyLocation();
}