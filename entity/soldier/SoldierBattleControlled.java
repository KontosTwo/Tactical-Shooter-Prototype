package com.mygdx.entity.soldier;

import com.mygdx.control.Control;
import com.mygdx.control.PlayerControllable;
import com.mygdx.control.Steerable;
import com.mygdx.graphic.Animator;
import com.mygdx.physics.MovablePoint;
import com.mygdx.physics.MovableRectangle;
import com.mygdx.physics.PrecisePoint;

class SoldierBattleControlled extends SoldierBattle implements PlayerControllable,Steerable
{	
	private static final int XHITBOX = 10;
	private static final int YHITBOX = 10;
	// make this a singleton
	private Control control;
	private final MovableRectangle collisionBox;
	
	SoldierBattleControlled(SoldierBattleMediator mediator,SoldierBattleState state)
	{
		super(mediator,state);
		control = new Control(this);
		collisionBox = new MovableRectangle(center.getCenterReference(),XHITBOX,YHITBOX);
	}
	public void update(float dt)
	{
		super.update(dt);
		control.update(dt);
	}
	
	@Override
	public PrecisePoint provideCenterCamera() {
		// TODO Auto-generated method stub
		return center.getCenterReference();
	}

	@Override
	public void moveRight() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveLeft() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveUpLeft() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveUpRight() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveDownLeft() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveDownRight() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notMoveAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cMoveRight(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cMoveLeft(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cMoveUp(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cMoveDown(boolean b) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cCrouch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cLay() {
		// TODO Auto-generated method stub
		
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
	protected void addToSighted() {
		// TODO Auto-generated method stub
		
	}
	
	
}
