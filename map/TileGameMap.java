package com.mygdx.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.debug.Debugger;
import com.mygdx.graphic.MapRenderer;
import com.mygdx.physics.MyVector3;
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
			int currentTileX = coordToTileAxis(location.x);
			int currentTileY = coordToTileAxis(location.y);
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
		x1 = coordToTileAxis(x1);
		x2 = coordToTileAxis(x2);
		y1 = coordToTileAxis(y1);
		y2 = coordToTileAxis(y2);

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
	    		break traverse;
	    	}
	        if (error > 0){
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
	private boolean stoppedBy(RayBlockable block,VectorEquation ray){
		boolean stopped = false;
		
		
		return stopped;
	}
	private RayBlockable createTileRayBlockable(int x,int y){
		return new RayBlockable(){
			
			@Override
			public MyVector3 getSides() {
				return new MyVector3(tileSize,tileSize,getHeightPhyAt(x,y));
			}

			@Override
			public PrecisePoint getCenter() {
				return new PrecisePoint(x,y);
			}
		};
	}
	

	private boolean walkableFromXToY(Tile x,Tile y){
		return y.walkable() && x.walkableTo(y);
	}
	
	private Tile coordToTile(double y,double x){
		return map[(int)(y/tileSize)][(int)(x/tileSize)];
	}
	private int coordToTileAxis(double a){
		return (int)(a/tileSize);
	}
	private int tileToCoordAxis(int a){
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
