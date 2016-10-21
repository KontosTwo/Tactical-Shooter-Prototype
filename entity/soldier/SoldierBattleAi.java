package com.mygdx.entity.soldier;

import com.mygdx.ai.functional.RoutineExecuter;

final class SoldierBattleAi extends SoldierBattle{
	private final RoutineExecuter ai;
	
	SoldierBattleAi(SoldierBattleMediator sbm, SoldierBattleState sbs) {
		super(sbm, sbs);
		ai = RoutineExecuter.createRiflemanRoutine();
	}

	interface AiMediator{
		
	}

	@Override
	protected void addToSighted(SoldierBattle other) {
		
	}
}
