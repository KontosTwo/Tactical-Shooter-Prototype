package com.mygdx.entity;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.misc.Box;

public abstract class Hurtboxable extends Hitboxable
{
	private Box hurtbox;
	private float height;

	public Hurtboxable(Vector2 center) 
	{
		super(center);
		hurtbox = new Box(100,100,this.center);
	}
	protected void setSizeAll(int x,int y)
	{
		super.setSizeAll(x, y);
		hurtbox.setSize(x,y);
	}
	public void update(float dt)
	{
		super.update(dt);
	}
	public boolean hurtboxContains(float x,float y)
	{
		return hurtbox.contains(x, y);
	}
	protected void setHurtBoxSize(float x,float y)
	{
		hurtbox.setSize((int)x, (int)y);
	}
	public boolean hurtboxOverlaps(float x1,float y1,float x2,float y2)
	{
		return hurtbox.overLaps(x1, y1, x2, y2);
	}
	public boolean crossRightHurtBox(float x,float y)
	{
		return (int)x == (int)hurtbox.getRight() && (int)y < (int)hurtbox.getTop() && (int)y > (int)hurtbox.getBot();
	}
	public boolean crossLeftHurtBox(float x,float y)
	{
		return (int)x == (int)hurtbox.getLeft() && (int)y < (int)hurtbox.getTop() && (int)y > (int)hurtbox.getBot();
	}
	public boolean crossTopHurtBox(float x,float y)
	{
		return (int)y == (int)hurtbox.getTop() && (int)x < (int)hurtbox.getRight() && (int)x > (int)hurtbox.getLeft();
	}
	public boolean crossBotHurtBox(float x,float y)
	{
		return (int)y == (int)hurtbox.getBot() && (int)x < (int)hurtbox.getRight() && (int)x > (int)hurtbox.getLeft();
	}
	public boolean intersectAgainHurtBox(int x1,int y1,int x2,int y2)
	{
		return hurtbox.intersectAgain(x1, y1, x2, y2);
	}
	public float getHeight()
	{
		return height;
	}
	public void setHeight(float height)
	{
		this.height = height;
	}
}
