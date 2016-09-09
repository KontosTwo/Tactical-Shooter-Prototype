package com.mygdx.entity.soldier;

import com.mygdx.control.PlayerControllable;

public class SoldierFactory 
{
	public static PlayerControllable createControllableSoldier()
	{
		return new SoldierBattleControlled(SoldierBattleState.createProtectorState());
		
	}
}
