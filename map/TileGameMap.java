package com.mygdx.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.debug.Debugger;
import com.mygdx.graphic.MapRenderer;
import com.mygdx.misc.Pair;
import com.mygdx.physics.MyVector3;
import com.mygdx.physics.Point;
import com.mygdx.physics.PrecisePoint;
import com.mygdx.physics.VectorEquation;

public class TileGameMap {
	private final MapRenderer renderer;
	private int tileSize;
	private Tile[][] map;
	private int[][] heightPhy;
	private int[][] heightVis;
	
	private final ArrayList <Collidable> collidableList;

	private static final String BACKGROUNDLAYER = "backgroundterrain";
	
	public interface Collidable{
		public boolean aboutToCrossRightOf(int x);
		public boolean aboutToCrossLeftOf(int x);
		public boolean aboutToCrossAbove(int y);
		public boolean aboutToCrossBelow(int y);
		public PrecisePoint getCurrentCollidablePosition();
		public void stoppedbyCollision();
	}
	public interface RayBlockable{
		public MyVector3 getSides();
		public PrecisePoint getCenter();
	}
	
	public TileGameMap(){
		renderer = new MapRenderer();
		map = new Tile[1][1];
		tileSize = 0;
		collidableList = new ArrayList<>();
	}
	public void update(){
		collisionCheck();
	}
	public void render(){
		renderer.render();
	}
	
	public void loadLevel(int level){
		loadLevelData("map/level" + level + ".tmx");
	}
	public void loadLevel(String specialLevel){
		loadLevelData("map/" + specialLevel + ".tmx");
	}
	private void loadLevelData(String file){
		TiledMap mapdata = new TmxMapLoader().load(file);
		MapProperties property = mapdata.getProperties();
		
		// setting dimensions of map
		int height = property.get("height",Integer.class);
		int width = property.get("width",Integer.class);
		tileSize = property.get("tilewidth",Integer.class);
		map = new Tile[height][width];
		for(int x = 0; x < map[0].length; x ++){
			for(int y = 0; y < map.length; y ++){
				map[y][x] = new Tile();
			}
		}
		
		// adding the background
		for(MapLayer tmtl : mapdata.getLayers()){
			TiledMapTileLayer maplayer = (TiledMapTileLayer)tmtl;
			if(tmtl.getName().contains("background")){
				renderer.addTileLayer(maplayer);
			}
			if(tmtl.getName().contains("backgroundterrain")){
				TiledMapTileLayer layer = (TiledMapTileLayer)maplayer;
				for(int row = 0; row < layer.getHeight(); row++) {
					for(int col = 0; col < layer.getWidth(); col++) {
						if(layer.getCell(col, row) != null){
							MapProperties properties = layer.getCell(col, row).getTile().getProperties();
							int heightPhy = Integer.parseInt(properties.get("heightPhy", String.class));
							int heightVis = Integer.parseInt(properties.get("heightVis", String.class));
							boolean walkable = Boolean.parseBoolean((properties.get("walkable", String.class)));
							boolean ramp = Boolean.parseBoolean((properties.get("ramp", String.class)));
							map[row][col].setInfo(heightPhy, heightVis, walkable, ramp);
						}
					}
				}
			}
		}
	}
	
	public void treatAsCollidable(Collidable collider)
	{
		collidableList.add(collider);
	}
	
	private void collisionCheck(){
		for(Collidable collider : collidableList){
			PrecisePoint location = collider.getCurrentCollidablePosition();
			int currentTileX = coordToTileScaling(location.x);
			int currentTileY = coordToTileScaling(location.y);
			Tile center = map[currentTileY][currentTileX];
			Tile left = map[currentTileY][currentTileX - 1];
			Tile right = map[currentTileY][currentTileX + 1];
			Tile top = map[currentTileY + 1][currentTileX];
			Tile bottom = map[currentTileY - 1][currentTileX];
			if(!walkableFromXToY(center,left) && collider.aboutToCrossLeftOf((currentTileX)*tileSize)){
				collider.stoppedbyCollision();
			}
			if(!walkableFromXToY(center,right) && collider.aboutToCrossRightOf(((currentTileX + 1)*tileSize))){
				collider.stoppedbyCollision();
			}
			if(!walkableFromXToY(center,top) && collider.aboutToCrossAbove((currentTileY + 1)*tileSize)){
				collider.stoppedbyCollision();
			}
			if(!walkableFromXToY(center,bottom) && collider.aboutToCrossBelow((currentTileY)*tileSize)){
				collider.stoppedbyCollision();
			}
		}
	}
	/**
	 * Source:
	 * http://tech-algorithm.com/articles/drawing-line-using-bresenham-algorithm/
	 */
	public boolean raytracePossible(int x1,int y1,int z1,int x2,int y2,int z2){
		boolean possible = true;
		VectorEquation ray = new VectorEquation(x1,y1,z1,x2,y2,z2);
		x1 = coordToTileScaling(x1);
		x2 = coordToTileScaling(x2);
		y1 = coordToTileScaling(y1);
		y2 = coordToTileScaling(y2);

		/*
		 * Bresenham's algorithm for identifying
		 * tiles that lay on a vector. Those tiles
		 * will be processed through stoppedBy()
		 */
		int dx = Math.abs(x2 - x1);
	    int dy = Math.abs(y2 - y1); 
	    int x = x1;
	    int y = y1;
	    int n = 1 + dx + dy;
	    int x_inc = (x2 > x1) ? 1 : -1;
	    int y_inc = (y2 > y1) ? 1 : -1;
	    int error = dx - dy;
	    dx *= 2;
	    dy *= 2;
	    traverse:
	    for (; n > 0; --n){
	    	if(stoppedBy(createTileRayBlockable(x,y),ray)){
	    		possible = false;
	    		Debugger.mark(x*tileSize, y*tileSize);
	    		break traverse;
	    	}
	        if(error > 0){
	            x += x_inc;
	            error -= dy;
	        }
	        else{
	            y += y_inc;
	            error += dx;
	        }
	    }
		return possible;
	}
	/**
	 * Precondition - ray and block are guarenteed to intersect
	 * at two points
	 */
	private boolean stoppedBy(RayBlockable obstacle,VectorEquation ray){
		HashSet<PrecisePoint> rayHeuristic = ray.getIntersectionWithSquare(
				obstacle.getCenter().x, 
				obstacle.getCenter().x + obstacle.getSides().getX(), 
				obstacle.getCenter().y,
				obstacle.getCenter().y + obstacle.getSides().getY()
		);
		ArrayList<PrecisePoint> intersections = new ArrayList<>(rayHeuristic);
		PrecisePoint intersectionCenter = new PrecisePoint();
		
		if(intersections.size() == 0){
			return false;
		}
		else if(intersections.size() == 1){
			intersectionCenter = intersections.get(0);
		}else{
			intersectionCenter.x = ((intersections.get(0).x + intersections.get(1).x)/2);
			intersectionCenter.y = ((intersections.get(0).y + intersections.get(1).y)/2);
		}
		
		float heightOfRayAtObstacle = Math.abs(
			ray.getZFromXOrY(intersectionCenter.x)
		);
		float heightOfMapAtObstacle = getHeightPhyAt(
			coordToTileScaling(intersectionCenter.x),
			coordToTileScaling(intersectionCenter.y)
		);
		return heightOfMapAtObstacle > heightOfRayAtObstacle ;
	}
	private RayBlockable createTileRayBlockable(int x,int y){
		return new RayBlockable(){
			
			@Override
			public MyVector3 getSides() {
				return new MyVector3(tileSize,tileSize,getHeightPhyAt(x,y));
			}

			@Override
			public PrecisePoint getCenter() {
				return new PrecisePoint(tileToCoordScaling(x),tileToCoordScaling(y));
			}
		};
	}
	public Pair<Boolean,LinkedList<Point>> findPath(int sx, int sy, int tx, int ty) // boolean is whether a path was found
	{
		Pair<Boolean,LinkedList<Point>> ret = new Pair<Boolean,LinkedList<Point>>();
		int sizeOfPath = 0; 
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
	    if(!map[ty][tx].walkable())
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
	            List<Node> adjacentNodes = current.getAdjacent(map[0].length, map.length,this,closedList);                    
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
	
	boolean validMove(int sx,int sy,int tx,int ty)
	{
		return map[sy][sx].walkableTo(map[ty][tx]) && map[ty][tx].walkable();
	}
	
	private boolean walkableFromXToY(Tile x,Tile y){
		return y.walkable() && x.walkableTo(y);
	}
	
	private Tile getTileFromCoord(double y,double x){
		return map[(int)(y/tileSize)][(int)(x/tileSize)];
	}
	private int coordToTileScaling(double a){
		return (int)(a/tileSize);
	}
	private int tileToCoordScaling(int a){
		return a*tileSize;
	}
	private boolean inBounds(int x,int y){
		return x >= 0 && x < map[0].length && y >= 0 && y < map.length;
	}
	private int getHeightPhyAt(int x,int y){
		return map[y][x].getHeightPhy();
	}
	private static class OverlappingTileException extends Exception{
		
	}
}
