package com.mygdx.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.entity.Entity;
import com.mygdx.entity.Hitboxable;
import com.mygdx.entity.Visible;
import com.mygdx.entity.soldier.SoldierBattleConcrete;
import com.mygdx.graphic.MapRenderer;
import com.mygdx.misc.Tuple;
import com.mygdx.physics.Point;

public class GameMap
{
	private Array<Array<Tile>> map;
	private int tileSize;
	//private Array <TerrainChunk> terrainChunk;
	private Array <Hitboxable> hurtboxableList;
	private HashSet<Point> coverPoint;
	
	private static final String INFILETERRAINID = "map";
	private static final String INFILEOBSTACLEID = "obstacle";
	private static final String INFILERAMPID = "ramp";
	private static final String MAPENTITYID = "doodad";
	
	private static final int  MAXHEIGHT = 300;
	
	private enum LevelData
	{
		REMNANTS("testmapintegrated.tmx");
		
		private final String tileFile;
		
		LevelData(String tileFile)
		{
			this.tileFile = tileFile;
		}
		
		private String getTiledFile()
		{
			return tileFile;
		}
	}
	public void update()
	{
		collisionCheck();
	}
	public GameMap()
	{
		map = new Array<Array<Tile>>();
		//terrainChunk = new Array <TerrainChunk>();
		hurtboxableList = new Array<Hitboxable>();
		coverPoint = new HashSet<Point>();
	}
	public void addCollidable(Hitboxable h)
	{
		hurtboxableList.add(h);
	}
	public void loadLevel(int level,MapRenderer mr)
	{
		// data first
		TiledMap mapdata = new TmxMapLoader().load(LevelData.values()[level].getTiledFile());
		MapProperties property = mapdata.getProperties();
		int height = property.get("height",Integer.class);
		int width = property.get("width",Integer.class);
		tileSize = property.get("tilewidth",Integer.class);
		map.clear();
		for(int i = 0; i < height; i ++)
		{
			map.add(new Array<Tile>());
			for(int j = 0; j < width; j ++)
			{
				map.get(i).add(new Tile());
				setTile(i, j, 0, 0);
				setWalkable(i, j, true);
			}
		}
		createTileType(mapdata);
		createCoverPoint(map);
		//createChunk(terrainChunk);	
		property = mapdata.getProperties();
		for(MapLayer tmtl : mapdata.getLayers())
		{
			TiledMapTileLayer maplayer = (TiledMapTileLayer)tmtl;
			if(tmtl.getName().contains("background"))
			{
				mr.addToBack(maplayer);
			}
			else if(tmtl.getName().contains("frontground"))
			{
				mr.addToFront(maplayer);
			}
		}
		int tilegraphicsize = Integer.parseInt(property.get("tilegraphicsize",String.class));
		Visible.supplyTileSizeGraphic(tilegraphicsize);
		//TiledMapTileLayer tmtl = (TiledMapTileLayer)mapvisual.getLayers().get("ground");
		//mr.addToBack(tmtl);
	}
	public Array<Entity> getLoadedEntity(int level)// must be called after load level. this time call all doodads and put them into this array
	{
		Array <Entity> ret = new Array<Entity>();
		TiledMap mapvisual = new TmxMapLoader().load(LevelData.values()[level].getTiledFile());
		for(MapLayer tmtl : mapvisual.getLayers())
		{
			String name = tmtl.getName();
			if(name.contains("doodad"))
			{
				String doodadId = name.replace("doodad_",""); // the layer's name in Tiled better correspond to an animationDepot enumeration in visible	
				TiledMapTileLayer layer = (TiledMapTileLayer)tmtl;			
				for(int row = 0; row < layer.getHeight(); row++) // y
				{
					for(int col = 0; col < layer.getWidth(); col++) //x
					{
						if(layer.getCell(col, row) != null)
						{
							ret.add(Visible.createDoodad(doodadId,tileSize*(col + .5f),tileSize *(row + .5f)));					
						}
					}
				}
			}
		}
		return ret;
	}
	private void createCoverPoint(Array<Array<Tile>> map)
	{
		
		for(int i = 0; i < map.size; i ++) // y
		{
			for(int j = 0; j < map.get(0).size; j ++) // x
			{
				if(map.get(i).get(j).walkable())
				{
					if(withinMap(i + 1,j + 1) && map.get(i).get(j).lowerHeightThan(map.get(i + 1).get(j + 1)))
					{
						coverPoint.add(new Point(j,i));
						continue;
					}
					if(withinMap(i + 1,j ) && map.get(i).get(j).lowerHeightThan(map.get(i + 1).get(j)))
					{
						coverPoint.add(new Point(j,i));
						continue;
					}
					if(withinMap(i + 1,j - 1) && map.get(i).get(j).lowerHeightThan(map.get(i + 1).get(j - 1)))
					{
						coverPoint.add(new Point(j,i));
						continue;
					}
					if(withinMap(i - 1,j + 1) && map.get(i).get(j).lowerHeightThan(map.get(i - 1).get(j + 1)))
					{
						coverPoint.add(new Point(j,i));
						continue;
					}
					if(withinMap(i - 1,j) && map.get(i).get(j).lowerHeightThan(map.get(i - 1).get(j)))
					{
						coverPoint.add(new Point(j,i));
						continue;
					}
					if(withinMap(i - 1,j - 1) && map.get(i).get(j).lowerHeightThan(map.get(i - 1).get(j - 1)))
					{
						coverPoint.add(new Point(j,i));
						continue;
					}
					if(withinMap(i,j + 1) && map.get(i).get(j).lowerHeightThan(map.get(i).get(j + 1)))
					{
						coverPoint.add(new Point(j,i));
						continue;
					}
					if(withinMap(i,j - 1) && map.get(i).get(j).lowerHeightThan(map.get(i).get(j - 1)))
					{
						coverPoint.add(new Point(j,i));
						continue;
					}
				}
			}
		}
	}
	private boolean withinMap(int x,int y)
	{
		return x >= 0 && x < map.get(0).size && y >= 0 && y < map.size;
	}
	private void setTile(int y,int x,int heightP,int heightV)
	{
		map.get(y).get(x).setHeightPhy(heightP);
		map.get(y).get(x).setHeightVis(heightV);
	}
	private void setTileObstacle(int y,int x,int heightP,int heightV)
	{
		map.get(y).get(x).setObstacleHeightPhy(heightP);
		map.get(y).get(x).setObstacleHeightVis(heightV);
	}
	private void setWalkable(int y,int x,boolean b)
	{
		map.get(y).get(x).toggleWalkable(b);
	}
	private void setRamp(int y,int x,boolean b)
	{
		map.get(y).get(x).toggleRamp(b);
	}
	private void createTileType(TiledMap mapdata)
	{
		for(MapLayer maplayer : mapdata.getLayers())
		{
			if(maplayer.getName().contains(INFILERAMPID))
			{
				TiledMapTileLayer layer = (TiledMapTileLayer)maplayer;
				for(int row = 0; row < layer.getHeight(); row++) 
				{
					for(int col = 0; col < layer.getWidth(); col++) 
					{
						if(layer.getCell(col, row) != null)
						{
							setRamp(row,col,true);
						}
					}
				}
			}
			else if(maplayer.getName().contains(INFILETERRAINID))
			{
				TiledMapTileLayer layer = (TiledMapTileLayer)maplayer;
				MapProperties layerproperty = layer.getProperties();
				int heightPhy = Integer.parseInt(layerproperty.get("heightP", String.class));
				int heightVis = Integer.parseInt(layerproperty.get("heightV", String.class));
				boolean walkable = Boolean.parseBoolean((layerproperty.get("walkable", String.class)));
				boolean ramp = Boolean.parseBoolean((layerproperty.get("ramp", String.class)));
				for(int row = 0; row < layer.getHeight(); row++) 
				{
					for(int col = 0; col < layer.getWidth(); col++) 
					{
						if(layer.getCell(col, row) != null)
						{
					
							setTile(row, col, heightPhy, heightVis);
							setWalkable(row, col, walkable);
							setRamp(row,col,ramp);
						}
					}
				}
			}
			else if(maplayer.getName().contains(INFILEOBSTACLEID))// in tiled, the obstacles are considered last, so their terrain information will ovverride terrain data
			{
				TiledMapTileLayer layer = (TiledMapTileLayer)maplayer;
				MapProperties layerproperty = layer.getProperties();
				int heightPhy = Integer.parseInt(layerproperty.get("heightP", String.class));
				int heightVis = Integer.parseInt(layerproperty.get("heightV", String.class));
				boolean walkable = Boolean.parseBoolean((layerproperty.get("walkable", String.class)));
				boolean ramp = Boolean.parseBoolean((layerproperty.get("ramp", String.class)));
				for(int row = 0; row < layer.getHeight(); row++) 
				{
					for(int col = 0; col < layer.getWidth(); col++) 
					{
						if(layer.getCell(col, row) != null)
						{
							setTileObstacle(row, col, heightPhy, heightVis);
							setWalkable(row, col, walkable);
							setRamp(row,col,ramp);
						}
					}
				}
			}
		}
	}
	/*private void createChunk(Array<TerrainChunk> terrainChunk)
	{
		HashSet<Point> tileTemp = new HashSet <Point>();
		HashSet<Point> coverTemp = new HashSet <Point>();
		boolean[][] checked = new boolean[map.size][map.get(0).size];
		for(int i = 0; i < map.size; i ++)
		{
			for(int j = 0; j < map.get(0).size; j ++)
			{
				checked[i][j] = false;
			}
		}
		search:
		for(int y = 0; y < map.size; y ++)
		{
			for(int x = 0; x < map.get(0).size; x ++)
			{
				if(!map.get(y).get(x).groundLevel())
				{
					recursiveChunk(tileTemp,y,x,checked);
					coverFromTile(tileTemp,coverTemp);
					if(tileTemp.size() > 0)
					{
						terrainChunk.add(new TerrainChunk(tileTemp,coverTemp));
					}
					tileTemp.clear();
					coverTemp.clear();
				}
			}
		}	
		//System.out.println(terrainChunk.size);
	}
	private void coverFromTile(HashSet<Point> tile,HashSet<Point> cover)
	{
		//HashSet<Point> ret = new HashSet<Point>();
		for(Point p : tile)
		{
			int x = p.getX();
			int y = p.getY();
			if(!map.get(y).get(x).isRamp())
			{
				if(y + 1 < map.size && map.get(y + 1).get(x).walkable() )
				{
					cover.add(new Point(x,y + 1));
				}
				if(x + 1 < map.get(0).size && map.get(y).get(x + 1).walkable() )
				{
					cover.add(new Point(x + 1,y));
				}
				if(y - 1 >= 0 && map.get(y - 1).get(x).walkable() )
				{
					cover.add(new Point(x,y - 1));
				}
				if(x - 1 >= 0 && map.get(y).get(x - 1).walkable() )
				{
					cover.add(new Point(x - 1,y));
				}
			}
		}
		System.out.println(cover.size() + " done");
	}
	private void recursiveChunk(HashSet<Point> tile,int y,int x,boolean[][] checked)
	{
		//System.out.println(x + " " +y + " " + checked[y][x] + " " + getTrue(checked));
		if(!checked[y][x])
		{
			tile.add(new Point(x,y));
			checked[y][x] = true;
			//System.out.println("doing");
			if(y < map.size - 1)
			{
				if(map.get(y).get(x).differentHeightPAs(map.get(y + 1).get(x)))
				{
					//tile.add(new Point(x,y + 1));
					//System.out.println("up");
					recursiveChunk(tile,y +1 ,x,checked);
				}
			}
			if(y > 0)
			{
				if(map.get(y).get(x).differentHeightPAs(map.get(y - 1).get(x)))
				{
					//tile.add(new Point(x,y - 1));
					//System.out.println("down");
					recursiveChunk(tile,y - 1,x,checked);

				}
			}
			if(x < map.get(0).size - 1)
			{
				if(map.get(y).get(x).differentHeightPAs(map.get(y).get(x + 1)))
				{
					//tile.add(new Point(x + 1,y));
					//System.out.println("right");
					recursiveChunk(tile,y,x + 1,checked);

				}
			}
			if(x > 0)
			{
				if(map.get(y).get(x).differentHeightPAs(map.get(y).get(x - 1)))
				{
					//tile.add(new Point(x - 1,y));
					//System.out.println("left");
					recursiveChunk(tile,y,x - 1,checked);
					//System.out.println("testing for:" + checked[y][x - 1]);
				}
			}
		}
	}*/
	public int getTrue(boolean[][] checked)
	{
		int num = 0;
		for(boolean[] c : checked)
		{
			for(boolean r : c)
			{
				if(r)
				{
					num ++;
				}
			}
		}
		return num;
	}
	
	
	public boolean walkable(int y,int x)
	{
		return map.get(y).get(x).walkable();
	}
	
	private void collisionCheck()
	{
		for(int p = 0; p < hurtboxableList.size; p ++)
		{
			Hitboxable h = hurtboxableList.get(p);
			int tileX = (int) h.getTileX(tileSize);
			int tileY = (int) h.getTileY(tileSize);
			for(int i = notZero(tileX - 1); i <= tileX + 1; i ++)
			{
				for(int j = notZero(tileY - 1); j <= tileY + 1; j ++)
				{
					if(!map.get(j).get(i).walkable() || !map.get(tileY).get(tileX).walkableTo(map.get(j).get(i)))
					{		
						if(h.willCrossHitBox(i*tileSize, (j + 1)*tileSize, (i + 1)*tileSize, j*tileSize))
						{
							h.stoppedBy(i*tileSize, (j + 1)*tileSize, (i + 1)*tileSize, j*tileSize);
							//break search;
						}
					}
				}
			}
		}	
	}
	public void print()
	{
		for(Array<Tile> a1: map)
		{
			for(Tile a2 : a1)
			{
				System.out.print(a2 + " ");
			}
			System.out.println();
		}
	}
	private int notZero(int i)
	{
		if(i < 0)
		{
			return 0;
		}
		else
		{
			return i;
		}
	}
	public boolean passThroughVis(double y1,double x1,double height1,double y2,double x2,double height2)
	{
		boolean straight = true;
		Array <Point> intersect = new Array<Point>();
		raytraceMap((int)x1,(int)y1,(int)x2,(int)y2,tileSize,intersect);
		double angleOri = findAngleAbs(y1,x1,height1,y2,x2,height2);
		boolean below = isBelowVis(y1,x1,height1,y2,x2,height2); // changed physical to visual?
		double height = 0;
		//System.out.println(angleOri);
		search:
		for(int i = 0; i < intersect.size - 1; i ++)
		{
			double x = (intersect.get(i).getX() + intersect.get(i + 1).getX())/2;
			double y = (intersect.get(i).getY() + intersect.get(i + 1).getY())/2;
			height = getMapVis(y,x);
			double angleTemp = findAngleAbs(y1, x1, height1, y, x,0);
			if(below)
			{
				if(height >= getMapVis(y1,x1) + height1)
				{
					straight = false;
					break search;
				}
				else if(height <=getMapVis(y2,x2) + height2)
				{
					continue search;
				}

				else if(angleTemp > angleOri)
				{
					straight = false;
					break search;
				}
			}
			else
			{
				if(height >= getMapVis(y2,x2) + height2)
				{
					straight = false;
					break search;
				}
				else if(height <= getMapVis(y1,x1) + height1)
				{
					continue search;
				}
				else if(angleTemp < angleOri)
				{
					straight = false;
					break search;
				}
			}
		}	
		return straight;
	}
	public boolean passThroughVisGround(double y1,double x1,double height1,double y2,double x2)
	{
		boolean straight = true;
		Array <Point> intersect = new Array<Point>();
		raytraceMap((int)x1,(int)y1,(int)x2,(int)y2,tileSize,intersect);
		double height2 = getMapPhy(y2,x2);
		double angleOri = findAngleAbs(y1,x1,height1,y2,x2,height2);
		boolean below = isBelowVis(y1,x1,height1,y2,x2,height2); // changed physical to visual?
		double height = 0;
		//System.out.println(angleOri);
		search:
		for(int i = 0; i < intersect.size - 1; i ++)
		{
			double x = (intersect.get(i).getX() + intersect.get(i + 1).getX())/2;
			double y = (intersect.get(i).getY() + intersect.get(i + 1).getY())/2;
			height = getMapVis(y,x);
			double angleTemp = findAngleAbs(y1, x1, height1, y, x,0);
			if(below)
			{
				if(height >= getMapVis(y1,x1) + height1)
				{
					straight = false;
					break search;
				}
				else if(height <=getMapVis(y2,x2) + height2)
				{
					continue search;
				}

				else if(angleTemp > angleOri)
				{
					straight = false;
					break search;
				}
			}
			else
			{
				if(height >= getMapVis(y2,x2) + height2)
				{
					straight = false;
					break search;
				}
				else if(height <= getMapVis(y1,x1) + height1)
				{
					continue search;
				}
				else if(angleTemp < angleOri)
				{
					straight = false;
					break search;
				}
			}
		}	
		return straight;
	}
	public boolean passThroughPhy(double y1,double x1,double height1,double y2,double x2,double height2)
	{
		boolean straight = true;
		Array <Point> intersect = new Array<Point>();
		raytraceMap((int)x1,(int)y1,(int)x2,(int)y2,tileSize,intersect);
		double angleOri = findAngleAbs(y1,x1,height1,y2,x2,height2);
		boolean below = isBelowPhy(y1,x1,height1,y2,x2,height2); // changed
		double height = 0;
		search:
		for(int i = 0; i < intersect.size - 1; i ++)
		{
			double x = (intersect.get(i).getX() + intersect.get(i + 1).getX())/2;
			double y = (intersect.get(i).getY() + intersect.get(i + 1).getY())/2;
			height = getMapPhy(y,x);
			double angleTemp = findAngleAbs(y1, x1, height1, y, x,0);
			if(below)
			{
				if(height >= getMapPhy(y1,x1) + height1)
				{
					straight = false;
					break search;
				}
				else if(height <=getMapPhy(y2,x2) + height2)
				{
					continue search;
				}

				else if(angleTemp > angleOri)
				{
					straight = false;
					break search;
				}
			}
			else
			{
				if(height >= getMapPhy(y2,x2) + height2)
				{
					straight = false;
					break search;
				}
				else if(height <= getMapPhy(y1,x1) + height1)
				{
					continue search;
				}
				else if(angleTemp < angleOri)
				{
					straight = false;
					break search;
				}
			}
		}	
		return straight;
	}
	
	
	public Vector3 mapImpact(double y1,double x1,double height1,double y2,double x2,double height2,double accuracy)
	{
		
		double ground = Math.hypot(x1 - x2,y1 - y2);
		double vertical = height2 - height1;
		double hypoteneuse = Math.hypot(ground, vertical);
		double accuracyRadian = (accuracy * Math.PI) / 180;

		double yDev = ground * Math.tan(accuracyRadian)* (Math.random() - .5);
		double xDev = hypoteneuse * Math.tan(accuracyRadian)* (Math.random() - .5);
		
		Vector2 shotVector = new Vector2((float)(x2 - x1),(float)(y2 - y1));
		double angleOfShot = shotVector.angle();
		Vector2 yVector = new Vector2(0,(float) yDev);
		Vector2 xVector = new Vector2((float) xDev,0);

		yVector.rotate((float)angleOfShot);
		xVector.rotate((float)angleOfShot);
		
		
		Vector2 finalTarget = new Vector2((float)x2,(float)y2);
		
		finalTarget.add(yVector);
		finalTarget.add(xVector);

		double dx = finalTarget.x - x1;
		double dy = finalTarget.y - y1;
		double dz = height2 - height1;
		
		double dMax = Math.hypot(Math.hypot(dx, dy),dz);
		
		Vector3 extendedTarget = new Vector3(finalTarget.x,finalTarget.y,(float)height2);

		Vector3 extendedUnitVector = new Vector3((float)(dx/dMax),(float)(dy/dMax),(float)(dz/dMax));
		
		if(!(height2 < .1))
		{
		while(extendedTarget.x + extendedUnitVector.x > 0 && extendedTarget.x + extendedUnitVector.x < (map.get(0).size * tileSize) - 1 &&
				extendedTarget.y + extendedUnitVector.y > 0 && extendedTarget.y + extendedUnitVector.y < (map.size * tileSize) - 1 &&
				extendedTarget.z + extendedUnitVector.z > 0 && extendedTarget.z + extendedUnitVector.z < MAXHEIGHT)
			{
				extendedTarget.add(extendedUnitVector);
			}
		}
		//System.out.println(x2 + shotVector.x);
		
		x2 = extendedTarget.x;
		y2 = extendedTarget.y;
		height2 = extendedTarget.z;
		
		
		
		Vector3 impact = new Vector3();
		Array <Point> intersect = new Array<Point>();
		raytraceMap((int)x1,(int)y1,(int)x2,(int)y2,tileSize,intersect);
		double angleOri = findAngleAbs(y1,x1,height1,y2,x2,height2);
		double slope = findSlopePhy(y1,x1,height1,y2,x2,height2);
		boolean stopped = false;
		boolean below = isBelowPhy(y1,x1,height1,y2,x2,height2);
		double height = 0;
		search:
		for(int i = 0; i < intersect.size - 1; i ++)
		{
			double x = (intersect.get(i).getX() + intersect.get(i + 1).getX())/2;
			double y = (intersect.get(i).getY() + intersect.get(i + 1).getY())/2;
			height = getMapPhy(y,x);
			double angleTemp = findAngleAbs(y1, x1, height1, y, x,0);
			if(below)
			{
				if(height >= getMapPhy(y1,x1) + height1)
				{
					impact.set(intersect.get(i).getX(),intersect.get(i).getY(),0);
					stopped = true;
					break search;
				}
				else if(height <=getMapPhy(y2,x2) + height2)
				{
					continue search;
				}

				else if(angleTemp > angleOri)
				{
					impact.set(intersect.get(i).getX(),intersect.get(i).getY(),0);
					stopped = true;
					break search;
				}
			}
			else
			{
				if(height >= getMapPhy(y2,x2) + height2)
				{
					impact.set(intersect.get(i).getX(),intersect.get(i).getY(),0);
					stopped = true;
					break search;
				}
				else if(height <= getMapPhy(y1,x1) + height1)
				{
					continue search;
				}
				else if(angleTemp < angleOri)
				{
					impact.set(intersect.get(i).getX(),intersect.get(i).getY(),0);
					stopped = true;
					break search;
				}
			}
		}
		if(!stopped)
		{
			impact.set((float)x2,(float)y2,0);
		}
		impact.z = (float) (((slope * Math.hypot(impact.x - x1, impact.y - y1))) + height1 + getMapPhy(y1,x1)) ;
		if(impact.z > height)
		{
			
			//impact.z = height;
		}
		impact.z -= getMapPhy(impact.y,impact.x);
		//System.out.println(impact);
		// impact.z is false height
		return impact;
	}
	
	private void raytraceMap(int x0, int y0, int x1, int y1,int tileSize,Array <Point> cross)// guarenteed to at the very least include one element in the array - (x1,y1)
	{
	    cross.clear();
		int dx = Math.abs(x1 - x0);// Bresenham's algorithm
	    int dy = Math.abs(y1 - y0); 
	    int x = x0;
	    int y = y0;
	    int n = 1 + dx + dy;
	    int x_inc = (x1 > x0) ? 1 : -1;
	    int y_inc = (y1 > y0) ? 1 : -1;
	    int error = dx - dy;
	    dx *= 2;
	    dy *= 2;
	    for (; n > 0; --n)
	    {
	    	if(x%tileSize == 0 || y%tileSize == 0)
	        {
	    		cross.add(new Point(x,y));
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
	    cross.add(new Point(x1,y1));
	    Iterator <Point>iterator = cross.iterator();
	    Point t = new Point(0,0);
	    int counter = 0;
	    while(iterator.hasNext())
	    {
	    	Point current = iterator.next();
	    	if(current.getX() == t.getX() && Math.abs(current.getY() - t.getY()) < tileSize)
	    	{
	    		if(counter != cross.size - 1)
	    		{
	    			iterator.remove();
	    		}
	    	}
	    	else if(current.getY() == t.getY() && Math.abs(current.getX() - t.getX()) < tileSize)
	    	{
	    		if(counter != cross.size - 1)
	    		{
	    			iterator.remove();
	    		}
	    	}
	    	t.set(current);
	    	counter ++;
	    }    
	} 
	public boolean angleBlock(double y1,double x1,double height1,double y2,double x2,double height2,double yTar,double xTar,double heightTar)
	{
		boolean stopped = false;
		boolean below = isBelowPhy(y1, x1, height1, y2, x2, height2);
		double angleOri = findAngleAbs(y1,x1,height1,yTar,xTar,heightTar);
		double height = getMapPhy(y2,x2) + height2;
		double angleTemp = findAngleAbs(y1, x1, height1, y2, x2,height2);
		if(below)
		{
			if(height >= getMapPhy(y1,x1) + height1)
			{
				stopped = true;
			}
			else if(height <= getMapPhy(yTar,xTar) + heightTar)
			{
				
			}
			else if(angleTemp > angleOri)
			{
				stopped = true;
			}
		}
		else
		{
			if(height >= getMapPhy(yTar,xTar) + heightTar)
			{
				stopped = true;
			}
			else if(height <= getMapPhy(y1,x1) + height1)
			{
				
			}
			else if(angleTemp < angleOri)
			{
				stopped = true;
			}
		}
		return stopped;
	}
	private boolean isBelowPhy(double y1,double x1,double height1,double y2,double x2,double height2)
	{
		return getMapPhy(y1,x1) + height1> getMapPhy(y2,x2) + height2;
	}
	private boolean isBelowVis(double y1,double x1,double height1,double y2,double x2,double height2)
	{
		return getMapVis(y1,x1) + height1> getMapVis(y2,x2) + height2;
	}
	public double findSlopePhy(double y1,double x1,double height1,double y2,double x2,double height2)
	{
		return (double)(((height2 + getMapPhy(y2,x2)) - (height1 + getMapPhy(y1,x1)))/Math.hypot(x2-x1, y2-y1));
	}
	public double findAngleAbs(double y1,double x1,double height1,double y2,double x2,double height2)
	{
		return Math.atan((Math.hypot(x2-x1, y2-y1))/Math.abs((height1 + getMapPhy(y1,x1)) - (height2 + getMapPhy(y2,x2))));
		// remove atan if possible
	}
	public double getMapPhy(double y,double x)
	{
		return map.get((int)(y/tileSize)).get((int)(x/tileSize)).getHeightPhy();
	}
	private double getMapVis(double y,double x)
	{
		return map.get((int)(y/tileSize)).get((int)(x/tileSize)).getHeightVis();
	}
	public Tuple<Boolean,LinkedList<Point>> findPath(int sx, int sy, int tx, int ty) // boolean is whether a path was found
	{
		Tuple<Boolean,LinkedList<Point>> ret = new Tuple<Boolean,LinkedList<Point>>();
		int sizeOfPath = 0;
		/*
		 * Use G or F costs to make sure that 
		 * the sizeOfPath does not exceed the
		 * paramters i.e. the distance cannot exceed
		 * 5 blocks
		 */
		ret.x = true;
		sx /= tileSize;
		sy /= tileSize;
		tx /= tileSize;
		ty /= tileSize;	
		LinkedList<Node> openList = new LinkedList<Node>();
		LinkedList<Node> closedList = new LinkedList<Node>();
	    boolean done = false;
	    Node current;
	    Node start = new Node(sx,sy);
	    Node target = new Node(tx,ty);
	    if(!map.get(ty).get(tx).walkable())
	    {
	    	ret.x = false; // target is unwalkable
	    }
	    if(ret.x)
	    {
		    openList.add(start);
		    calc:
	        while (!done) 
	        {
	            current = Node.lowestFInOpen(openList); 
	            closedList.add(current); 
	            openList.remove(current); 
	            if ((current.matches(tx, ty))) 
	            { 
	            	//return Node.calcPath(start, current,tileSize);
	            	LinkedList<Point> pathway = Node.calcPath(start, current,tileSize);
	            	Collections.reverse(pathway);
	            	ret.y =  pathway;
	            	break calc;
	            }
	            List<Node> adjacentNodes = current.getAdjacent(map.get(0).size, map.size,this,closedList);                    
	            for (int i = 0; i < adjacentNodes.size(); i++) 
	            {
	            	Node currentAdj = adjacentNodes.get(i);         	
	                if (!openList.contains(currentAdj)) 
	                {                  
	                    current.setAsParentAs(currentAdj);
	                    currentAdj.setHCost(target); 
	                    currentAdj.setGCost(start); 
	                    openList.add(currentAdj);
	                }
	                else
	                { 
	                    if (currentAdj.moreCostlyThan(current)) 
	                    { 
	                        current.setAsParentAs(currentAdj);
	                        currentAdj.setGCost(start);
	                    }
	                }
	            }
	            if (openList.isEmpty()) 
	            { 
	                //LinkedList<Point> ret = new LinkedList<Point>();
	               // ret.add(new Point(sx*tileSize,sy*tileSize));
	            	//return ret; 
	            	ret.x = false;
	            }                     
	            //System.out.println("one loop done");   
	        }
	    }
		//return null;
	    return ret;
	}
	public boolean validMove(int sx,int sy,int tx,int ty)
	{
		return map.get(sy).get(sx).walkableTo(map.get(ty).get(tx)) && map.get(ty).get(tx).walkable();
	}
	public boolean sameTile(int x1,int y1,int x2,int y2)
	{
		return ((x1/tileSize) == (x2/tileSize)) && ((y1/tileSize) == (y2/tileSize));
	}
	private boolean outOfRange(double x,double y)// accepts true coordinates
	{
		x /=tileSize;
		y /= tileSize;
		return x < 0 || x > map.get(0).size || y < 0 || y > map.size;
	}
	public Tuple<Boolean,Vector2> findCover(int x,int y,Array<SoldierBattleConcrete> otherHumanoid,int searchCoverDistance)
	{
		Tuple<Boolean,Vector2> ret = new Tuple<Boolean,Vector2>();
		PriorityQueue<Point> allPoint = new PriorityQueue<Point>(new PointComparator(x,y));
		int posX = x/tileSize;
		int posY = y/tileSize;
		// everything is tile coords
		ArrayList <Point> takenPoint = new ArrayList <Point>();
		for(SoldierBattleConcrete h : otherHumanoid)
		{
			takenPoint.add(new Point((int)h.getTileX(tileSize),(int)h.getTileY(tileSize)));
		}
		Array <Point> searchPoint = new Array<Point>();
		Point topLeft = new Point(posX - searchCoverDistance,posY - searchCoverDistance);
		Point bottomRight = new Point(posX + searchCoverDistance,posY + searchCoverDistance);
		toBound(topLeft);
		toBound(bottomRight);
		for(int i = topLeft.getX(); i < bottomRight.getX(); i ++)
		{
			for(int j = topLeft.getY(); j < bottomRight.getY(); j ++)
			{
				Point temp = new Point(i,j);
				if(coverPoint.contains(temp))
				{
					searchPoint.add(temp);
				}
			}
		}
		Iterator <Point>iterator = searchPoint.iterator();
		while(iterator.hasNext())
		{
			Point current = iterator.next();
			if(takenPoint.contains(current))
			{
				iterator.remove();
			}
		}
		// translating to map coords
		for(Point p : searchPoint)
		{
			allPoint.add(new Point(p.getX() * tileSize,p.getY() * tileSize));
		}
		Point poll = allPoint.poll();
		ret.x = poll != null;
		if(poll != null)
		{
			ret.y = new Vector2(poll.getX(),poll.getY());
		}
		return ret;
	}
	private void toBound(Point p)
	{
		if(p.getX() < 0)
		{
			p.setX(0);
		}
		if(p.getY() < 0)
		{
			p.setY(0);
		}
		if(p.getX() >= map.get(0).size)
		{
			p.setX(map.get(0).size - 1);
		}
		if(p.getY() >= map.size)
		{
			p.setY(map.size - 1);
		}
	}
	
	private static class PointComparator implements Comparator<Point>
	{
		private int x;
		private int y;
		
		private PointComparator(int x,int y)
		{
			this.x = x;
			this.y = y;
		}
		
		@Override
		public int compare(Point a, Point b) 
		{
			return (int)Math.hypot(a.getX() - x, a.getY() - y) - (int)Math.hypot(b.getX() - x, b.getY() - y);
		}

		@Override
		public Comparator<Point> reversed() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Comparator<Point> thenComparing(Comparator<? super Point> other) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <U> Comparator<Point> thenComparing(
				Function<? super Point, ? extends U> keyExtractor,
				Comparator<? super U> keyComparator) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <U extends Comparable<? super U>> Comparator<Point> thenComparing(
				Function<? super Point, ? extends U> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Comparator<Point> thenComparingInt(
				ToIntFunction<? super Point> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Comparator<Point> thenComparingLong(
				ToLongFunction<? super Point> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Comparator<Point> thenComparingDouble(
				ToDoubleFunction<? super Point> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}
		public static <T extends Comparable<? super T>> Comparator<T> reverseOrder() {
			// TODO Auto-generated method stub
			return null;
		}

		public static <T extends Comparable<? super T>> Comparator<T> naturalOrder() {
			// TODO Auto-generated method stub
			return null;
		}

		public static <T> Comparator<T> nullsFirst(
				Comparator<? super T> comparator) {
			// TODO Auto-generated method stub
			return null;
		}

		public static <T> Comparator<T> nullsLast(
				Comparator<? super T> comparator) {
			// TODO Auto-generated method stub
			return null;
		}

		public static <T, U> Comparator<T> comparing(
				Function<? super T, ? extends U> keyExtractor,
				Comparator<? super U> keyComparator) {
			// TODO Auto-generated method stub
			return null;
		}

		public static <T, U extends Comparable<? super U>> Comparator<T> comparing(
				Function<? super T, ? extends U> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}

		public static <T> Comparator<T> comparingInt(
				ToIntFunction<? super T> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}

		public static <T> Comparator<T> comparingLong(
				ToLongFunction<? super T> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}

		
		public static <T> Comparator<T> comparingDouble(
				ToDoubleFunction<? super T> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	public double coverDisparity(double y1,double x1,double height1,double y2,double x2,double height2)// errors when y1 or x1 equals y2 or x2. 
	{
		Vector3 keyPoint = new Vector3(0,0,0);
		Array <Point> intersect = new Array<Point>();
		raytraceMap((int)x1,(int)y1,(int)x2,(int)y2,tileSize,intersect);
		//double angleOri = findAngleAbs(y1,x1,height1,y2,x2,height2);
		boolean below = isBelowPhy(y1,x1,height1,y2,x2,height2);
		double angleHighest = 0;
		double height = 0;
		for(int i = 0; i < intersect.size - 1; i ++)
		{
			double x = (intersect.get(i).getX() + intersect.get(i + 1).getX())/2;
			double y = (intersect.get(i).getY() + intersect.get(i + 1).getY())/2;
			height = getMapPhy(y,x);
			double angleTemp;
			if(below)
			{
				angleTemp = findAngleAbs(y1, x1, height1, y, x,0);// viewer is higher
			}
			else
			{
				angleTemp = findAngleAbs(y2, x2, height2, y, x,0);// foe is higher
			}
			// still need to check which original height is higher
			if(Double.compare(angleTemp, angleHighest) > 0)
			{
				angleHighest = angleTemp;
				keyPoint.set((float)x,(float)y,(float)height);
			}
		}	
		
		
		double slopeFromViewer =  findSlopePhy(y1,x1,height1,keyPoint.y,keyPoint.x,0);
		double slopeFromFoe =  findSlopePhy(y2,x2,height2,keyPoint.y,keyPoint.x,0);
		double distance = distance(x1,y1,x2,y2);
		double botRayFromViewer = (double) (getMapPhy(y1,x1) + height1 +  (slopeFromViewer * distance));
		double botRayFromFoe =  (double) (getMapPhy(y2,x2) + height2 +  (slopeFromFoe * distance));
		
		
		double gapOfViewer = getMapPhy(y1,x1) + height1 - botRayFromFoe;
		double gapOfFoe  = getMapPhy(y2,x2) + height2 - botRayFromViewer;
		if(gapOfViewer > height1)
		{
			gapOfViewer = height1;
		}
		if(gapOfFoe > height2)
		{
			gapOfFoe = height2;
		}
		return (gapOfFoe - gapOfViewer);
	}
	private int distance(double x1,double y1,double x2,double y2)
	{
		return (int) Math.hypot(x1 - x2, y1 - y2);
	}
	
	public Array<Vector2> createEnemyMarkerBound(Vector2 enemy,int radius)// creates the 8 corner points that must be checked
	{
		// this algorith will "spread out" 8 different  vectors until it either collides with unwalkable ground or uneven ground, or until it reaches the radius. . Whe
		// where it stops is where each vector is
		Array<Vector2>  ret = new Array<Vector2>();
		Tile enemyTile = getTile(enemy.y,enemy.x);
		Vector2 current = new Vector2();
		

		
		//top right
		//dx = 1;
		//dy = 1;
		current.set(enemy.x,enemy.y);
		search:
			for(int i = 0; i < radius; i ++)
			{
				
			}
		
		// top left

		// bottom right

		// bottom left

		// top
		
		// right
		
		// down
		
		// left
		return ret;
	}
	private Vector2 extendDiagonalToBoundary(Vector2 center,int dx,int dy,int radius)// this will do the job of extending the point until it "hits" a boundary. 
	{
		Vector2 ret = new Vector2();
		
		
		
		return ret;
	}
	private Tile getTile(double y,double x)// x and y are map, not maptile, coordinates
	{
		return map.get((int)(y/tileSize)).get((int)(x/tileSize));
	}
}
