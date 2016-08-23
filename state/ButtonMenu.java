package com.mygdx.state;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.entity.Button;
import com.mygdx.entity.EntityManager;

class ButtonMenu extends Button
{
	private GameModeAction gameMode;
	private static GameModeSwitchable gms;
	
	private ButtonMenu(Vector2 center,String animation,GameModeAction gameMode,GameModeSwitchable g) 
	{
		super(center,animation);
		this.gameMode = gameMode;	
		gms = g;
	}
	static void setGame(GameModeSwitchable g)
	{
		gms = g;
	}

	@Override
	public void action() 
	{
		gms.switchToGameMode(gameMode);
	}

	@Override
	public void update(float dt) 
	{
		super.update(dt);
	}
	static ButtonMenu ShrekButton(Vector2 position, GameModeAction gameMode)
	{
		return new ButtonMenu(position,"SHREK",gameMode,gms);
	}
}
