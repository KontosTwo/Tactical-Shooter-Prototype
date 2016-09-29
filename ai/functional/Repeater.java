package com.mygdx.ai.functional;

/**
 * Executes a routine a specified number of times. If the executed routine
 * ever fails, then, provided that Repeater can still run the routine, Repeater simply
 * attempts another execution of the routine. 
 * @succeeds if the current routine has been executed a certian number of times
 *
 */
final class Repeater implements Routineable
{
	private final Routineable routine;
	private int times;
	private int currentTimes;
	
	Repeater(Routineable rs)
	{
		routine = rs;
	}
	
	void setTimes(int t)
	{
		times = t;
	}
	
	@Override
	public void startRoutine() 
	{
		routine.startRoutine();
		currentTimes = 0;
	}

	@Override
	public void updateRoutine(float dt) 
	{
		/*
		 * repeater does not concern whether the routine has completed or not,
		 * it checks for failure
		 */
		if(routine.succeededRoutine())
		{
			routine.completeRoutine();
			routine.startRoutine();
			currentTimes ++;
		}
		else if(routine.failedRoutine())
		{
			routine.cancelRoutine();
			routine.startRoutine();
			currentTimes ++;
		}
		else
		{
			routine.updateRoutine(dt);
		}
	}

	@Override
	public void completeRoutine() 
	{
		currentTimes = 0;
		routine.completeRoutine();
	}

	@Override
	public void cancelRoutine() 
	{
		currentTimes = 0;
		routine.cancelRoutine();
	}

	@Override
	public boolean succeededRoutine() {
		return currentTimes >= times;
	}

	/**
	 * If the routine fails, then simply attempt again
	 */
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
		// TODO Auto-generated method stub
		
	}
   
}
