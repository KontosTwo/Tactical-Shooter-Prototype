package com.mygdx.graphic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BatchRepo 
{
	private static SpriteBatch nightBatch;
	private static SpriteBatch dayBatch;
	
	public static void createBatch()
	{
		if(nightBatch == null)
		{
			nightBatch = new SpriteBatch();
			nightBatch.setColor(.5f,.5f,.5f, 1);
		}
		if(dayBatch == null)
		{
			dayBatch = new SpriteBatch();
		}
	}	
	static SpriteBatch createNightShader()
	{
		return nightBatch;
	}
	static SpriteBatch createDayShader()
	{
		return dayBatch;
	}
	static SpriteBatch createTransparentShader()
	{
		return dayBatch;
	}
}
