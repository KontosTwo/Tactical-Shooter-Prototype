package com.mygdx.ai;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


 class SurvivalSequence implements RoutineSequencialable
{
	/*
	 * this solves the problem of the extra
	 * startSequence call before the permanent
	 * succeed - fail - update loop
	 * "Prioritizes" routines in order
	 * For a routine to continue updating, all previous 
	 * conditions have to be met
	 * should a pre-requisite condition fail
	 * The difference between this and Sequence
	 * is that all of the routines that Sequence has can instaSucceed, 
	 * thus allowing the selector to instantly succeed. The Sequence 
	 * is always a lower level node
	 * SurvivalSequence howwever is the topmost node. It cannot instantly
	 * succeed, or even normally succeed. The last node it has is the 
	 * "aspirational node" - if all other nodes have succeeded, then the aspirational
	 * node will always run.
	 */
	private final List<RoutineSurvivalable> survival;
	private int currentRoutine;
	
	SurvivalSequence(List<RoutineSurvivalable> r)
	{
		survival = new LinkedList<>(r);
	}
	
	
	@Override
	public void startSequence() 
	{
		/*
		 * here's the critical part. Check each node's upheld(), THEN
		 * separately check the accompanying routine to see if it does not instaSucceeded(), then 
		 * (it wont instafail cuz upheld() accounts for that) then start it
		 */
		transverseFrom(0);
		survival.get(currentRoutine).startSequence();
	}

	@Override
	public void update(float dt) 
	{
		if(!upheldUpTo() || survival.get(currentRoutine).failed())
		{
			survival.get(currentRoutine).cancelSequence();
			transverseFrom(0);
			survival.get(currentRoutine).startSequence();
		}
		else if(survival.get(currentRoutine).succeeded())
		{
			survival.get(currentRoutine).completeSequence();
			transverseFrom(currentRoutine);
			survival.get(currentRoutine).startSequence();
		}
		else
		{
			survival.get(currentRoutine).update(dt);
		}
	}

	@Override
	public boolean sequenceIsComplete()
	{
		return false;
	}

	@Override
	public void completeSequence() 
	{
		survival.get(currentRoutine).completeSequence();
	}

	@Override
	public void cancelSequence() 
	{
		survival.get(currentRoutine).cancelSequence();
	}

	@Override
	public boolean succeeded() {
		return false;
	}

	@Override
	public boolean failed() {
		return false;
	}
	
	/*
	 * SurvivalSequence never fails nor succeed
	 */
	@Override
	public boolean instaSucceeded() {
		return false;
	}

	@Override
	public boolean instaFailed() {
		return false;
	}
	

	private boolean upheldUpTo()
	{
		boolean ret = true;
		search:
		for(int i = 0; i < currentRoutine; i ++)
		{
			if(!survival.get(i).upheld())
			{
				ret = false;
				break search;
			}
		}
		return ret;
	}
	private void transverseFrom(int mark)
	{
		boolean survivalComplete = true;
		/*
		 * this loop will not check the last routine
		 * for upheld-ness because the last routine
		 * is the aspirational routine, which will constantly
		 * run
		 */
		search:
		for(int i = mark; i < survival.size() - 1; i ++)
		{
			if(!survival.get(i).upheld())
			{
				currentRoutine = i;
				survivalComplete = false;
				break search;
			}
		}
		if(survivalComplete)
		{
			currentRoutine = survival.size() - 1;
		}
	}
}
