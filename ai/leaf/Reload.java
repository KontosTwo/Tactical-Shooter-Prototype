package com.mygdx.ai.leaf;

import com.mygdx.ai.functional.Routineable;

public class Reload implements Routineable{
	
	private final Reloadable actor;
	
	Reload(Reloadable actor){
		this.actor = actor;
	}
	
	@Override
	public void calculateInstaHeuristic() {	}

	@Override
	public boolean instaSucceededRoutine() {
		return actor.doesNotNeedToReload();
	}

	@Override
	public boolean instaFailedRoutine() {
		return false;
	}

	@Override
	public boolean succeededRoutine() {
		return actor.finishedReload();
	}

	@Override
	public boolean failedRoutine() {
		return false;
	}

	@Override
	public void updateRoutine(float dt) {
		
	}

	@Override
	public void startRoutine() {
		actor.beginReload();
	}

	@Override
	public void completeRoutine() {
		actor.completeReload();
	}

	@Override
	public void cancelRoutine() {
		actor.cancelReload();
	}
	
	interface Reloadable{
		public void beginReload();
		public boolean finishedReload();
		public void completeReload();
		public void cancelReload();
		public boolean doesNotNeedToReload();
	}
}
