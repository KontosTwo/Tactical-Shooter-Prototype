package com.mygdx.ai.functional;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.mygdx.debug.Debugger;

public class Sequence implements Routineable{
    protected final List<Routineable> routine;
    protected final Queue<Routineable> routineQueue;
    
     
    /*
     * due to the generic nature of this class, it can find use
     * in other areas such as cutscenes and game scripts
     */
    
	public Sequence(List<? extends Routineable> Routine) {
        routine = new LinkedList<Routineable>(Routine);
        routineQueue = new LinkedList<Routineable>();
    }
	
	public Sequence(Routineable... rs){
		LinkedList <Routineable> routineList = new LinkedList<Routineable>();
		for(int i = 0; i < rs.length; i ++){
			routineList.add(rs[i]);
		}
		routine = new LinkedList<Routineable>(routineList);
        routineQueue = new LinkedList<Routineable>();
	}

	public static Sequence build(Routineable... rs){
		LinkedList <Routineable> routineList = new LinkedList<Routineable>();
		for(int i = 0; i < rs.length; i ++)
		{
			routineList.add(rs[i]);
		}
		return new Sequence(routineList);
	}
	
	@Override
	public void startRoutine() 
	{
		//Debugger.tick("Routine is starting");
		routineQueue.clear();
        routineQueue.addAll(routine);
        transverse();
    	routineQueue.peek().startRoutine();   
	}

	@Override
	public void updateRoutine(float dt) 
	{
		if(routineQueue.peek().succeededRoutine())
		{
			routineQueue.peek().completeRoutine();
			routineQueue.poll();
			transverse();
			routineQueue.peek().startRoutine();
		}
		else
		{
			routineQueue.peek().updateRoutine( dt);
		}
	}

	@Override
	public void completeRoutine() {
		if(!routineQueue.isEmpty()){
			routineQueue.peek().completeRoutine();
		}
		routineQueue.clear();
	}

	@Override
	public void cancelRoutine() {
		routineQueue.peek().cancelRoutine();
	}

	@Override
	public boolean succeededRoutine() {
		if(routineQueue.isEmpty()){
			return true;
		}else if(routineQueue.peek().succeededRoutine()){
			return succeededAfterTransverseInstaSucceeded();
		}
		return false;
	}
	
	private boolean succeededAfterTransverseInstaSucceeded()
	{
		LinkedList<Routineable> copy = new LinkedList<>(routineQueue);
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
	public boolean failedRoutine() {
		if(routineQueue.isEmpty()){
			return false;
		}
		else if(routineQueue.peek().failedRoutine()){
			return true;
		}
		else if(routineQueue.peek().succeededRoutine()){
			return failedAfterTransverseInstaFailed();
		}
		return false;
	}
	
	private boolean failedAfterTransverseInstaFailed()
	{
		boolean ret = false;
		LinkedList<Routineable> copy = new LinkedList<>(routineQueue);
		// get rid of the routine that succeeded
		copy.poll();
		search:
		for(int i = 0; i < copy.size(); i ++)
		{
			if(copy.get(i).instaFailedRoutine())
			{
				// checking that all preceding routines have ALREADY succeeded
				for(int j = 0; j < i; j++)
				{
					if(!copy.get(j).instaSucceededRoutine())
					{
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
	/**
	 * Sifts through all routines in the routineQueue, then determines the first one
	 * that has not succeeded to run. Otherwise, it would take one tick to check 
	 * a routine that has already succeeded instead of instantly moving on to the next one
	 * Must also detect failures
	 * Also returns the list of remaining routineSeq
	 */
	private void transverse(){
		/*
		 * 
		 */
		Iterator <Routineable> iterator = routineQueue.iterator();
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
	public boolean instaSucceededRoutine(){
		if(routine.isEmpty()){
			return true;
		}
		boolean ret = true;
		for(Routineable r : routine){
			if(!r.instaSucceededRoutine()){
				ret = false;
			}
		}
		return ret;
	}
	@Override
	public boolean instaFailedRoutine(){
		boolean ret = false;
		search:
		for(int i = 0; i < routine.size(); i ++){
			if(routine.get(i).instaFailedRoutine()){
				// checking that all preceding routines have ALREADY succeeded
				for(int j = 0; j < i; j++){
					if(!routine.get(j).instaSucceededRoutine()){
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
	@Override
	public void calculateInstaHeuristic() {
		// TODO Auto-generated method stub
		
	}
}
