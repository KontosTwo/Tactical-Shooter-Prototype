package com.mygdx.physics;

public class MovableBox 
{
	private final PrecisePoint center;
	private final MyVector3 dimensions;
	
	public MovableBox(PrecisePoint pp,int xWidth,int yWidth,int zHeight)
	{
		center = pp;
		dimensions = new MyVector3();
	}
	public void setHeight(float h)
	{
		dimensions.setZ(h);
	}
	public float getHeight()
	{
		return dimensions.getZ();
	}
}
