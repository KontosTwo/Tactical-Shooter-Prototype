package com.mygdx.entity.coordinator;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.mygdx.control.Auxiliarable;
import com.mygdx.control.PlayerControllable;
import com.mygdx.control.AiTestable;
import com.mygdx.entity.soldier.InteractionSoldierBattle;
import com.mygdx.entity.soldier.InteractionSoldierBattle.TacticalAction;
import com.mygdx.entity.soldier.InteractionSoldierBattle.TacticalInfoGatherer;
import com.mygdx.map.GameMap;
import com.mygdx.map.GameMap.Collidable;
import com.mygdx.map.GameMap.HitBoxable;
import com.mygdx.map.Path;
import com.mygdx.physics.PrecisePoint;
import com.mygdx.physics.PrecisePoint3;
import com.mygdx.script.EnvironmentBuilder;

public final class Environment implements TacticalAction,TacticalInfoGatherer,EnvironmentBuilder
{
	private final InteractionSoldierBattle soldierManager;
	//private final ListOfGasClouds gas;
	private final GameMap gameMap;

	public Environment(){
		soldierManager = new InteractionSoldierBattle(this,this);
		gameMap = new GameMap();
	}
	
	@Override
	public PlayerControllable createPlayer(int x,int y){
		PlayerControllable player = soldierManager.createPlayer(x, y);
		gameMap.treatAsCollidable((Collidable)player);
		return player;
	}
	
	@Override
	public Auxiliarable createAuxiliary(int x, int y) {
		Auxiliarable aux = soldierManager.createAuxiliary(x, y);
		return aux;
	}
	
	@Override
	public AiTestable createRifleman(int x,int y){
		AiTestable tele = soldierManager.createRifleman(x, y);
		return tele;
	}
	
	@Override
	public void loadLevel(String name){
		gameMap.loadLevel(name);
	}
	
	public void update(float dt){
		soldierManager.update(dt);
		gameMap.update();
	}
	public void render(){
		/*
		 * 
		 * 
		 * 
		 * 
		 * add all entities into a list of render requests
		 * sorted by their y coordinate
		 */
		soldierManager.render();
		gameMap.render();
	}

	@Override
	public Path findPath(PrecisePoint start,PrecisePoint target,int maxDistance) {
		return gameMap.findPath(start,target,maxDistance);
	}

	@Override
	public boolean see(PrecisePoint3 observer,PrecisePoint3 target) {
		return gameMap.rayTraceVisualPossible(observer,target);
	}
	
	@Override
	public List<HitBoxable> calculateTargetsHit(PrecisePoint3 shooterVantage,
				PrecisePoint3 target, Collection<HitBoxable> potentialTargets) {
		// first, apply deviation to the origin
		//PrecisePoint3 targetConttrainedByInaccuracy = gameMap.c
		
		List <HitBoxable> orderedTargetsHit = gameMap.findIntersectionBoxesThroughPhyTerrain(shooterVantage, target, potentialTargets);
		
		// sort based on distance from shooterVantage
		Collections.sort(orderedTargetsHit,new HitBoxComparator(shooterVantage.create2DProjection()));
		
		return orderedTargetsHit;
	}
	
	
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
}
