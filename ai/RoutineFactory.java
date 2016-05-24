package com.mygdx.ai;

import com.mygdx.script.Sequencialable;


abstract public class RoutineFactory 
{
	/*
     * Factory methods begin here
     * They will return a Routine. The whole point of these Routines is to assemble a behavior tree
     * They must all be package-visible
     * Some Routines will have other Routines as composition
     * Use these factory methods to create Routines to pass into the constructors of more complex Routines. 
     * Note the first factory method, the one that accepts the HumanoidAiable.
     * HumanoidAiable is a super-composite of multiple Routines, one being MoveTo. 
     * These factory methods can include any type of super-composites. There's going to be a lot of writing though
     */
 
	static Idle createIdle(RiflemanRoutineable aa)
	{
		return new Idle(aa);
	}
     static PathTo createPathTo(RiflemanRoutineable aa)
    {
    	return new PathTo(aa,createMoveTo(aa));
    }
     static PathTo createPathTo(RiflemanRoutineable aa,double x,double y)
     {
    	 PathTo ret = new PathTo(aa,createMoveTo(aa));
    	 ret.designateDestination(x, y);
    	 return ret;
     }
     static MoveTo createMoveTo(RiflemanRoutineable aa)
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
     static BurstShoot createBurstShoot(RiflemanRoutineable aa)
     {
      	Shoot shoot = new Shoot(aa);
    	BurstShoot burst = new BurstShoot(aa,shoot);
    	return burst;
     }
     static BurstShoot createBurstShoot(RiflemanRoutineable aa,double x,double y,double z)
     {
      	Shoot shoot = new Shoot(aa);
    	BurstShoot burst = new BurstShoot(aa,shoot);
    	burst.designateTarget(x, y,z);
    	return burst;
     }
     static Reload createReload(RiflemanRoutineable aa)
     {
    	 return new Reload(aa);
     }
     static Wait createWait(int tick)
     {
    	 return new Wait(tick);
     }
    static RoutineSequencialable createRifleManRoutine(RiflemanRoutineable r)
    {
    	// assemble all routines here. 
    	//Sequence base = Sequence.buildSequence(new InstaSucceed(),new Wait(1),Sequence.buildSequence(new InstaSucceed(),new Wait(1),new InstaSucceed()),new InstaSucceed());
    	Selector base2 = Selector.buildSequence(new InstaFail(),new AlwaysFail(new AlwaysSucceed(new Wait(10))),new InstaFail());
    	Selector base = Selector.buildSequence(new InstaFail(),base2,new InstaFail(),new InstaSucceed());
    	
    	
    	
    	
    	
    	return base;
    }
    
    
    
    /*
     * public static methods begin here
     */
    public static Sequencialable createPathToSeq(double x,double y,RiflemanRoutineable aa)
    {
	   	 
	   	 PathTo ret = new PathTo(aa,createMoveTo(aa));
	   	 ret.designateDestination(x, y);
	   	 return ret;
    }
    public static Sequencialable createBurstShootSeq(double x,double y,double z,RiflemanRoutineable aa)
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
    }
}