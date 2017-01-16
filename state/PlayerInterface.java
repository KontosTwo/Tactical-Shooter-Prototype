package com.mygdx.state;

public interface PlayerInterface 
{
	/*
	 * a bunch of dastardly evil getters
	 * the information they provide assist in determining
	 * whether or not certain controls are permissible
	 * example: the player's click will register as a shoot command
	 * only if getAmmo() returns a positive number
	 * Some of these getters which prevent actions will greatly simplify some 
	 * Sequencialable. For example, 
	 */
	public int getAmmo();
}
