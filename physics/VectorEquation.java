package com.mygdx.physics;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.misc.Pair;
import com.sun.javafx.geom.Line2D;

public class VectorEquation{
	private float ax;
	private float ay;
	private float az;
	private float vx;
	private float vy;
	private float vz;
	
	public VectorEquation(float x1,float y1,float z1,float x2,float y2,float z2){
		setOrigin( x1, y1, z1);
		setRay( x2-x1, y2-y1, z2-z1);
	}
	public void setOrigin(float ax,float ay,float az){
		this.ax = ax;
		this.ay = ay;
		this.az = az;
	}
	public void setOrigin(Vector3 v){
		this.ax = v.x;
		this.ay = v.y;
		this.az = v.z;
	}
	public void setRay(float vx,float vy,float vz){
		this.vx = vx;
		this.vy = vy;
		this.vz = vz;
	}
	public void setRay(Vector3 v){
		this.vx = v.x;
		this.vy = v.y;
		this.vz = v.z;
	}
	/**
	 * Assumes that the intersection between
	 * this vector and the vertical line Y = y
	 * exists
	 */
	public float getXAtY(float y)
	{
		return ax + ((y-ay)/vy)*vx;
	}
	/**
	 * Assumes that the intersection between
	 * this vector and the horizontal line X = x
	 * exists
	 */
	public float getYAtX(float x)
	{
		return ay + ((x-ax)/vx)*vy;
	}
	public Pair<Boolean,Pair<PrecisePoint,PrecisePoint>> projection2DIntersectsSquare(float topLeft,float bottomLeft,float bottomRight,float topRight){
		Pair<Boolean,Pair<PrecisePoint,PrecisePoint>> result = new Pair<>();
		result.x = false;
		int intersections = 0;
		
		
		
		if(intersections == 2)
		{
			result.x = true;
		}
		return null;
	}
	/**
	 * Considers only the vector's x and y components. Calculates whether they intersect the vertical line
	 * segment designated by the xBound, y1, and y2. 
	 */

	


}
