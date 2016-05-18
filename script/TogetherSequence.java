package com.mygdx.script;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


class TogetherSequence implements Sequencialable
{
	/*
	 * Sequences that are executed together, and is itself complete once all have been completed
	 */
	private List <Sequencialable> sequence;
	
	TogetherSequence(List<Sequencialable> s)
	{
		sequence = new ArrayList <Sequencialable>(s);
	}
	
	void add(Sequencialable s)
	{
		sequence.add(s);
	}

	@Override
	public void startSequence() 
	{
		for(int i = 0; i < sequence.size(); i ++)
		{
			sequence.get(i).startSequence();
		}
	}

	@Override
	public void update(float dt) 
	{
		Iterator <Sequencialable> iterator = sequence.iterator();
		while(iterator.hasNext())
		{
			Sequencialable current = iterator.next();
			if(current.sequenceIsComplete())
			{
				iterator.remove();
			}
			else
			{
				current.update(dt);
			}
		}
	}

	@Override
	public boolean sequenceIsComplete() 
	{
		// returns true once sequence is empty, meaning that all sequences have been executed
		
		return sequence.size() == 0;
	}

	@Override
	public void completeSequence() 
	{
		for(int i = 0; i < sequence.size(); i ++)
		{
			sequence.get(i).completeSequence();
		}
	}

	@Override
	public void cancelSequence()
	{
		for(int i = 0; i < sequence.size(); i ++)
		{
			sequence.get(i).cancelSequence();
		}
	}
}
