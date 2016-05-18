package com.mygdx.ai;

import com.badlogic.gdx.math.Vector2;


class Wander
{
	/*
	 * for wander, the actor will randomly choose a point to walk to. 
	 
	private Wanderable actor;
	private MoveTo moveTo;
	private double x;
	private double y;
	
	public Wander(Wanderable w) 
    {
		actor = w;
    }	
    @Override
    void start() 
    {
        super.start();
        Vector2 point = actor.selectWanderPoint();
        x = point.x;
        y = point.y;
        moveTo.designateDestination(x, y);
        this.moveTo.start();
    }
    @Override
    public void act() 
    {
    	if(moveTo.isSuccess())
    	{
    		succeed();
    	}
    	else if(moveTo.isFailure())
    	{
    		fail();
    	}
    	else
    	{
    		moveTo.act();
    	}
    }
    void succeed()
    {
    	super.succeed();
    	moveTo.fail();
    }
    void fail()
    {
    	super.fail();
    	moveTo.fail();
    }*/
}
interface Wanderable
{
	public Vector2 selectWanderPoint();
}
