package com.mygdx.ai.leaf;

import com.mygdx.ai.functional.RoutineSurvivalable;
import com.mygdx.ai.leaf.Reload.Reloadable;

public class CheckReload implements RoutineSurvivalable{

	private final CheckReloadable actor;
	private final Reload reload;
	
	CheckReload(CheckReloadable actor){
		this.actor = actor;
		reload = new Reload(actor);
	}
	
	@Override
	public void calculateInstaHeuristic() {	}

	@Override
	public boolean instaSucceededRoutine() {
		return !actor.needToReload();
	}

	@Override
	public boolean instaFailedRoutine() {
		return false;
	}

	@Override
	public boolean succeededRoutine() {
		return reload.succeededRoutine();
	}

	@Override
	public boolean failedRoutine() {
		return reload.failedRoutine();
	}

	@Override
	public void updateRoutine(float dt) {
		reload.updateRoutine(dt);
	}

	@Override
	public void startRoutine() {
		reload.startRoutine();
	}

	@Override
	public void completeRoutine() {
		reload.completeRoutine();
	}

	@Override
	public void cancelRoutine() {
		reload.cancelRoutine();
	}

	@Override
	public boolean conditionUpheld() {
		return actor.needToReload();
	}
	
	interface CheckReloadable extends Reloadable{
		public boolean needToReload();
	}
}
