package com.mygdx.ai.functional;

import java.util.LinkedList;
import java.util.List;

public class Concurrent implements Routineable
{
	private final List<Routineable> routine;
	private List<Routineable> routineQueue;
	
	public Concurrent (List<Routineable> routine)
	{
		this.routine = new LinkedList<>(routine);
		routineQueue = new LinkedList<>();
	}
	
	
	public static Selector buildSelector(Routineable... rs)
	{
		LinkedList <Routineable> routineList = new LinkedList<Routineable>();
		for(int i = 0; i < rs.length; i ++)
		{
			routineList.add(rs[i]);
		}
		return new Selector(routineList);
	}
	@Override
	public void startRoutine() 
	{
		routineQueue.clear();
		routineQueue.addAll(routine);
		routineQueue.forEach(r -> r.startRoutine());
	}
	@Override
	public void updateRoutine(float dt) 
	{
		routineQueue.forEach(r ->
		{
			if(r.routineSucceeded())
			{
				r.completeRoutine();
				routineQueue.remove(r);
			}
			else if(r.routineFailed())
			{
				r.cancelRoutine();
				routineQueue.remove(r);
			}
			else
			{
				r.updateRoutine(dt);
			}	
		}			
		);
	}

	@Override
	public void completeRoutine() {
		routineQueue.clear();
	}

	@Override
	public void cancelRoutine() {
		routineQueue.forEach(r -> r.cancelRoutine());
	}

	@Override
	public boolean routineSucceeded()
	{
		return routineQueue.isEmpty();
	}

	@Override
	public boolean routineFailed() 
	{
		return false;
	}

	@Override
	public boolean routineInstaSucceeded() 
	{
		boolean ret = true;
		for(Routineable r : routine)
		{
			if(!r.routineInstaSucceeded())
			{
				ret = false;
			}
		}
		return ret;
	}


	@Override
	public boolean routineInstaFailed() 
	{
		boolean ret = true;
		for(Routineable r : routine)
		{
			if(!r.routineInstaFailed())
			{
				ret = false;
			}
		}
		return ret;
	}

}
