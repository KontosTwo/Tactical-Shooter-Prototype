package com.mygdx.script;


class WaitForEvent implements Sequencialable
{
	private boolean eventComplete;
	private Triggerable trigger;
	
	WaitForEvent(Triggerable trigger)
	{
		this.trigger = trigger;
	}
	
	@Override
	public void startSequence() 
	{
		eventComplete = false;
	}

	@Override
	public void update(float dt) 
	{
		trigger.update(dt);
		if(eventComplete == false)// the update loop will check trigger for triggered() only when it hasn't been triggered yet
		{
			eventComplete = trigger.triggered();
		}
	}

	@Override
	public boolean sequenceIsComplete() 
	{
		return eventComplete;
	}

	@Override
	public void completeSequence() 
	{
		
	}

	@Override
	public void cancelSequence()
	{
		
	}
}
interface Triggerable
{
	public boolean triggered();// returns true when the implementer's state changes so that it returns true, signaling a change
	public void update(float dt);
}
