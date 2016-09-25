package com.mygdx.map;

import java.util.function.Consumer;

import com.mygdx.physics.Point;

/**
 * A collection of relative coordinate from an origin 
 * x, y. Useful for iterating through a coordinate's 
 * adjacent coordinates. 
 */
 enum CoordinateDirection {
	UP(0,1),
	UPRIGHT(1,1),
	RIGHT(1,0),
	DOWNRIGHT(1,-1),
	DOWN(0,-1),
	DOWNLEFT(-1,-1),
	LEFT(-1,0),
	UPLEFT(-1,1);
	
	private Point vector;
	
	private CoordinateDirection(int x,int y){
		vector = new Point(x,y);
	}
	
	 static void iterateAdjacent(Consumer<Point> function){
		for(CoordinateDirection direction : CoordinateDirection.values()){
			function.accept(direction.vector);
		}
	}
}
