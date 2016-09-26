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
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean routineSucceeded() {
		return routine.routineSucceeded() || routine.routineFailed();
	}

	@Override
	public boolean routineFailed() {
		return false;
	}

	@Override
	public boolean routineInstaSucceeded() {
		return false;
	}

	@Override
	public boolean routineInstaFailed() {
		return false;
	}
}
