package com.mygdx.handler;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.camera.CameraHoggable;

public interface Controllable extends CameraHoggable
{
	public void cMoveRight(boolean b);
	public void cMoveLeft(boolean b);
	public void cMoveUp(boolean b);
	public void cMoveDown(boolean b);
	public void cShoot(float x,float y,float z);
	public void cReload();
	public void cFace(double x,double y);
	public void cStand();
	public void cCrouch();
	public void cLay();
	public void cGrenade(double x,double y);
	
	public void initiateControllable();// this is to allow the controller to execute whatever tasks needed
		
	public Vector2 provideCenter();

	// debugging purposes
}
