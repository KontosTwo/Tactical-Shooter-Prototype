package com.mygdx.ai.leaf;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.ai.functional.Routineable;
import com.mygdx.physics.PrecisePoint;

/**
 * @Succeeds when actor has reached destination
 */
final class MoveTo implements Routineable{

	private PrecisePoint destination;
	private final MoveToable actor;
	
	MoveTo(MoveToable m){
		this.actor = m;
		destination = new PrecisePoint();
	}
	
	void designateDestination(float x,float y){
		destination.set(x, y);
	}
	
    List<MoveTo> duplicate(int times){
		List<MoveTo> ret = new ArrayList<MoveTo>();
		for(int i = 0; i < times; i ++){
			ret.add(new MoveTo(actor));
		}
		return ret;
	}

	@Override
	public void startRoutine() {
		actor.beginMoveTo(destination);
	}
	
	@Override
	public void updateRoutine(float dt) {
		
	}

	@Override
	public void completeRoutine() {
		actor.completeMoveTo();
	}
	
	@Override
	public void cancelRoutine() {
		actor.stopMoveTo();
	}
	
	@Override
	public boolean succeededRoutine() {
		return  actor.finishedMoveTo(destination);
	}
	
	@Override
	public boolean failedRoutine() {
		return false;
	}
	
	@Override
	public boolean instaSucceededRoutine() {
		return false;
	}
	
	@Override
	public boolean instaFailedRoutine() {
		return false;
	}
	
	interface MoveToable{
		public void beginMoveTo(PrecisePoint location);//orient the actor so that it moves in the direction of x,y, and set it into motion. The actor's own update() methods should take care of its movement
		public boolean finishedMoveTo(PrecisePoint location); // whether the actor has arrived at its destination
		public void completeMoveTo(); // action to take after arriving at the destination. Resets things
		public void stopMoveTo(); // just stop the actor's velocity. May or may not be the exact same as compelteMoveTo
	}

	@Override
	public void calculateInstaHeuristic() {
		
	}
}

