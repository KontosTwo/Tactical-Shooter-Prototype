package com.mygdx.entity;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.misc.Differentable;

public class HitMarker extends Visible
{
	private Differentable <Humanoid> shooter;
	
	public HitMarker(Vector2 position,Differentable shooter)
	{
		super(position);
		this.shooter = shooter;
		updateAnimation("hitmarker");
		setAnimationSize(10,10);
	}
	public Vector2 getPosition()
	{
		return new Vector2(center);
	}
	
	public void update(float dt)
	{
		super.update(dt);
		if(animationComplete())
		{
			delete();
		}
	}
	
	public boolean recognizeFriendlyFire(Humanoid perciever)
	{
		return shooter.sameAs(perciever);
	}

}
