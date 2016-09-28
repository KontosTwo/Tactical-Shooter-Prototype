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
	public boolean succeededRoutine() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean failedRoutine() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean instaSucceededRoutine() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean instaFailedRoutine() {
		// TODO Auto-generated method stub
		return false;
	}

}
