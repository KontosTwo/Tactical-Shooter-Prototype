package com.mygdx.state;


import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.camera.Camera;
import com.mygdx.control.Auxiliarable;
import com.mygdx.control.PlayerControllable;
import com.mygdx.control.AiTestable;
import com.mygdx.debug.Debugger;
import com.mygdx.entity.coordinator.Environment;
import com.mygdx.entity.soldier.InteractionSoldierBattle;
import com.mygdx.graphic.Animator;
import com.mygdx.graphic.BatchCoordinator;
import com.mygdx.graphic.MapRenderer;
import com.mygdx.physics.PrecisePoint;
import com.mygdx.script.Script;
import com.mygdx.script.Scripter.Sequencialable;
import com.mygdx.sound.SoundRepository;

final class Play extends GameState implements PlayControlSwitchable
{
	private Level level;
	
	private PrecisePoint mousePosition;	
	
	private PlayerControllable controller;
	private Auxiliarable auxiliary;
	private AiTestable teleportee;
	
	// Control State Variables
	private Command command;	
	private	ControlState controlState;
	private boolean paused;
	
	private enum Command
	{
		SHOOT,
		ATTACKMOVE,
		GRENADE,
		MOVE,
		FOLLOW,
		DEBUGMOVETO,
		CUTSCENE
		;
	}
	
	private enum ControlState
	{
		/*
		 * determines which keys can be pressed and act
		 * does not restrict clicks, because clicks are restricted
		 * depending on which buttons have been loaded into entityManager
		 */
		CUTSCENE,
		GAMEPLAY;
	}
	
	Play(Camera cam,GameModeSwitchable gms) 
	{
		super(cam,gms);
		
		BatchCoordinator.createBatch(cam);
		
		Environment entityListener = new Environment();
		
		controller = entityListener.createPlayer(45, 45);
		auxiliary = entityListener.createAuxiliary(450,450);		//cam.focus(player);
		teleportee = entityListener.createRifleman(700, 700);
		
		
		cam.focus(controller);
		mousePosition = new PrecisePoint();
		cam.zoom = .7f;
		cam.focusOnLead(mousePosition);
		Animator.setBoundaries(cam);
		
		level = new Level(entityListener);
		
		Music music = SoundRepository.GiftOfThistle.getMusic();
		music.setVolume(.6f);
		//music.play();
		music.setLooping(true);
		
		
		controlState = ControlState.GAMEPLAY;
		command = Command.SHOOT;
		paused = false;
	}

	@Override
	public void render() 
	{
		super.render();
		level.render(cam);

	}
	public void update(float dt)
	{
		if(!paused)
		{		
			cam.update();
			level.update(dt);
			
			//controller.cMouseMoveTo(cam.unproject(mousePosition));
		}
		
	}

	@Override
	public void dispose() 
	{
		
	}
	
	@Override
	public boolean keyDown(int keycode) 
	{
		if(controlState.equals(ControlState.GAMEPLAY))
		{
			switch (keycode)
	        {
		        case Keys.S:
		        	controller.cMoveLeft(true);            
					break;
		        case Keys.F:
		        	controller.cMoveRight(true);            
					break;
		        case Keys.E:
		        	controller.cMoveUp(true);            
					break;
		        case Keys.D:
		        	controller.cMoveDown(true);            
					break;
		        case Keys.Q:
		        	controller.cStand();            
					break;
		        case Keys.A:
		        	controller.cCrouch();          
					break;
		        case Keys.Z:
		        	controller.cLay();          
					break;
		        case Keys.R:
		        	controller.cReload();          
					break;
		        case Keys.W:
					//auxiliary.aFollow(controller);
					break;
		        case Keys.NUM_1:
					command = Command.SHOOT;
					break; 
				case Keys.NUM_2:
					command = Command.MOVE;
					break;
				case Keys.NUM_3:
					command = Command.GRENADE;
					break;
				case Keys.NUM_4:
					command = Command.DEBUGMOVETO;
					break;
				case Keys.L:
					cam.focus(controller);
					break; 
				case Keys.K:
					cam.unfocus();
					break;
				case Keys.O:
					cam.zoom += .1f;
					break; 
				case Keys.P:
					cam.zoom -= .1f;
					break;	
				case Keys.NUM_0:
					paused = !paused;
					break;
	        }
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) 
	{
		if(controlState.equals(ControlState.GAMEPLAY))
		{
			switch (keycode)
	        {
		        case Keys.S:
		        	controller.cMoveLeft(false);            
					break;
		        case Keys.F:
		        	controller.cMoveRight(false);            
					break;
		        case Keys.E:
		        	controller.cMoveUp(false);            
					break;
		        case Keys.D:
		        	controller.cMoveDown(false);            
					break;
		    }
		}
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
		super.touchDown(screenX, screenY, pointer, button);		
		//if not playing, then click. Click will execute any clickable
		Vector3 truePoint = new Vector3(screenX,screenY,0);
		cam.unproject(truePoint);

		if(controlState.equals(ControlState.GAMEPLAY))
		{
			switch(command)
			{
				case SHOOT:	controller.cShoot(new PrecisePoint(truePoint.x,truePoint.y));
							break;
				case MOVE: auxiliary.aMoveTo(new PrecisePoint(truePoint.x,truePoint.y));
							break;
				case GRENADE: //controller.cGrenade((int)truePoint.x, (int)truePoint.y);;
							break;
				case DEBUGMOVETO: teleportee.debugMoveTo(new PrecisePoint(truePoint.x,truePoint.y));
					System.out.println("asdf");
				break;
				default:
			}
		}
		return false;
	}



	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) 
	{
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) 
	{
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean mouseMoved(int screenX, int screenY) 
	{
		super.mouseMoved(screenX, screenY);
		mousePosition.set(screenX,Game.V_HEIGHT - screenY);
		return false;
	}
	@Override
	public boolean scrolled(int amount) 
	{
		return false;
	}

	@Override
	public void controlCutscene() 
	{
		controlState = ControlState.CUTSCENE;
	}

	@Override
	public void controlLevel() 
	{
		controlState = ControlState.GAMEPLAY;
	}
	
	private static class Level
	{
		private Environment em;
		private Script script;
		private int level;

		
		private Level(Environment em)
		{
			List <Sequencialable>list = new LinkedList<Sequencialable>();
			script = new Script(list);
			this.em = em;
			level = 0;
			load("level0");
		}		
		private void load(String level)
		{
			em.loadLevel(level);
		}
		
		private void render(Camera cam)
		{
			em.render();
			Debugger.render();
			Debugger.update(.4f);
			BatchCoordinator.coordinatedRender();
		}
		private void update(float dt)
		{
			em.update(dt);
			script.updateSequence(dt);
			/*if(script.sequenceIsComplete())
			{
				/*
				 * once all the triggers/sequences are compelte in script, increment level and load again
				 *
				level ++;
				load(level);
				script.startSequence();
			}*/
		}
	}

}
interface PlayControlSwitchable
{
	/*
	 * this interface controls which controls in the Play state are allowed.
	 */
	public void controlCutscene();// only controls for cutscenes are allowed
	public void controlLevel();
}

