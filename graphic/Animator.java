package com.mygdx.graphic;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Animator 
{
	private static HashMap<String,Animation> existingAnimation; 
	private Animation animation;
	private SpriteBatch spritebatch;
	
	static
	{
		existingAnimation = new HashMap<>();
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
	
	public void render()
	{
		
	}
	
	public void updateAnimation(String name)
	{
		
	}
/*	public void updateAnimation(String name)
	{
		
	}*/
	
	private void loadAnimation(String filePath)
	{
		if(existingAnimation.containsKey(filePath))
		{
			
		}
	}
	private void createAnimation(String filePath)
	{
		
	}
}
