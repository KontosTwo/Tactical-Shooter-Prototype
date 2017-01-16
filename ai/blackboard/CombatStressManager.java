package com.mygdx.ai.blackboard;

import java.util.HashSet;
import java.util.List;


public class CombatStressManager
{
	private static final int GRENADED = 300;
	private static final int LANDMINED = 300;
	private static final int ALLYWOUNDED = 100;
	private static final int ALLYKILLED = 100;
	private static final int OUTFLANKED = 200;
	private static final int SUPPRESSIVESHOT = 50;
	private static final int DIRECTSHOT = 100;
	private static final int SUCCESSIVELYSHOT = 150;

	
	private static final int DEFAULTDUCKTHRESHOLD = 5;
	private static final int DEFAULTRETREATTHRESHOLD = 10;
	
	private int duckThreshold;
	private int retreatThreshold;
	
	private int stress;
	
	public CombatStressManager()
	{
		stress = 0;
		duckThreshold = DEFAULTDUCKTHRESHOLD;
		retreatThreshold = DEFAULTRETREATTHRESHOLD;
	}

	public void update(float dt) 
	{
		if(stress > 0)
		{
			stress --;
		}
	}
	
	public boolean mustRetreat()
	{
		return stress > retreatThreshold;
	}
	
	public boolean mustDuck()
	{
		return stress > duckThreshold;
	}
	
	public void grenaded()
	{
		stress += GRENADED;
	}
	
	public void landmined()
	{
		stress += LANDMINED;
	}
	
	public void allyWounded()
	{
		stress += ALLYWOUNDED;
	}
	
	public void allyKilled()
	{
		stress += ALLYKILLED;
	}
	
	public void outflanked()
	{
		stress += OUTFLANKED;
	}
	
	public void suppressiveShot()
	{
		stress += SUPPRESSIVESHOT;
	}
	
	public void directShot()
	{
		stress += DIRECTSHOT;
	}
	
	public void successivelyShot()
	{
		stress += SUCCESSIVELYSHOT;
	}
}