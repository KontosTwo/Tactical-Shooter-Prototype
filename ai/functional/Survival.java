package com.mygdx.ai.functional;

import java.util.ArrayList;
import java.util.List;
/**
 * Conditionally and sequencially executes a list of RoutineSurvivalables. Takes
 * a list of Survival routines and a single aspirational routine as arguments. Each
 * Survival routine has an upheld() method that returns whether a certain condition is satisfied
 * Whilst executing a routine, Survival continuously check each preceding/already completed routine's
 * upheld() condition. If any of these return false, then Survival cancels the current routine
 * and begins executing the routine that returned false. Only when all survival routines
 * are successful does Survival execute the aspirational routine, which never instantly completes
 * 
 * This class is used only by RoutineExecutor
 * @author Vincent Li
 *
 */
public final class Survival extends Sequence implements Routineable{

    private final List<Survivalable> condition;
    private boolean readyToStart;

	private Survival(List<Routineable> survivalRoutine,List<? extends Survivalable> survivalCondition) {
		super(survivalRoutine);
		condition = new ArrayList<>(survivalCondition);
		readyToStart = true;
	}
	public static Survival build(List<RoutineSurvivalable> survivalRoutine,Routineable aspirational){
		List<Routineable> fullSurvivalRoutine = new ArrayList<>();
		fullSurvivalRoutine.addAll(survivalRoutine);
		fullSurvivalRoutine.add(new AlwaysExecute(aspirational));
		return new Survival(fullSurvivalRoutine,survivalRoutine);
	}
	
	@Override
	public boolean succeededRoutine(){
		return super.succeededRoutine() && conditionUpheld();
	}
	
	@Override
	public boolean failedRoutine() {
		return !conditionUpheld() || super.failedRoutine() || routineQueue.peek().succeededRoutine() ? !nextConditionUpheld() : false;
	}

	private boolean conditionUpheld(){
		boolean upheld = true;
		int stoppingPoint =  routine.indexOf(routineQueue.peek());
		search:
		for(int i = 0; i < stoppingPoint; i ++){
			if(!condition.get(i).conditionUpheld()){
				upheld = false;
				break search;
			}
		}
		return upheld;
	}
	
	private boolean nextConditionUpheld(){
		boolean upheld = true;
		/*
		 *  since the current routine of the sequence just completed
		 *  survival must check the very next condition to see if it
		 *  has already failed
		 */
		int stoppingPoint =  routine.indexOf(routineQueue.peek()) + 1;
		search:
		for(int i = 0; i < stoppingPoint; i ++){
			if(!condition.get(i).conditionUpheld()){
				upheld = false;
				break search;
			}
		}
		return upheld;
	}

	
	/*
	 * Survival never instaCompletes because it is designed to ensure 
	 * that, should all other routines instaComplete, one, and only one,
	 * routine will still execute - and that's the aspirational routine
	 */
	@Override
	public boolean instaFailedRoutine(){
		return false;
	}

	@Override
	public boolean instaSucceededRoutine(){
		return false;
	}
	
	@Override
	public void startRoutine(){
		super.startRoutine();
		readyToStart = false;
	}
	
	boolean isReady(){
		return readyToStart;
	}
	
	void finish(){
		readyToStart = true;
	}
}
