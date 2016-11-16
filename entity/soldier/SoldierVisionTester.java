package com.mygdx.entity.soldier;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import com.mygdx.entity.coordinator.Environment;

public class SoldierVisionTester {
	private Environment environment;
	private SoldierBattleAi subject;
	private ArrayList<SoldierBattle> enemies;
	private ArrayList<SoldierBattle> allies;
	
	private static final String testfilename = "";
	
	
	@Test
	public void test() {
		fail("Not yet implemented");
		SoldierVisionTester tester = new SoldierVisionTester();
	}
	
	public SoldierVisionTester(){
		environment = new Environment();
		environment.loadLevel(testfilename);
	}
	
	private static final class MockEnemy extends SoldierBattle{

		MockEnemy(SoldierBattleMediator sbm, SoldierBattleState sbs) {
			super(sbm, sbs);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void addToSighted(SoldierBattle other) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		void render(){
			
		}
	}
}

