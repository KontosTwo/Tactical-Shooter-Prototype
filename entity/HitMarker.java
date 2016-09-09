package com.mygdx.entity;

import com.mygdx.entity.soldier.SoldierBattleConcrete;
import com.mygdx.misc.Differentable;
import com.mygdx.physics.PrecisePoint;

public class HitMarker extends Visible
{
	private Differentable <SoldierBattleConcrete> shooter;
	
	public HitMarker(PrecisePoint position,Differentable shooter)
	{
		super(position);
		this.shooter = shooter;
		updateAnimation("animation/environment/Hitmarker.png","animation/data/Hitmarker.txt");
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
	
	public boolean recognizeFriendlyFire(SoldierBattleConcrete perciever)
	{
		return shooter.sameAs(perciever);
	}

}
