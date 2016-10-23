package com.mygdx.ai.blackboard;

import com.mygdx.physics.PrecisePoint3;

class TrackableEnemy{
	private final PrecisePoint3 target;
	
	TrackableEnemy(PrecisePoint3 p){
		target = p;
	}
	
	PrecisePoint3 getPosition(){
		return target;
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
