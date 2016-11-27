package com.mygdx.entity.soldier;


import com.mygdx.ai.leaf.AuxiliaryRoutineable;
import com.mygdx.ai.leaf.RoutineFactory;
import com.mygdx.control.Auxiliarable;
import com.mygdx.control.PlayerControllable;
import com.mygdx.map.Path;
import com.mygdx.physics.PrecisePoint;
import com.mygdx.physics.PrecisePoint3;
import com.mygdx.script.Scripter;

class SoldierBattleAuxiliary extends SoldierBattle implements Auxiliarable,AuxiliaryRoutineable
{
	private final Scripter script;
	
	private static final int MAXPATHFINDINGDISTANCE = 30;
	private static final int MAXDISTANCEFROMTILECHECKINGDISTANCE = 21;
	
	private SoldierBattleAuxiliary(SoldierBattleMediator sbm, SoldierBattleState sbs) 
	{
		super(sbm, sbs);
		script = new Scripter();
	}
	static SoldierBattleAuxiliary createAuxiliary(
			InteractionSoldierBattle interactionSoldierBattle){
		return new SoldierBattleAuxiliary(interactionSoldierBattle,SoldierBattleState.createProtectorState());
	}
	public void update(float dt){
		super.update(dt);
		script.update(dt);
	}

	@Override
	protected void addToSighted(SoldierBattle other) {
		
	}

	@Override
	public void aMoveTo(PrecisePoint target) {
		script.pushSequence(RoutineFactory.createSequencialablePathTo(this,target));		
	}

	@Override
	public void aAttackMoveTo(PrecisePoint target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aFollow(PlayerControllable c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aTurn(PrecisePoint target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void completePathTo() {
		soldierBattleState.setToIdle();
	}

	@Override
	public Path calculatePath(PrecisePoint target) {
		return findPath(soldierBattleState.getCenter(),target,MAXPATHFINDINGDISTANCE);
	}

	@Override
	public void beginMoveTo(PrecisePoint destination) {
		soldierBattleState.center.orientVelocity(destination);
		soldierBattleState.face(destination);
		soldierBattleState.move();
	}


	@Override
	public void completeMoveTo() {
		soldierBattleState.center.stopVelocity();
	}

	@Override
	public void stopMoveTo() {
		soldierBattleState.center.stopVelocity();
		soldierBattleState.setToIdle();
	}
	@Override
	public boolean finishedMoveTo(PrecisePoint destination) {
		return  /*soldierBattleState.center.reachTarget(x,y) ||*/ soldierBattleState.center.distanceFrom(destination.x, destination.y) < MAXDISTANCEFROMTILECHECKINGDISTANCE;
	}
	@Override
	public void beginShoot(PrecisePoint3 target) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean finishedShooting() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void completeShoot() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void cancelShoot() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
