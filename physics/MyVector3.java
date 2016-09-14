package com.mygdx.physics;

import com.badlogic.gdx.math.Vector3;

public class MyVector3 
{
	private Vector3 vector;
	
	public MyVector3()
	{
		vector = new Vector3();
	}
	public void set(float x,float y,float z)
	{
		vector.set(x, y, z);
	}
}
