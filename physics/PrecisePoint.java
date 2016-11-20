package com.mygdx.physics;

/**
 * A 2-D point that uses floats instead of doubles
 */
public class PrecisePoint implements Comparable
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
	public static PrecisePoint createMidpointof(PrecisePoint first,PrecisePoint second){
		return new PrecisePoint((first.x + second.x)/2,(first.y + second.y)/2);
	}
	public String toString()
	{
		return "" + x + " " + y;
	}
	public static double manhattanDistance(PrecisePoint a,PrecisePoint b){
		return Math.abs(a.x - b.x) + Math.abs(a.y - b.y); 
	}
	public static double euclideanDistance(PrecisePoint a,PrecisePoint b){
		return Math.sqrt(Math.pow(Math.abs(a.x - b.x),2) + Math.pow(Math.abs(a.y - b.y),2)); 
	}
	public boolean equals(Object other)
	{
		if(other == null){
			return false;
		}
		if(!(other instanceof PrecisePoint)){
			return false;
		}
		if((Math.abs(((PrecisePoint)other).x - this.x) < .001) &&
				Math.abs(((PrecisePoint)other).y - this.y) < .001){
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
	@Override
	public int compareTo(Object o) {
		return Math.round(((PrecisePoint)o).x - this.x) + Math.round(((PrecisePoint)o).y - this.y);
	}
}
