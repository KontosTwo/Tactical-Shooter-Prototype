package com.mygdx.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Camera extends OrthographicCamera
{
	private CameraHoggable attentionWhore;
	private Vector2 lead;
	private State state;
	private int speed;
	private Vector2 velocityUnit;
	private Vector2 waypoint;
	
	public Camera()
	{
		super();
		state = State.IDLE;
		velocityUnit = new Vector2(0,0);
		waypoint = new Vector2();
		lead = new Vector2();
	}
	public Camera(int x,int y)
	{
		super(x,y);
	}
	public void update()
	{
		super.update();
		switch(state)
		{
			case IDLE:
					break;
			case FOCUSED:
					Vector2 focusPoint = attentionWhore.provideCenterCamera();
					
					
					//unproject(lead);
					this.position.x = (focusPoint.x + lead.x)/2;		
					this.position.y= (focusPoint.y + lead.y)/2;
					break;
			case TRANSLATE:
					if(stillPan())
					{
						position.add(velocityUnit.x * speed,velocityUnit.y * speed,0);
					}
					else
					{
						state = State.IDLE;
						position.set(waypoint.x,waypoint.y,0);
					}
					break;
		}
		
		
	}
	public void focus(CameraHoggable ch)
	{
		attentionWhore = ch;
		state = State.FOCUSED;
	}
	public void focus2(Vector2 lead)
	{
		this.lead = lead;
	}
	public void unfocus()
	{
		state = State.IDLE;
	}
	public void snapTo(int x,int y)
	{
		position.set(x,y,0);
	}
	public void panTo(int x,int y,int speed)
	{
		state = State.TRANSLATE;
		this.speed = speed;
		waypoint.set(x,y);
		float angle = (float) Math.atan2((y - position.y),(x - position.x));
		velocityUnit.set((float)Math.cos(angle),(float)Math.sin(angle));	
	}

	private enum State
	{
		FOCUSED,
		TRANSLATE,
		IDLE
		
		;
	}
	private boolean stillPan()
	{
		double futurex = position.x + (velocityUnit.x*speed);
		double futurey = position.y + (velocityUnit.y*speed);
		
		return (sameSign(waypoint.x - futurex,waypoint.x - position.x) && sameSign(waypoint.y - futurey,waypoint.y - position.y));
	}
	private boolean sameSign(double x1,double x2)// potential problem if either is 0
	{
		return x1*x2 > 0;
	}
}
