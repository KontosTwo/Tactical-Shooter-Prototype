package com.mygdx.ai;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.mygdx.debug.Debugger;

public class Sequence implements RoutineSequencialable
{
    private RoutineSequencialable currentRoutine;
    private final List<RoutineSequencialable> routine;
    private Queue<RoutineSequencialable> routineQueue;
    
    /*
     * due to the generic nature of this class, it can find use
     * in other areas such as cutscenes and game scripts
     */
    
	Sequence(List<? extends RoutineSequencialable> sequence) 
    {
        this.currentRoutine = null;
        routine = new LinkedList<RoutineSequencialable>(sequence);
        routineQueue = new LinkedList<RoutineSequencialable>();
    }
	static Sequence buildSequence(RoutineSequencialable... rs)
	{
		LinkedList <RoutineSequencialable> routineList = new LinkedList<RoutineSequencialable>();
		for(int i = 0; i < rs.length; i ++)
		{
			routineList.add(rs[i]);
		}
		return new Sequence(routineList);
	}
	
	@Override
	public void startSequence() 
	{
		Debugger.tick("Sequence is starting");

		routineQueue.clear();
        routineQueue.addAll(routine);
        /*
         * transverse here. Do not worry about
         * peek() returning null, start sequence will only
         * run if the routine has not instasucceeded, 
         * meaning that routine will contain >1 runnable
         * routines
         */
        	currentRoutine = routineQueue.peek();
        	currentRoutine.startSequence();
        
	}

	@Override
	public void update(float dt) 
	{
		if(currentRoutine.sequenceIsComplete())
		{
			currentRoutine.completeSequence();
			routineQueue.poll();
			if(!routineQueue.isEmpty())	
			{
				currentRoutine = routineQueue.peek();
				currentRoutine.startSequence();
			}
		}
		else
		{
			currentRoutine.update( dt);
		}
	}

	@Override
	public boolean sequenceIsComplete() 
	{
		return routineQueue.isEmpty();
	}

	@Override
	public void completeSequence() 
	{
		Debugger.tick("Sequence is complete");
	}

	@Override
	public void cancelSequence() 
	{
		currentRoutine.cancelSequence();
	}

	@Override
	public boolean succeeded() 
	{
		/*
		 * rewrite a way to check if succeeded
		 */
		return routineQueue.isEmpty();
	}

	@Override
	public boolean failed() 
	{
		return currentRoutine.failed();
	}
	
	private void transverseSuccess()
	{
		/*
		 * Sifts through all routines in the routineQueue, then determines the first one
		 * that has not succeeded to run. Otherwise, it would take one tick to check 
		 * a routine that has already succeeded instead of instantly moving on to the next one
		 * Must also detect failures
		 * Also returns the list of remaining routineSeq
		 */
		Iterator <RoutineSequencialable> iterator = routineQueue.iterator();
		search:
		while(iterator.hasNext())
		{
			RoutineSequencialable i = iterator.next();
			if(i.succeeded())
			{
				iterator.remove();
			}this has yet to be completed and debugged
			else
			{
				break search;
			}
		}
	}
	public boolean instaSucceeded()
	{
		boolean ret = true;
		for(RoutineSequencialable r : routine)
		{
			if(!r.instaSucceeded())
			{
				ret = false;
			}
		}
		return ret;
	}
	public boolean instaFailed()
	{
		boolean ret = false;
		search:
		for(int i = 0; i < routine.size(); i ++)
		{
			if(routine.get(i).instaFailed())
			{
				// checking that all preceding routines have ALREADY succeeded
				for(int j = 0; j < i; j++)
				{
					if(!routine.get(j).succeeded())
					{
						ret = false;
						break search;
					}
				}
			ret = true;
			break search;
			}
		}
		return ret;
	}
}
