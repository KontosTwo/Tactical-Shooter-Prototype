package com.mygdx.entity.soldier;

import com.mygdx.ai.leaf.ControlledRoutineable;
import com.mygdx.control.Control;
import com.mygdx.control.PlayerControllable;
import com.mygdx.control.Steerable;
import com.mygdx.map.TileGameMap.Collidable;
import com.mygdx.physics.MovableRectangle;
import com.mygdx.physics.PrecisePoint;
import com.mygdx.script.Scripter;

import static com.mygdx.entity.soldier.SoldierBattleState.*;

final class SoldierBattleControlled extends SoldierBattle implements PlayerControllable,Steerable,Collidable
,ControlledRoutineable{	
	
	private static final int XHITBOX = 10;
	private static final int YHITBOX = 10;
	private final Control control;
	private final Scripter script;
	private final MovableRectangle collisionBox;
	
	private SoldierBattleControlled(SoldierBattleMediator mediator,SoldierBattleState state){
		super(mediator,state);
		control = new Control(this);
		script = new Scripter();
		collisionBox = new MovableRectangle(state.center.getCenterReference(),XHITBOX,YHITBOX);
	}
	static SoldierBattleControlled createControlledProtector(SoldierBattleMediator mediator){
		return new SoldierBattleControlled(mediator,SoldierBattleState.createProtectorState());
	}
	public void update(float dt){
		super.update(dt);
		control.update(dt);
		script.update(dt);
		if(control.isActive() && script.isActive()){
			script.cancelScript();
		}
	}

	@Override
	protected void addToSighted() {
		
	}
	
	@Override
	public PrecisePoint provideCenterCamera() {
		return soldierBattleState.center.getCenterReference();
	}

	@Override
	public void moveRight() {
		soldierBattleState.direction = Direction.right;
		soldierBattleState.center.setUnitVelocity(1,0);
	}

	@Override
	public void moveLeft() {
		soldierBattleState.direction = Direction.left;
		soldierBattleState.center.setUnitVelocity(-.707f,0);
	}

	@Override
	public void moveUp() {
		soldierBattleState.direction = Direction.up;
		soldierBattleState.center.setUnitVelocity(0,.707f);
	}

	@Override
	public void moveUpLeft() {
		soldierBattleState.direction = Direction.upleft;
		soldierBattleState.center.setUnitVelocity(-.707f,.707f);
	}

	@Override
	public void moveUpRight() {
		soldierBattleState.direction = Direction.upright;
		soldierBattleState.center.setUnitVelocity(.707f,.707f);
	}

	@Override
	public void moveDown() {
		soldierBattleState.direction = Direction.down;
		soldierBattleState.center.setUnitVelocity(0,-.707f);
	}

	@Override
	public void moveDownLeft() {
		soldierBattleState.direction = Direction.downleft;
		soldierBattleState.center.setUnitVelocity(-.707f,-.707f);
	}

	@Override
	public void moveDownRight() {
		soldierBattleState.direction = Direction.downright;
		soldierBattleState.center.setUnitVelocity(.707f,-.707f);
	}

	@Override
	public void notMoveAction() {
		if(!script.isActive()){
			soldierBattleState.state = State.still;
			soldierBattleState.center.setUnitVelocity(0,0);
		}
	}

	@Override
	public void moveAction() {
		soldierBattleState.state = State.move;	
		if(soldierBattleState.height.equals(Height.crouch)){
			soldierBattleState.stand();
		}
	}

	@Override
	public void cMoveRight(boolean b) {
		control.pendRight(b);
	}

	@Override
	public void cMoveLeft(boolean b) {
		control.pendLeft(b);
	}

	@Override
	public void cMoveUp(boolean b) {
		control.pendUp(b);
	}

	@Override
	public void cMoveDown(boolean b) {
		control.pendDown(b);
	}

	@Override
	public void cShoot(float x, float y, float z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cReload() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cFace(double x, double y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cStand() {
		soldierBattleState.stand();
	}

	@Override
	public void cCrouch() {
		soldierBattleState.crouch();
	}

	@Override
	public void cLay() {
		soldierBattleState.lay();
	}

	@Override
	public void cGrenade(double x, double y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initiateControllable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PrecisePoint provideCenterForDebugger() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	
	
	@Override
	public void beginShoot(double x, double y, double z) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean hasAmmo() {
		// TODO Auto-generated method stub
		return false;
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
	public void failShoot() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getBurstAmount() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void beginReload() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean finishedReloading() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void completeReload() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void cancelReload() {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public PrecisePoint getCurrentCollidablePosition() {
		return soldierBattleState.center.getCenterReference();
	}
	@Override
	public void stoppedbyCollision() {
		soldierBattleState.center.teleportTo(soldierBattleState.centerPrevious);
	}
	@Override
	public boolean aboutToCrossRightOf(int x) {
		return soldierBattleState.centerFuture.x > x;
	}
	@Override
	public boolean aboutToCrossLeftOf(int x) {
		return soldierBattleState.centerFuture.x < x;
	}
	@Override
	public boolean aboutToCrossAbove(int y) {
		return soldierBattleState.centerFuture.y > y;
	}
	@Override
	public boolean aboutToCrossBelow(int y) {
		return soldierBattleState.centerFuture.y < y;
	}
	
	
}
