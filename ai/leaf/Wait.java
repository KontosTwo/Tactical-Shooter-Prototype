package com.mygdx.ai.leaf;

import com.mygdx.ai.functional.Routineable;
import com.mygdx.debug.Debugger;
/**
 * @Succeeds once a designated amount of time has passed
 */
public class Wait implements Routineable
{
	private int tickQuota;
	private int tickCount;
		
	 public Wait(int wait){
		tickQuota = wait;
	}
	
	void setWaitTime(int wait){
		tickQuota = wait;
	}
	
	@Override
	public void startRoutine() {
		tickCount = 0;
		Debugger.tick("Wait is starting");
	}
	
	@Override
	public void updateRoutine(float dt) {
		tickCount ++;
		Debugger.tick("Wait is updating for " + tickCount + " out of " + tickQuota );
	}
	
	@Override
	public void completeRoutine() {
		Debugger.tick("Wait is complete");
	}
	
	@Override
	public void cancelRoutine() {
		tickCount = 0;
	}
	
	@Override
	public boolean routineSucceeded() {
		return tickCount + 1 >= tickQuota;
	}
	
	@Override
	public boolean routineFailed() {
		return false;
	}

	@Override
	public boolean routineInstaSucceeded() {
		return false;
	}
	
	@Override
	public boolean routineInstaFailed() {
		return false;
	}

	
}