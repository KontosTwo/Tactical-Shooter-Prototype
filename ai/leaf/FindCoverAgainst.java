package com.mygdx.ai.leaf;

import java.util.PriorityQueue;

import com.mygdx.misc.Point;

class FindCoverAgainst
{
	/*private PathTo pathTo;
	
	public FindCoverAgainst(Humanoid h) 
	{
		super(h);
		retrace();
		/*PriorityQueue <Point> coverList = h.findCover();
		boolean found = false;
		Point dest = new Point();
		while(coverList.peek() != null && !found)
		{
			Point p = coverList.poll();
			//System.out.println(p.getX()/32 + " " + p.getY()/32 + " " + h.judgeCover(foe, p.getX(), p.getY()));
			if(h.judgeCover(foe, p.getX(), p.getY()))
			{
				found = true;
				dest.set(p);
			}
		}
		if(!found)
		{
			dest.set(h.getCenterAsPoint());
		}
		//System.out.println(dest);
		this.pathTo = new PathTo(h,dest.getX(),dest.getY());
	}

	@Override
	public void reset() 
	{
		start();
		retrace();
	}
	
	private void retrace()
	{
		PriorityQueue <Point> coverList = humanoid.findCover();
		boolean found = false;
		Point dest = new Point();
		while(coverList.peek() != null && !found)
		{
			Point p = coverList.poll();
			//if(humanoid.judgeCoverAdequate(getTargetPosition(humanoid), p.getX(), p.getY()))
			{
				found = true;
				dest.set(p);
			}
		}
		if(!found)
		{
			dest.set(humanoid.getCenterAsPoint());
		}
		//System.out.println(dest);
		this.pathTo = new PathTo(humanoid,dest.getX(),dest.getY());
	}

	@Override
	public void act() 
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
	
	public void start()
	{
		super.start();
		this.pathTo.start();
	}*/

}
