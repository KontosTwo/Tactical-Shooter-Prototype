package com.mygdx.ai.functional;

import com.mygdx.ai.functional.Routineable;
import com.mygdx.debug.Debugger;
/**
 * @Succeeds once a designated amount of time has passed
 */
public class TestWait implements Routineable
{
	private int tickQuota;
	private int tickCount;
	private String name;
		
	 public TestWait(int wait,String name){
		setWaitTime(wait);
		this.name = name;
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
		Debugger.tick(name + " is starting");
	}
	
	@Override
	public void updateRoutine(float dt) {
		tickCount ++;
		Debugger.tick(name + " is updating for " + tickCount + " out of " + tickQuota );
	}
	
	@Override
	public void completeRoutine() {
		Debugger.tick("Completing "+ name);
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