package com.mygdx.ai.functional;

public class Repeater implements RoutineSequencialable
{
	private final RoutineSequencialable routine;
	private int times;
	private int currentTimes;
	
	public Repeater(RoutineSequencialable rs)
	{
		routine = rs;
	}
	
	public void setTimes(int t)
	{
		times = t;
	}
	
	@Override
	public void startSequence() 
	{
		routine.startSequence();
		currentTimes = 0;
	}

	@Override
	public void update(float dt) 
	{
		/*
		 * repeater does not concern whether the routine has completed or not,
		 * it checks for failure
		 */
		if(routine.succeeded())
		{
			routine.completeSequence();
			routine.startSequence();
			currentTimes ++;
		}
		else if(routine.failed())
		{
			routine.cancelSequence();
			routine.startSequence();
			currentTimes ++;
		}
		else
		{
			routine.update(dt);
		}
	}

	@Override
	public boolean sequenceIsComplete() 
	{
		return currentTimes >= times;
	}

	@Override
	public void completeSequence() 
	{
		currentTimes = 0;
	}

	@Override
	public void cancelSequence() 
	{
		routine.cancelSequence();
	}

	@Override
	public boolean succeeded() 
	{
		return currentTimes >= times;
	}

	@Override
	public boolean failed() 
	{
		return false;
	}

	@Override
	public boolean instaSucceeded() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean instaFailed() {
		// TODO Auto-generated method stub
		return false;
	}
   
}
