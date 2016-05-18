package com.mygdx.ai;

import com.mygdx.debug.Debugger;

 class InstaSucceed implements RoutineSequencialable
{

	@Override
	public void startSequence() 
	{
		// TODO Auto-generated method stub
		Debugger.tick("InstaSucceed is starting");

	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean sequenceIsComplete() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void completeSequence() 
	{
		// TODO Auto-generated method stub
		Debugger.tick("InstaSucceed is completing");
	}

	@Override
	public void cancelSequence() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean succeeded() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean failed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean instaSucceeded() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean instaFailed() {
		// TODO Auto-generated method stub
		return false;
	}


}
