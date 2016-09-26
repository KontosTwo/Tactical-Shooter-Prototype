package com.mygdx.ai.functional;

public class Repeater implements Routineable
{
	private final Routineable routine;
	private int times;
	private int currentTimes;
	
	public Repeater(Routineable rs)
	{
		routine = rs;
	}
	
	public void setTimes(int t)
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
		if(routine.routineSucceeded())
		{
			routine.completeRoutine();
			routine.startRoutine();
			currentTimes ++;
		}
		else if(routine.routineFailed())
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
	}

	@Override
	public void cancelRoutine() 
	{
		routine.cancelRoutine();
	}

	@Override
	public boolean routineSucceeded() 
	{
		return currentTimes >= times;
	}

	@Override
	public boolean routineFailed() 
	{
		return false;
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
