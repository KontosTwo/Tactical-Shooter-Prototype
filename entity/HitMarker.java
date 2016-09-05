package com.mygdx.entity;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.entity.soldier.SoldierBattle;
import com.mygdx.misc.Differentable;
import com.mygdx.misc.PrecisePoint;

public class HitMarker extends Visible
{
	private Differentable <SoldierBattle> shooter;
	
	public HitMarker(PrecisePoint position,Differentable shooter)
	{
		super(position);
		this.shooter = shooter;
		updateAnimation("animation\\environment\\Hitmarker.png","animation\\data\\Hitmarker.txt");
		setAnimationSize(10,10);
	}
	public PrecisePoint getPosition()
	{
		return new PrecisePoint(center);
	}
	
	public void update(float dt)
	{
		super.update(dt);
		if(animationComplete())
		{
			delete();
		}
	}
	
	public boolean recognizeFriendlyFire(SoldierBattle perciever)
	{
		return shooter.sameAs(perciever);
	}

}
