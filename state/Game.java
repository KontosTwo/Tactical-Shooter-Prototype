package com.mygdx.state;

import java.util.Stack;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.camera.Camera;
import com.mygdx.debug.Debugger;



public class Game implements ApplicationListener,GameModeSwitchable
{
	// resizing this does not change the window, but the scaling -_-
	public static final int V_WIDTH = 1700;
	public static final int V_HEIGHT = 1000;
	
	private static final float STEP = 1 / 60f; // used to determinet he frequency of game ticks
	
	private float accum;// used to pace the game ticks
	
	private Stack <GameState> state;
	
	private GameModeAction previousAction;
	
	private SpriteBatch sb;
	private Camera cam;
	private Viewport viewport;
	
		
	public void create() 
	{
		sb = new SpriteBatch(); // spritebatch must be instantiated in create()
		cam = new Camera(); // so does Camera
		cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);
		viewport = new FitViewport(V_WIDTH,V_HEIGHT,cam);
		ButtonMenu.setGame(this);
		
		state = new Stack<GameState>();
		state.push(getPlay());
		previousAction = GameModeAction.MENU;
		//currentState = getMenu();		
	}
	
	public void render() 
	{
		accum += Gdx.graphics.getDeltaTime();
		while(accum >= STEP) 
		{
			accum -= STEP;
			update(STEP);
		}		
	}
	private void update(float step)
	{
		if(!state.isEmpty())
		{
			GameState toBeUpdated = state.peek();
			toBeUpdated.render();
			toBeUpdated.update(step);
			Debugger.update();
		}
		//System.out.println(state);
		//currentState.render();
		//currentState.update(step);		
	}
	
	public void dispose() 
	{
		
	}
	public void resize(int w, int h) 
	{
	    viewport.update(w, h);
	}
	public void pause() 
	{
		
	}
	public void resume() 
	{
		
	}
	private Play getPlay()
	{
		return new Play(sb,cam,this);
	}
	private Menu getMenu()
	{
		return new Menu(sb,cam,this);
	}

	@Override
	public void switchToGameMode(GameModeAction gm) 
	{
		/*
		 * As of 1/26, this switch case is incomplete
		 * the implementation of pause, with the usage 
		 * of a stack just for pause, is awkward
		 * Currently, any action other than UN/PAUSE 
		 * will delete previous game instances
		 */
		switch(gm)
		{
			case PLAY : //currentState = getPlay();
				state.clear();
				state.push(getPlay());
				break;
			case MENU : //currentState = getMenu();
				state.clear();
				state.push(getMenu());
						break;
			case CREDIT : //currentState = getPlay();
				state.clear();
				state.push(getPlay());
						break;
			case INFO : //currentState = getPlay();
				state.clear();
				state.push(getPlay());
						break;
		}
		previousAction = gm;
	}
	private class PauseException extends RuntimeException
	{
		private static final long serialVersionUID = 1L;
		
	}
}
interface GameModeSwitchable
{
	/*
	 * restricts references of Game to only allow the switching of gameModes
	 * defined by the enum GameMode
	 */
	public void switchToGameMode(GameModeAction gm);
}
enum GameModeAction 
{
	/*
	 * These denote Gamestates
	 * Gamemodes are essentially different GameStates
	 * 
	 */
	PLAY,
	MENU,
	INFO,
	CREDIT,
}

