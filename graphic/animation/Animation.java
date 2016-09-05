package com.mygdx.graphic.animation;


import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.mygdx.misc.MovableBox;

	
public final class Animation
{
	private final AnimationData animationReference;
	private final short frameAmount;
	private short currentFrame;
	private int time;
	private final float tickerDelay;
	private boolean looped;
	private short timesPlayed;
	
	Animation(String filePath,String dataPath)
	{
		AnimationData data = AnimationData.getAnimationData(filePath,dataPath);
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
	public boolean equals(Object o)
	{
		Animation other = (Animation)o;
		if (this == other) return true;
	    if (!(other instanceof Animation)) return false;
	    
	    return
	      this.animationReference.equals(other.animationReference);
	}

	
	public static void clearExistingAnimation(){
		AnimationData.clearExistingAnimation();
	}
	
	
	
	private static final class AnimationData 
	{
		private final TextureRegionDrawable[] frame;
		private final float delay;// default delay seems to be 1/12f
		
		/*
		 * This is an object pool. Because TextureRegionDrawable
		 * uses native resources, an object pool is necessary to
		 * minimize RAM usage. 
		 */
		private final static HashMap<String,AnimationData> existingAnimationData; 
		
		static
		{
			existingAnimationData = new HashMap<>();
		}
		/**
		 * Call this whenever starting a new level
		 * in order to conserve resources
		 */
		private static void clearExistingAnimation()
		{
			Collection<AnimationData> animation = existingAnimationData.values();
			animation.forEach(a -> a.dispose());
			existingAnimationData.clear();
		}
		
		private AnimationData(String filePath,String dataPath)
		{
			Texture texture = new Texture(Gdx.files.internal(filePath));
			Scanner scanner = null;
			
			try {
				scanner = new Scanner(Gdx.files.internal(dataPath).file());
			} catch (FileNotFoundException e) {
				System.err.print("The data file for the animation located at " + "\"" + filePath + "\" was not found");
			}
			
			delay = scanner.nextFloat();
			int width = scanner.nextInt();
			int height = scanner.nextInt();
					
			TextureRegion[][] textureRegion = TextureRegion.split(texture,texture.getWidth()/width,texture.getHeight()/height); // must be fixed
			TextureRegionDrawable[] textureRegionDrawable = new TextureRegionDrawable[width*height];
			int counter = 0;
			for(TextureRegion[] t : textureRegion)
			{
				for(TextureRegion ti : t)
				{
					textureRegionDrawable[counter] = new TextureRegionDrawable(ti);
					counter++;
				}
			}
			frame = textureRegionDrawable;
		}
		
		private void dispose()
		{
			for(int i = 0; i < frame.length; i ++)
			{
				frame[i].getRegion().getTexture().dispose();
			}
		}
		
		private static AnimationData getAnimationData(String filePath,String dataPath)
		{
			return loadAnimationData(filePath,dataPath);
		}
		private static AnimationData loadAnimationData(String filePath,String dataPath)
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
				animationData = createAnimationData(filePath,dataPath);
			}
			return animationData;	
		}
		private static AnimationData createAnimationData(String filePath,String dataPath)
		{
			// upon creation, store in hashmap for later retrieval
			AnimationData animation = new AnimationData(filePath,dataPath);
			existingAnimationData.put(filePath, animation);
			return animation;
		}
		
		public boolean equals(Object o)
		{
			AnimationData other = (AnimationData)o;
			if (this == other) return true;
		    if (!(other instanceof AnimationData)) return false;
		    
		    return
		      Arrays.equals(this.frame, other.frame);
		}
	}
}

