package com.mygdx.ai.blackboard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mygdx.physics.PrecisePoint3;
import com.mygdx.ai.blackboard.AiEnemyManager.AiEnemyCognizable;
import com.mygdx.debug.Debugger;

/**
 * An implementation of a Blackboard that describes the behavior and information-gathering
 * of a soldier that remembers the locations of both visible enemies and enemies that had 
 * disappeared out of sight.
 * 
 * @author Vincent Li
 */

public final class AiEnemyManager <O extends AiEnemyCognizable,T extends Trackable & Markable>{
	
	public interface AiEnemyCognizable{
		public boolean aiSeeDirectly(PrecisePoint3 target);
		public boolean aiSeeThroughTerrain(PrecisePoint3 target);
		public PrecisePoint3 getPositionForCognizable();
	}
	
	private final AiEnemyCognizable observer;
	private final List<EnemyMarker<T>> predictedEnemies;
	private final List<EnemyTracker<T>> visibleEnemies;

	public AiEnemyManager(O o){
		observer = o;
		predictedEnemies = new ArrayList<>();
		visibleEnemies = new ArrayList<>();
	}
	
	public void update(){
		removeOutOfSightEnemies();
		visibleEnemies.forEach(e -> Debugger.mark(e.getTargetLocation().create2DProjection()));
		System.out.println(visibleEnemies.size());
	}
	
	private void removeOutOfSightEnemies(){
		Iterator<EnemyTracker<T>> iterator = visibleEnemies.iterator();
		while(iterator.hasNext()){
			EnemyTracker<T> currentTrackableEnemy = iterator.next();
			if(observer.aiSeeDirectly(currentTrackableEnemy.getTargetLocation()) == false){
				iterator.remove();
			}
		}
	}
	
	public void spotEnemy(T enemy){
		checkToAddToVisible(enemy);
	}
	
	private void checkToAddToVisible(T enemy){
		// check for duplicates; if found, return
		for(EnemyTracker<T> enemyTracker : visibleEnemies){
			if(enemyTracker.isSameEnemy(enemy)){
				return;
			}
		}
		
		//otherwise, add to visible
		visibleEnemies.add(new EnemyTracker<T>(enemy));
	}
}