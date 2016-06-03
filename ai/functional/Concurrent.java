package com.mygdx.ai.functional;

import java.util.LinkedList;
import java.util.List;

public class Concurrent implements RoutineSequencialable
{
	private final List<RoutineSequencialable> routine;
	private List<RoutineSequencialable> routineQueue;
	
	public Concurrent (List<RoutineSequencialable> routine)
	{
		this.routine = new LinkedList<>(routine);
		routineQueue = new LinkedList<>();
	}
	
	
	public static Selector buildSelector(RoutineSequencialable... rs)
	{
		LinkedList <RoutineSequencialable> routineList = new LinkedList<RoutineSequencialable>();
		for(int i = 0; i < rs.length; i ++)
		{
			routineList.add(rs[i]);
		}
		return new Selector(routineList);
	}
	@Override
	public void startSequence() 
	{
		routineQueue.clear();
		routineQueue.addAll(routine);
		routineQueue.forEach(r -> r.startSequence());
	}
	@Override
	public void update(float dt) 
	{
		routineQueue.forEach(r ->
		{
			if(r.succeeded())
			{
				r.completeSequence();
				routineQueue.remove(r);
			}
			else if(r.failed())
			{
				r.cancelSequence();
				routineQueue.remove(r);
			}
			else
			{
				r.update(dt);
			}	
		}			
		);
	}

	@Override
	public boolean sequenceIsComplete() 
	{
		return routineQueue.isEmpty();
	}

	@Override
	public void completeSequence() 
	{
		routineQueue.clear();
	}

	@Override
	public void cancelSequence() 
	{
		routineQueue.forEach(r -> r.cancelSequence());
	}

	@Override
	public boolean succeeded()
	{
		return routineQueue.isEmpty();
	}

	@Override
	public boolean failed() 
	{
		return false;
	}


	@Override
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


	@Override
	public boolean instaFailed() 
	{
		boolean ret = true;
		for(RoutineSequencialable r : routine)
		{
			if(!r.instaFailed())
			{
				ret = false;
			}
		}
		return ret;
	}


}
