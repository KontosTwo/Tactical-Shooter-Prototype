package com.mygdx.handler;

public interface Auxiliarable 
{
	public void aMoveTo(double x,double y);
	public void aAttackMoveTo(double x,double y);
	public void aFollow(Controllable c);
	public void aTurn(double x,double y);
	
}
