package com.mygdx.ai.functional;

/**
 * Executes a routine regardless of whether it instaSucceeds
 * or instaFails
 */
class AlwaysExecute implements Routineable{

	private Routineable routine;
	
	AlwaysExecute(Routineable routine){
		this.routine = routine;
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
		return routine.succeededRoutine();
	}

	@Override
	public boolean failedRoutine() {
		return routine.failedRoutine();
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
