package com.mygdx.ai.functional;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.mygdx.ai.leaf.RiflemanRoutineable;
import com.mygdx.ai.leaf.RoutineFactory;
import com.mygdx.debug.Debugger;

public class FunctionalNodeTester {

	private List<Expire> condition;
	
	@Test
	public void test() {
		condition = new LinkedList<>();
		Expire expire1 = new Expire(15);		
		Expire expire2 = new Expire(15);

		condition.add(expire1);
		condition.add(expire2);
		
		RoutineTester tester = new RoutineTester();
				
		List<RoutineSurvivalable> survivalList = new ArrayList<>();
		survivalList.add(FunctionalNodeFactory.assemble(new TestWait(5,"first"), expire1));
		survivalList.add(FunctionalNodeFactory.assemble(new TestWait(5,"second"), expire2));
		
		Survival survival = Survival.build(survivalList,new TestWait(5,"aspirational"));
		
		tester.calculateRoutine(survival);
		
		if(tester.instaCompleted()){
			
		}else{
			tester.startRoutine();
			for(int i = 0; i < 20; i ++){
				Debugger.update(4);
				condition.forEach(c -> c.update());
				tester.update(5);
			}
		}
		
		System.out.println(expire1);
		System.out.println(expire2);
		expire1.reset();
		expire2.reset();
		for(int i = 0; i < 20; i ++){
			Debugger.update(4);
			condition.forEach(c -> c.update());
			tester.update(5);
		}
	}
	
	private static class RoutineTester{
		private Survival routine;
		private boolean routineActive;
		
		private int times = 0;

		private boolean done(){
			return times >=4;
		}
		
		public RoutineTester(){
			routineActive = false;
		}

		public boolean instaCompleted(){
			return routine.instaFailedRoutine() || routine.instaSucceededRoutine();
		}
		
		public void update(float dt) 
		{
			System.out.println("new tick");
			if(routineActive){
				if(routine.isReady()){
					if(routine.instaSucceededRoutine()){
						Debugger.tick("Instasucceeded, not executing anything");
						return;
					}else if(routine.instaFailedRoutine()){
						Debugger.tick("Instafailed, not executing anything");
						return;
					}else{
						routine.startRoutine();
					}
				}
				if(routine.succeededRoutine())
				{
					Debugger.tick("Routine succeeded");
					routine.completeRoutine();
					routine.finish();
					times ++;
				}
				else if(routine.failedRoutine())
				{
					Debugger.tick("Routine failed");
					routine.cancelRoutine();
					routine.finish();
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
		public void calculateRoutine(Survival executable)
		{
			routineActive = true;

			routine = executable;
			
			System.out.println("InstaSucceeded " + routine.instaSucceededRoutine());
			System.out.println("InstaFailed " + routine.instaFailedRoutine());
		}
		public void startRoutine(){
			routine.startRoutine();
		}
	}
	
	private static class Expire implements Survivalable{
		private int num;
		private final int max;
		
		public Expire(int maximum){
			max = maximum;
			num = 0;
		}
		
		@Override
		public boolean conditionUpheld() {
			if(num>=max){
				Debugger.tick("Condition for Expire failed due to value being at: " + num);
			}
			return num < max;
		}
		
		void update(){
			num ++;
		}
		void reset(){
			num = 0;
		}
		
		public String toString(){
			return "" + num;
		}
	}
}
