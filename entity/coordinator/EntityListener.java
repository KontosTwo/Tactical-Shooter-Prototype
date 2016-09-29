package com.mygdx.entity.coordinator;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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

public class EntityListener implements TacticalAction,TacticalInfoGatherer
{
	private final InteractionSoldierBattle soldierManager;
	private final GameMap gameMap;
	
	public interface SightBlockable{
		// for smoke grenades
	}
	
	public EntityListener()
	{
		soldierManager = new InteractionSoldierBattle(this,this);
		gameMap = new GameMap();
	}
	public PlayerControllable createPlayer(int x,int y)
	{
		PlayerControllable player = soldierManager.createPlayer(x, y);
		gameMap.treatAsCollidable((Collidable)player);
		return player;
	}
	public Auxiliarable createProtector(int x, int y) {
		Auxiliarable aux = soldierManager.createAuxiliary(x, y);
		return aux;
	}

	public void update(float dt)
	{
		soldierManager.update(dt);
		gameMap.update();
	}
	public void render()
	{
		soldierManager.render();
		gameMap.render();
	}

	public void loadLevel(int level) 
	{
		gameMap.loadLevel(level);
		
	}
	@Override
	public Path findPath(PrecisePoint start,PrecisePoint target,int maxDistance) {
		return gameMap.findPath(start,target,maxDistance);
	}

	@Override
	public boolean see(PrecisePoint3 observer,PrecisePoint3 target) {
		return gameMap.canSee(observer,target);
	}

}
