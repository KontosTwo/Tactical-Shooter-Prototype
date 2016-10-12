package com.mygdx.control;

import com.mygdx.camera.CameraHoggable;
import com.mygdx.physics.PrecisePoint;

public interface PlayerControllable extends CameraHoggable{
	public void cMoveRight(boolean b);
	public void cMoveLeft(boolean b);
	public void cMoveUp(boolean b);
	public void cMoveDown(boolean b);
	public void cShoot(PrecisePoint target);
	public void cReload();
	public void cStand();
	public void cCrouch();
	public void cLay();
	public void cGrenade(PrecisePoint target);
	public void cMouseMoveTo(PrecisePoint location);
}
