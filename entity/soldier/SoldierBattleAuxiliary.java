package com.mygdx.entity.soldier;

import java.util.LinkedList;
import java.util.List;

import com.mygdx.ai.leaf.AuxiliaryRoutineable;
import com.mygdx.ai.leaf.RoutineFactory;
import com.mygdx.control.Auxiliarable;
import com.mygdx.control.PlayerControllable;
import com.mygdx.misc.Pair;
import com.mygdx.physics.Point;
import com.mygdx.script.Scripter;

class SoldierBattleAuxiliary extends SoldierBattle implements Auxiliarable,AuxiliaryRoutineable
{
	private final Scripter script;
	
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
	protected void addToSighted() {
		
	}

	@Override
	public void aMoveTo(double x, double y) {
		script.pushSequence(RoutineFactory.createPathToSeq(x,y, this));		
	}

	@Override
	public void aAttackMoveTo(double x, double y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aFollow(PlayerControllable c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aTurn(double x, double y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void completePathTo() {
		soldierBattleState.idle();
	}

	@Override
	public Pair<Boolean, List<Point>> calculatePath(double x, double y) {
		// TODO Auto-generated method stub
		return findPath((int)soldierBattleState.center.getCenterReference().x,
				(int)soldierBattleState.center.getCenterReference().y,
				(int)x, (int)y);
	}

	@Override
	public void beginMoveTo(double x, double y) {
		soldierBattleState.center.orientVelocity(x,y);
		soldierBattleState.face(x,y);
		soldierBattleState.move();
	}


	@Override
	public void completeMoveTo() {
		soldierBattleState.center.stopVelocity();
	}

	@Override
	public void stopMoveTo() {
		soldierBattleState.center.stopVelocity();
		soldierBattleState.idle();
	}
	@Override
	public boolean finishMoveTo(double x, double y) {
		return  soldierBattleState.center.reachTarget(x,y) || soldierBattleState.center.distanceFrom(x, y) < 50;
	}
	
	
	
	
}
