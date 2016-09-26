package com.mygdx.ai.leaf;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.mygdx.ai.functional.Routineable;
import com.mygdx.ai.leaf.MoveTo.MoveToable;
import com.mygdx.ai.leaf.PathTo.PathToable;
import com.mygdx.script.Scripter.Sequencialable;


abstract public class RoutineFactory 
{
     static PathTo createPathTo(PathToable aa)
    {
    	return new PathTo(aa,createMoveTo(aa));
    }
     static PathTo createPathTo(PathToable aa,double x,double y)
     {
    	 PathTo ret = new PathTo(aa,createMoveTo(aa));
    	 ret.designateDestination(x, y);
    	 return ret;
     }
     public static Sequencialable createSequencialablePathTo(PathToable pathToActor,double x,double y)
     {
    	 PathTo ret = new PathTo(pathToActor,createMoveTo(pathToActor));
    	 ret.designateDestination(x, y);
    	 return ret;
     }
     static MoveTo createMoveTo(MoveToable aa)
    {
    	return new MoveTo(aa);
    }
    static Shoot createShoot(RiflemanRoutineable aa)
    {
    	return new Shoot(aa);
    }
     static Shoot createShoot(RiflemanRoutineable aa,double x,double y,double z)
     {
     	Shoot s = new Shoot(aa);
     	s.designateTarget(x, y,z);
    	return s;
     }
     
     static Wait createWait(int tick)
     {
    	 return new Wait(tick);
     }
     
    static Routineable createMandatoryRoutine(Routineable r){
    	return new Routineable(){

			@Override
			public void startRoutine() {
				r.startRoutine();
			}

			@Override
			public void updateRoutine(float dt) {
				r.updateRoutine(dt);
			}
	
			@Override
			public void completeRoutine() {
				r.completeRoutine();
			}

			@Override
			public void cancelRoutine() {
				r.cancelRoutine();
			}

			@Override
			public boolean routineSucceeded() {
				return r.routineSucceeded();
			}

			@Override
			public boolean routineFailed() {
				return r.routineFailed();
			}

			@Override
			public boolean routineInstaSucceeded() {
				return false;
			}

			@Override
			public boolean routineInstaFailed() {
				return false;
			}

    	};
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