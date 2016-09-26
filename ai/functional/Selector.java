package com.mygdx.ai.functional;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.mygdx.debug.Debugger;

/**
 * Executes Routineables until one succeeded
 * @Succeeds as soon as one Routineable succeeds
 * @Fails once all Routineables have failed
 * @InstaSucceeds if the first Routineable InstaSucceeds
 * @InstaFails if all Routineables InstaFails
 */
public class Selector implements Routineable
{
	 private final List<Routineable> routine;
	 private Queue<Routineable> routineQueue;
	    
	   
	    public Selector(List<? extends Routineable> Routine) 
	    {
	        routine = new LinkedList<Routineable>(Routine);
	        routineQueue = new LinkedList<Routineable>();
	    }
	    public static Selector build(Routineable... rs)
		{
			LinkedList <Routineable> routineList = new LinkedList<Routineable>();
			for(int i = 0; i < rs.length; i ++)
			{
				routineList.add(rs[i]);
			}
			return new Selector(routineList);
		}
		
		@Override
		public void startRoutine() 
		{
			Debugger.tick("Selector is starting");
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
			Debugger.tick("completing selector");
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
		public boolean routineFailed() 
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
				return failedAfterTransverseInstaFailed();
			}
			return false;
		}
		private boolean failedAfterTransverseInstaFailed()
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
		public boolean routineSucceeded() 
		{
			if(routineQueue.isEmpty())
			{
				return false;
			}
			else if(routineQueue.peek().routineSucceeded())
			{
				return true;
			}
			else if(routineQueue.peek().routineFailed())
			{
				return succeededAfterTransverseInstaSucceeded();
			}
			return false;
		}
		
		private boolean succeededAfterTransverseInstaSucceeded()
		{
			boolean ret = false;
			LinkedList<Routineable> copy = new LinkedList<>(routineQueue);
			// get rid of the routine that succeeded
			copy.poll();
			search:
			for(int i = 0; i < copy.size(); i ++)
			{
				if(copy.get(i).routineInstaSucceeded())
				{
					// checking that all preceding routines have ALREADY succeeded
					for(int j = 0; j < i; j++)
					{
						if(!copy.get(j).routineInstaFailed())
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
				if(i.routineInstaFailed())
				{
					iterator.remove();
				}
				else
				{
					break search;
				}
			}
		}

		public boolean routineInstaFailed()
		{
			boolean ret = true;
			for(Routineable r : routine)
			{
				if(!r.routineInstaFailed())
				{
					ret = false;
				}
			}
			return ret;
		}
		public boolean routineInstaSucceeded()
		{
			boolean ret = false;
			search:
			for(int i = 0; i < routine.size(); i ++)
			{
				if(routine.get(i).routineInstaSucceeded())
				{
					// checking that all preceding routines have ALREADY succeeded
					for(int j = 0; j < i; j++)
					{
						if(!routine.get(j).routineInstaFailed())
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
		
}
