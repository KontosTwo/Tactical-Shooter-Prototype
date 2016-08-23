package com.mygdx.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.misc.MyVector2;
import com.mygdx.misc.PrecisePoint;

public class Camera extends OrthographicCamera
{
	private CameraHoggable cameraHog;
	private PrecisePoint lead;
	private State state;
	private byte speed;
	private MyVector2 velocityUnit;
	private PrecisePoint waypoint;
	
	public Camera()
	{
		super();
		state = State.IDLE;
		velocityUnit = new MyVector2(0,0);
		waypoint = new PrecisePoint();
		lead = new PrecisePoint();
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
					MyVector2 focusPoint = cameraHog.provideCenterCamera();
					
					
					//unproject(lead);
					this.position.x = (focusPoint.getX() + lead.x)/2;		
					this.position.y= (focusPoint.getY() + lead.y)/2;
					break;
			case TRANSLATE:
					if(stillPan())
					{
						position.add(velocityUnit.getX() * speed,velocityUnit.getY() * speed,0);
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
		cameraHog = ch;
		state = State.FOCUSED;
	}
	public void focusOnLead(PrecisePoint lead)
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
	public void panTo(int x,int y,byte speed)
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
		double futurex = position.x + (velocityUnit.getX()*speed);
		double futurey = position.y + (velocityUnit.getY()*speed);
		
		return (sameSign(waypoint.x - futurex,waypoint.x - position.x)
				&& sameSign(waypoint.y - futurey,waypoint.y - position.y));
	}
	private boolean sameSign(double x1,double x2)// potential problem if either is 0
	{
		return x1*x2 > 0;
	}
}
