package com.mygdx.entity.soldier;

import java.util.LinkedList;
import java.util.List;

import com.mygdx.control.PlayerControllable;
import com.mygdx.entity.soldier.SoldierBattle.SoldierBattleMediator;
import com.mygdx.physics.MovableBox;
import com.mygdx.physics.PrecisePoint;

public class InteractionSoldierBattle implements SoldierBattleMediator
{
	private SoldierBattle player;
	//private final SoldierBattle auxiliary;
	private final List<SoldierBattle> enemies;
	private final List<SoldierBattle> enemiesInactive;
	private final TacticalAction actionMaker;
	private final TacticalInfoGatherer infoGatherer;
	
	public interface TacticalInfoGatherer 
	{
		public boolean see(MovableBox observer,MovableBox target);
		
	}
	public interface TacticalAction 
	{
		
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
		SoldierBattleControlled newPlayer = SoldierBattleControlled.createControlledProtector(this);
		player = newPlayer;
		player.soldierBattleState.center.teleportTo(x,y);
		return newPlayer;
	}
	public void render()
	{
		
		player.render();
		//auxiliary.render();
		enemies.forEach(e -> e.render());
	}
	public void update(float dt)
	{
		updateSoldiers(dt);
		soldierScanBattleField();
	}
	
	private void updateSoldiers(float dt)
	{
		player.update(dt);
		//auxiliary.update(dt);
		enemies.forEach(e -> update(dt));
	}
	
	private void soldierScanBattleField()
	{
		enemies.forEach(e -> 
		{
			player.scanFor(e);
			//auxiliary.scanFor(e);
			e.scanFor(player);
			//e.scanFor(auxiliary);
		});
	}

	@Override
	public void shoot(SoldierBattle shooter, float accuracy, int xTarget,
			int yTarget, int zTarget) {
		
	}

	@Override
	public boolean see(MovableBox observer, MovableBox target) {
		return infoGatherer.see(observer, target);
	}
}
