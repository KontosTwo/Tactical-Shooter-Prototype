package com.mygdx.control;

import com.mygdx.camera.CameraHoggable;
import com.mygdx.physics.PrecisePoint;

public interface PlayerControllable extends CameraHoggable
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
		
	public PrecisePoint provideCenterForDebugger();

	// debugging purposes
}
