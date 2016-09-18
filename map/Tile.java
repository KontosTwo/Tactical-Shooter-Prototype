package com.mygdx.map;

import java.util.EnumSet;
import java.util.HashSet;

import com.badlogic.gdx.utils.Array;

class Tile
{
	private int heightPhy;
	private int heightVis;
	private int heightPhyObstacle;
	private int heightVisObstacle;
	private boolean walkable;
	private final static int groundLevel = 0;
	private boolean ramp;
	
	public Tile()
	{
		heightPhy = groundLevel;
		heightVis = groundLevel;
		heightPhyObstacle = 0;
		heightVisObstacle = 0;
		ramp = false;
		walkable = true;
	}
	
	public void setHeightPhy(int h)
	{
		heightPhy = h;
	}
	public void setObstacleHeightPhy(int h)
	{
		heightPhyObstacle = h;
	}
	public void setHeightVis(int h)
	{
		heightVis = h;
	}
	public void setObstacleHeightVis(int h)
	{
		heightVisObstacle = h;
	}
	public void setInfo(int heightP,int heightV,boolean w,boolean r)
	{
		heightPhy = heightP;
		heightVis = heightV;
		ramp = r;
		walkable = w;
	}
	public boolean isRamp()
	{
		return ramp;
	}
	public boolean walkableTo(Tile t)
	{
		boolean ret = false;
		if(ramp||t.ramp || (sameHeightPAs(t) && t.walkable))
		{
			ret = true;
		}
		return ret;
	}
	public void toggleWalkable(boolean b)
	{
		walkable = b;
	}
	public void toggleRamp(boolean b)
	{
		ramp = b;
	}

	public boolean walkable()
	{
		return walkable;
	}
	public float getHeightPhy()
	{
		return heightPhy + heightPhyObstacle;
	}
	public float getHeightVis()
	{
		return heightVis + heightVisObstacle;
	}
	public boolean sameHeightPAs(Tile t)// these may not
	{
		return t.heightPhy + t.heightPhyObstacle== this.heightPhy + this.heightPhyObstacle;
	}
	public boolean lowerHeightThan(Tile t)
	{
		return t.heightPhy + t.heightPhyObstacle > this.heightPhy + this.heightPhyObstacle;
	}
	public String toString()
	{
		return "" + heightPhyObstacle ;//+ " " + heightVis + " " + walkable + " " + ramp;
	}
	public boolean visualPeekAvailable()
	{
		return heightVis > heightPhy;
	}
}
