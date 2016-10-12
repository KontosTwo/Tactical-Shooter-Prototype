package com.mygdx.physics;

public class PrecisePoint3 {
	public float x;
	public float y;
	public float z;
	
	public PrecisePoint3(){
		
	}
	public PrecisePoint3(float x,float y,float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public PrecisePoint3(PrecisePoint3 other){
		this(other.x,other.y,other.z);
	}
	
	public void set(float x,float y,float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public void set(PrecisePoint3 other){
		set(other.x,other.y,other.z);
	}
	public PrecisePoint create2DProjection(){
		return new PrecisePoint(x,y);
	}
	public String toString(){
		return "" + x + " " + y + " " + z;
	}
	public static PrecisePoint3 createMidpointof(PrecisePoint3 first,PrecisePoint3 second){
		return new PrecisePoint3((first.x + second.x)/2,(first.y + second.y)/2,(first.z + second.z)/2);
	}
	public boolean equals(Object other){
		if(other == null){
			return false;
		}
		if(!(other instanceof PrecisePoint3)){
			return false;
		}
		if((Math.abs(((PrecisePoint3)other).x - this.x) < .001) &&
				Math.abs(((PrecisePoint3)other).y - this.y) < .001 &&
				Math.abs(((PrecisePoint3)other).z - this.z) < .001){
			return true;
		}
		return false;
	}
	public int hashCode(){
		int hash = 7;
	    hash = (int) (71 * hash + this.x);
	    hash = (int) (71 * hash + this.y);
	    hash = (int) (71 * hash + this.z);
	    return hash;
	}
}
