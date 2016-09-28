package com.mygdx.ai.leaf;

import com.mygdx.ai.functional.Routineable;
/**
 * @Succeeds once the firing sequence is complete
 * @Instafails if the actor has no ammo
 */
class Shoot implements Routineable// once initialized, the humanoid will only shoot at x,y when prompted
{
	// the actor will fire on x, y, failing before it starts if it doesn't have any ammo to begin with
	private double x;
	private double y;
	private double z;
	private final Shootable actor;
	
	// used for the sequencialable implementation
	private boolean end;
	
	public Shoot(Shootable s) 
	{
		actor = s;
		end = false;
	}
	void designateTarget(double x,double y,double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void startRoutine(){
		actor.beginShoot(x,y,z);
	}
	
	@Override
	public void updateRoutine(float dt) {

	}
	
	@Override
	public void completeRoutine() {
		actor.completeShoot();
	}
	
	@Override
	public void cancelRoutine() {
		actor.failShoot();
	}
	
	@Override
	public boolean succeededRoutine() {
		return actor.finishedShooting();
	}
	
	@Override
	public boolean failedRoutine() {
		return false;
	}
	
	@Override
	public boolean instaSucceededRoutine() {
		return false;
	}
	
	@Override
	public boolean instaFailedRoutine() {
		return !actor.hasAmmo();
	}
	
	interface Shootable{
		public void beginShoot(double x,double y,double z);// it is up to the implementing class on how long to shoot
		public boolean hasAmmo();// fail if said actor does not have any ammo
		public boolean finishedShooting(); // once the shooting and the animation and whatever "cooldown" effect have finished
		public void completeShoot();
		public void failShoot();
	}
}