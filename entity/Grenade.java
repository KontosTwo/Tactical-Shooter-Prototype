package com.mygdx.entity;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.physics.PrecisePoint;

public class Grenade extends Hitboxable
{
	private Vector2 destination;
	private float speedY;
	private int movingTime;
	private int currentTime;
	private int tempGravity;
	
	private static final float gravity = -1f;
	private static final int throwSpeedX = 5;
	private static final int timer = 300;
	private static final int throwHeight = 60;
	
	public Grenade(PrecisePoint center,PrecisePoint destination) 
	{
		super(center);
		/*this.destination = new Vector2(destination);
		
		
		Vector2 xVelocity = new Vector2(destination.x - center.x,destination.y - center.y);
		float hyp = (float) Math.hypot(xVelocity.x,xVelocity.y);
		xVelocity.scl(1/hyp);
		setUnitVelocity(xVelocity.x,xVelocity.y);
		setSpeed(throwSpeedX);
		
		updateAnimation("SHREK");
		setAnimationSize(20,20);
		movingTime = (int) (hyp/Math.hypot(xVelocity.x*throwSpeedX, xVelocity.y*throwSpeedX));
		currentTime = 0;
		
		
		float timeApex = movingTime/2;
		int apex = (int) ((int) (throwHeight + (destination.y - center.y)/2 + center.y)/(timeApex));
		//speedY = (float) ((apex/timeApex) - ((.5) * gravity * timeApex) - (center.y/timeApex));
		//speedY = (4*throwHeight/movingTime) - (gravity*movingTime/8);
		//speedY = -gravity * movingTime / 2;
		speedY = ((2*throwHeight)/movingTime) - ((gravity * movingTime)/4);
		
		*/
		
		// TODO Auto-generated constructor stub
	}
	
	public void update(float dt)
	{
		super.update(dt);
		speedY += gravity;
		currentTime ++;
		if(currentTime >= movingTime)
		{
			stopVelocity();
		}
		else
		{
			center.y += speedY;
		}
	}

}
