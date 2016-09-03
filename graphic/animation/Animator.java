package com.mygdx.graphic.animation;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.misc.MovableBox;
/**
 * Controls the rendering and characteristics of an animation
 *
 */
public final class Animator 
{
	public interface CameraBoundaryProvider
	{
		public float getLeftBoundary();
		public float getRightBoundary();
		public float getTopBoundary();
		public float getBottomBoundary();
	}
	
	private SpriteBatch spritebatch;
	private CameraBoundaryProvider cameraBoundaryProvider;
	private final MovableBox animationDimensions;
	private Animation animation;
	private boolean animationActive;
	
	public Animator(CameraBoundaryProvider cbp,PointTracker pointTracker)
	{
		cameraBoundaryProvider = cbp;
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
			animation.render(spritebatch, 
					animationDimensions.getLeft(),
					animationDimensions.getBot(),
					animationDimensions.getWidth(),
					animationDimensions.getHeight());
		}
	}
	
	public void updateAnimation(String filePath)
	{
		animation = new Animation(filePath);
	}
}
