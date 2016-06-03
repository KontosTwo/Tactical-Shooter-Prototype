package com.mygdx.ai.functional;

public interface RoutineSurvivalable extends RoutineSequencialable,Survivalable
{
	/*
	 * this method allows any class to return whether or not a survival condition for the behavior tree is upheld
	 * should be true by default if the routine does not suggest
	 * anything to be "upheld"
	 */
}
