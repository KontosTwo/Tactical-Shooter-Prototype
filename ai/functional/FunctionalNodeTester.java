package com.mygdx.ai.functional;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.mygdx.debug.Debugger;

public class FunctionalNodeTester {

	private List<Expire> condition;
	
	public void test() {
		condition = new LinkedList<>();
		Expire expire1 = new Expire(20);
		Expire expire2 = new Expire(25);
		Expire expire3 = new Expire(35);
		Expire expire4 = new Expire(45);
		Expire expire5 = new Expire(55);
		Expire expire6 = new Expire(65);
		
		TestWait wait1 = new TestWait(5,"first");
		TestWait wait2 = new TestWait(5,"second");
		TestWait wait3 = new TestWait(5,"third");
		TestWait wait4 = new TestWait(5,"fourth");
		TestWait wait5 = new TestWait(5,"fifth");
		TestWait wait6 = new TestWait(5,"sixth");
		
		RoutineTester tester = new RoutineTester();
				
		List<RoutineSurvivalable> survivalList = new ArrayList<>();

		condition.add(expire1);
		condition.add(expire2);
		condition.add(expire3);
		condition.add(expire4);
		condition.add(expire5);
		condition.add(expire6);
		survivalList.add(FunctionalNodeFactory.assemble(Sequence.build(new InstaSucceed(),new TestWait(5)), expire1));

		survivalList.add(FunctionalNodeFactory.assemble(Sequence.build(new InstaSucceed(),new TestWait(5)), () -> true));
		survivalList.add(FunctionalNodeFactory.assemble(Sequence.build(new InstaSucceed(),new TestWait(5)), () -> true));
		Survival survival = Survival.build(survivalList,new TestWait(5,"aspirational"));
		
		tester.calculateRoutine(survival);
		
		
		tester.startRoutine();
		for(int i = 0; i < 25; i ++){
			Debugger.update(4);
			condition.forEach(c -> c.update());
			tester.update(5);
		}
		
		for(int i = 0; i < 20; i ++){
			Debugger.update(4);
			condition.forEach(c -> c.update());
			tester.update(5);
		}
	}
/*
	@Test
	public void test2(){
		Repeater repeater = new Repeater(new TestWait(5));
		repeater.setTimes(5);
		repeater.startRoutine();
		while(!repeater.succeededRoutine()){
			repeater.updateRoutine(3);
			Debugger.update(3);
		}
		repeater.completeRoutine();
		
	}*/
	
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
				//Debugger.tick("Condition for Expire failed due to value being at: " + num);
			}
			return num < max;
		}
		
		void update(){
			num ++;
		}
		void reset(){
			num = 0;
		}
		void set(int to)
		{
			num = to;
		}
		@Override
		public String toString(){
			return "" + num;
		}
	}
}
