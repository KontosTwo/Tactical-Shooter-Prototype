package com.mygdx.ai;

import com.mygdx.debug.Debugger;

class Wait implements RoutineSequencialable
{
	private int tickQuota;
	private int tickCount;
	
	Wait() 
	{
		
	}
	Wait(int wait)
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
		Debugger.tick("Wait is updating");
	}
	@Override
	public boolean sequenceIsComplete() 
	{
		return tickCount >= tickQuota;
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
		return tickCount >= tickQuota;
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