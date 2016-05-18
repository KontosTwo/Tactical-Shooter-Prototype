package com.mygdx.entity;

import com.badlogic.gdx.math.Vector2;

public class BulletMark extends Visible
{
	private int timeLeft;
	private static final int existTime = 5;
	
	
	public BulletMark(int x,int y)
	{
		super(new Vector2(x,y));
		timeLeft = existTime;
	}
	
	public void update(float dt)
	{
		super.update(dt);
		timeLeft --;
	}
	public boolean removable()
	{
		return timeLeft < 0;
	}
}
