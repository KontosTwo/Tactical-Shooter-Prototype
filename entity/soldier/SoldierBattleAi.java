package com.mygdx.entity.soldier;

import java.util.HashSet;
import java.util.Set;

import com.mygdx.ai.blackboard.AiEnemyManager;
import com.mygdx.ai.blackboard.AiEnemyManager.AiEnemyCognizable;
import com.mygdx.ai.functional.RoutineExecuter;
import com.mygdx.physics.PrecisePoint3;

final class SoldierBattleAi extends SoldierBattle implements AiEnemyCognizable{
	private final RoutineExecuter ai;
	private final AiEnemyManager<SoldierBattleAi,SoldierBattle> enemyManager;
	
	SoldierBattleAi(SoldierBattleMediator sbm, SoldierBattleState sbs) {
		super(sbm, sbs);
		ai = RoutineExecuter.createRiflemanRoutine();
		enemyManager = new AiEnemyManager<>(this);
	}

	interface AiMediator{
		
	}

	@Override
	protected void addToSighted(SoldierBattle other) {
		
	}

	@Override
	public boolean aiSeeDirectly(PrecisePoint3 target) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean aiSeeThroughTerrain(PrecisePoint3 target) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PrecisePoint3 getPositionForAi() {
		// TODO Auto-generated method stub
		return null;
	}


}
