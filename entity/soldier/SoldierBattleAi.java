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
	
	private SoldierBattleAi(SoldierBattleMediator sbm, SoldierBattleState sbs) {
		super(sbm, sbs);
		ai = RoutineExecuter.createRiflemanRoutine();
		enemyManager = new AiEnemyManager<>(this);
	}
	
	static SoldierBattleAi createRifleman(SoldierBattleMediator sbm){
		return new SoldierBattleAi(sbm,SoldierBattleState.createProtectorState());
	}

	@Override
	void update(float dt){
		super.update(dt);
		enemyManager.update();
	}
	
	@Override
	protected void addToSighted(SoldierBattle other) {
		enemyManager.spotEnemy(other);
	}

	@Override
	public boolean aiSeeDirectly(PrecisePoint3 target) {
		return canSee(target);
	}

	@Override
	public boolean aiSeeThroughTerrain(PrecisePoint3 target) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PrecisePoint3 getPositionForCognizable() {
		return soldierBattleState.getVantagePoint();
	}
	
	public String toString(){
		return enemyManager.toString();
	}
}
