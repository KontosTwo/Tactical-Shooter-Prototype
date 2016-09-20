package com.mygdx.entity.soldier;

import java.util.LinkedList;
import java.util.List;

import com.mygdx.control.Auxiliarable;
import com.mygdx.control.PlayerControllable;
import com.mygdx.entity.soldier.SoldierBattle.SoldierBattleMediator;
import com.mygdx.misc.Pair;
import com.mygdx.physics.MovableBox;
import com.mygdx.physics.Point;
import com.mygdx.physics.PrecisePoint;

public class InteractionSoldierBattle implements SoldierBattleMediator
{
	private SoldierBattle player;
	private SoldierBattle auxiliary;
	private final List<SoldierBattle> enemies;
	private final List<SoldierBattle> enemiesInactive;
	private final TacticalAction actionMaker;
	private final TacticalInfoGatherer infoGatherer;
	
	public interface TacticalInfoGatherer 
	{
		public boolean see(int x1,int y1,int z1,int x2,int y2,int z2);
		public Pair<Boolean,LinkedList<Point>> findPath(int sx, int sy, int tx, int ty);
		
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
	public void render()
	{
		
		player.render();
		auxiliary.render();
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
		auxiliary.update(dt);
		enemies.forEach(e -> update(dt));
	}
	
	private void soldierScanBattleField()
	{
		enemies.forEach(e -> 
		{
			player.scanFor(e);
			auxiliary.scanFor(e);
			e.scanFor(player);
			e.scanFor(auxiliary);
		});
	}

	@Override
	public void shoot(SoldierBattle shooter, float accuracy, int xTarget,
			int yTarget, int zTarget) {
		
	}

	@Override
	public boolean see(int x1,int y1,int z1,int x2,int y2,int z2) {
		return infoGatherer.see( x1, y1, z1, x2, y2, z2);
	}
	@Override
	public Pair<Boolean, LinkedList<Point>> findPath(int sx, int sy, int tx,
			int ty) {
		return infoGatherer.findPath(sx, sy, tx, ty);
	}
	
}
