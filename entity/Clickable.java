package com.mygdx.entity;

import com.badlogic.gdx.math.Vector2;

public interface Clickable 
{
	boolean getClicked(int x,int y);
	void action();
}
