package com.mygdx.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.misc.PrecisePoint;

public abstract class Tangible extends Hitboxable
{	
	public Tangible(PrecisePoint center) 
	{
		super(center);
	}	
	
	public void update(float dt)
	{
		super.update(dt);
		
	}
}
