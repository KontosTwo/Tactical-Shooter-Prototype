package com.mygdx.ai.blackboard;

import com.mygdx.physics.PrecisePoint;
import com.mygdx.physics.PrecisePoint3;


final class EnemyMarker<E extends Markable>{

	private final PrecisePoint3 location;
	
	private static int enemyDisappearRadius;
	private static int enemyAppearRadius;
	
	
	
	static{
		enemyDisappearRadius = 60;
		enemyAppearRadius = 60;
	}
	public static void setParameters(int disappearRadius,int appearRadius){
		enemyDisappearRadius = disappearRadius;
		enemyAppearRadius = appearRadius;
	}
	
	EnemyMarker(E e){
		location = new PrecisePoint3(e.getLocationForBlackBoard());
	}

	PrecisePoint3 getTargetLocation(){
		return location;
	}
	
	boolean canAppearNextTo(EnemyMarker<E> other){
		return PrecisePoint.euclideanDistance(this.location.create2DProjection()
				, other.location.create2DProjection()) < enemyAppearRadius;
	}
	
	boolean canDisappearNextTo(EnemyTracker other){
		return PrecisePoint.euclideanDistance(this.location.create2DProjection()
				, other.getTargetLocation().create2DProjection()) < enemyDisappearRadius;
	}
	
	@Override
	public int hashCode(){
		return location.hashCode();
	}
}
