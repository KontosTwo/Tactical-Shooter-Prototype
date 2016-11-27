package com.mygdx.ai.functional;

import java.util.List;

import com.mygdx.script.Scripter.Sequencialable;

public class FunctionalNodeFactory {
	 
	/**
	 * @param  A Routineable instance
	 * @return An adapter for Sequencialable
	 */
    public static Sequencialable createSequencialableAdapterFrom(Routineable routine){
    	return new Sequencialable(){

			@Override
			public void startSequence() {
				routine.startRoutine();
			}

			@Override
			public void updateSequence(float dt) {
				routine.updateRoutine(dt);
			}

			@Override
			public boolean completed() {
				return routine.succeededRoutine() || routine.failedRoutine();
			}

			@Override
			public void completeSequence() {
				routine.completeRoutine();
			}

			@Override
			public void cancelSequence() {
				routine.cancelRoutine();
			}

			@Override
			public void calculateInstaCompleted() {
				routine.calculateInstaHeuristic();
			}

			@Override
			public boolean sequenceInstaCompleted() {
				return routine.instaFailedRoutine() || routine.instaSucceededRoutine();
			}   		
    	};
    }
    
    static RoutineSurvivalable assemble(Routineable r,Survivalable s){
    	return new RoutineSurvivalable(){
    		private final Routineable routine = r;
    		private final Survivalable condition = s;
    		
			@Override
			public void calculateInstaHeuristic() {routine.calculateInstaHeuristic();}

			@Override
			public boolean instaSucceededRoutine() {
				return routine.instaSucceededRoutine();
			}

			@Override
			public boolean instaFailedRoutine() {
				return routine.instaFailedRoutine();
			}

			@Override
			public boolean succeededRoutine() {
				return routine.succeededRoutine();
			}

			@Override
			public boolean failedRoutine() {
				return routine.failedRoutine();
			}

			@Override
			public void updateRoutine(float dt) {
				routine.updateRoutine(dt);
			}

			@Override
			public void startRoutine() {
				routine.startRoutine();
			}

			@Override
			public void completeRoutine() {
				routine.completeRoutine();
			}

			@Override
			public void cancelRoutine() {
				routine.cancelRoutine();
			}

			@Override
			public boolean conditionUpheld() {
				return condition.conditionUpheld();
			}
			
			@Override 
			public String toString(){
				return condition.toString();
			}
    	};
    }
    
    public static Routineable createSequence(List<Routineable> routineableList){
    	return new Sequence(routineableList);
    }
    
    public static Routineable createSelector(List<Routineable> routineableList){
    	return new Selector(routineableList);
    }
    
    public static Routineable createSurvival(List<RoutineSurvivalable> survivalRoutine,Routineable aspirational){
    	return Survival.build(survivalRoutine,aspirational);
    }
}
