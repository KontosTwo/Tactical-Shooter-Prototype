package com.mygdx.misc;

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
	public void set(float x,float y)
	{
		this.x = x;
		this.y = y;
	}
	public PrecisePoint()
	{
		this(0,0);
	}
	public PrecisePoint(PrecisePoint p)
	{
		this(p.x,p.y);
	}
}
