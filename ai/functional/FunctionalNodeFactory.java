package com.mygdx.ai.functional;

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
}
