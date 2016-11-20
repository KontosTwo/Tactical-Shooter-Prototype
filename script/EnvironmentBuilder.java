package com.mygdx.script;

import com.mygdx.control.Auxiliarable;
import com.mygdx.control.PlayerControllable;
import com.mygdx.control.AiTestable;

public interface EnvironmentBuilder {
	public void loadLevel(String name);
	public Auxiliarable createAuxiliary(int x,int y);
	public PlayerControllable createPlayer(int x,int y);
	public AiTestable createRifleman(int x,int y);
}
