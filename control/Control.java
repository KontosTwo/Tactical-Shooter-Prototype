package com.mygdx.control;



public class Control
{
	/*
	 * encapsulating movement behaviors for classes implementing
	 * ControlManagerable
	 * Other classes should have an instance of this
	 * Supports keyboard input which takes into account
	 * pressing up and releasing movement keys
	 * also supports switching from moving one
	 * direction to the opposite without causing issues
	 */
	private Steerable controller;
	private boolean moveLeft;
	private boolean moveRight;
	private boolean moveUp;
	private boolean moveDown;
	private boolean enabled;
	private boolean moving;
	
	public Control(Steerable c)
	{
		controller = c;
		enabled = true;
		moving = false;
	}
	public void enableMoving(boolean b)
	{
		enabled = b;
	}
	public boolean isActive()
	{
		return moving;
	}
	public void pendLeft(boolean b)
	{
		if(moveRight && b) 
        {
        	moveRight = false;
        }
        moveLeft = b;
	}
	public void pendRight(boolean b)
	{
		if(moveLeft && b) 
        {
        	moveLeft = false;
        }
        moveRight = b;
	}
	public void pendUp(boolean b)
	{
		if(moveDown && b) 
        {
        	moveDown = false;
        }
        moveUp = b;
	}
	public void pendDown(boolean b)
	{
		if(moveUp && b) 
        {
        	moveUp = false;
        }
        moveDown = b;
	}


	public void update(float dt) 
	{
		moving = moveRight || moveLeft || moveUp || moveDown;
		boolean moved = true;
		if(enabled)
		{
			if(moveUp)
			{
				if(moveRight)
				{
					controller.moveUpRight();
				}
				else if(moveLeft)
				{
					controller.moveUpLeft();
				}
				else
				{
					controller.moveUp();
				}
			}
			else if(moveDown)
			{
				if(moveRight)
				{
					controller.moveDownRight();
				}
				else if(moveLeft)
				{
					controller.moveDownLeft();
				}
				else
				{
					controller.moveDown();
				}
			}
			else
			{
				if(moveRight)
				{
					controller.moveRight();
				}
				else if(moveLeft)
				{
					controller.moveLeft();
				}
				else
				{
					moved = false;
				}
			}	
		}
		/*
		 * special cases for the implementation
		 * to account for when it moves and when it doesn't
		 */
		if(moved)
		{
			controller.moveAction();
		}
		else
		{
			controller.notMoveAction();
		}
	}
}
