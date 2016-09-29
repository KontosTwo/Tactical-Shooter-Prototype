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
	public void set(PrecisePoint3 other){
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}
}
