package com.mygdx.misc;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.physics.PrecisePoint;

public class MyVector2
{
	/*
	 * A wrapper class for LibGDX's Vector2
	 * It accepts doubles as arguments instead of floats
	 * because most machines are 64 bit anyways
	 */
	private final Vector2 vector;
	
	public MyVector2(double x,double y)
	{
		vector = new Vector2((float)x,(float)y);
	}
	public MyVector2(float x,float y)
	{
		vector = new Vector2(x,y);
	}
	public MyVector2(MyVector2 vectorArg)
	{
		vector = new Vector2(vectorArg.vector);
	}
	public MyVector2()
	{
		vector = new Vector2();
	}
	
	public void set(MyVector2 vectorArg)
	{
		vector.set(vectorArg.vector);
	}
	public void set(double x,double y)
	{
		vector.set((float)x,(float)y);
	}
	public void add(MyVector2 vectorArg)
	{
		vector.add(vectorArg.vector);
	}
	public void scale(double s)
	{
		vector.scl((float)s);
	}
	public void rotateDegree(double degree)
	{
		vector.rotate((float)degree);
	}
	public PrecisePoint asPrecisePoint()
	{
		return new PrecisePoint(vector.x,vector.y);
	}
	public float getX()
	{
		return vector.x;
	}
	public float getY()
	{
		return vector.y;
	}
	public void setZero()
	{
		vector.setZero();
	}
}
