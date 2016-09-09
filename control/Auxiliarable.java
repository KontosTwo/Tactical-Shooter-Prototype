package com.mygdx.control;

public interface Auxiliarable 
{
	public void aMoveTo(double x,double y);
	public void aAttackMoveTo(double x,double y);
	public void aFollow(PlayerControllable c);
	public void aTurn(double x,double y);
	
}
