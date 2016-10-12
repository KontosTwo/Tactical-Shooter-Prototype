package com.mygdx.ai.leaf;


import com.mygdx.ai.functional.Routineable;
import com.mygdx.ai.leaf.MoveTo.MoveToable;
import com.mygdx.ai.leaf.PathTo.PathToable;
import com.mygdx.ai.leaf.Shoot.Shootable;
import com.mygdx.physics.PrecisePoint;
import com.mygdx.physics.PrecisePoint3;
import com.mygdx.script.Scripter.Sequencialable;


abstract public class RoutineFactory {
	 
	public static Sequencialable createSequencialablePathTo(PathToable pathToActor,PrecisePoint target){
    	 PathTo pathToRoutine = new PathTo(pathToActor,createMoveTo(pathToActor));
    	 pathToRoutine.designateDestination(target);
    	 return createSequencialableAdapterFrom(pathToRoutine);
     }
	
	 public static Sequencialable createSequencialableShoot(Shootable shootActor,PrecisePoint target){
    	 Shoot shootRoutine = new Shoot(shootActor);
    	 shootRoutine.designateTarget(new PrecisePoint3(target.x,target.y,0));
    	 return createSequencialableAdapterFrom(shootRoutine);
     }
	
    
	
	static PathTo createPathTo(PathToable aa)
    {
    	return new PathTo(aa,createMoveTo(aa));
    }
     static PathTo createPathTo(PathToable aa,PrecisePoint target)
     {
    	 PathTo ret = new PathTo(aa,createMoveTo(aa));
    	 ret.designateDestination(target);
    	 return ret;
     }
    
     static MoveTo createMoveTo(MoveToable aa)
    {
    	return new MoveTo(aa);
    }
    static Shoot createShoot(Shootable aa)
    {
    	return new Shoot(aa);
    }
     static Shoot createShoot(Shootable aa,PrecisePoint3 target)
     {
     	Shoot s = new Shoot(aa);
     	s.designateTarget(target);
    	return s;
     }
     
     static Wait createWait(int tick)
     {
    	 return new Wait(tick);
     }
}