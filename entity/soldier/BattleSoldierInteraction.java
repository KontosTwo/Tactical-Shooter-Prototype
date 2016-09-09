package com.mygdx.entity.soldier;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.physics.PrecisePoint;

public class BattleSoldierInteraction 
{
	private SoldierBattle player;
	private SoldierBattle auxiliary;
	private List<SoldierBattle> enemy;
	
	public BattleSoldierInteraction()
	{
		enemy = new LinkedList<>();
	}
	
	public void update(float dt)
	{
		soldierScanBattleField();
	}
	
	private void soldierScanBattleField()
	{
		
	}
}
