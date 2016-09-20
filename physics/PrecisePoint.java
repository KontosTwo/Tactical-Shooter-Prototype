package com.mygdx.physics;


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
	public void add(float x,float y)
	{
		this.x += x;
		this.y += y;
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
	public boolean isZero()
	{
		return x == 0 && y == 0;
	}
	public PrecisePoint(PrecisePoint p)
	{
		this(p.x,p.y);
	}
	public String toString()
	{
		return "" + x + " " + y;
	}
	public boolean equals(Object other)
	{
		if(other == null){
			return false;
		}
		if(!(other instanceof PrecisePoint)){
			return false;
		}
		if((Math.abs(((PrecisePoint)other).x - this.x) < .1) &&
				Math.abs(((PrecisePoint)other).y - this.y) < .1){
			return true;
		}
		return false;
	}
	public int hashCode(){
		int hash = 7;
	    hash = (int) (71 * hash + this.x);
	    hash = (int) (71 * hash + this.y);
	    return hash;
	}
}
