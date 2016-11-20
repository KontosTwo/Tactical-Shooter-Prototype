package com.mygdx.ai.blackboard;

import com.mygdx.physics.PrecisePoint3;

/**
 * Enemies that are visible. Used by the AI, which will single
 * out the most vulnerable Trackable enemy to attack.
 */
public interface Trackable{
	public PrecisePoint3 getLocationForBlackBoard();
}
