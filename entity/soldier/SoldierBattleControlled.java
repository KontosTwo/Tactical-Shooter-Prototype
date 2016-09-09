package com.mygdx.entity.soldier;

import com.mygdx.control.Control;
import com.mygdx.control.PlayerControllable;
import com.mygdx.control.Steerable;
import com.mygdx.graphic.Animator;
import com.mygdx.physics.MovablePoint;
import com.mygdx.physics.PrecisePoint;

class SoldierBattleControlled implements SoldierBattle,PlayerControllable,Steerable
{
	private SoldierBattleState state;
	private Animator animator;
	private Control control;
	private MovablePoint center;
	
	SoldierBattleControlled(SoldierBattleState s)
	{
		animator = new Animator();
		state = s;
	}
	
	@Override
	public void render() 
	{
		
	}


	@Override
	public void update() 
	{
		
	}
	
	@Override
	public PrecisePoint provideCenterCamera() 
	{
		// TODO Auto-generated method stub
		return null;
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
	public void switchAnimation(String animeFile, String dataFile) {
		// TODO Auto-generated method stub
		
	}

	


	
}
