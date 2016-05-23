package com.mygdx.ai;

import com.mygdx.debug.Debugger;
import com.mygdx.misc.Updatable;

public class RoutineManager implements Updatable
{
	/*
	 * Ai routines keep running until stopped by an outside action
	 */
	private RoutineSequencialable routine;
	private boolean routineActive;
	
	
	private int times = 0;

	public RoutineManager()
	{
		routineActive = false;
	}
	
	@Override
	public void update(float dt) 
	{
		if(routineActive && times < 1)
		{
			System.out.println("updating");
			if(routine.succeeded())
			{
				Debugger.tick("Routine succeeded");
				routine.completeSequence();
				routine.startSequence();
				times ++;
			}
			else if(routine.failed())
			{
				Debugger.tick("Routine failed");
				routine.cancelSequence();
				routine.startSequence();
				times ++;
			}
			else
			{
				routine.update(dt);
			}
		}
	}
	
	public void stopRoutine()
	{
		if(routineActive)
		{
			routine.cancelSequence();
			routineActive = false;
		}
	}
	public void pauseRoutine(boolean b)
	{
		routineActive = b;
	}
	public void startRiflemanRoutine(RiflemanRoutineable r)
	{
		routineActive = true;
		routine = RoutineFactory.createRifleManRoutine(r);
		routine.startSequence();
	}
}