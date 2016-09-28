package com.mygdx.ai.functional;

import java.util.function.Supplier;

public class Conditional implements Routineable
{
	 private final Routineable success;
	 private final Routineable fail;
	 private Routineable routine;
	 private Supplier<Boolean> heuristic;
	 
	 public Conditional (Supplier<Boolean> heuristic, Routineable success,Routineable fail)
	 {
		 this.success = success;
		 this.fail = fail;
		 this.heuristic = heuristic;
	 }
	 
	@Override
	public void startRoutine() 
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
		routine.startRoutine();
	}
	@Override
	public void updateRoutine(float dt) 
	{
		routine.updateRoutine(dt);
	}

	@Override
	public void completeRoutine() 
	{
		routine.completeRoutine();
	}

	@Override
	public void cancelRoutine() 
	{
		routine.cancelRoutine();
	}

	@Override
	public boolean succeededRoutine()
	{
		return routine.succeededRoutine();
	}

	@Override
	public boolean failedRoutine() 
	{
		return routine.failedRoutine();
	}

	@Override
	public boolean instaSucceededRoutine() 
	{
		return heuristic.get() ? success.instaSucceededRoutine() : fail.instaSucceededRoutine();
	}

	@Override
	public boolean instaFailedRoutine() {
		return heuristic.get() ? success.instaFailedRoutine() : fail.instaFailedRoutine();
	}
}
