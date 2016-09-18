package com.mygdx.script;

import com.mygdx.misc.Updatable;

public class Scripter implements Updatable
{
	/*
	 * runs one sequencialable at a time. The sequencialable
	 * can only affect one actor. Controls when the actor
	 * will execute a script
	 * Scripts can be interrupted by the cancelScript method or the pushing
	 * of another script
	 */
	private Sequencialable sequence;
	private boolean sequenceIsRunning;

	public Scripter()
	{
		sequenceIsRunning = false;
	}
	public void cancelScript()
	{
		if(sequenceIsRunning)
		{
			sequenceIsRunning = false;
			sequence.cancelSequence();
		}
	}
	public void pushSequence(Sequencialable s)
	{
		if(sequenceIsRunning)
		{
			sequence.cancelSequence();
		}
		sequence = s;
		sequence.startSequence();		
		sequenceIsRunning = true;
	}
	@Override
	public void update(float dt) 
	{
		if(sequenceIsRunning)
		{
			if(sequence.sequenceIsComplete())
			{
				sequence.completeSequence();
				sequenceIsRunning = false;
			}
			else
			{
				sequence.update(dt);
			}
		}
		
	}
	public boolean isActive()
	{
		return sequenceIsRunning;
	}
}