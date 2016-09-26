package com.mygdx.script;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mygdx.script.Scripter.Sequencialable;


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
	public void updateSequence(float dt) 
	{
		Iterator <Sequencialable> iterator = sequence.iterator();
		while(iterator.hasNext())
		{
			Sequencialable current = iterator.next();
			if(current.completed())
			{
				iterator.remove();
			}
			else
			{
				current.updateSequence(dt);
			}
		}
	}

	@Override
	public boolean completed() 
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

	@Override
	public boolean sequenceInstaCompleted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void calculateInstaCompleted() {
		// TODO Auto-generated method stub
		
	}
}
