package com.mygdx.control;

import com.mygdx.physics.PrecisePoint;

public interface Auxiliarable 
{
	public void aMoveTo(PrecisePoint target);
	public void aAttackMoveTo(PrecisePoint target);
	public void aFollow(PlayerControllable c);
	public void aTurn(PrecisePoint target);
	
}
