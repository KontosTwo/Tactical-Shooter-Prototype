package com.mygdx.ai.functional;

final class AlwaysFail implements Routineable
{
	private Routineable routine;
	
	AlwaysFail(Routineable r){
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
		return false;
	}

	@Override
	public boolean failedRoutine() {
		return routine.succeededRoutine() || routine.failedRoutine();
	}

	@Override
	public boolean instaSucceededRoutine() {
		return false;
	}

	@Override
	public boolean instaFailedRoutine() {
		return routine.instaFailedRoutine() || routine.instaSucceededRoutine();
	}

	@Override
	public void calculateInstaHeuristic() {
		
	}
}
