package com.mygdx.handler;

public interface ControlManagerable 
{
	/*
	 * implemented by actors who use ControlManager to
	 * encapsulate moving behavior triggered by controls
	 * the boolean determines whether or not the entity
	 * will move in the direction
	 */
	public void moveRight();
	public void moveLeft();
	public void moveUp();
	public void moveUpLeft();
	public void moveUpRight();
	public void moveDown();
	public void moveDownLeft();
	public void moveDownRight();
	
	public void notMoveAction();
	public void moveAction();
}
