package com.mygdx.entity.soldier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import com.mygdx.control.Auxiliarable;
import com.mygdx.control.PlayerControllable;
import com.mygdx.debug.Debugger;
import com.mygdx.entity.soldier.SoldierBattle.SoldierBattleMediator;
import com.mygdx.map.GameMap.HitBoxable;
import com.mygdx.map.Path;
import com.mygdx.physics.MyVector3;
import com.mygdx.physics.PrecisePoint;
import com.mygdx.physics.PrecisePoint3;
import com.mygdx.physics.Util;

public class InteractionSoldierBattle implements SoldierBattleMediator
{
	private SoldierBattle player;
	private SoldierBattle auxiliary;
	private final List<SoldierBattle> enemies;
	private final List<SoldierBattle> enemiesInactive;
	private final TacticalAction actionMaker;
	private final TacticalInfoGatherer infoGatherer;
	
	public interface TacticalInfoGatherer {
		public boolean see(PrecisePoint3 observer,PrecisePoint3 target);
		public Path findPath(PrecisePoint start,PrecisePoint target,int maxDistance);
		
		
		
		/**
		 * @param The 2-D coordinates of the target as initially
		 * supplied by the shooter
		 * @return The 3-D coordinates of the target. All dimensions
		 * have been modified by the TacticalInfoGatherer to a 3-D coordinate
		 * that is VISUALLY EXPECTED by the player
		 */
		//public PrecisePoint3 refineTargetForNonPlayer(PrecisePoint target,Collection<HitBoxable> potentialTargets);
	}
	
	public interface TacticalAction {
		/**
		 * 
		 * @return all hitboxes that are intersected by the shot vector ordered
		 * from those closest from shooterVantage to those farthest away
		 */
		public List<HitBoxable> calculateTargetsHit(PrecisePoint3 shooterVantage,PrecisePoint3 target,Collection<HitBoxable> potentialTargets);
	}

	
	
	public InteractionSoldierBattle(TacticalAction am,TacticalInfoGatherer ig)
	{
		enemies = new LinkedList<>();
		enemiesInactive = new LinkedList<>();
		player = null;
		actionMaker = am;
		infoGatherer = ig;
	}
	public PlayerControllable createPlayer(int x,int y)
	{		
		SoldierBattleControlled newPlayer = SoldierBattleControlled.createControlled(this);
		player = newPlayer;
		player.soldierBattleState.center.teleportTo(x,y);
		return newPlayer;
	}
	public Auxiliarable createAuxiliary(int x, int y) {
		SoldierBattleAuxiliary newAux = SoldierBattleAuxiliary.createAuxiliary(this);
		auxiliary = newAux;
		auxiliary.soldierBattleState.center.teleportTo(x,y);
		return newAux;
	}
	
	public void createRifleman(int x, int y){
		SoldierBattleAi newRif = SoldierBattleAi.createRifleman(this);
		newRif.soldierBattleState.center.teleportTo(x, y);
		enemies.add(newRif);
	}
	
	public void render(){
		
		player.render();
		auxiliary.render();
		enemies.forEach(e -> e.render());
	}
	public void update(float dt){
		updateSoldiers(dt);
		soldierScanBattleField();
	}
	
	private void updateSoldiers(float dt){
		player.update(dt);
		auxiliary.update(dt);
		enemies.forEach(e -> e.update(dt));
	}
	
	private void soldierScanBattleField(){
		enemies.forEach(e -> {
			player.scanFor(e);
			auxiliary.scanFor(e);
			e.scanFor(player);
			e.scanFor(auxiliary);
		});
	}

	@Override
	public void shootForPlayer(SoldierBattle shooter, float accuracy, PrecisePoint3 shooterVantage,PrecisePoint initialTarget) {
		
		// map each hitboxable to its owner for quick and easy retrieval
		HashMap<HitBoxable,SoldierBattle> hitboxOwnership = new HashMap<>();
		
		// collecting all potential targets ordered by how far they are from the shooter's vantage
		Collection <HitBoxable> potentialTargets = new ArrayList<>();
		enemies.forEach(enemy ->{
			 HitBoxable enemyHitbox = enemy.getBody();
			 potentialTargets.add(enemyHitbox);
			 hitboxOwnership.put(enemyHitbox, enemy);
		});
		
		// in case you ever want to shoot your partner... for testing purposes
		HitBoxable auxiliaryHitbox = auxiliary.getBody();
		potentialTargets.add(auxiliaryHitbox);
		hitboxOwnership.put(auxiliaryHitbox, auxiliary);
		
		// calculating the location of the proper target
		PrecisePoint3 refinedTarget = refineTargetForPlayer(initialTarget,potentialTargets);
		
		// for as many targetsHits as the shooter's bullet can pierce, have the shooter damage them
		List<HitBoxable> targetsHit = actionMaker.calculateTargetsHit(shooterVantage, refinedTarget, potentialTargets);
		targetsHit.forEach(hitbox ->{
			SoldierBattle victim = hitboxOwnership.get(hitbox);
			shooter.damageUsingMainWeapon(victim);
		});
	}
	
	/**
	 * @param The 2-D coordinates of the target as initially
	 * supplied by the shooter
	 * @return The 3-D coordinates of the target. All dimensions
	 * have been modified by the TacticalInfoGatherer to a 3-D coordinate
	 * that is EXPECTED VISUALLY by the player
	 * @edgecase The player aims at a region where two enemy soldiers overlap. 
	 * The player may end up aiming at the enemy that is overlapped rather than
	 * the foremost overlapping one, which is unexpected. Deal with it. 
	 */
	private PrecisePoint3 refineTargetForPlayer(PrecisePoint initialTarget,Collection<HitBoxable> potentialTargets){
		
		/*
		 * by default, if the initialTarget does not collide with any potentials, 
		 * the refinedTarget becomes the initialTarget
		 */
		PrecisePoint3 refinedTarget = new PrecisePoint3(initialTarget.x,initialTarget.y,0);

		searchForOverLap:
		for(HitBoxable target : potentialTargets){
			PrecisePoint targetBottomLeftCorner = target.getBottomLeftCorner();
			MyVector3 targetDimensions = target.getSides();
			
			// determine if initialTarget overlaps the 2-D projection of the hitbox
			if(Util.inBounds(initialTarget
							, targetBottomLeftCorner.x
							/*
							 *  this is getZ because the hitbox's height should be
							 *   the animation box's height
							 */
							, targetBottomLeftCorner.y + targetDimensions.getZ()
							, targetBottomLeftCorner.x + targetDimensions.getX()
							, targetBottomLeftCorner.y)){
				
				/*
				 * the difference between the location of
				 * of initialTarget.y and the bottom of hitbox
				 * (which is equivalent to the bottom of the animation box)
				 * is the zHeight
				 */
				float zHeight = Math.abs(initialTarget.y - targetBottomLeftCorner.y);
				refinedTarget.set(targetBottomLeftCorner.x + targetDimensions.getX()/2
						,targetBottomLeftCorner.y +targetDimensions.getY()/2
						,zHeight);
				
				break searchForOverLap;
			}
		}
		return refinedTarget;
	}
	
	@Override
	public boolean canSee(PrecisePoint3 observer,PrecisePoint3 target) {
		return infoGatherer.see(observer,target);
	}
	
	@Override
	public Path findPath(PrecisePoint start,PrecisePoint target,int maxDistance) {
		return infoGatherer.findPath(start,target,maxDistance);
	}
	
}