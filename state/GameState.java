package com.mygdx.state;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.camera.Camera;
import com.mygdx.entity.Clickable;
import com.mygdx.entity.EntityManager;

abstract class GameState implements InputProcessor
{
	/*
	 * A gamestate is a segment of the game, denoted so for convenience
	 * Play is a gamestate, because it deals with the player playing the game
	 * Credit is a gamestate, because all it does is scroll down the credits
	 * Gamestate can be 
	 */
	protected SpriteBatch sb;
	protected Camera cam;
	//protected EntityManager entityManager;
	protected GameModeSwitchable gms;
	
	public GameState(SpriteBatch sb,Camera cam,GameModeSwitchable gms) 
	{
		this.sb = sb;
		this.cam = cam;
		this.gms = gms;
		Gdx.input.setInputProcessor(this);
		//entityManager = new EntityManager();
	}
	
	public abstract void update(float dt);
	
	public void render()
	{
		//This used to be gl30 until it had to be downgraded due to gl30 returning null. Some backends may be missing
		Gdx.gl20.glClear(GL30.GL_COLOR_BUFFER_BIT);
		sb.setProjectionMatrix(cam.combined);
	}
	
	public abstract void dispose();
	
	public boolean touchDown(int screenX, int screenY, int pointer, int button) 
	{
		screenY = Game.V_HEIGHT-screenY;
		return false;
	}

	@Override
	public boolean keyDown(int keycode) 
	{
		return false;
	}

	@Override
	public boolean keyUp(int keycode) 
	{
		return false;
	}

	@Override
	public boolean keyTyped(char character) 
	{
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) 
	{
		screenY = Game.V_HEIGHT-screenY;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) 
	{
		screenY = Game.V_HEIGHT-screenY;
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) 
	{
		screenY = Game.V_HEIGHT-screenY;
		return false;
	}

	@Override
	public boolean scrolled(int amount) 
	{
		return false;
	}
}