package com.mygdx.entity.coordinator;

import com.mygdx.control.PlayerControllable;
import com.mygdx.entity.soldier.InteractionSoldierBattle;
import com.mygdx.entity.soldier.InteractionSoldierBattle.TacticalAction;
import com.mygdx.entity.soldier.InteractionSoldierBattle.TacticalInfoGatherer;
import com.mygdx.map.TileGameMap;
import com.mygdx.map.TileGameMap.Collidable;
import com.mygdx.map.TileGameMap.RayBlockable;
import com.mygdx.physics.MovableBox;

public class EntityListener implements TacticalAction,TacticalInfoGatherer
{
	private final InteractionSoldierBattle soldierManager;
	private final TileGameMap gameMap;
	
	public interface Hurtboxable extends RayBlockable{
		public void hurt();
	}
	
	public interface SightBlockable{
		// for smoke grenades
	}
	
	public EntityListener()
	{
		soldierManager = new InteractionSoldierBattle(this,this);
		gameMap = new TileGameMap();
	}
	public PlayerControllable createPlayer(int x,int y)
	{
		PlayerControllable player = soldierManager.createPlayer(x, y);
		gameMap.treatAsCollidable((Collidable)player);
		return player;
	}

	@Override
	public boolean see(int x1,int y1,int z1,int x2,int y2,int z2) {
		return gameMap.raytracePossible(x1, y1, z1, x2, y2, z2);
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
}
