package com.mygdx.entity;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.entity.soldier.SoldierBattle;
import com.mygdx.graphic.MapRenderer;
import com.mygdx.handler.Auxiliarable;
import com.mygdx.handler.Controllable;
import com.mygdx.map.GameMap;
import com.mygdx.misc.Point;
import com.mygdx.misc.PrecisePoint;
import com.mygdx.misc.Tuple;
import com.mygdx.script.Sequencialable;

public class EntityManager implements HumanoidEffectuator
{
	private Array<Entity> entity;
	
	private SoldierBattle player;
	private SoldierBattle auxiliary;
	private List<SoldierBattle> enemy;

	
	private GameMap gameMap;
	private State state;
	private int cycle;
	private ShapeRenderer sr;

	public EntityManager()
	{
		gameMap = new GameMap();
		entity = new Array<Entity>();
	}
	public void setShapeRenderer(ShapeRenderer shapeRenderer)
	{
		 sr = shapeRenderer;	 
	}
	
	private Array<SoldierBattle> getFriendly() // this only means humanoids of epeiros
	{
		Array<SoldierBattle> ret = new Array<SoldierBattle>();
		for(Entity e : entity)
		{
			if(e instanceof SoldierBattle)// this is bad
			{
				SoldierBattle h = (SoldierBattle) e;
				if(h.isFriendly())
				{
					ret.add(h);
				}
			}
		}
		return ret;
	}
	private Array<HitMarker> getHitMarker()
	{
		Array<HitMarker> ret = new Array<HitMarker>();
		for(Entity e : entity)
		{
			if(e instanceof HitMarker)// this is bad
			{
				ret.add((HitMarker) e);
			}
		}
		return ret;
	}

	public void loadLevel(int level,MapRenderer mr)
	{
		gameMap.loadLevel(level,mr);
		entity.addAll(gameMap.getLoadedEntity(level));
	}	
	public void update(float dt)
	{
		this.cycle = cycle;
		for(int i = 0; i < entity.size; i ++)
		{
			entity.get(i).update(dt);
		}
		gameMap.update();
		/*if(!doOnce)
		{
			protector.setRoutine(new FindCoverAgainst(protector,human));
			doOnce = true;
		}*/
		Iterator<Entity> iterator = entity.iterator();
		while(iterator.hasNext())
		{
			Entity e = iterator.next();
			if(e.removable())
			{
				iterator.remove();
			}
		}	
	}
	public void render()
	{
		PriorityQueue<Visible> visible = getVisible();
		while(!visible.isEmpty())
		{
			visible.poll().render();
		}
		
	}
	public void click(int x,int y)
	{
		for(Clickable c : getClickable())
		{
			if(c.getClicked(x, y))
			{
				c.action();
				break;
			}
		}
	}
	public Tuple<Boolean,LinkedList<Point>> findPath(double sx, double sy, double tx, double ty)
	{
		return gameMap.findPath((int)sx, (int)sy, (int)tx, (int)ty);
	}
	public void add(Entity e)
	{
		entity.add(e);
	}
	private Array<Clickable> getClickable()
	{
		Array<Clickable> ret = new Array<Clickable>();
		for(Entity e : entity)
		{
			if(e instanceof Clickable)// this is bad
			{
				ret.add((Clickable) e);
			}
		}
		return ret;
	}
	private PriorityQueue <Visible> getVisible()
	{
		PriorityQueue<Visible> ret = new PriorityQueue<Visible>();
		for(Entity e : entity)
		{
			if(e instanceof Visible)// this is bad
			{
				ret.add((Visible)e);
			}
		}
		return ret;
	}
	private Array<Hurtboxable> getHurtboxable()
	{
		Array<Hurtboxable> ret = new Array<Hurtboxable>();
		for(Entity e : entity)
		{
			if(e instanceof Hurtboxable)// this is bad
			{
				ret.add((Hurtboxable)e);
			}
		}
		return ret;
	}	
	private Array<SoldierBattle> getHumanoid()
	{
		Array<SoldierBattle> ret = new Array<SoldierBattle>();
		for(Entity e : entity)
		{
			if(e instanceof SoldierBattle)// this is bad
			{
				ret.add((SoldierBattle)e);
			}
		}
		return ret;
	}
	public Controllable createHuman(float x,float y)
	{
		SoldierBattle player = SoldierBattle.createHuman(new PrecisePoint(x,y));
		//human.setSizeAll(150, 150);
		add(player);
		gameMap.addCollidable(player);
		//milliard = player;
		return player;
	}
	public Auxiliarable createProtector(float x,float y)
	{
		SoldierBattle protector = SoldierBattle.createProtector(new PrecisePoint(x,y));
		add(protector);
		//chanion = protector;
		return protector;
		//add(new Protector(this, AnimationEnum.MAN, new Vector2(x,y)));
	}
	public void createRifleman(float x,float y)
	{
		SoldierBattle rifleman = SoldierBattle.createRifleman(new PrecisePoint(x,y));
		add(rifleman);
		//add(new Protector(this, AnimationEnum.MAN, new Vector2(x,y)));
	}
	public boolean see(double y1,double x1,double height1,double y2,double x2,double height2)
	{
		boolean found = true;
		found = gameMap.passThroughVis(y1, x1, height1, y2, x2, height2);
		return found;
	}
	@Override
	public boolean seePhysical(double x1, double y1, double z1, double x2,
			double y2, double z2) {
		boolean found = true;
		found = gameMap.passThroughPhy(y1, x1, z1, y2, x2, z2);	
		return found;
		
		
	}
	public void shoot(double y1,double x1,double height1,double y2,double x2,double z2,SoldierBattle shooter,double accuracy)
	{
		// height 1 and height 2 are entity heights independent of the map heights
		// the final impact's z is far too low and completely inaccurate, but for a good reason. if a hurtboxable containes a point, it's dead. end of story
		Vector3 impactFinal = new Vector3();
		double height2 = z2;
		float prev = 0;
		Array <SoldierBattle> allHumanoid = getHumanoid();
		for(SoldierBattle gb : allHumanoid)
		{
			if(gb.hurtboxContains((float)x2, (float)y2))
			{
				if(gb.getHeight() > prev)
				{
					height2 = gb.getHeight();
					prev = gb.getHeight();
				}
			}
		}
		Vector3 impactMap = gameMap.mapImpact(y1, x1, height1,y2, x2,height2,accuracy);
				
		
		Array <Hurtboxable> hurtboxable = new Array <Hurtboxable>();
		for(SoldierBattle hb : allHumanoid)
		{
			if(hb.hurtboxOverlaps((float)x1,(float) y1, (float)impactMap.x, (float)impactMap.y) && /*check this, */hb != shooter)
			{
				hurtboxable.add(hb);
			}
		}
		int i, j, first;
	    for ( i = hurtboxable.size - 1; i > 0; i -- ) 
	    {
	         first = 0;   
	         for(j = 1; j <= i; j ++)   
	         {
	              if( hurtboxable.get(j).distanceFrom((float)x1,(float) y1) > hurtboxable.get(first).distanceFrom((float)x1, (float)y1) )         
	              first = j;
	         }
	         hurtboxable.swap(first, i);
	    }           			
		if(hurtboxable.size != 0)
		{
			Vector3 impactHurtBox = hurtBoxImpact( (int)x1, (int)y1,(float) height1,(int)impactMap.x,(int)impactMap.y,(int)impactMap.z,hurtboxable);			
			
			impactFinal.set(impactHurtBox);
		}
		else
		{
			impactFinal.set(impactMap);
		}
		
		
		/*FakeVisible target = new FakeVisible(new Vector2(impactFinal.x,impactFinal.y));
		target.setAnimationSize(5, 5);
		entity.add(target);	*/
				
		Array<SoldierBattle> test = new Array<SoldierBattle>();
		for(SoldierBattle hb : allHumanoid)
		{
			if(hb.hurtboxContains(impactFinal.x, impactFinal.y))
			{
				test.add(hb);
				
			}
		}
		
		int x, y, first2;
	    for ( x = test.size - 1; x > 0; x -- ) 
	    {
	         first2 = 0;   
	         for(y = 1; y <= x; y ++)   
	         {
	              if( test.get(y).getHeight() > test.get(first2).getHeight())         
	              first2 = y;
	         }
	         test.swap(first2, x);
	    }
	    
		search:
		for(SoldierBattle hb : test)
		{
			if(hb.getHeight() + 1 >= impactFinal.z && hb != shooter)
			{
				//entity.removeValue(hb, true);
				shooter.damage(hb);
				break search;
				// fuck this hb up
			}
		}
	    /*sr.begin(ShapeType.Line);
		sr.setColor(1, 0, 0, 1);
		sr.line(x1, y1, impactFinal.x, impactFinal.y);
		sr.end();*/
	    entity.add(new HitMarker((new PrecisePoint(impactFinal.x,impactFinal.y)),shooter));
	}
	private Vector3 hurtBoxImpact(int x0, int y0, float height1, int x1, int y1,float height2,Array<Hurtboxable> hurtboxable)
	{
		Vector3 impact = new Vector3(x1,y1,height2);
		int dx = Math.abs(x1 - x0);
	    int dy = Math.abs(y1 - y0);
	    int x = x0;
	    int y = y0;
	    int n = 1 + dx + dy;
	    int x_inc = (x1 > x0) ? 1 : -1;
	    int y_inc = (y1 > y0) ? 1 : -1;
	    int error = dx - dy;
	    dx *= 2;
	    dy *= 2;
	    Array <Hurtboxable> searched = new Array <Hurtboxable>(); 
	    Vector2 currentNode = new Vector2();
	    search:
	    for (; n > 0; --n)
	    {
	    	search2:
	    	for(Hurtboxable hb : hurtboxable)
	        {
	    		for(Hurtboxable hbsearched: searched)
	    		{
	    			if(hbsearched == hb)
	    			{
	    				continue search2;
	    			}
	    		}
	    		if(hb.crossBotHurtBox(x,y) || hb.crossTopHurtBox(x,y) || hb.crossRightHurtBox(x,y)||hb.crossLeftHurtBox(x,y))
	        	{
	    			searched.add(hb);
	    			Vector2 currentPoint2 = miniHurtBoxImpact(n, hb, x, y, x_inc, y_inc, error, dx, dy, new Point(x,y),x1,y1);
	        		currentNode.set((x + currentPoint2.x)/2,(y + currentPoint2.y)/2);
	        		if(gameMap.angleBlock(y0, x0, height1, currentNode.y, currentNode.x, hb.getHeight(),y1,x1,height2))
	        		{
	        			impact.x = currentNode.x;
	        			impact.y = currentNode.y;
	        			impact.z = (float) ((gameMap.findSlopePhy(y0, x0, height1, y1, x1, height2) * Math.hypot(impact.x - x0, impact.y - y0)) + height1 + gameMap.getMapPhy(y0,x0) - gameMap.getMapPhy(impact.y,impact.x));

	        			break search;
	        		}
	        	}
	        }
	    	if (error > 0)
	        {
	            x += x_inc;
	            error -= dy;
	        }
	        else
	        {
	            y += y_inc;
	            error += dx;
	        }
	    }
	    return impact;
	}
	private Vector2 miniHurtBoxImpact(int n,Hurtboxable hb,int x,int y,int x_inc,int y_inc,int error,int dx,int dy,Point ori,int x1,int y1)
	{
		Vector2 impact = new Vector2();
		boolean found = false;
	    search:
		for (; n > 0; --n)
	    {
			if(hb.intersectAgainHurtBox(ori.getX(), ori.getY(), x, y))
			{
				impact.set(x,y);
				found = true;
				break search;
			}
			
			
	    	if (error > 0)
	        {
	            x += x_inc;
	            error -= dy;
	        }
	        else
	        {
	            y += y_inc;
	            error += dx;
	        }
	    }
	    if(!found)
	    {
	    	impact.set(x1,y1);
	    }
	    /*if(!found)
	    {
	    	impact.set(x,y);
	    }*/
		return impact;
	}
	public  void mark(double x,double y)
	{
		entity.add(Visible.createDoodad("SHREK",(float)x, (float)y));
	}
	public boolean sameTile(int x1,int y1,int x2,int y2)
	{
		return gameMap.sameTile(x1, y1, x2, y2);
	}
	public Tuple<Boolean,Vector2> findCover(double x,double y,int searchCoverDistance)
	{
		Array<SoldierBattle> otherHumanoid = new Array<SoldierBattle>();
		for(SoldierBattle h : getHumanoid())
		{
			if(Visible.withinDistance((int)x, (int)y, h, searchCoverDistance))
			{
				otherHumanoid.add(h);
			}
		}
		/*for(Point p : gameMap.findCover( (int)x, (int)y,otherHumanoid, searchCoverDistance))
		{
			//mark(p.getX(),p.getY());
		}*/
		return gameMap.findCover( (int)x, (int)y,otherHumanoid, searchCoverDistance);
	}
	public double judgeCover(double x1,double y1,double height1,double x2,double y2,double height2)
	{		
		return gameMap.coverDisparity(y1, x1, height1, y2, x2, height2);
	}
	public Tuple<Boolean,SoldierBattle> seeEnemy(SoldierBattle humanoid) 
	{		
		Tuple<Boolean,SoldierBattle> ret = new Tuple<Boolean, SoldierBattle>();
		ret.x = false;
		search:
		for(SoldierBattle h : getFriendly())
		{
			if(humanoid.see(h))
			{
				ret.y = h;
				ret.x = true;
				break search;
			}
		}
		return ret;
	}

	public boolean hitMarkerNear(SoldierBattle h) 
	{
		boolean ret = false;
		for(HitMarker hm : getHitMarker())
		{
			if(h.senseShot(hm))
			{
				ret= true;
			}
		}
		return ret;
	}
	@Override
	public void remove(Entity e) 
	{
		entity.removeValue(e, true);
	}
	@Override
	public void grenade(double x1, double y1,double x2,double y2) 
	{
		entity.add(new Grenade(new PrecisePoint(x1,y1),new PrecisePoint(x2,y2)));
	}
	@Override
	public void scanBattleField(SoldierBattle h) // only usable by enemies
	{
		for(SoldierBattle humanoid : getHumanoid())
		{
			if(SoldierBattle.areEnemies(h, humanoid) && h.see(humanoid))
			{
				h.spotEnemy(humanoid);
			}
		}
	}
	@Override
	public boolean enemyWithinRange(Vector2 position, SoldierBattle h, int radius) 
	{
		boolean ret = false;
		for(SoldierBattle humanoid : getHumanoid())
		{
			if(SoldierBattle.areEnemies(h, humanoid) && h.distanceFrom(position.x, position.y) < radius)
			{
				ret = true;
			}
		}
		return ret;
	}
	@Override
	public void foundObscuredEnemy(SoldierBattle target, SoldierBattle sender) 
	{
		Array<SoldierBattle> allHumanoid = getHumanoid();
		for(int i = 0; i < allHumanoid.size; i ++)
		{
			SoldierBattle current = allHumanoid.get(i);
			if(SoldierBattle.areAllies(sender, current) && sender != current)// second condition makes sure that the two soldiers aren't the same
			{
				
				/*
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 */
				
				//current.recieveObscuredEnemyMessage(target);// all allies recieve the target that the sender has spotted
			}
		}
	}
	@Override
	public boolean seeGround(double x1, double y1, double z1, double x2, double y2) 
	{
		return gameMap.passThroughVisGround(y1, x1, z1, y2, x2);
	}
	
	
	private enum State
	{
		BEGIN,
		RUNNING,
		COMPLETE,
		PAUSED;
	}
}
