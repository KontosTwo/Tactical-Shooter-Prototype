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
import com.mygdx.entity.Entity;
import com.mygdx.entity.EntityManager;
import com.mygdx.graphic.Animator;
import com.mygdx.graphic.BatchCoordinator;
import com.mygdx.graphic.MapRenderer;
import com.mygdx.physics.PrecisePoint;
import com.mygdx.script.Script;
import com.mygdx.script.Sequencialable;
import com.mygdx.sound.SoundRepository;

final class Play extends GameState implements PlayControlSwitchable
{
	private Level level;
	
	private PrecisePoint mousePosition;	
	
	private PlayerControllable controller;
	private Auxiliarable auxiliary;
	
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
		
		
		EntityManager entityManager = new EntityManager();
		controller = entityManager.createHuman(200, 200);
		controller.initiateControllable();

		auxiliary = entityManager.createProtector(210,150);		//cam.focus(player);
		entityManager.createRifleman(120, 300);

		MapRenderer mr = new MapRenderer();
		
		cam.focus(controller);
		mousePosition = new PrecisePoint();
		cam.zoom = .7f;
		cam.focusOnLead(mousePosition);
		Animator.setBoundaries(cam);
		
		level = new Level(entityManager,mr);
		
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
					auxiliary.aFollow(controller);
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
				case SHOOT:	controller.cShoot((int)truePoint.x,(int)truePoint.y,0);
							break;
				case MOVE: auxiliary.aMoveTo((int)truePoint.x,(int)truePoint.y);
							break;
				case GRENADE: //controller.cGrenade((int)truePoint.x, (int)truePoint.y);;
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
		Vector3 truePoint = new Vector3(screenX,screenY,0);
		cam.unproject(truePoint);
		mousePosition.set(truePoint.x,truePoint.y);
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
		private EntityManager em;
		private Script script;
		private MapRenderer mr;
		private int level;

		
		private Level(EntityManager em,MapRenderer mr)
		{
			List <Sequencialable>list = new LinkedList<Sequencialable>();
			//list.add(new WaitForEvent(new TimesUp(50000)));///currently a placeholder
			script = new Script(list);
			this.em = em;
			Entity.setEntityListener(em);
			this.mr = mr;					
			level = 0;
			load(level);
		}		
		private void load(int level)
		{
			em.loadLevel(level, mr);
			script.loadScript(level);
		}
		private void render(Camera cam)
		{
			mr.setView(cam);
			mr.renderBack();
			//BatchCoordinator.beginBatch();
			em.render();
			//BatchCoordinator.endBatch();
			mr.renderFront();	
			BatchCoordinator.coordinatedRender();
		}
		private void update(float dt)
		{
			em.update(dt);
			script.update(dt);
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

