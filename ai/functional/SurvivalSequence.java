package com.mygdx.ai.functional;

import java.util.ArrayList;
import java.util.List;

public class SurvivalSequence extends Sequence implements Routineable{

    private final List<Survivalable> condition;

	public SurvivalSequence(List<RoutineSurvivalable> survivalRoutine,Routineable aspirational) {
		super(Sequence.build(new Sequence(survivalRoutine),new AlwaysExecute(aspirational)));
		condition = new ArrayList<>(survivalRoutine);
	}
	
	@Override
	public boolean failedRoutine() {
		return !conditionUpheld() || super.failedRoutine();
	}
	
	private boolean conditionUpheld(){
		boolean upheld = true;
		int stoppingPoint =  routine.indexOf(routineQueue.peek());
		for(int i = 0; i < stoppingPoint; i ++){
			if(!condition.get(i).conditionUpheld()){
				upheld = false;
			}
		}
		return upheld;
	}
}
