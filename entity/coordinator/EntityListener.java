package com.mygdx.entity.coordinator;

import com.mygdx.control.PlayerControllable;
import com.mygdx.entity.soldier.InteractionSoldierBattle;
import com.mygdx.entity.soldier.InteractionSoldierBattle.TacticalAction;
import com.mygdx.entity.soldier.InteractionSoldierBattle.TacticalInfoGatherer;
import com.mygdx.graphic.MapRenderer;
import com.mygdx.map.GameMap;
import com.mygdx.map.TileGameMap;
import com.mygdx.map.TileGameMap.Collidable;
import com.mygdx.map.TileGameMap.RayBlockable;
import com.mygdx.physics.MovableBox;
import com.mygdx.physics.MyVector3;
import com.mygdx.physics.PrecisePoint;

public class EntityListener implements TacticalAction,TacticalInfoGatherer
{
	private final InteractionSoldierBattle soldierManager;
	private final TileGameMap gameMap;
	
	public interface Hurtboxable extends RayBlockable
	{
		public void hurt();
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
	public boolean see(MovableBox observer, MovableBox target) {
		return false;
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
