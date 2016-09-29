package com.mygdx.ai.functional;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.mygdx.debug.Debugger;

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
final class Survival implements Routineable
{
 	private final List<Routineable> routineBank;
    private final Queue<Routineable> survivalRoutineQueue;
    private final List<Survivalable> condition;
    
    Survival(List<RoutineSurvivalable> routineList,Routineable aspirational) {
        routineBank = new LinkedList<Routineable>(routineList);
        
        // ensuring that the aspirational routine is always executed
        routineBank.add(new AlwaysExecute(aspirational));
        
        survivalRoutineQueue = new LinkedList<Routineable>();
        condition = new LinkedList<>();
    }
     
     static Survival build(Routineable aspirational,RoutineSurvivalable... rs){
		List <RoutineSurvivalable> routineList = new LinkedList<>();
		for(int i = 0; i < rs.length; i ++){
			routineList.add(rs[i]);
		}
		return new Survival(routineList,aspirational);
	}
	
	@Override
	public void startRoutine() 
	{
		survivalRoutineQueue.clear();
        survivalRoutineQueue.addAll(routineBank);
        transverse();
    	survivalRoutineQueue.peek().startRoutine();   
	}

	@Override
	public void updateRoutine(float dt) 
	{
		if(survivalRoutineQueue.peek().succeededRoutine())
		{
			survivalRoutineQueue.peek().completeRoutine();
			survivalRoutineQueue.poll();
			transverse();
			survivalRoutineQueue.peek().startRoutine();
		}
		/*
		 * No failure condition; if the current routine ever
		 * fails, Survival will fail as a whole, but RoutineExecutor 
		 * will restart Survival 
		 */
		else
		{
			survivalRoutineQueue.peek().updateRoutine( dt);
		}
	}

	@Override
	public void completeRoutine() 
	{
		Debugger.tick("completing Routine");
		if(!survivalRoutineQueue.isEmpty())
		{
			survivalRoutineQueue.peek().completeRoutine();
		}
		survivalRoutineQueue.clear();
	}

	@Override
	public void cancelRoutine() 
	{
		survivalRoutineQueue.peek().cancelRoutine();
	}

	@Override
	public boolean succeededRoutine() 
	{
		/*
		 * rewrite a way to check if succeeded
		 */
		//Debugger.tick(routineQueue.peek().getClass().getName());
		if(survivalRoutineQueue.isEmpty())
		{
			return true;
		}
		else if(survivalRoutineQueue.peek().succeededRoutine())
		{
			return succeededAfterTransverseInstaSucceeded();
		}
		return false;
	}
	private boolean succeededAfterTransverseInstaSucceeded()
	{
		LinkedList<Routineable> copy = new LinkedList<>(survivalRoutineQueue);
		copy.poll();
		Iterator <Routineable> iterator = copy.iterator();
		search:
		while(iterator.hasNext())
		{
			Routineable i = iterator.next();
			if(i.instaSucceededRoutine())
			{
				iterator.remove();
			}
			else
			{
				break search;
			}
		}
		return copy.isEmpty();
	}

	@Override
	public boolean failedRoutine() 
	{
		if(!conditionUpheld())
		{
			return true;
		}
		else if(survivalRoutineQueue.isEmpty())
		{
			return false;
		}
		else if(survivalRoutineQueue.peek().failedRoutine())
		{
			return true;
		}
		else if(survivalRoutineQueue.peek().succeededRoutine())
		{
			return failedAfterTransverseInstaFailed();
		}
		return false;
	}
	
	private boolean conditionUpheld(){
		boolean upheld = true;
		for(int i = 0; i < routineBank.indexOf(survivalRoutineQueue.peek()); i ++){
			if(!condition.get(i).conditionUpheld()){
				upheld = false;
			}
		}
		return upheld;
	}
	
	private boolean failedAfterTransverseInstaFailed(){
		boolean ret = false;
		LinkedList<Routineable> copy = new LinkedList<>(survivalRoutineQueue);
		// get rid of the routine that succeeded
		copy.poll();
		search:
		for(int i = 0; i < copy.size(); i ++){
			if(copy.get(i).instaFailedRoutine()){
				// checking that all preceding routines have ALREADY succeeded
				for(int j = 0; j < i; j++){
					if(!copy.get(j).instaSucceededRoutine()){
						ret = false;
						break search;
					}
				}
			ret = true;
			break search;
			}
		}
		return ret;
	}
	
	private void transverse()
	{
		/*
		 * Sifts through all routines in the routineQueue, then determines the first one
		 * that has not succeeded to run. Otherwise, it would take one tick to check 
		 * a routine that has already succeeded instead of instantly moving on to the next one
		 * Must also detect failures
		 */
		Iterator <Routineable> iterator = survivalRoutineQueue.iterator();
		search:
		while(iterator.hasNext()){
			Routineable i = iterator.next();
			if(i.instaSucceededRoutine()){
				iterator.remove();
			}
			else{
				break search;
			}
		}
	}

	@Override
	public boolean instaSucceededRoutine(){return false;}
	
	@Override
	public boolean instaFailedRoutine(){return false;}

	@Override
	public void calculateInstaHeuristic() {
		// TODO Auto-generated method stub
		
	}
}
