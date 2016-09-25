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
		private RoutineSequencialable routine;
		private boolean routineActive;
		
		
		private int times = 0;

		private boolean doneOnce(){
			return times >=1;
		}
		
		public RoutineTester()
		{
			routineActive = false;
		}
		
		//retest selector and sequence, which has the sequenceIsComplete replaced
		//with succeeded. Test to make sure that no extraneous startSequence is called
		
		
		
		
		
		
		public void update(float dt) 
		{
			if(routineActive && times < 1)
			{
				//System.out.println("updating");
				if(routine.succeeded())
				{
					Debugger.tick("Routine succeeded");
					routine.completeSequence();
					//routine.startSequence();
					times ++;
				}
				else if(routine.failed())
				{
					Debugger.tick("Routine failed");
					routine.cancelSequence();
					//routine.startSequence();
					times ++;
				}
				else
				{
					routine.update(dt);
				}
			}
		}
		
		public void stopRoutine()
		{
			if(routineActive)
			{
				routine.cancelSequence();
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
			
			List<RoutineSequencialable> sequence = new LinkedList<>();

	
			sequence.add(new Wait(10));

			
			sequence.add(new InstaFail());

			sequence.add(new InstaFail());
			sequence.add(new InstaFail());
			sequence.add(new InstaFail());
			sequence.add(new InstaFail());
			sequence.add(new InstaSucceed());
			sequence.add(new Wait(10));
			sequence.add(new InstaFail());

			routine = new Selector(sequence);
			
			System.out.println("InstaSucceeded " + routine.instaSucceeded());
			System.out.println("InstaFailed " + routine.instaFailed());
			routine.startSequence();
		}
	}

}
