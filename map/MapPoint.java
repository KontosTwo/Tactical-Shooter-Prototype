package com.mygdx.map;

final class MapPoint 
{

	private int x;
	private int y;
	
	MapPoint(int x,int y)
	{
		this.x = x;
		this.y = y;
	}
	MapPoint()
	{
		this(0,0);
	}
	MapPoint(MapPoint p)
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
	void set(int x,int y){
		setX(x);
		setY(y);
	}
	void set(MapPoint p)
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
		return ((MapPoint)o).x == this.x &&  ((MapPoint)o).y == this.y;
	}
	MapPoint toMapCoord(int tileSize)
	{
		return new MapPoint((x * tileSize) + tileSize/2, (y * tileSize) + tileSize/2);
	}
	MapPoint toTileCoord(int tileSize)
	{
		return new MapPoint((x / tileSize) , (y / tileSize) );
	}
}
