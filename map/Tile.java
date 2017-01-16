package com.mygdx.map;

final class Tile
{
	private int heightPhy;
	private int heightVis;
	private boolean walkable;
	private boolean ramp;
	
	Tile(int pHeight,int vHeight,boolean w,boolean r){
		heightPhy = pHeight;
		heightVis = vHeight;
		walkable = w;
		ramp = r;
	}
	
	boolean isRamp()
	{
		return ramp;
	}
	boolean walkableTo(Tile t)
	{
		boolean ret = false;
		if(ramp||t.ramp || (sameHeightPAs(t) && t.walkable))
		{
			ret = true;
		}
		return ret;
	}
	

	boolean walkable()
	{
		return walkable;
	}
	int getHeightPhy()
	{
		return heightPhy ;
	}
	int getHeightVis()
	{
		return heightVis ;
	}
	boolean sameHeightPAs(Tile t)
	{
		return t.heightPhy == this.heightPhy ;
	}
	boolean lowerHeightThan(Tile t)
	{
		return t.heightPhy > this.heightPhy;
	}
	public String toString()
	{
		return ""  ;
	}
	boolean visualPeekAvailable()
	{
		return heightVis > heightPhy;
	}

}
