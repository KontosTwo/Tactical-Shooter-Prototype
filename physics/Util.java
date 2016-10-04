package com.mygdx.physics;

public class Util {
	public static boolean inBounds(PrecisePoint point,double left,double top,double right,double bottom){
		return point.x > left && point.x < right && point.y > bottom && point.y < top;
	}
}
