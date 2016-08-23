package com.mygdx.ai.leaf;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.ai.functional.RoutineSequencialable;
import com.mygdx.script.Sequencialable;


 class MoveTo implements RoutineSequencialable
{
	/*
	 * this routine will orient the actor to the destination upon start()
	 * this routine will continue until the actor has arrived at the destination
	 * the actor must NOT reorient itself, otherwise  finishMoveTo would never return true and the
	 * actor will travel forever. 
	 * as a result, the actor is ASSUMED TO NOT BE AFFECTED BY COLLISION
	 * In summary, the actor will move from point to point in a preferably straight line until it reaches the destination. Implement accordingly
	 * For the purpose of this game's mechanics, MoveTo must be private. Since it only defines movement
	 * from point A to point B, PathTo must exist to ensure that point A and point B are valid starting and ending points
	 * always succeeds
	 */
	private float destX;
	private float destY;
	private final MoveToable actor;
	
	MoveTo(MoveToable m)
	{
		this.actor = m;
	}
	void designateDestination(float x,float y)
	{
		destX = x;
		destY = y;
	}
    List<MoveTo> duplicate(int times)
	{
		List<MoveTo> ret = new ArrayList<MoveTo>();
		for(int i = 0; i < times; i ++)
		{
			ret.add(new MoveTo(actor));
		}
		return ret;
	}

	@Override
	public void startSequence() 
	{
		actor.beginMoveTo(destX, destY);
		System.out.println("Start MoveTo to " +  destX + " " + destY);
	}
	@Override
	public void update(float dt) 
	{
		
	}
	@Override
	public boolean sequenceIsComplete() 
	{
		return actor.finishMoveTo(destX, destY);
	}
	@Override
	public void completeSequence() 
	{
		actor.completeMoveTo();
	}
	@Override
	public void cancelSequence() 
	{
		actor.stopMoveTo();
	}
	@Override
	public boolean succeeded() 
	{
		return  actor.finishMoveTo(destX, destY);
	}
	@Override
	public boolean failed() 
	{
		/*
		 * only outside events such as cancelSequence can cause moveTo
		 * to fail, this this method always returns false
		 */
		return false;
	}
	interface MoveToable
	{
		public void beginMoveTo(double x,double y );//orient the actor so that it moves in the direction of x,y, and set it into motion. The actor's own update() methods should take care of its movement
		public boolean finishMoveTo(double x,double y); // whether the actor has arrived at its destination
		public void completeMoveTo(); // action to take after arriving at the destination. Resets things
		public void stopMoveTo(); // just stop the actor's velocity. May or may not be the exact same as compelteMoveTo
	}
	@Override
	public boolean instaSucceeded() 
	{
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean instaFailed() 
	{
		// TODO Auto-generated method stub
		return false;
	}
}

