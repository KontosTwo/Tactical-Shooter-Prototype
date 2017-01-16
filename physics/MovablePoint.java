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
	public void stopVelocity(){
		unitVelocity.set(0, 0);
	}
	public PrecisePoint createProjectedLocation()
	{
		PrecisePoint newLocation = new PrecisePoint(center);
		newLocation.add(unitVelocity.getX()*speed,unitVelocity.getY()*speed);
		return newLocation;
	}
	public boolean reachTarget(double x, double y) {
		// TODO Auto-generated method stub
		double futurex = center.x + (unitVelocity.getX()*speed);
		double futurey = center.y + (unitVelocity.getY()*speed);
		return !(sameSign(x - futurex,x - center.x)
				&& sameSign(y - futurey,y - center.y));
	}
	 private boolean sameSign(double x1,double x2)// potential problem if either is 0
	{
		return x1*x2 > 0;
	}
	 public void orientVelocity(PrecisePoint location)
	{
		 double angle = Math.atan2((location.y - center.y),(location.x - center.x));
		unitVelocity.set((float)Math.cos(angle),(float)Math.sin(angle));
	}
	 
	public int distanceFrom(double x, double y) {
			return (int) 
				Math.hypot(
					Math.pow(x-center.x, 2),
					Math.pow(y-center.y, 2)
		);
	}
}
