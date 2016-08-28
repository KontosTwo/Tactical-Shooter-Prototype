package com.mygdx.entity.soldier;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.misc.PrecisePoint;

public class BattleSoldierManager 
{
	private SoldierBattle player;
	private SoldierBattle auxiliary;
	private List<SoldierBattle> enemy;
	
	public BattleSoldierManager()
	{
		enemy = new LinkedList<>();
	}
	
	public void createPlayer(float x,float y)
	{
		player = SoldierBattle.createProtector(new PrecisePoint(x,y));
	}
	public void createAuxiliary()
	{
		
	}
	public void update(float dt)
	{
		soldierScanBattleField();
	}
	
	private void soldierScanBattleField()
	{
		enemy.forEach(foe -> 
		{
			if(foe.see(player))
			{
				foe.spotEnemy(player);
			}
			if(foe.see(auxiliary))
			{
				foe.spotEnemy(auxiliary);
			}
		});
	}
}
