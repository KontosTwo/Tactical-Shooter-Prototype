package com.mygdx.debug;

import com.mygdx.graphic.Animator;
import com.mygdx.physics.PrecisePoint;

class Marker 
{
	private Animator animator;
	private PrecisePoint center;
	
	Marker(double x,double y)
	{
		center = new PrecisePoint(x,y);
		animator = new Animator(center,"full.png","animation/data/static.txt");
		animator.setDimensions(10, 10);
	}
	
	void render()
	{
		animator.render();
	}
	void update(float dt)
	{
		animator.update(dt);
	}
}
