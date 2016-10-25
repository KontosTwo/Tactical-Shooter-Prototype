package com.mygdx.ai.blackboard;

import com.mygdx.physics.PrecisePoint3;
import com.mygdx.ai.blackboard.EnemyTracker.Trackable;


public final class EnemyTracker <T extends Trackable>{
	
	public interface Trackable{
		public PrecisePoint3 getLocationForBlackBoard();
	}
	
	private final T enemy;
	
	EnemyTracker(T t){
		enemy = t;
	}
	
	T getTarget(){
		return enemy;
	}
	
	PrecisePoint3 getTargetLocation(){
		return enemy.getLocationForBlackBoard();
	}
	
	boolean isSameEnemy(T other){
		return enemy == other;
	}
	
	/*
	 * change of plan, do not code this whole
	 * "enemy comparer" thing
	 * YAGNI
	 * it is already established that there are 
	 * only two players
	 * So have 2 separate arrays, one for player and auxiliary,
	 * and one for all enemies. 
	 * Since Soldier implements this and Markable Enemy,
	 * for processes involving Trackable Enemy, shove in all enemy
	 * Soldiers
	 */
}
