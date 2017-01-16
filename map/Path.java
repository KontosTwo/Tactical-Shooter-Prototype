package com.mygdx.map;

import java.util.List;

import com.mygdx.physics.PrecisePoint;


public class Path 
{
	private final boolean pathPossible;
	private final boolean alreadyAtDestination;
	private final List<PrecisePoint> path;
	
	Path(List<PrecisePoint> p,boolean possible,boolean alreadyThere){
		path = p;
		pathPossible = possible;
		alreadyAtDestination = alreadyThere;
	}
	
	public boolean pathIsPossible(){
		return pathPossible;
	}
	
	public List<PrecisePoint> getPath(){
		return path;
	}
	
	public boolean alreadyAtDestionation(){
		return alreadyAtDestination;
	}
}
