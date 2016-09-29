package com.mygdx.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Consumer;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.debug.Debugger;
import com.mygdx.graphic.MapRenderer;
import com.mygdx.physics.MyVector3;
import com.mygdx.physics.PrecisePoint;
import com.mygdx.physics.PrecisePoint3;
import com.mygdx.physics.VectorEquation;
/**
 * Stores tile data and provides methods that allow
 * one to interact with the map environment
 * 
 * @author Vincent Li
 */
public final class GameMap {
	private final MapRenderer renderer;
	private final MapData map;
	
	private final ArrayList <Collidable> collidableList;

	public interface Collidable{
		public boolean aboutToCrossRightOf(int x);
		public boolean aboutToCrossLeftOf(int x);
		public boolean aboutToCrossAbove(int y);
		public boolean aboutToCrossBelow(int y);
		public PrecisePoint getCollidablePosition();
		public void stoppedbyCollision();
	}
	public interface HitBoxable{
		public MyVector3 getSides();
		public PrecisePoint getCenter();
	}
	
	public GameMap(){
		renderer = new MapRenderer();
		map = new MapData();
		collidableList = new ArrayList<>(1);
	}
	public void update(){
		collisionCheck();
	}
	public void render(){
		renderer.render();
	}
	
	public void loadLevel(int level){
		loadLevelHelper("map/level" + level + ".tmx");
	}
	public void loadLevel(String specialLevel){
		loadLevelHelper("map/" + specialLevel + ".tmx");
	}
	
	private void loadLevelHelper(String file){
		TiledMap mapdata = new TmxMapLoader().load(file);
		map.loadLevelData(mapdata);
		readBackgroundData(mapdata);
	}
	private void readBackgroundData(TiledMap mapdata){
		for(MapLayer tmtl : mapdata.getLayers()){			
			renderer.addTileLayer((TiledMapTileLayer)tmtl);
		}
	}
	
	public void treatAsCollidable(Collidable collider){
		collidableList.add(collider);
	}
	
	private void collisionCheck(){
		for(Collidable collider : collidableList){
			PrecisePoint location = collider.getCollidablePosition();
			int colliderTileX = map.scaleToTileCoord(location.x);
			int colliderTileY = map.scaleToTileCoord(location.y);
			Tile center = map.createTileAt(colliderTileX, colliderTileY);
			
			// creating surrounding tiles
			Tile left = map.createTileAt(colliderTileX - 1, colliderTileY);
			Tile right = map.createTileAt(colliderTileX + 1, colliderTileY);
			Tile top = map.createTileAt(colliderTileX, colliderTileY + 1);
			Tile bottom = map.createTileAt(colliderTileX, colliderTileY - 1);
			
			// determine if the collider will cross adjacent tiles. If so, move the collider back to its original location
			if(!walkableFromAToB(center,left) && collider.aboutToCrossLeftOf(map.scaleToMapCoord(colliderTileX))){
				collider.stoppedbyCollision();
			}
			if(!walkableFromAToB(center,right) && collider.aboutToCrossRightOf(map.scaleToMapCoord(colliderTileX + 1))){
				collider.stoppedbyCollision();
			}
			if(!walkableFromAToB(center,top) && collider.aboutToCrossAbove(map.scaleToMapCoord(colliderTileY + 1))){
				collider.stoppedbyCollision();
			}
			if(!walkableFromAToB(center,bottom) && collider.aboutToCrossBelow(map.scaleToMapCoord(colliderTileY))){
				collider.stoppedbyCollision();
			}
		}
	}
	
	public boolean canSee(PrecisePoint3 observer,PrecisePoint3 target){
		return raytracePossible(map.getHeightVisView(), observer,target);
	}
	
	public boolean canShoot(PrecisePoint3 shooter,PrecisePoint3 target){
		return raytracePossible(map.getHeightPhyView(), shooter,target);
	}
	
	private boolean raytracePossible(int[][] heightView,PrecisePoint3 start,PrecisePoint3 target){
		boolean possible = true;
		
		map.scaleToTerrainHeight(start);
		map.scaleToTerrainHeight(target);
		
		VectorEquation ray = new VectorEquation(start,target);
		int x1 = map.scaleToTileCoord(start.x);
		int x2 = map.scaleToTileCoord(target.x);
		int y1 = map.scaleToTileCoord(start.y);
		int y2 = map.scaleToTileCoord(target.y);
		
		/*
		 * Bresenham's algorithm for identifying
		 * tiles that lay on a vector. Those tiles
		 * will be processed through stoppedBy()
		 * Source:
		 * http://tech-algorithm.com/articles/drawing-line-using-bresenham-algorithm/
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
	    	
	    	// the first tile that stops the ray will create an intersection
	    	if(stoppedBy(createTileRayBlockable(x,y,heightView),ray,heightView)){
	    		possible = false;
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
	 * Returns whether the ray intersects the obstacle
	 */
	private boolean stoppedBy(HitBoxable obstacle,VectorEquation ray,int[][] heightView){
		
		// find all the points where the ray intersects the obstacle
		HashSet<PrecisePoint> rayHeuristic = ray.getIntersectionWithSquare(
				obstacle.getCenter().x, 
				obstacle.getCenter().x + obstacle.getSides().getX(), 
				obstacle.getCenter().y,
				obstacle.getCenter().y + obstacle.getSides().getY()
		);
		ArrayList<PrecisePoint> intersections = new ArrayList<>(rayHeuristic);
		PrecisePoint intersectionCenter = new PrecisePoint();
		
		// determining which points, if any, exist
		if(intersections.size() == 0){
			return false;
		}
		else if(intersections.size() == 1){
			intersectionCenter = intersections.get(0);
		}else{
			intersectionCenter.x = ((intersections.get(0).x + intersections.get(1).x)/2);
			intersectionCenter.y = ((intersections.get(0).y + intersections.get(1).y)/2);
		}
		
		// obtaining the heights of the obstacle and at the ray
		float heightOfRayAtObstacle = Math.abs(
			ray.getZFromXOrY(intersectionCenter.x)
		);
		float heightOfMapAtObstacle = 
				heightView[map.scaleToTileCoord(intersectionCenter.y)]
						[map.scaleToTileCoord(intersectionCenter.x)];
		
		// if the obstacle's height is higher than that of the ray, the obstacle "stops" the ray
		if(heightOfMapAtObstacle > heightOfRayAtObstacle){
			Debugger.mark(intersectionCenter.x, intersectionCenter.y);
		}
		return heightOfMapAtObstacle > heightOfRayAtObstacle ;
	}
	/**
	 * Creates a representation of the terrain
	 * at coordinates (x,y) as a 3-D box
	 * Accepts tile coordinates
	 */
	private HitBoxable createTileRayBlockable(int x,int y,int[][] heightView){
		return new HitBoxable(){
			
			@Override
			public MyVector3 getSides() {
				return new MyVector3(map.getTileSize(),map.getTileSize(),heightView[y][x]);
			}

			@Override
			public PrecisePoint getCenter() {
				return new PrecisePoint(map.scaleToMapCoord(x),map.scaleToMapCoord(y));
			}
		};
	}
	/**
	 * Returns a list of points representing the shortest
	 * path from (sx,sy) to (tx,ty), and a boolean signifying whether
	 * the path exists
	 * 
	 * Accepts map coordinates as parameters
	 */
	public Path findPath(PrecisePoint start,PrecisePoint target,int maxDistance) {				
		boolean pathPossible = true;
		List<PrecisePoint> shortestPath = new LinkedList<PrecisePoint>();

		//double start = System.currentTimeMillis();
		
		
		
		
		
		int sx = map.scaleToTileCoord(start.x);
		int sy = map.scaleToTileCoord(start.y);
		int tx = map.scaleToTileCoord(target.x);
		int ty = map.scaleToTileCoord(target.y);	
		
		if(!map.inBounds(tx, ty) || !map.createTileAt(tx, ty).walkable()){
			pathPossible = false; 
	    	return new Path(shortestPath,pathPossible,false);
	    }
		//TreeSet<Node> openList = new TreeSet<Node>(Node.createComparator());
		Collection<Node> openList = new HashSet<Node>();
		Collection<Node> closedList = new HashSet<Node>();
	    Node startingNode = new Node(sx,sy);
	    Node targetNode = new Node(tx,ty);
	  
	    openList.add(startingNode);
	    calc:
        while (true) {
        	
        	// considering the node closest to the target
        	Node currentNode = Node.lowestFInOpen(openList); 
        	//Node currentNode = openList.first();
            closedList.add(currentNode); 
            openList.remove(currentNode); 
            
            // if the shortest path is found
            if ((currentNode.equals(targetNode))) { 
            	List<PrecisePoint> pathway = currentNode.createPath(map.getTileSize());
            	shortestPath =  pathway;
            	break calc;
            }        
            
            // sifting through adjacentNodes
            List<Node> adjacentNodes = getAdjacentNodes(currentNode,closedList);
            for (Node currentAdjNode : adjacentNodes) {
                if(!openList.contains(currentAdjNode)) {                  
                    currentNode.setAsParentAs(currentAdjNode);  
                    currentAdjNode.calculateCost(startingNode, targetNode);
                    
                    // check to see whether the current node is within range of the startingNode
                    if(currentAdjNode.withinRangeOfOrigin(maxDistance)){      
                    	openList.add(currentAdjNode);
                    }                
                }
                else if (currentAdjNode.moreCostlyThan(currentNode)){                   
                    currentNode.setAsParentAs(currentAdjNode);
                    currentAdjNode.calculateCost(startingNode, targetNode);
                }
            }
            if (openList.isEmpty()) { 
            	pathPossible = false;
            	return new Path(shortestPath,pathPossible,shortestPath.isEmpty());
            }                     
        }
	    //System.out.println(System.currentTimeMillis());
	    //System.out.println(System.currentTimeMillis() - start);
	    return new Path(shortestPath,pathPossible,shortestPath.isEmpty());
	}
	
	private List<Node> getAdjacentNodes(Node center,Collection<Node> closedList){
		List<Node> adjacentNodes = new ArrayList<Node>(8);
		/*
		 * This functional interface will add
		 * every valid node adjacent to center to
		 * adjacentNodes
		 */
		Consumer<Point> function = point ->{
			Node adjNode = new Node(center.getX() + point.getX(),center.getY() + point.getY());
			if(inBounds(adjNode) && walkableFromAToB(center,adjNode) && !closedList.contains(adjNode)){
				adjacentNodes.add(adjNode);
			}
		};
		CoordinateDirection.iterateAdjacent(function);
		return adjacentNodes;
	}
	
	/**
	 * Uses tile coordinates
	 */
	private boolean walkableFromAToB(int sx,int sy,int tx,int ty)
	{
		return walkableFromAToB(map.createTileAt(sx, sy),map.createTileAt(tx, ty));
	}
	private boolean walkableFromAToB(Tile x,Tile y){
		return y.walkable() && x.walkableTo(y);
	}
	private boolean walkableFromAToB(Node a,Node b){
		return walkableFromAToB(a.getX(),a.getY(),b.getX(),b.getY());
	}
	private boolean inBounds(Node node){
		return map.inBounds(node.getX(),node.getY());
	}
}