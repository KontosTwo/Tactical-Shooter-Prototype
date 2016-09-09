package com.mygdx.graphic;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.physics.MovableBox;
import com.mygdx.physics.PrecisePoint;
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
	private static CameraBoundaryProvider cameraBoundaryProvider;
	private final MovableBox animationDimensions;
	private Animation animation;
	
	private static final int defaultWidth = 100;
	private static final int defaultHeight = 100;
	
	public static void setBoundaries(CameraBoundaryProvider cbp)
	{
		cameraBoundaryProvider = cbp;
	}
	
	public Animator(PrecisePoint center)
	{
		animationDimensions = new MovableBox(defaultWidth,defaultHeight,center);
		spritebatch = BatchCoordinator.getNightShader();
	}
	
	public void update(float dt)
	{
		animation.update(dt);
	}
	public void render()
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
	public boolean animationIsComplete()
	{
		return animation.complete();
	}
	public void updateAnimation(String filePath,String dataPath)
	{
		animation = new Animation(filePath,dataPath);
	}
	public void doodadify()
	{
		animationDimensions.centerToBottomCenter();
	}
	public void setOffset(int x,int y)
	{
		animationDimensions.setOffset(x, y);
	}
	public void setDimensions(int x,int y)
	{
		animationDimensions.setSize(x, y);
	}
	public boolean equals(Object o)
	{
		Animator other = (Animator)o;
		if (this == other) return true;
	    if (!(other instanceof Animator)) return false;
	    
	    return
	      this.animation.equals(other.animation);
	}
	
}
