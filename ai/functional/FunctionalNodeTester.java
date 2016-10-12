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
		tester.calculateRoutine();

		if(tester.instaCompleted()){
			
		}else{
			tester.startRoutine();
			while(!tester.doneOnce()){
				Debugger.update(4);
	
				tester.update(5);
			}
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
		
		
		
		public boolean instaCompleted(){
			return routine.instaFailedRoutine() || routine.instaSucceededRoutine();
		}
		
		
		public void update(float dt) 
		{
			if(routineActive && times < 1)
			{
				//System.out.println("updating");
				if(routine.succeededRoutine())
				{
					Debugger.tick("Routine succeeded");
					routine.completeRoutine();
					//routine.startRoutine();
					times ++;
				}
				else if(routine.failedRoutine())
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
		public void calculateRoutine()
		{
			routineActive = true;
			

			List<Routineable> list = new LinkedList<>();


			//selector1.add(new AlwaysSucceed(new Wait(10)));

	
			//selector1.add(new AlwaysSucceed(new Wait(10)));
			list.add(new InstaSucceed());
			list.add(new Wait(10));
			list.add(new InstaSucceed());
			Survival listRoutine = new Sequence(list);
			
			Routineable finalRoutine = Sequence.build(new Wait(10),new InstaFail());
			//Routine.add(new AlwaysSucceed(new Wait(10)));
			routine = finalRoutine;
			
			System.out.println("InstaSucceeded " + routine.instaSucceededRoutine());
			System.out.println("InstaFailed " + routine.instaFailedRoutine());
		}
		public void startRoutine(){
			routine.startRoutine();
		}
	}
}
