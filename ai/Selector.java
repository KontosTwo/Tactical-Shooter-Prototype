package com.mygdx.ai;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.mygdx.script.Sequencialable;


class Selector implements RoutineSequencialable
{
	private RoutineSequencialable currentRoutine;
    private final List<RoutineSequencialable> routine;
    private Queue<RoutineSequencialable> routineQueue;
    
    /*
     * due to the generic nature of this class, it can find use
     * in other areas such as cutscenes and game scripts
     */
    
	Selector(List<? extends RoutineSequencialable> sequence) 
    {
        this.currentRoutine = null;
        routine = new LinkedList<RoutineSequencialable>(sequence);
        routineQueue = new LinkedList<RoutineSequencialable>();
    }
	static Selector buildSelector(RoutineSequencialable... rs)
	{
		LinkedList <RoutineSequencialable> routineList = new LinkedList<RoutineSequencialable>();
		for(int i = 0; i < rs.length; i ++)
		{
			routineList.add(rs[i]);
		}
		return new Selector(routineList);
	}
	
	public void clear()
	{
		routine.clear();
	}

	@Override
	public void startSequence() 
	{
		routineQueue.clear();
        routineQueue.addAll(routine);
        currentRoutine = routineQueue.poll();
        currentRoutine.startSequence();
	}

	@Override
	public void update(float dt) 
	{
		if(currentRoutine.sequenceIsComplete())
		{
			currentRoutine.completeSequence();
			if(!routineQueue.isEmpty())	
			{
				currentRoutine = routineQueue.poll();
				currentRoutine.startSequence();
			}
			else
			{
				return;
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
		return routineQueue.isEmpty() && currentRoutine.sequenceIsComplete();
	}

	@Override
	public void completeSequence() 
	{
		currentRoutine.completeSequence();
	}

	@Override
	public void cancelSequence() 
	{
		currentRoutine.cancelSequence();
	}

	@Override
	public boolean succeeded() 
	{
		return currentRoutine.succeeded();
	}

	@Override
	public boolean failed() 
	{
		return routineQueue.isEmpty() && currentRoutine.failed();
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
