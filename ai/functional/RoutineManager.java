package com.mygdx.ai.functional;

import com.mygdx.ai.leaf.RiflemanRoutineable;
import com.mygdx.ai.leaf.RoutineFactory;
import com.mygdx.debug.Debugger;

public class RoutineManager
{
	/*
	 * Ai routines keep running until stopped by an outside action
	 */
	private Routineable routine;
	private boolean routineActive;
	
	
	private int times = 0;

	public RoutineManager()
	{
		routineActive = false;
	}
	
	//retest selector and Routine, which has the RoutineIsComplete replaced
	//with succeeded. Test to make sure that no extraneous startRoutine is called
	
	
	
	
	
	
	public void update(float dt) 
	{
		if(routineActive && times < 1)
		{
			System.out.println("updating");
			if(routine.routineSucceeded())
			{
				Debugger.tick("Routine succeeded");
				routine.completeRoutine();
				//routine.startRoutine();
				times ++;
			}
			else if(routine.routineFailed())
			{
				Debugger.tick("Routine failed");
				routine.cancelRoutine();
				//routine.startRoutine();
				times ++;
			}
			else
			{
				routine.updateRoutine(dt);
			}
		}
	}
	
	public void stopRoutine()
	{
		if(routineActive)
		{
			routine.cancelRoutine();
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
		System.out.println("go to RoutineManager to implemen this");
		routine.startRoutine();
	}
}