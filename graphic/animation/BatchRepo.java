package com.mygdx.graphic.animation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public final class BatchRepo 
{
	private static SpriteBatch nightBatch;
	private static SpriteBatch dayBatch;
	
	public static void createBatch()
	{
		if(nightBatch == null)
		{
			nightBatch = new SpriteBatch();
			nightBatch.setColor(.2f,.2f,.6f, 1);
		}
		if(dayBatch == null)
		{
			dayBatch = new SpriteBatch();
		}
	}	
	public static SpriteBatch createNightShader()
	{
		return nightBatch;
	}
	static SpriteBatch getDayShader()
	{
		return dayBatch;
	}
	static SpriteBatch getTransparentShader()
	{
		return dayBatch;
	}
}
