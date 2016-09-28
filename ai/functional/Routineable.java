package com.mygdx.ai.functional;

/**
 * Allows any implementing class to describe behavior
 * that will integrate into a behavior tree. The behavior
 * is represented by an action that will have 
 * clearly defined starting conditions,starting action, updating action, 
 * ending condition, ending action, etc. In addition, the action
 * will also provide booleans for whether it was able to successfully
 * complete the action
 */
public interface Routineable{
	public boolean instaSucceededRoutine();
	/**
	 * @Precondition: instaSucceeded returned false
	 */
	public boolean instaFailedRoutine();
	/**
	 * @Precondition: instaSucceeded and instaFailed returned false
	 */
	public boolean succeededRoutine();
	/**
	 * @Precondition: instaSucceeded, instaFailed, and succeeded returned false
	 */ 
	public boolean failedRoutine();

	public void updateRoutine(float dt);// updating
	
	
	
	public void startRoutine();// initializing
	public void completeRoutine();// action to "clean up" the sequencialable
	public void cancelRoutine();//
	
	
	

	
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
