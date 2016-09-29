package com.mygdx.entity.soldier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.mygdx.control.Auxiliarable;
import com.mygdx.control.PlayerControllable;
import com.mygdx.entity.soldier.SoldierBattle.SoldierBattleMediator;
import com.mygdx.map.GameMap.HitBoxable;
import com.mygdx.map.Path;
import com.mygdx.physics.PrecisePoint;
import com.mygdx.physics.PrecisePoint3;

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
		enemies.forEach(e -> update(dt));
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
	public void shootForPlayer(SoldierBattle shooter, float accuracy, PrecisePoint initialTarget) {
		
		// collecting all potential targets
		Collection <HitBoxable> potentialTargets = new ArrayList<>(enemies.size() + 1);
		enemies.forEach(e -> potentialTargets.add(e.getBody()));
		
		// in case you ever want to shoot your partner... for testing purposes
		potentialTargets.add(auxiliary.getBody());
		
		// calculating the location of the proper target
		PrecisePoint3 refinedTarget = refineTargetForPlayer(initialTarget,potentialTargets);
		
		
	}
	
	/**
	 * @param The 2-D coordinates of the target as initially
	 * supplied by the shooter
	 * @return The 3-D coordinates of the target. All dimensions
	 * have been modified by the TacticalInfoGatherer to a 3-D coordinate
	 * that is EXPECTED VISUALLY by the player
	 * @edgecase The player aims at a region where two enemy soldiers overlap. 
	 * The player may end up aiming at the enemy that is overlapped rather than
	 *  the foremost overlapping one.
	 */
	private PrecisePoint3 refineTargetForPlayer(PrecisePoint initialTarget,Collection<HitBoxable> potentialTargets){
		PrecisePoint3 refinedTarget = new PrecisePoint3();
		for(HitBoxable target : potentialTargets){
			
		}
		
		
		return refinedTarget;
	}
	
	@Override
	public boolean see(PrecisePoint3 observer,PrecisePoint3 target) {
		return infoGatherer.see(observer,target);
	}
	
	@Override
	public Path findPath(PrecisePoint start,PrecisePoint target,int maxDistance) {
		return infoGatherer.findPath(start,target,maxDistance);
	}
	
}
