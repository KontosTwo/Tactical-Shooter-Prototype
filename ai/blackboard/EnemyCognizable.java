package com.mygdx.ai.blackboard;

public interface EnemyCognizable 
{
	public boolean see(double x,double y,double z);
	public double proximityFrom(double x,double y);
	public boolean verifyEnemyMarkerAt(double x,double y);
}
