package com.mygdx.physics;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

import com.mygdx.misc.Pair;
import com.sun.javafx.geom.Line2D;

public class PhysicsTester {
	float ax = 0;
	float ay = 0;
	float vx = 0;
	float vy = 100;
	
	@Test
	public void test() 
	{
		VectorEquation vector = new VectorEquation(0,0,0,0,-3,-4);
		System.out.println(vector.createUnitVector());
		//assertTrue((getIntersectionWithSquare(-50,50,0,300).size() == 0));
	}
	

	/**
	 * @return the intersection of this ray with the orthogonal square bounded
	 * by the four parameters
	 */
	public Collection<PrecisePoint> getIntersectionWithSquare(float left,float right,float bottom,float top){
		
		/*
		 * first, check if the square is too far away from the ray
		 * if a certain side of the square is beyond the vertex of
		 * the perpendicular ray, then the square cannot possibly
		 * within range of the ray
		 */
		
		if(vx > 0){
			if(left > ax + vx){
				return new ArrayList<>();
			}
			if(right < ax){
				return new ArrayList<>();
			}
		}else if(vx < 0){
			if(left > ax){
				return new ArrayList<>();
			}
			if(right < ax + vx){
				return new ArrayList<>();
			}
		}
		
		if(vy > 0){
			if(bottom > ay + vy){
				return new ArrayList<>();
			}
			if(top < ay){
				return new ArrayList<>();
			}
		}else if(vy < 0){
			if(bottom > ay){
				return new ArrayList<>();
			}
			if(top < ay + vy){
				return new ArrayList<>();
			}
		}
		
		Collection<PrecisePoint> result = new HashSet<PrecisePoint>();
		
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
	
	
	private float getXAtY(float y)
	{
		
		
		return ((int)ax + ((y-(int)ay)/(int)vy)*(int)vx);
	}

	private float getYAtX(float x)
	{
		
		
		return ((int)ay + ((x-(int)ax)/(int)vx)*(int)vy);
	}

}
