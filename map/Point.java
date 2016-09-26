package com.mygdx.map;

final class Point 
{

	private int x;
	private int y;
	
	Point(int x,int y)
	{
		this.x = x;
		this.y = y;
	}
	Point()
	{
		this(0,0);
	}
	Point(Point p)
	{
		this.x = p.x;
		this.y = p.y;
	}
	
	int getX()
	{
		return x;
	}
	int getY()
	{
		return y;
	}
	void setY(int y)
	{
		this.y = y;
	}
	void setX(int x)
	{
		this.x = x;
	}
	void set(Point p)
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
	boolean semiEquals(Object o)
	{
		return ((Point)o).x == this.x ||  ((Point)o).y == this.y;
	}
	Point toMapCoord(int tileSize)
	{
		return new Point((x * tileSize) + tileSize/2, (y * tileSize) + tileSize/2);
	}
	Point toTileCoord(int tileSize)
	{
		return new Point((x / tileSize) , (y / tileSize) );
	}
}
