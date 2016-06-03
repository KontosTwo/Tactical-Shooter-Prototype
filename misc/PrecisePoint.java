package com.mygdx.misc;

public class PrecisePoint 
{
	public double x;
	public double y;
	
	public PrecisePoint(double x,double y)
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
