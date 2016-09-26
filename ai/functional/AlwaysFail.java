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
	public boolean routineSucceeded() {
		return false;
	}

	@Override
	public boolean routineFailed() {
		return routine.routineSucceeded() || routine.routineFailed();
	}

	@Override
	public boolean routineInstaSucceeded() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean routineInstaFailed() {
		// TODO Auto-generated method stub
		return false;
	}
}
