package com.mygdx.state;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.camera.Camera;
import com.mygdx.physics.PrecisePoint;

class Menu extends GameState
{
	
	Menu(Camera cam,GameModeSwitchable gms) 
	{
		super(cam,gms);
		//em.add(ButtonMenu.ShrekButton(new PrecisePoint(100,100), GameModeAction.PLAY));
	}
	@Override
	public void dispose() 
	{
		
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
	public boolean touchDown(int screenX, int screenY, int pointer, int button) 
	{
		super.touchDown(screenX,screenY,pointer,button);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) 
	{
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) 
	{
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) 
	{
		return false;
	}

	@Override
	public boolean scrolled(int amount) 
	{
		return false;
	}
	@Override
	public void update(float dt) 
	{
		// TODO Auto-generated method stub
		
	}
}