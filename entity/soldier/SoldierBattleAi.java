package com.mygdx.entity.soldier;

import java.util.HashSet;
import java.util.Set;

import com.mygdx.ai.blackboard.AiEnemyManager;
import com.mygdx.ai.blackboard.AiEnemyManager.AiEnemyCognizable;
import com.mygdx.ai.functional.RoutineExecuter;

final class SoldierBattleAi extends SoldierBattle implements AiEnemyCognizable{
	private final RoutineExecuter ai;
	private final AiEnemyManager enemyManager;
	
	SoldierBattleAi(SoldierBattleMediator sbm, SoldierBattleState sbs) {
		super(sbm, sbs);
		ai = RoutineExecuter.createRiflemanRoutine();
		enemyManager = new AiEnemyManager(this);
	}

	interface AiMediator{
		
	}

	@Override
	protected void addToSighted(SoldierBattle other) {
		
	}


}
