package com.mygdx.entity.coordinator;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import com.mygdx.control.Auxiliarable;
import com.mygdx.control.PlayerControllable;
import com.mygdx.entity.soldier.InteractionSoldierBattle;
import com.mygdx.entity.soldier.InteractionSoldierBattle.TacticalAction;
import com.mygdx.entity.soldier.InteractionSoldierBattle.TacticalInfoGatherer;
import com.mygdx.map.GameMap;
import com.mygdx.map.GameMap.Collidable;
import com.mygdx.map.GameMap.HitBoxable;
import com.mygdx.map.Path;
import com.mygdx.misc.Pair;
import com.mygdx.physics.MovableBox;
import com.mygdx.physics.PrecisePoint;
import com.mygdx.physics.PrecisePoint3;
import com.mygdx.physics.VectorEquation;

public final class EntityListener implements TacticalAction,TacticalInfoGatherer
{
	private final InteractionSoldierBattle soldierManager;
	//private final ListOfGasClouds gas;
	private final GameMap gameMap;

	public EntityListener(){
		soldierManager = new InteractionSoldierBattle(this,this);
		gameMap = new GameMap();
	}
	public PlayerControllable createPlayer(int x,int y){
		PlayerControllable player = soldierManager.createPlayer(x, y);
		gameMap.treatAsCollidable((Collidable)player);
		return player;
	}
	public Auxiliarable createProtector(int x, int y) {
		Auxiliarable aux = soldierManager.createAuxiliary(x, y);
		return aux;
	}

	public void update(float dt){
		soldierManager.update(dt);
		gameMap.update();
	}
	public void render(){
		soldierManager.render();
		gameMap.render();
	}

	public void loadLevel(int level) {
		gameMap.loadLevel(level);
	}
	@Override
	public Path findPath(PrecisePoint start,PrecisePoint target,int maxDistance) {
		return gameMap.findPath(start,target,maxDistance);
	}

	@Override
	public boolean see(PrecisePoint3 observer,PrecisePoint3 target) {
		return gameMap.calculateRayTraceImpactPhysical(observer,target).equals(target);
	}
	
	@Override
	public List<HitBoxable> calculateTargetsHit(PrecisePoint3 shooterVantage,
				PrecisePoint3 target, Collection<HitBoxable> potentialTargets) {
		//PrecisePoint3 targetConttrainedByInaccuracy = gameMap.c
		
		// calculates where the ray will hit the terrain. 
		PrecisePoint3 targetConstrainedByTerrain = gameMap.calculateRayTraceImpactPhysical(shooterVantage, target);
		
		
		final class HitBoxComparator implements Comparator<HitBoxable>{
		
			private final PrecisePoint origin;
			
			private HitBoxComparator(PrecisePoint origin){
				this.origin = origin;
			}
			
			/**
			 * Compares each PrecisePoint's Manhattan distance to the origin
			 */
			@Override
			public int compare(HitBoxable o1, HitBoxable o2) {
				return (int) ((Math.abs(o1.getBottomLeftCorner().x - origin.x) + Math.abs(o1.getBottomLeftCorner().y - origin.y))
						- (Math.abs(o2.getBottomLeftCorner().x - origin.x) + Math.abs(o2.getBottomLeftCorner().y - origin.y)));
			}
		}
		
		PriorityQueue<HitBoxable> targetsHit = new PriorityQueue<>(new HitBoxComparator(shooterVantage.create2DProjection()));
		//VectorEquation shotRay = new VectorEquation();
		for(HitBoxable hb : potentialTargets){
			
		}
		
		
		return null;
	}

}
