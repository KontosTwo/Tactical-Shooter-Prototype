package com.mygdx.ai.blackboard;


final class EnemyMarker<E extends Markable>{

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
