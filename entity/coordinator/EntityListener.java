package com.mygdx.entity.coordinator;

import com.mygdx.entity.soldier.InteractionSoldierBattle;
import com.mygdx.entity.soldier.TacticalAction;
import com.mygdx.entity.soldier.TacticalInfoGatherer;
import com.mygdx.graphic.MapRenderer;
import com.mygdx.map.GameMap;
import com.mygdx.physics.MovableBox;

public class EntityListener implements TacticalAction,TacticalInfoGatherer
{
	private final InteractionSoldierBattle soldierManager;
	private GameMap gameMap;
	
	
	public EntityListener(InteractionSoldierBattle isb)
	{
		soldierManager = isb;
		gameMap = new GameMap();
	}

	@Override
	public boolean see(MovableBox observer, MovableBox target) {
		// TODO Auto-generated method stub
		return false;
	}
	public void update(float dt)
	{
		gameMap.update();
		soldierManager.update(dt);
	}
	public void render()
	{
		soldierManager.render();
	}

	public void loadLevel(int level, MapRenderer mr) 
	{
		gameMap.loadLevel(level, mr);
		
	}
}
