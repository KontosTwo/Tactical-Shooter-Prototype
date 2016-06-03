package com.mygdx.ai.functional;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.mygdx.debug.Debugger;
import com.mygdx.script.Sequencialable;


public class Selector implements RoutineSequencialable
{
	 private final List<RoutineSequencialable> routine;
	    private Queue<RoutineSequencialable> routineQueue;
	    
	     
	    /*
	     * due to the generic nature of this class, it can find use
	     * in other areas such as cutscenes and game scripts
	     */
	    
	    public Selector(List<? extends RoutineSequencialable> sequence) 
	    {
	        routine = new LinkedList<RoutineSequencialable>(sequence);
	        routineQueue = new LinkedList<RoutineSequencialable>();
	    }
	    public static Selector build(RoutineSequencialable... rs)
		{
			LinkedList <RoutineSequencialable> routineList = new LinkedList<RoutineSequencialable>();
			for(int i = 0; i < rs.length; i ++)
			{
				routineList.add(rs[i]);
			}
			return new Selector(routineList);
		}
		
		@Override
		public void startSequence() 
		{
			Debugger.tick("Selector is starting");
			routineQueue.clear();
	        routineQueue.addAll(routine);
	        transverse();
	    	routineQueue.peek().startSequence();   
		}

		@Override
		public void update(float dt) 
		{
			if(routineQueue.peek().succeeded())
			{
				routineQueue.peek().completeSequence();
				routineQueue.poll();
				transverse();
				routineQueue.peek().startSequence();
			}
			else
			{
				routineQueue.peek().update( dt);
			}
		}

		@Override
		public boolean sequenceIsComplete() 
		{
			if(routineQueue.isEmpty())
			{
				return false;
			}
			else if(routineQueue.peek().succeeded())
			{
				return true;
			}
			else if(routineQueue.peek().failed())
			{
				return succeededAfterTransverseInstaSucceeded();
			}
			return false;
		}

		@Override
		public void completeSequence() 
		{
			Debugger.tick("completing selector");
			if(!routineQueue.isEmpty())
			{
				routineQueue.peek().completeSequence();
			}
			routineQueue.clear();
		}

		@Override
		public void cancelSequence() 
		{
			routineQueue.peek().cancelSequence();
		}

		@Override
		public boolean failed() 
		{
			/*
			 * rewrite a way to check if succeeded
			 */
			//Debugger.tick(routineQueue.peek().getClass().getName());
			if(routineQueue.isEmpty())
			{
				return true;
			}
			else if(routineQueue.peek().succeeded())
			{
				return failedAfterTransverseInstaFailed();
			}
			return false;
		}
		private boolean failedAfterTransverseInstaFailed()
		{
			LinkedList<RoutineSequencialable> copy = new LinkedList<>(routineQueue);
			copy.poll();
			Iterator <RoutineSequencialable> iterator = copy.iterator();
			search:
			while(iterator.hasNext())
			{
				RoutineSequencialable i = iterator.next();
				if(i.instaSucceeded())
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
		public boolean succeeded() 
		{
			if(routineQueue.isEmpty())
			{
				return false;
			}
			else if(routineQueue.peek().succeeded())
			{
				return true;
			}
			else if(routineQueue.peek().failed())
			{
				return succeededAfterTransverseInstaSucceeded();
			}
			return false;
		}
		
		private boolean succeededAfterTransverseInstaSucceeded()
		{
			boolean ret = false;
			LinkedList<RoutineSequencialable> copy = new LinkedList<>(routineQueue);
			// get rid of the routine that succeeded
			copy.poll();
			search:
			for(int i = 0; i < copy.size(); i ++)
			{
				if(copy.get(i).instaSucceeded())
				{
					// checking that all preceding routines have ALREADY succeeded
					for(int j = 0; j < i; j++)
					{
						if(!copy.get(j).instaFailed())
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
			Iterator <RoutineSequencialable> iterator = routineQueue.iterator();
			search:
			while(iterator.hasNext())
			{
				RoutineSequencialable i = iterator.next();
				if(i.instaFailed())
				{
					iterator.remove();
				}
				else
				{
					break search;
				}
			}
		}
		public boolean instaFailed()
		{
			boolean ret = true;
			for(RoutineSequencialable r : routine)
			{
				if(!r.instaFailed())
				{
					ret = false;
				}
			}
			return ret;
		}
		public boolean instaSucceeded()
		{
			boolean ret = false;
			search:
			for(int i = 0; i < routine.size(); i ++)
			{
				if(routine.get(i).instaSucceeded())
				{
					// checking that all preceding routines have ALREADY succeeded
					for(int j = 0; j < i; j++)
					{
						if(!routine.get(j).instaFailed())
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
