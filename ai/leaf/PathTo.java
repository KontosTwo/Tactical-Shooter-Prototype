package com.mygdx.ai.leaf;

import java.util.List;

import com.mygdx.ai.functional.Routineable;
import com.mygdx.ai.functional.Sequence;
import com.mygdx.ai.leaf.MoveTo.MoveToable;
import com.mygdx.map.Path;
import com.mygdx.physics.PrecisePoint;
import com.mygdx.script.Scripter.Sequencialable;
/**
 * WARNING
 * WARNING
 * WARNING
 * 
 * Never put pathTo into a Selector, Survival, or Sequence
 * due to how PathTo calculates instaSucceed/Fail, 
 * 
 * @Instafails if the path is not possible
 * @Succeeds once the path has been completed
 * @author Vincent Li
 */
final class PathTo implements Routineable{
	
	private final MoveTo moveTo;
	private final PathToable actor;
	private Sequence sequenceForPath;
	private PrecisePoint destination;
	private Path pathHeuristic;
	
	PathTo(PathToable p,MoveTo mt) {
		actor = p;
		moveTo = mt;
		destination = new PrecisePoint();
	}
	
	void designateDestination(PrecisePoint target){
		destination.set(target);
	}
	
	@Override
	public void startRoutine() {
		List<PrecisePoint> pathPoint = pathHeuristic.getPath();	
		List <MoveTo> pathMoveTo = moveTo.duplicate(pathPoint.size());
		for(int i = 0; i < pathPoint.size(); i ++){
			pathMoveTo.get(i).designateDestination(pathPoint.get(i).x, pathPoint.get(i).y);
		}
		sequenceForPath = new Sequence(pathMoveTo);
		sequenceForPath.startRoutine();	
	}
	
	@Override
	public void updateRoutine(float dt) {
		sequenceForPath.updateRoutine(dt);
	}
	
	@Override
	public void completeRoutine() {
		sequenceForPath.completeRoutine();
		actor.completePathTo();
	}
	
	@Override
	public void cancelRoutine(){
		sequenceForPath.cancelRoutine();
	}
	
	@Override
	public boolean succeededRoutine() {
		return sequenceForPath.succeededRoutine();
	}
	
	@Override
	public boolean failedRoutine() {
		return sequenceForPath.failedRoutine();
	}

	@Override
	public void calculateInstaHeuristic() {
		pathHeuristic = actor.calculatePath(destination);
	}

	@Override
	public boolean instaSucceededRoutine() {
		return pathHeuristic.alreadyAtDestionation();
	}
	
	@Override
	public boolean instaFailedRoutine() {
		return !pathHeuristic.pathIsPossible();
	}
	
	interface PathToable extends MoveToable{	
		// provide  an ordered set of points. It is up to the implementing class to ensure that the points are valid and sequential.
		public void completePathTo();// execute once all moveTos have completed
		public Path calculatePath(PrecisePoint destination);
	}
}

