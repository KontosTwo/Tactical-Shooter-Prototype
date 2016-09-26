package com.mygdx.ai.functional;

import com.mygdx.debug.Debugger;

public class InstaSucceed implements Routineable
{

	@Override
	public void startRoutine() 
	{
		// TODO Auto-generated method stub
		Debugger.tick("InstaSucceed is starting");

	}

	@Override
	public void updateRoutine(float dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void completeRoutine() 
	{
		// TODO Auto-generated method stub
		Debugger.tick("InstaSucceed is completing");
	}

	@Override
	public void cancelRoutine() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean routineSucceeded() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean routineFailed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean routineInstaSucceeded() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean routineInstaFailed() {
		// TODO Auto-generated method stub
		return false;
	}

}
