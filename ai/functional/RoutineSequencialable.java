package com.mygdx.ai.functional;

import com.mygdx.script.Sequencialable;

public interface RoutineSequencialable extends Sequencialable
{
	public boolean succeeded();
	public boolean failed();
	public boolean instaSucceeded();
	public boolean instaFailed();

	
	/*
	 * RoutineSequencialable are Sequencialable that are adapted to 
	 * function as nodes in a behavior tree. RoutineSequencialables MUST:
	 *  - never be expected to fail or succeed in less than one update loop
	 *  	that is the job of the Splitter class. Normal RoutineSequencialables
	 *  	are expected to execute through startSequence, updateSequence between 1 and
	 *  	n times, and completeSequence
	 *   
	 *  */
}
