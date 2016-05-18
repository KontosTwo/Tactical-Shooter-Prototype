package com.mygdx.entity;


import com.mygdx.graphic.Animation;

public abstract class Entity 
{
	protected static EntityListener entityListener;
	private boolean removable;
	private int cycle;
	

	
	public Entity()
	{
		removable = false;
	}
	public static void setEntityListener(EntityListener e)
	{
		entityListener = e;
	}
	
	public void update(float dt)
	{
		
	}
	protected void delete()
	{
		removable = true;
	}
	public boolean removable()
	{
		return removable;
	}

	public void cycle(int cycle)
	{
		this.cycle = cycle;
	}
	public boolean cycleMod(int cycle)
	{
		return this.cycle % cycle == 0;
	}
}
