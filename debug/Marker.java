package com.mygdx.debug;

import com.mygdx.graphic.Animator;
import com.mygdx.physics.PrecisePoint;

class Marker 
{
	private Animator animator;
	private PrecisePoint center;
	
	Marker(double x,double y,String filepath)
	{
		center = new PrecisePoint(x,y);
		animator = new Animator(center,filepath,"animation/data/static.txt");
		animator.setDimensions(10, 10);
	}
	
	void render()
	{
		animator.render();
	}
	void update(float dt)
	{
		animator.update(dt);
	}
	public boolean equals(Object other){
		if(this == other){
			return true;
		}
		if(other == null){
			return false;
		}
		if(!(other instanceof Marker)){
			return false;
		}
		return this.center.equals(((Marker)other).center);
	}
	public int hashCode(){
		int hash = 1;
        hash = (int) (hash * 17 + center.x);
        hash = (int) (hash * 31 + center.y);
        return hash;
	}
}
