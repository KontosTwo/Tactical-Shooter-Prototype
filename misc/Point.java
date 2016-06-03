package com.mygdx.misc;

public class Point 
{
	private int x;
	private int y;
	
	public Point(int x,int y)
	{
		this.x = x;
		this.y = y;
	}
	public Point()
	{
		this(0,0);
	}
	public Point(Point p)
	{
		this.x = p.x;
		this.y = p.y;
	}
	
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public void setY(int y)
	{
		this.y = y;
	}
	public void setX(int x)
	{
		this.x = x;
	}
	public void set(Point p)
	{
		this.x = p.x;
		this.y = p.y;
	}
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}
	public int hashCode()
	{
		int hash = 1;
        hash = hash * 17 + x;
        hash = hash * 31 + y;
        return hash;
	}
	public boolean equals(Object o)
	{
		return ((Point)o).x == this.x &&  ((Point)o).y == this.y;
	}
	public boolean semiEquals(Object o)
	{
		return ((Point)o).x == this.x ||  ((Point)o).y == this.y;
	}
	public Point toMapCoord(int tileSize)
	{
		return new Point((x * tileSize) + tileSize/2, (y * tileSize) + tileSize/2);
	}
	public Point toTileCoord(int tileSize)
	{
		return new Point((x / tileSize) , (y / tileSize) );
	}
}
