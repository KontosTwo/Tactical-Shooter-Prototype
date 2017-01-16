package com.mygdx.ai.leaf;

import com.mygdx.ai.functional.Routineable;
import com.mygdx.ai.leaf.Shoot.Shootable;
import com.mygdx.physics.PrecisePoint3;


final class ShootBurst implements Routineable{
	private final Repeater repeater;
	private final Shoot shoot;
	private final ShootBurstable actor;
	
	ShootBurst(ShootBurstable actor){
		Shoot shoot = new Shoot(actor);
		repeater = new Repeater(shoot);
		this.shoot = shoot;
		repeater.setTimes(1);
		this.actor = actor;
	}
	
	void designateTarget(PrecisePoint3 target){
		shoot.designateTarget(target);
	}
	
	void setTimes(int times){
		repeater.setTimes(times);
	}

	@Override
	public void calculateInstaHeuristic() {
		repeater.calculateInstaHeuristic();
	}

	@Override
	public boolean instaSucceededRoutine() {
		return repeater.instaSucceededRoutine();
	}

	@Override
	public boolean instaFailedRoutine() {
		return repeater.instaFailedRoutine() || !actor.hasAmmoForBurst();
	}

	@Override
	public boolean succeededRoutine() {
		return repeater.succeededRoutine();
	}

	@Override
	public boolean failedRoutine() {
		return repeater.failedRoutine();// || !actor.hasAmmoForBurst();
	}

	@Override
	public void updateRoutine(float dt) {
		repeater.updateRoutine(dt);
	}

	@Override
	public void startRoutine() {
		repeater.startRoutine();
	}

	@Override
	public void completeRoutine() {
		repeater.completeRoutine();
		actor.cancelBurst();
	}

	@Override
	public void cancelRoutine() {
		repeater.cancelRoutine();
		actor.cancelBurst();
	}
	
	interface ShootBurstable extends Shootable{
		public boolean hasAmmoForBurst();
		public void cancelBurst();
	}
}
