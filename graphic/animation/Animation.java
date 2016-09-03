package com.mygdx.graphic.animation;


import java.util.Collection;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.misc.MovableBox;

	
final class Animation
{
	private final AnimationData animationReference;
	private final short frameAmount;
	private short currentFrame;
	private int time;
	private final float tickerDelay;
	private boolean looped;
	private short timesPlayed;
	
	/*
	 * This is an object pool. Because TextureRegionDrawable
	 * uses native resources, an object pool is necessary to
	 * minimize RAM usage. 
	 */
	private final static HashMap<String,AnimationData> existingAnimationData; 

	
	Animation(String filePath)
	{
		AnimationData data = AnimationData.getAnimationData(filePath);
		animationReference = data;
		frameAmount = (short) data.frame.length;
		currentFrame = 0;
		tickerDelay = data.delay;
		looped = false;
		timesPlayed = 0;
		time = 0;
	}
	
	void update(float dt)
	{
		if(tickerDelay <=0)
		{
			return;
		}
		time +=dt;
		while(time >= tickerDelay)
		{
			step();
		}
	}
	
	private void step(){
		time -= tickerDelay;
		currentFrame++;
		if(looped = true){
			looped = false;
		}
		if(currentFrame == frameAmount){
			currentFrame = 0;
			timesPlayed ++;
			looped = true;
		}
	}
	
	void render(SpriteBatch sb,float bottomRightX,float bottomRightY,float width,float height)
	{
		animationReference.frame[currentFrame].draw(sb, bottomRightX, bottomRightY, width, height);
	}
	
	void reset(){
		currentFrame = 0;
	}
	
	boolean complete(){
		return looped;
	}

	
	static
	{
		existingAnimationData = new HashMap<>();
	}
	/**
	 * Call this whenever starting a new level
	 * in order to conserve resources
	 */
	public static void clearExistingAnimation()
	{
		Collection<AnimationData> animation = existingAnimationData.values();
		animation.forEach(a -> a.dispose());
		existingAnimationData.clear();
	}
	
	
	
	private static final class AnimationData 
	{
		private  TextureRegionDrawable[] frame;
		private  float delay;// default delay seems to be 1/12f

		private AnimationData(String filePath)
		{
			String dataFilePath = filePath.
			
			
			Texture texture = new Texture(Gdx.files.internal(filePath));
			TextureRegion[][] textureRegion = TextureRegion.split(texture,texture.getWidth()/width,texture.getHeight()/height); // must be fixed
			TextureRegionDrawable[] value = new TextureRegionDrawable[filePath*height];
			int counter = 0;
			for(TextureRegion[] t : textureRegion)
			{
				for(TextureRegion ti : t)
				{
					value[counter] = new TextureRegionDrawable(ti);
					counter++;
				}
			}
			
		}
		private void dispose()
		{
			for(int i = 0; i < frame.length; i ++)
			{
				frame[i].getRegion().getTexture().dispose();
			}
		}
		static AnimationData getAnimationData(String filePath)
		{
			return loadAnimationData(filePath);
		}
		private static AnimationData loadAnimationData(String filePath)
		{
			AnimationData animationData;
			// if the requested animation has already been created, load it
			if(existingAnimationData.containsKey(filePath))
			{
				animationData = existingAnimationData.get(filePath);
				System.out.println("animation already exists!");
			}
			// otherwise, create a new one
			else
			{
				animationData = createAnimationData(filePath);
			}
			return animationData;	
		}
		private static AnimationData createAnimationData(String filePath)
		{
			// upon creation, store in hashmap for later retrieval
			AnimationData animation = new AnimationData(filePath);
			existingAnimationData.put(filePath, animation);
			return animation;
		}
	}
}

