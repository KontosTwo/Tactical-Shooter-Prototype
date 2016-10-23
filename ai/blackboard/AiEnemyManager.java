package com.mygdx.ai.blackboard;

import java.util.HashSet;
import java.util.Set;

import com.mygdx.physics.PrecisePoint3;


public final class AiEnemyManager {
	public interface AiEnemyCognizable{
		public void aiSeeDirectly(PrecisePoint3 target);
		public void aiSeeThroughTerrain(PrecisePoint3 target);
	}
	private final AiEnemyCognizable observer;
	private final Set<MarkableEnemy> predictedEnemies;// implement hashCode later plz
	private final Set<TrackableEnemy> visibleEnemies;// implement hashCode later plz

	public AiEnemyManager(AiEnemyCognizable o){
		observer = o;
		predictedEnemies = new HashSet<>();
		visibleEnemies = new HashSet<>();
	}
	
	public void spotEnemyAt(PrecisePoint3 location){
		
	}
}
