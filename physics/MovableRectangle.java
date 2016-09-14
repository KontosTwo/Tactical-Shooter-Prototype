package com.mygdx.physics;

import com.badlogic.gdx.math.Vector2;

public final class MovableRectangle  
{
	/*
	 * a rectangle that is shifted as its center shifts
	 * Just pass in a SHALLOW copy, like a reference, into the constructor. 
	 * The getters will return the corret coordinates accounting for changes in the center
	 */
	private int height;
	private int width;
	private int offsetX;
	private int offsetY;
	private PrecisePoint center;
	
	public MovableRectangle(PrecisePoint center,int 
			xWidth,int yWidth)
	{
		width = xWidth;
		height = yWidth;
		this.center = center;
	}
	public void setOffset(int x,int y)
	{
		offsetX = x;
		offsetY = y;
	}
	public void centerToBottomCenter()
	{
		offsetY = height/2;
	}
	public void setSize(int x,int y)
	{
		width = x;
		height = y;
	}
	public int getLeft()
	{
		return (int) (center.x + offsetX - width/2);
	}
	public int getRight()
	{
		return (int) (center.x + offsetX + width/2);
	}
	public int getBot()
	{
		return (int) (center.y + offsetY - height/2);
	}
	public int getTop()
	{
		return (int) (center.y + offsetX + height/2);
	}
	public int getWidth()
	{
		return width;
	}
	public int getHeight()
	{
		return height;
	}
	
	public boolean contains(float x,float y)
	{
		return (getLeft()<=x)&&(x<=getRight())&& (getBot()<=y)&&(y<=getTop());
	}
	public boolean overLaps(float x1,float y1,float x2,float y2)// 1 must be top left, 2 must be btotom right
	{
		return crossX(x1,x2) && crossY(y1,y2);
	}
	
	public boolean crossX(float x1,float x2)
	{
		float temp = 0;
		if(x1 > x2)
		{
			temp = x1;
			x1 = x2;
			x2 = temp;
		}
		boolean ret = false;
		if(!(getLeft() > x2) && !(x1 > getRight()))
		{
			ret = true;
		}
		return ret;
	}
	public boolean crossY(float y1,float y2)
	{
		float temp = 0;
		if(y1 < y2)
		{
			temp = y1;
			y1 = y2;
			y2 = temp;
		}
		boolean ret = false;
		if(!(getTop() < y2) && !(y1 < getBot()))
		{
			ret = true;
		}
		return ret;
	}
	public MovableRectangle newBox(PrecisePoint center)
	{
		return new MovableRectangle(center,width,height);
	}
	public boolean intersectAgain(int x1,int y1,int x2,int y2)
	{
		boolean intersect = false;
		if(x2 == (int)getRight() || x2 == (int)getRight())
		{
			if(y2 == y1)
			{
				if(Math.abs(x1-x2) == width)
				{
					intersect = true;
				}
			}
			else
			{
				intersect = true;
			}
		}
		if(y2 == (int)getTop() || y2 == (int)getBot())
		{
			if(x2 == x1)
			{	
				if(Math.abs(y1-y2) == height)
				{
					intersect = true;
				}
			}
			else
			{
				intersect = true;
			}
		}
		return intersect;
	}
	
	private final static class  PointTracker 
	{
		private final PrecisePoint center;
		
		PointTracker(PrecisePoint c)
		{
			center = c;
		}
		
		float getX()
		{
			return center.x;
		}
		float getY()
		{
			return center.y;
		}
	}
}
