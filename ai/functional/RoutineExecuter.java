package com.mygdx.ai.functional;

import com.mygdx.debug.Debugger;
/**
 * Handles the execution the AI for a given actor. 
 * Different actors supplied to the factory methods
 * will return the appropriate AI implementation
 * @author Vincent Li
 */
public final class RoutineExecuter{
	
	private Survival survivalRoutine;
	private boolean routineActive;

	private RoutineExecuter(){
		routineActive = false;
	}
	
	public static RoutineExecuter createRiflemanRoutine(){
		return new RoutineExecuter();
	}
	
	public void update(float dt) {	
		if(routineActive){
			// continuously run the survival routine
			if(survivalRoutine.succeededRoutine()){
				survivalRoutine.completeRoutine();
				survivalRoutine.startRoutine();
			}
			else if(survivalRoutine.failedRoutine()){
				survivalRoutine.cancelRoutine();
				survivalRoutine.startRoutine();
			}
			else{
				survivalRoutine.updateRoutine(dt);
			}
		}
	}
	
	public void stop(){
		if(routineActive){
			survivalRoutine.cancelRoutine();
			routineActive = false;
		}
	}
	
	public void pause(){
		routineActive = false;
	}
	
	public void resume(){
		routineActive = true;
	}
	
	public void start(){
		if(routineActive){
			survivalRoutine.cancelRoutine();
		}
		routineActive = true;
		survivalRoutine.startRoutine();
	}
}