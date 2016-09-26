package com.mygdx.ai.functional;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.mygdx.debug.Debugger;


public class Survival implements Routineable
{
	 	private final List<Routineable> routine;
	    private Queue<Routineable> routineQueue;
	    private List<Survivalable> condition;
	     
	    /*
	     * due to the generic nature of this class, it can find use
	     * in other areas such as cutscenes and game scripts
	     */
	    
	    public Survival(List<RoutineSurvivalable> routineList,Routineable aspirational) 
	    {
	        routine = new LinkedList<Routineable>(routineList);
	        routineQueue = new LinkedList<Routineable>();
	        condition = new LinkedList<>();
	    }
	    public static Survival build(Routineable aspirational,RoutineSurvivalable... rs)
		{
			List <RoutineSurvivalable> routineList = new LinkedList<>();
			for(int i = 0; i < rs.length; i ++)
			{
				routineList.add(rs[i]);
			}
			return new Survival(routineList,aspirational);
		}
		
		@Override
		public void startRoutine() 
		{
			Debugger.tick("Routine is starting");
			routineQueue.clear();
	        routineQueue.addAll(routine);
	        transverse();
	    	routineQueue.peek().startRoutine();   
		}

		@Override
		public void updateRoutine(float dt) 
		{
			if(routineQueue.peek().routineSucceeded())
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
		public void completeRoutine() 
		{
			Debugger.tick("completing Routine");
			if(!routineQueue.isEmpty())
			{
				routineQueue.peek().completeRoutine();
			}
			routineQueue.clear();
		}

		@Override
		public void cancelRoutine() 
		{
			routineQueue.peek().cancelRoutine();
		}

		@Override
		public boolean routineSucceeded() 
		{
			/*
			 * rewrite a way to check if succeeded
			 */
			//Debugger.tick(routineQueue.peek().getClass().getName());
			if(routineQueue.isEmpty())
			{
				return true;
			}
			else if(routineQueue.peek().routineSucceeded())
			{
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
				if(i.routineInstaSucceeded())
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
		public boolean routineFailed() 
		{
			if(!conditionUpheld())
			{
				return true;
			}
			else if(routineQueue.isEmpty())
			{
				return false;
			}
			else if(routineQueue.peek().routineFailed())
			{
				return true;
			}
			else if(routineQueue.peek().routineSucceeded())
			{
				return failedAfterTransverseInstaFailed();
			}
			return false;
		}
		
		private boolean conditionUpheld()
		{
			boolean upheld = true;
			for(int i = 0; i < routine.indexOf(routineQueue.peek()); i ++)
			{
				if(!condition.get(i).conditionUpheld())
				{
					upheld = false;
				}
			}
			return upheld;
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
				if(copy.get(i).routineInstaFailed())
				{
					// checking that all preceding routines have ALREADY succeeded
					for(int j = 0; j < i; j++)
					{
						if(!copy.get(j).routineInstaSucceeded())
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
		
		private void transverse()
		{
			/*
			 * Sifts through all routines in the routineQueue, then determines the first one
			 * that has not succeeded to run. Otherwise, it would take one tick to check 
			 * a routine that has already succeeded instead of instantly moving on to the next one
			 * Must also detect failures
			 * Also returns the list of remaining routineSeq
			 */
			//assert !routineQueue.isEmpty();
			Iterator <Routineable> iterator = routineQueue.iterator();
			search:
			while(iterator.hasNext())
			{
				Routineable i = iterator.next();
				if(i.routineInstaSucceeded())
				{
					iterator.remove();
				}
				else
				{
					break search;
				}
			}
		}

		public boolean routineInstaSucceeded()
		{
			boolean ret = true;
			for(Routineable r : routine)
			{
				if(!r.routineInstaSucceeded())
				{
					ret = false;
				}
			}
			return ret;
		}
		public boolean routineInstaFailed(){
			boolean ret = false;
			search:
			for(int i = 0; i < routine.size(); i ++){
				if(routine.get(i).routineInstaFailed()){
					
					// checking that all preceding routines have ALREADY succeeded
					for(int j = 0; j < i; j++){
						if(!routine.get(j).routineInstaSucceeded()){
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
}
