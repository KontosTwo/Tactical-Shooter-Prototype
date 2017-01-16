package com.mygdx.ai.functional;

/**
 * Handles the execution the AI for a given actor. 
 * Different actors supplied to the factory methods
 * will return the appropriate AI implementation
 * @author Vincent Li
 */
public final class RoutineExecuter{
	
	private Survival routine;
	private boolean routineActive;

	private RoutineExecuter(){
		routineActive = false;
	}
	
	public static RoutineExecuter createRiflemanRoutine(){
		return new RoutineExecuter();
	}
	
	public void update(float dt){	
		if(routineActive){
			if(routine.isReady()){
				if(routine.instaFailedRoutine() || routine.instaSucceededRoutine()){
					return;
				}else{
					routine.startRoutine();
				}
			}else if(routine.succeededRoutine()){
				routine.completeRoutine();
				routine.finish();
			}
			else if(routine.failedRoutine()){
				routine.cancelRoutine();
				routine.finish();
			}
			else{
				routine.updateRoutine(dt);
			}
		}
	}
	
	public void stop(){
		if(routineActive){
			routine.cancelRoutine();
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
			routine.cancelRoutine();
		}
		routineActive = true;
		routine.startRoutine();
	}
}