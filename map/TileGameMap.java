package com.mygdx.map;

import java.util.ArrayList;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.graphic.MapRenderer;
import com.mygdx.physics.MyVector3;
import com.mygdx.physics.PrecisePoint;

public class TileGameMap {
	private final MapRenderer renderer;
	private int tileSize;
	private Tile[][] map;
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
			int currentTileX = coordToTile(location.x);
			int currentTileY = coordToTile(location.y);
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
	private boolean walkableFromXToY(Tile x,Tile y)
	{
		return y.walkable() && x.walkableTo(y);
	}
	
	private Tile coordToTile(double y,double x)
	{
		return map[(int)(y/tileSize)][(int)(x/tileSize)];
	}
	private int coordToTile(double x)
	{
		return (int)(x/tileSize);
	}
	private boolean inBounds(int x,int y)
	{
		return x >= 0 && x < map[0].length && y >= 0 && y < map.length;
	}
	private static class OverlappingTileException extends Exception
	{
		
	}
}
