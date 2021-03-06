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
	public void set(MyVector3 other){
		this.vector.set(other.vector);
	}
	public void scale(float factor){
		vector.scl(factor);
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
	public void rotate(){
		
	}
	public boolean equals(Object other){
		if(other == null){
			return false;
		}
		if(!(other instanceof MyVector3)){
			return false;
		}
		return this.vector.equals(((MyVector3)other).vector);
	}
	public int hashCode(){
		return vector.hashCode();
	}
}
