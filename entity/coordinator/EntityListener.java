package com.mygdx.entity.coordinator;

import java.util.LinkedList;
import java.util.List;

import com.mygdx.control.Auxiliarable;
import com.mygdx.control.PlayerControllable;
import com.mygdx.entity.soldier.InteractionSoldierBattle;
import com.mygdx.entity.soldier.InteractionSoldierBattle.TacticalAction;
import com.mygdx.entity.soldier.InteractionSoldierBattle.TacticalInfoGatherer;
import com.mygdx.map.GameMap;
import com.mygdx.map.GameMap.Collidable;
import com.mygdx.map.GameMap.RayBlockable;
import com.mygdx.map.Path;
import com.mygdx.misc.Pair;
import com.mygdx.physics.MovableBox;

public class EntityListener implements TacticalAction,TacticalInfoGatherer
{
	private final InteractionSoldierBattle soldierManager;
	private final GameMap gameMap;
	
	public interface Hurtboxable extends RayBlockable{
		public void hurt();
	}
	
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

	@Override
	public boolean see(int x1,int y1,int z1,int x2,int y2,int z2) {
		return gameMap.canSee(x1, y1, z1, x2, y2, z2);
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
	public Path findPath(int sx, int sy, int tx,
			int ty,int maxDistance) {
		return gameMap.findPath(sx, sy, tx, ty,maxDistance);
	}
	
}
