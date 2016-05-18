package com.mygdx.ai;

import java.util.function.Supplier;

 class Conditional implements RoutineSequencialable
{
	 private final RoutineSequencialable success;
	 private final RoutineSequencialable fail;
	 private RoutineSequencialable routine;
	 private Supplier<Boolean> heuristic;
	 
	 Conditional (Supplier<Boolean> heuristic, RoutineSequencialable success,RoutineSequencialable fail)
	 {
		 this.success = success;
		 this.fail = fail;
		 this.heuristic = heuristic;
	 }
	 
	@Override
	public void startSequence() 
	{
		if(heuristic.get())
		{
			routine = success;
		}
		else
		{
			routine = fail;
		}
		/*
		 * this is the essential line. The purpose of this
		 * class is to allow instantaneous checking if a routine
		 * instantly fails or not
		 * For routines that could previously fail before even starting, 
		 * separate the failing condition into a Supplier<Boolean>
		 * to allow instantaneous determination. 
		 */
		routine.startSequence();
	}
	@Override
	public void update(float dt) 
	{
		routine.update(dt);
	}

	@Override
	public boolean sequenceIsComplete() 
	{
		return routine.sequenceIsComplete();
	}

	@Override
	public void completeSequence() 
	{
		routine.completeSequence();
	}

	@Override
	public void cancelSequence() 
	{
		routine.cancelSequence();
	}

	@Override
	public boolean succeeded()
	{
		return routine.succeeded();
	}

	@Override
	public boolean failed() 
	{
		return routine.failed();
	}

	@Override
	public boolean instaSucceeded() 
	{
		return heuristic.get() ? success.instaSucceeded() : fail.instaSucceeded();
	}

	@Override
	public boolean instaFailed() {
		return heuristic.get() ? success.instaFailed() : fail.instaFailed();
	}



}
