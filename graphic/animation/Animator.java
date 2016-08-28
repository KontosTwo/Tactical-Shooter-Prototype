package com.mygdx.graphic.animation;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.graphic.animation.Animation.AnimationTicker;
import com.mygdx.misc.Box;

public class Animator 
{
	public interface CameraBoundaryProvider
	{
		public float getLeftBoundary();
		public float getRightBoundary();
		public float getTopBoundary();
		public float getBottomBoundary();
	}
	
	private Animation animation;
	private SpriteBatch spritebatch;
	private CameraBoundaryProvider cameraBoundaryProvider;
	private Box animationDimensions;
	private AnimationTicker animationTicker;
	private boolean animationActive;
	
	
	private static HashMap<String,Animation> existingAnimation; 
	
	static
	{
		existingAnimation = new HashMap<>();
	}
	
	public Animator(CameraBoundaryProvider cbp)
	{
		cameraBoundaryProvider = cbp;
	}
	
	public static void clearExistingAnimation()
	{
		existingAnimation.clear();
	}
	public static Animator dayAnimator()
	{
		return new Animator(BatchRepo.createDayShader());
	}
	public static Animator nightAnimator()
	{
		return new Animator(BatchRepo.createNightShader());
	}
	public static Animator transparentAnimator()
	{
		return new Animator(BatchRepo.createTransparentShader());
	}
	
	private Animator(SpriteBatch sb)
	{
		
	}
	
	public void render(float x,float y)
	{
		if(animationDimensions.getLeft() < 
			cameraBoundaryProvider.getRightBoundary() &&
			
			animationDimensions.getRight() > 
			cameraBoundaryProvider.getLeftBoundary() &&
			
			animationDimensions.getTop() > 
			cameraBoundaryProvider.getBottomBoundary() &&
			
			animationDimensions.getBot() < 
			cameraBoundaryProvider.getTopBoundary())
		{
			
		}
	}
	
	public void updateAnimationDoodad(String name)
	{
		/*
		 * build the file locaiton here specific to
		 * Doodad
		 */
		loadAnimation(name);
	}
	public void updateAnimationSoldier(String name)
	{
		/*
		 * 
		 */
		loadAnimation(name);
	}
/*	public void updateAnimation(String name)
	{
		For soldiers
	}*/
	
	private void loadAnimation(String filePath)
	{
		// obtaining an animation for this animator.
		// if the animation already exists, it is extracted
		// if not, a new animation is created
		if(existingAnimation.containsKey(filePath))
		{
			animation = existingAnimation.get(filePath);
		}
		else
		{
			animation = createAnimation(filePath);
		}
		
	}
	private Animation createAnimation(String filePath)
	{
		return null;
	}
}
