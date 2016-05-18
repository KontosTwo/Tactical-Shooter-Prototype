package com.mygdx.graphic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.mygdx.misc.Box;

public class Animation 
{
	private TextureRegionDrawable[] frame;
	private float time;
	private float delay;// default delay seems to be 1/12f
	private int currentFrame;
	private int timesPlayed;
	private boolean looped;

	public Animation(TextureRegionDrawable[] frame,float delay) 
	{
		setAnimation(frame,delay);	
		looped = false;
	}
	public Animation(Array<TextureRegionDrawable> frame,float delay) 
	{
		setAnimation(frame,delay);	
		looped = false;
	}
	public Animation(Animation animation)
	{
		setAnimation(animation);
		looped = false;
	}
	public void setAnimation(TextureRegionDrawable[] frames,float delay)
	{
		this.frame = frames;
		this.delay = delay;
		time = 0;
		currentFrame = 0;
		timesPlayed = 0;
	}
	public void setAnimation(Array<TextureRegionDrawable> frames,float delay)
	{
		this.frame = frames.toArray();
		this.delay = delay;
		time = 0;
		currentFrame = 0;
		timesPlayed = 0;
	}
	public void reset()
	{
		currentFrame = 0;
	}
	private void setAnimation(Animation animation)
	{
		frame = new TextureRegionDrawable[animation.frame.length];
		for(int i = 0; i < frame.length; i ++)
		{
			frame[i] = new TextureRegionDrawable(animation.frame[i]);
		}
		this.delay = animation.delay;
	}
	
	public void update(float dt)
	{
		if(delay <=0)
		{
			return;
		}
		time +=dt;
		while(time >= delay)
		{
			step();
		}
	}
	private void step()
	{
		time -= delay;
		currentFrame++;
		if(looped = true)
		{
			looped = false;
		}
		if(currentFrame == frame.length)
		{
			currentFrame = 0;
			timesPlayed ++;
			looped = true;
		}
	}
	public boolean complete()
	{
		return looped;
	}
	public TextureRegionDrawable getFrame()
	{
		return frame[currentFrame];
	}
	public int getTimesPlayed()
	{
		return timesPlayed;
	}
	public boolean equals(Object o)
	{
		return frame.equals(((Animation)o).frame);
	}
	public void setToAnimationSize(Box animationBox)// must be the animation box
	{
		animationBox.setSize((int)frame[0].getMinWidth(),(int)frame[0].getMinHeight());
	}
}
