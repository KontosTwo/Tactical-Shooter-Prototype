package com.mygdx.ai.leaf;

import java.util.LinkedList;
import java.util.List;

import com.mygdx.ai.functional.RoutineSequencialable;
import com.mygdx.ai.functional.Sequence;
import com.mygdx.ai.leaf.MoveTo.MoveToable;
import com.mygdx.misc.Pair;
import com.mygdx.physics.Point;
import com.mygdx.script.Sequencialable;

final class PathTo implements RoutineSequencialable
{
	/*
	 * succeeds once the actor has reaches its destination
	 * fails if it's not possible to reach the destination
	 */
	private final MoveTo moveTo;
	private final PathToable actor;
	private Sequence path;
	private boolean pathIsPossible;
	private double destX;
	private double destY;
	
	PathTo(PathToable p,MoveTo mt) 
	{
		actor = p;
		moveTo = mt;
		destX = 0; 
		destY = 0;
	}
	void designateDestination(double x,double y)
	{
		destX = x;
		destY = y;
	}
	
	@Override
	public void startSequence() 
	{
		Pair<Boolean,LinkedList<Point>> result = actor.calculatePath(destX, destY);
		LinkedList<Point> pathPoint = result.y;
		pathIsPossible = result.x;
		if(pathIsPossible)
		{
			List <MoveTo> pathMoveTo = moveTo.duplicate(pathPoint.size());
			for(int i = 0; i < pathPoint.size(); i ++)
			{
				pathMoveTo.get(i).designateDestination(pathPoint.get(i).getX(), pathPoint.get(i).getY());
			}
			path = new Sequence(pathMoveTo);
			path.startSequence();
		}
		
	}
	@Override
	public void update(float dt) 
	{
		path.update(dt);
	}
	@Override
	public boolean sequenceIsComplete() 
	{
		return path.sequenceIsComplete() || !pathIsPossible;
	}
	@Override
	public void completeSequence() 
	{
		path.completeSequence();
		actor.completePathTo();
	}
	@Override
	public void cancelSequence()
	{
		path.cancelSequence();
	}
	@Override
	public boolean succeeded() 
	{
		return path.succeeded();
	}
	@Override
	public boolean failed() 
	{
		return path.failed();
	}
	interface PathToable extends MoveToable
	{	
		// provide  an ordered set of points. It is up to the implementing class to ensure that the points are valid and sequential.
		public void completePathTo();// execute once all moveTos have completed
		public Pair<Boolean,LinkedList<Point>> calculatePath(double x,double y);
	}
	@Override
	public boolean instaSucceeded() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean instaFailed() {
		// TODO Auto-generated method stub
		return false;
	}
}

