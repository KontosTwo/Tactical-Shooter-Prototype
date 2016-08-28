package com.mygdx.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.misc.PrecisePoint;

public abstract class Button extends Tangible implements Clickable
{

	public Button( PrecisePoint center,String animation) 
	{
		super(center);
		updateAnimation(animation);
		setSizeAll(100,100);
	}

	@Override
	public boolean getClicked(int x,int y) 
	{
		return animationBoxContains(x,y);
	}

	@Override
	public abstract void action();
	
	public void update(float dt)
	{
		super.update(dt);
	}
}
