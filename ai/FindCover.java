package com.mygdx.ai;

import java.util.PriorityQueue;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.misc.Point;
import com.mygdx.misc.Tuple;

class FindCover
{
	/*private PathTo pathTo;
	private boolean coverFound;
	
	public FindCover(Humanoid h) 
	{
		super(h);
		retrace();
	}

	@Override
	public void reset() 
	{
		start();
		retrace();
	}
	
	private void retrace()
	{
		Tuple<Boolean,Vector2> coverData = humanoid.findCover();
		coverFound = coverData.x;
		this.pathTo = new PathTo(humanoid,(int)coverData.y.x,(int)coverData.y.y);
	}

	@Override
	public void act() 
	{
		if(!coverFound)
		{
			fail();
		}
		else
		{
			if(pathTo.isSuccess())
			{
				succeed();
			}
			else if(pathTo.isFailure())
			{
				fail();
			}
			else
			{
				pathTo.act();
			}
		}
	}
	
	public void start()
	{
		super.start();
		this.pathTo.start();
	}*/
}
