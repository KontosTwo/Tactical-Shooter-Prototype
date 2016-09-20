package com.mygdx.physics;

import com.badlogic.gdx.math.Vector3;

public class MyVector3 
{
	private Vector3 vector;
	
	public MyVector3()
	{
		vector = new Vector3();
	}
	public MyVector3(float x,float y,float z)
	{
		vector = new Vector3(x,y,z);
	}
	public void set(float x,float y,float z)
	{
		vector.set(x, y, z);
	}
	public void setZ(float z)
	{
		vector.z = z;
	}
	public float getZ()
	{
		return vector.z;
	}
	public float getY()
	{
		return vector.y;
	}
	public float getX()
	{
		return vector.x;
	}
}
