package com.mygdx.ai.leaf;

import java.util.ArrayList;

import com.mygdx.ai.functional.Routineable;
import com.mygdx.ai.functional.Sequence;
import com.mygdx.physics.PrecisePoint3;
/**
 * @Succeeds once the firing sequence is complete
 * @Instafails if the actor has no ammo
 */
class Shoot implements Routineable// once initialized, the humanoid will only shoot at x,y when prompted
{
	// the actor will fire on x, y, failing before it starts if it doesn't have any ammo to begin with
	private final PrecisePoint3 target;
	private final Shootable actor;
	
	// used for the sequencialable implementation
	private boolean end;
	
	public Shoot(Shootable s) 
	{
		actor = s;
		end = false;
		target = new PrecisePoint3();
	}
	void designateTarget(PrecisePoint3 target)
	{
		this.target.set(target);
	}

	@Override
	public void startRoutine(){
		actor.beginShoot(target);
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
		actor.cancelShoot();
	}
	
	@Override
	public boolean succeededRoutine() {
		return actor.finishedShooting();
	}
	
	@Override
	public boolean failedRoutine() {
		return !actor.hasAmmo();
	}

	@Override
	public void calculateInstaHeuristic() {
		
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
		public void beginShoot(PrecisePoint3 target);// it is up to the implementing class on how long to shoot
		public boolean hasAmmo();// fail if said actor does not have any ammo
		public boolean finishedShooting(); // once the shooting and the animation and whatever "cooldown" effect have finished
		public void completeShoot();
		public void cancelShoot();
	}
}