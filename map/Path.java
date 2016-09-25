package com.mygdx.map;

import java.util.List;

import com.mygdx.physics.Point;


public class Path 
{
	private boolean pathPossible;
	private List<Point> path;
	
	Path(List<Point> p,boolean possible){
		path = p;
		pathPossible = possible;
	}
	
	public boolean pathIsPossible(){
		return pathPossible;
	}
	
}
