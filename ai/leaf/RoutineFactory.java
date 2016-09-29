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
	 
	/**
	 * @param  A Routineable instance
	 * @return An adapter for Sequencialable
	 */
    private static Sequencialable createSequencialableAdapterFrom(Routineable routine){
    	return new Sequencialable(){

			@Override
			public void startSequence() {
				routine.startRoutine();
			}

			@Override
			public void updateSequence(float dt) {
				routine.updateRoutine(dt);
			}

			@Override
			public boolean completed() {
				return routine.succeededRoutine() || routine.failedRoutine();
			}

			@Override
			public void completeSequence() {
				routine.completeRoutine();
			}

			@Override
			public void cancelSequence() {
				routine.cancelRoutine();
			}

			@Override
			public void calculateInstaCompleted() {
				routine.calculateInstaHeuristic();
			}

			@Override
			public boolean sequenceInstaCompleted() {
				return routine.instaFailedRoutine() || routine.instaSucceededRoutine();
			}   		
    	};
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
     
  
    
    /*
     * public static factory methods begin here
     */
    /*public static Routineable createRifleManRoutine(RiflemanRoutineable r)
    {
    	// assemble all routines here. 
    	//Sequence base = Sequence.buildSequence(new InstaSucceed(),new Wait(1),Sequence.buildSequence(new InstaSucceed(),new Wait(1),new InstaSucceed()),new InstaSucceed());
    
    	Routineable base = new Wait();
    	    	
    	
    	return base;
    }
   /* public static Sequencialable createPathToSeq(double x,double y,PathToable aa)
    {
	   	 
	   	 PathTo ret = new PathTo(aa,createMoveTo(aa));
	   	 ret.designateDestination(x, y);
	   	 return ret;
    }
    public static Sequencialable createBurstShootSeq(float x,float y,float z,RiflemanRoutineable aa)
    {
    	BurstShoot burstShoot = new BurstShoot(aa,createShoot(aa));
    	burstShoot.designateTarget(x, y,z);
    	return burstShoot;
    }
    public static Sequencialable createShootSeq(double x,double y,double z,RiflemanRoutineable aa)
    {
    	Shoot shoot = new Shoot(aa);
    	shoot.designateTarget(x, y,z);
    	return shoot;
    }
    public static Reload createReloadSeq(RiflemanRoutineable aa)
    {
   	 return new Reload(aa);
    }*/
}