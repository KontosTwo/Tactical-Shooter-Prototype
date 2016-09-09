package com.mygdx.physics;

import com.mygdx.misc.MyVector2;

public class PrecisePoint 
{
	/*
	 * precise coordinates
	 * useful for indication an entity's location
	 */
	public float x;
	public float y;
	
	public PrecisePoint(float x,float y)
	{
		this.x = x;
		this.y = y;
	}
	public PrecisePoint(double x,double y)
	{
		this.x = (float)x;
		this.y = (float)y;
	}
	public void add(MyVector2 vector)
	{
		this.x += vector.getX();
		this.y += vector.getY();
	}
	public void set(float x,float y)
	{
		this.x = x;
		this.y = y;
	}
	public void set(PrecisePoint p)
	{
		this.x = p.x;
		this.y = p.y;
	}
	public PrecisePoint()
	{
		this(0,0);
	}
	public PrecisePoint(PrecisePoint p)
	{
		this(p.x,p.y);
	}
	public String toString()
	{
		return "" + x + " " + y;
	}
}
