package com.mygdx.misc;

public class Vector 
{
	public double x;
	public double y;
	
	public Vector(double x,double y)
	{
		this.x = x;
		this.y = y;
	}
	public Vector(Vector v)
	{
		x = v.y;
		y = v.y;
	}
	public Vector()
	{
		x = 0;
		x = 0;
	}
	
	public void set(Vector v)
	{
		this.x = v.x;
		this.y = v.y;
	}
	public void add(Vector v)
	{
		this.x += v.x;
		this.y += v.y;
	}
	public void scale(double s)
	{
		this.x *= s;
		this.y *= s;
	}
	public void rotateDegree(double degree)
	{
		
	}
}
