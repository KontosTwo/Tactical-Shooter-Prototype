package com.mygdx.entity;

import com.badlogic.gdx.math.Vector2;

public abstract class Ethereal extends Entity
{
	private Vector2 position;
	
	public Ethereal(int x,int y)
	{
		position = new Vector2(x,y);
	}
}
