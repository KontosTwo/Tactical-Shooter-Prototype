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
		setWaitTime(wait);
	}
	
	void setWaitTime(int wait){
		tickQuota = wait;
		if(wait <= 0){
			System.err.println("YOU CANNOT WAIT FOR 0 SECONDS. OR FOR A NEGATIVE AMOUNT OF TIME");
			System.exit(1);
		}
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
		Debugger.tick("Completing Wait");
	}
	
	@Override
	public void cancelRoutine() {
		tickCount = 0;
	}
	
	@Override
	public boolean succeededRoutine() {
		return tickCount + 1 >= tickQuota;
	}
	
	@Override
	public boolean failedRoutine() {
		return false;
	}

	@Override
	public boolean instaSucceededRoutine() {
		return false;
	}
	
	@Override
	public boolean instaFailedRoutine() {
		return false;
	}

	@Override
	public void calculateInstaHeuristic() {
		// TODO Auto-generated method stub
		
	}

	
}