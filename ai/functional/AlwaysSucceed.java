package com.mygdx.ai.functional;


final class AlwaysSucceed implements Routineable
{
	private Routineable routine;
	
	AlwaysSucceed(Routineable r){
		routine = r;
	}
	
	@Override
	public void startRoutine() {
		routine.startRoutine();
	}

	@Override
	public void updateRoutine(float dt) {
		routine.updateRoutine(dt);
	}

	@Override
	public void completeRoutine() {
		routine.completeRoutine();
	}

	@Override
	public void cancelRoutine() {
		routine.cancelRoutine();
	}

	@Override
	public boolean succeededRoutine() {
		return routine.succeededRoutine() || routine.failedRoutine();
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
		return false;
	}

	@Override
	public void calculateInstaHeuristic() {
		
	}
}
