package com.mygdx.physics;

public class Util {
	public static boolean inBounds(PrecisePoint point,double left,double top,double right,double bottom){
		return point.x > left && point.x < right && point.y > bottom && point.y < top;
	}
	
	/**
	 * @return the number if it's positive or zero if it's negative
	 */
	public static int positiveOrZero(int number){
		return number > 0 ? number : 0;
	}
	
	public static double atLeastOne(double number){
		return number > 0 ? number : 1;
	}
}
