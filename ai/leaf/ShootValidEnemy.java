package com.mygdx.ai.leaf;

import com.mygdx.ai.functional.RoutineSurvivalable;

class ShootValidEnemy implements RoutineSurvivalable{

	private final ShootValidEnemyable actor;
	private final ShootBurst shooter;
	
	ShootValidEnemy(ShootValidEnemyable sve,ShootBurst bs){
		actor = sve;
		shooter = bs;
	}
	
	@Override
	public void calculateInstaHeuristic() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean instaSucceededRoutine() {
		return actor.targetableEnemyExists() == false;	
	}

	@Override
	public boolean instaFailedRoutine() {
		return false;
	}

	@Override
	public boolean succeededRoutine(){
		return actor.targetableEnemyExists() == false;
	}

	@Override
	public boolean failedRoutine() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateRoutine(float dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startRoutine() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void completeRoutine() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelRoutine() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean conditionUpheld() {
		return actor.targetableEnemyExists() == false;
	}
	
	interface ShootValidEnemyable{
		public boolean targetableEnemyExists();
	}
}