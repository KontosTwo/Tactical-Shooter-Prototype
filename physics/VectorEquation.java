package com.mygdx.physics;

import java.util.HashSet;

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
	public PrecisePoint get2DOrigin(){
		return new PrecisePoint(ax,ay);
	}
	/**
	 * Precondition: the intersection between
	 * this vector and the vertical line Y = y
	 * exists
	 */
	private float getXAtY(float y)
	{
		return (ax + ((y-ay)/vy)*vx);
	}
	/**
	 * Precondition: the intersection between
	 * this vector and the horizontal line X = x
	 * exists
	 */
	private float getYAtX(float x)
	{
		return (ay + ((x-ax)/vx)*vy);
	}
	public float getZFromXOrY(float x)
	{
		return (az + ((x-ax)/vx)*vz);
	}
	
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
