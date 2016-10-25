package com.mygdx.ai.blackboard;

import com.mygdx.physics.PrecisePoint3;
import com.mygdx.ai.blackboard.EnemyMarker.Markable;



public final class EnemyMarker<E extends Markable>{
	
	public interface Markable {
		public PrecisePoint3 getLocationForBlackBoard();
	}
	
	private final E enemy;
	
	private static int enemyDisappearRadius;
	private static int enemyAppearRadius;
	
	
	
	static{
		enemyDisappearRadius = 60;
		enemyAppearRadius = 30;
	}
	public static void setParameters(int disappearRadius,int appearRadius){
		enemyDisappearRadius = disappearRadius;
		enemyAppearRadius = appearRadius;
	}
	
	EnemyMarker(E e){
		enemy = e;
	}
	
	E getEnemy(){
		return enemy;
	}
	
	@Override
	public int hashCode(){
		return enemy.hashCode();
	}
}