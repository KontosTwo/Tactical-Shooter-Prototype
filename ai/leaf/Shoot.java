package com.mygdx.ai.leaf;

import com.mygdx.ai.functional.RoutineSequencialable;
import com.mygdx.script.Sequencialable;

class Shoot implements RoutineSequencialable// once initialized, the humanoid will only shoot at x,y when prompted
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
	public void startSequence()
	{
		actor.beginShoot(x,y,z);
	}
	@Override
	public void update(float dt) 
	{

	}
	@Override
	public boolean sequenceIsComplete() 
	{
		return actor.finishedShooting();
	}
	@Override
	public void completeSequence() 
	{
		actor.completeShoot();
	}
	@Override
	public void cancelSequence() 
	{
		actor.failShoot();
	}
	@Override
	public boolean succeeded() 
	{
		return actor.finishedShooting();
	}
	@Override
	public boolean failed() 
	{
		return false;
	}
	interface Shootable
	{
		public void beginShoot(double x,double y,double z);// it is up to the implementing class on how long to shoot
		public boolean hasAmmo();// fail if said actor does not have any ammo
		public boolean finishedShooting(); // once the shooting and the animation and whatever "cooldown" effect have finished
		public void completeShoot();
		public void failShoot();
	}
	@Override
	public boolean instaSucceeded() 
	{
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean instaFailed() 
	{
		// TODO Auto-generated method stub
		return !actor.hasAmmo();
	}
}