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
		visibleEnemies.forEach(e -> Debugger.poke(e.getTargetLocation().create2DProjection(),"beige"));
		predictedEnemies.forEach(e -> Debugger.poke(e.getTargetLocation().create2DProjection(),"teal"));

	}
	
	private void removeOutOfSightEnemies(){
		Iterator<EnemyTracker<T>> iterator = visibleEnemies.iterator();
		while(iterator.hasNext()){
			EnemyTracker<T> currentTrackableEnemy = iterator.next();
			if(observer.aiSeeDirectly(currentTrackableEnemy.getTargetLocation()) == false){
				// create a new enemymarker where the visible enemy had disappeared
				checkAddToPredicted(currentTrackableEnemy.getTarget());
				iterator.remove();
			}
		}
	}
	
	private void checkAddToPredicted(T enemy){
		/*boolean canAdd = true;
		EnemyMarker<T> newEnemyMarker = new EnemyMarker<>(enemy);
		for(EnemyMarker<T> currentEnemy : predictedEnemies){
			// enemymarkers cannot be placed too close to each other
			if(newEnemyMarker.canAppearNextTo(currentEnemy)){
				canAdd = false;
			}
		};
		if(canAdd){
			predictedEnemies.add(newEnemyMarker);
		}*/
		predictedEnemies.add(new EnemyMarker<T>(enemy));
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
		EnemyTracker<T> newEnemyTracker = new EnemyTracker<T>(enemy);
		visibleEnemies.add(newEnemyTracker);
		replacePredictedEnemyWithVisible(newEnemyTracker);
	}
	
	/*
	 * If a visible enemy is found, it is assumed to be the nearest 
	 * predicted. 
	 */
	private void replacePredictedEnemyWithVisible(EnemyTracker<T> enemyTracker){
		Iterator<EnemyMarker<T>> iterator = predictedEnemies.iterator();
		search:
		while(iterator.hasNext()){
			EnemyMarker<T> currentMarkableEnemy = iterator.next();
			if(currentMarkableEnemy.canDisappearNextTo(enemyTracker)){
				iterator.remove();
				break search;
			}
		}
	}
	
	@Override
	public String toString(){
		String string = "";
		
		return string;
	}
}