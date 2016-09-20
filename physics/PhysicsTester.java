package com.mygdx.physics;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Test;

import com.mygdx.misc.Pair;
import com.sun.javafx.geom.Line2D;

public class PhysicsTester {
	float ay = 0;
	float ax = 0;
	float vx = 1000;
	float vy = 1000;
	
	@Test
	public void test() 
	{
		HashSet<PrecisePoint> result = getIntersectionWithSquare(0,100,0,100);
		System.out.println(result.toString());
	}
	private float getXAtY(float y)
	{
		return (ax + ((y-ay)/vy)*vx);
	}

	private float getYAtX(float x)
	{
		return (ay + ((x-ax)/vx)*vy);
	}
	/**
	 * 
	 * @param left
	 * @param right
	 * @param bottom
	 * @param top
	 * @return
	 */
	public HashSet<PrecisePoint> getIntersectionWithSquare(float left,float right,float bottom,float top){
		HashSet<PrecisePoint> result = new HashSet<PrecisePoint>(4);
		float topX = getXAtY(top);
		if(topX >= left && topX <= right){
			result.add(new PrecisePoint(topX,top));
		}
		float bottomX = getXAtY(bottom);
		if(bottomX >= left && bottomX <= right){
			result.add(new PrecisePoint(bottomX,bottom));
		}
		float rightY = getYAtX(right);
		if(rightY >= bottom && rightY <= top){
			result.add(new PrecisePoint(right,rightY));
		}
		float leftY = getYAtX(left);
		if(leftY >= bottom && leftY <= top){
			result.add(new PrecisePoint(left,leftY));
		}		
		return result;
	}
}
