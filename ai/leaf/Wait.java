package com.mygdx.ai.leaf;

import com.mygdx.ai.functional.RoutineSequencialable;
import com.mygdx.debug.Debugger;

public class Wait implements RoutineSequencialable
{
	private int tickQuota;
	private int tickCount;
	
	Wait() 
	{
		
	}
	public Wait(int wait)
	{
		tickQuota = wait;
	}
	void setWaitTime(int wait)
	{
		tickQuota = wait;
	}
	@Override
	public void startSequence() 
	{
		tickCount = 0;
		Debugger.tick("Wait is starting");
	}
	@Override
	public void update(float dt) 
	{
		tickCount ++;
		Debugger.tick("Wait is updating for " + tickCount + " out of " + tickQuota );
	}
	@Override
	public boolean sequenceIsComplete() 
	{
		return tickCount + 1 >= tickQuota;
	}
	@Override
	public void completeSequence() 
	{
		Debugger.tick("Wait is complete");
	}
	@Override
	public void cancelSequence() 
	{
		tickCount = 0;
	}
	@Override
	public boolean succeeded() 
	{
		return tickCount + 1 >= tickQuota;
	}
	@Override
	public boolean failed() 
	{
		return false;
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