package com.mygdx.ai.leaf;

import com.mygdx.ai.functional.Routineable;


/**
 * Executes a routine a specified number of times. If the executed routine
 * ever fails, then, provided that Repeater can still run the routine, Repeater simply
 * attempts another execution of the routine. 
 * @succeeds if the current routine has been executed a certian number of times
 *
 */
final class Repeater implements Routineable
{
	private final Routineable routine;
	private boolean readyToStart;
	private int times;
	private int currentTimes;
	
	Repeater(Routineable rs){
		routine = rs;
		readyToStart = true;
	}
	
	void setTimes(int t){
		times = t;
	}
	
	@Override
	public void startRoutine(){
		routine.startRoutine();
		readyToStart = false;
		currentTimes = 0;
	}

	@Override
	public void updateRoutine(float dt){
		if(readyToStart){
			routine.startRoutine();
			readyToStart = false;
		}
		if(routine.succeededRoutine()){
			routine.completeRoutine();
			currentTimes ++;
			readyToStart = true;
		}
		else if(routine.failedRoutine()){
			routine.cancelRoutine();
			currentTimes ++;
			readyToStart = true;
		}
		else{
			routine.updateRoutine(dt);
		}
	}

	@Override
	public void completeRoutine(){
		currentTimes = 0;
		//routine.completeRoutine();
	}

	@Override
	public void cancelRoutine(){
		currentTimes = 0;
		routine.cancelRoutine();
	}

	@Override
	public boolean succeededRoutine() {
		return enoughTimes();
	}
	
	private boolean enoughTimes(){
		return currentTimes >= times;
	}

	/**
	 * If the routine fails, then simply attempt again
	 */
	@Override
	public boolean failedRoutine() {
		return routine.failedRoutine();
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
