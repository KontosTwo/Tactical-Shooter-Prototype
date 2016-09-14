package com.mygdx.entity.soldier;

import com.mygdx.physics.MovableBox;

public interface TacticalInfoGatherer 
{
	public boolean see(MovableBox observer,MovableBox target);
	
}
