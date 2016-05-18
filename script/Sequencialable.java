package com.mygdx.script;

import com.mygdx.misc.Updatable;

public interface Sequencialable extends Updatable
{
	/*
	 * this allows any implementing class to become a "cutscene"
	 * the class can then be sequenced along with other classes to create the game
	 * the class must have a starting and ending point
	 */
	public void startSequence();// initializing
	public void update(float dt);// updating
	public boolean sequenceIsComplete();// whether the sequence is complete
	public void completeSequence();// action to "clean up" the sequencialable
	public void cancelSequence();//
	/*
	 * 2/5/2016 - this bears resemblance to the Routine class
	 * Routine now implements Sequencialable, so behavior defined for behavior trees 
	 * can also be used for ControlManager, or executing single actions, or Script, or the 
	 * planned execution of in-game events. 
	 */
}
