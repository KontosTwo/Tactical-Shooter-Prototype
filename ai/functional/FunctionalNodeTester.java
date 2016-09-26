package com.mygdx.ai.functional;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import com.mygdx.ai.leaf.Wait;

import com.mygdx.ai.leaf.RiflemanRoutineable;
import com.mygdx.ai.leaf.RoutineFactory;
import com.mygdx.debug.Debugger;

public class FunctionalNodeTester {

	@Test
	public void test() {
		RoutineTester tester = new RoutineTester();
		tester.startRoutine();
		while(!tester.doneOnce()){
			Debugger.update(4);;

			tester.update(5);
		}
	}
	private static class RoutineTester{
		/*
		 * Ai routines keep running until stopped by an outside action
		 */
		private Routineable routine;
		private boolean routineActive;
		
		
		private int times = 0;

		private boolean doneOnce(){
			return times >=1;
		}
		
		public RoutineTester()
		{
			routineActive = false;
		}
		
		//retest selector and Routine, which has the RoutineIsComplete replaced
		//with succeeded. Test to make sure that no extraneous startRoutine is called
		
		
		
		
		
		
		public void update(float dt) 
		{
			if(routineActive && times < 1)
			{
				//System.out.println("updating");
				if(routine.routineSucceeded())
				{
					Debugger.tick("Routine succeeded");
					routine.completeRoutine();
					//routine.startRoutine();
					times ++;
				}
				else if(routine.routineFailed())
				{
					Debugger.tick("Routine failed");
					routine.cancelRoutine();
					//routine.startRoutine();
					times ++;
				}
				else
				{
					routine.updateRoutine(dt);
				}
			}
		}
		
		public void stopRoutine()
		{
			if(routineActive)
			{
				routine.cancelRoutine();
				routineActive = false;
			}
		}
		public void pauseRoutine(boolean b)
		{
			routineActive = b;
		}
		public void startRoutine()
		{
			routineActive = true;
			
			List<Routineable> Routine = new LinkedList<>();

	
			Routine.add(new Wait(10));

			
			Routine.add(new InstaFail());

			Routine.add(new InstaFail());
			Routine.add(new InstaFail());
			Routine.add(new InstaFail());
			Routine.add(new InstaFail());
			Routine.add(new InstaSucceed());
			Routine.add(new Wait(10));
			Routine.add(new InstaFail());

			routine = new Selector(Routine);
			
			System.out.println("InstaSucceeded " + routine.routineInstaSucceeded());
			System.out.println("InstaFailed " + routine.routineInstaFailed());
			routine.startRoutine();
		}
	}

}
