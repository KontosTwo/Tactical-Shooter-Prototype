package com.mygdx.ai.blackboard;

import com.mygdx.physics.PrecisePoint3;

/**
 * Pinpoints the locations of enemies that are no longer visible.
 * Essentially, the last known location of a enemy. The AI behavior
 * tree will devise and coordinate attacks against these locations
 * until the AI either achieves a direct line of sight with these
 * locations and verify that the enemy is no longer there or dead, or
 * neutralizes the location with explosives and believes the enemy to be
 * dead
 */
public interface Markable {
	public PrecisePoint3 getLocationForBlackBoard();
}
