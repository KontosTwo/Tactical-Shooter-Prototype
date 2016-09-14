package com.mygdx.physics;

import com.mygdx.graphic.Animator;


public class MovablePoint 
{
	private final PrecisePoint center;
	private final MyVector2 velocity;
	
	public MovablePoint()
	{
		center = new PrecisePoint();
		velocity = new MyVector2();
	}
	public PrecisePoint getCenterReference()
	{
		return center;
	}
	public void update()
	{
		center.add(velocity);
	}
	public void setVelocity(int x,int y)
	{
		velocity.set(x, y);
	}
}
