package com.mygdx.physics;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.debug.Debugger;
import com.mygdx.map.GameMap.HitBoxable;
import com.mygdx.misc.Pair;
import com.sun.javafx.geom.Line2D;

/**
 * A representation of a vector. Has both a 3D point and ray. Supports operations
 * that are used in computing line of sight/penetration.
 */
public class VectorEquation{
	private float ax;
	private float ay;
	private float az;
	private float vx;
	private float vy;
	private float vz;

	private VectorEquation(){
		
	}
	
	public VectorEquation(float x1,float y1,float z1,float x2,float y2,float z2){
		setOrigin( x1, y1, z1);
		setRay( x2-x1, y2-y1, z2-z1);
	}
	
	public VectorEquation(PrecisePoint3 start,PrecisePoint3 end){
		setOrigin(start.x,start.y,start.z);
		setRay(end.x - start.x, end.y - start.y, end.z - start.z);
	}
	
	public void setOrigin(float ax,float ay,float az){
		this.ax = ax;
		this.ay = ay;
		this.az = az;
	}
	
	public void setOrigin(PrecisePoint3 v){
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
	public VectorEquation createUnitVector(){
		float length = (float) Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2) + Math.pow(vz, 2));
		VectorEquation vectorEq = new VectorEquation();
		vectorEq.setOrigin(ax,ay,az);
		vectorEq.setRay(vx/length,vy/length,vz/length);
		return vectorEq;
	}
	
	public void randomRotateXYAxis(float angle){
		
	}
	
	public void randomRotateZAxis(float angle){
		
	}
	
	//private float getAngle(){
		
	//}
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
	private float getYAtX(float x){
		return (ay + ((x-ax)/vx)*vy);
	}
	public float getZFromXOrY(float x){
		return (az + ((x-ax)/vx)*vz);
	}
	
	public Collection<PrecisePoint3> getIntersectionWithBox(HitBoxable obstacle){
		Collection<PrecisePoint> intersections2D =  getIntersectionWithSquare(obstacle);
		Collection<PrecisePoint3> intersections3D = new ArrayList<>(intersections2D.size());
		float obstacleHeight = obstacle.getSides().getZ();
		intersections2D.forEach(point2D ->{
			/*
			 *  only add the point if the height of the obstacle is higher than the vector's
			 *  z value at the intersection. 
			 */
			if(obstacleHeight > getZFromXOrY(point2D.x)){
				intersections3D.add(new PrecisePoint3(point2D.x,point2D.y,getZFromXOrY(point2D.x)));
			}
		});
		return intersections3D;
	}
	/**
	 * @return the intersection of this ray with the 2-D projection of a hitbox
	 */
	public Collection<PrecisePoint> getIntersectionWithSquare(HitBoxable obstacle){
		return getIntersectionWithSquare(
				obstacle.getBottomLeftCorner().x, 
				obstacle.getBottomLeftCorner().x + obstacle.getSides().getX(), 
				obstacle.getBottomLeftCorner().y,
				obstacle.getBottomLeftCorner().y + obstacle.getSides().getY()
		);
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
		
		/*
		 * next, check each side for intersection with ray
		 */
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
	
	public String toString()
	{
		return "Origin: (" + ax + ", " + ay + ", " + az + ") || Vector: (" + vx +  ", " + vy + ", " +  vz + ")";
	}
}
