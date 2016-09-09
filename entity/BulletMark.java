package com.mygdx.entity;

import com.mygdx.physics.PrecisePoint;

public class BulletMark extends Visible
{
	private int timeLeft;
	private static final int existTime = 5;
	
	
	public BulletMark(int x,int y)
	{
		super(new PrecisePoint(x,y));
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
