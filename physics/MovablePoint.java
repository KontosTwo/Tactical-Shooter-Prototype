package com.mygdx.physics;

import com.mygdx.graphic.Animator;


public class MovablePoint 
{
	private final PrecisePoint center;
	private final MyVector2 unitVelocity;
	private float speed;
	
	public MovablePoint()
	{
		center = new PrecisePoint();
		speed = 0;
		unitVelocity = new MyVector2();
	}
	public PrecisePoint getCenterReference()
	{
		return center;
	}
	public void update()
	{
		center.add(unitVelocity.getX()*speed,unitVelocity.getY()*speed);
	}
	public void setUnitVelocity(double x,double y)
	{
		unitVelocity.set(x, y);
	}
	public void setSpeed(float s)
	{
		speed = s;
	}
	public void teleportTo(PrecisePoint dest)
	{
		center.set(dest);
	}
	public void teleportTo(float x,float y)
	{
		center.set(x,y);
	}
	public PrecisePoint createProjectedLocation()
	{
		PrecisePoint newLocation = new PrecisePoint(center);
		newLocation.add(unitVelocity.getX()*speed,unitVelocity.getY()*speed);
		return newLocation;
	}
}
