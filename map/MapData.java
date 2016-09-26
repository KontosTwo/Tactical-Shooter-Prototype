package com.mygdx.map;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

final class MapData {
	
	private int tileSize;
	private int height;
	private int width;
	private int[][] heightPhyMap;
	private int[][] heightVisMap;
	private boolean[][] walkableMap;
	private boolean[][] rampMap;

	private boolean [][]terrainAlreadySet;
	private boolean [][]obstacleAlreadySet;
	
	private final static int GROUNDLEVEL = 0;

	private static final String TERRAINLAYERSTRING = "terrain";
	private static final String BACKGROUNDLAYERSTRING = "background";
	private static final String OBSTACLELAYERSTRING = "obstacle";

	
	MapData(){
		
	}
	/**
	 * 
	 * @return The matrix of all the visual heights
	 */
	int[][] getHeightVisView(){
		return heightVisMap;
	}
	/**
	 * 
	 * @return The matrix of all the physical heights
	 */
	int[][] getHeightPhyView(){
		return heightPhyMap;
	}

	void loadLevelData(TiledMap mapdata){
		readMapDimensions(mapdata);
		readTerrainData(mapdata);
		readObstacleData(mapdata);
	}
	
	private void readMapDimensions(TiledMap mapdata){
		MapProperties dimensionsProperty = mapdata.getProperties();
		height = dimensionsProperty.get("height",Integer.class);
		width = dimensionsProperty.get("width",Integer.class);
		tileSize = dimensionsProperty.get("tilewidth",Integer.class);
		heightPhyMap = new int[height][width];
		heightVisMap = new int[height][width];
		walkableMap = new boolean[height][width];
		rampMap = new boolean[height][width];
		terrainAlreadySet = new boolean[height][width];
		obstacleAlreadySet = new boolean[height][width];
		
		for(int x = 0; x < width; x ++){
			for(int y = 0; y < height; y ++){
				heightPhyMap[y][x] = GROUNDLEVEL;
				heightVisMap[y][x] = GROUNDLEVEL;
				walkableMap[y][x] = true;
			}
		}
	}
	
	private void readTerrainData(TiledMap mapdata){
		List<TiledMapTileLayer> dataLayers = new ArrayList<>(15);
		for(MapLayer tmtl : mapdata.getLayers()){
			if(tmtl.getName().contains(TERRAINLAYERSTRING)){
				dataLayers.add((TiledMapTileLayer)tmtl);
			}
		}
		for(TiledMapTileLayer currentTerrainLayer : dataLayers){
			for(int row = 0; row < currentTerrainLayer.getHeight(); row++) {
				for(int col = 0; col < currentTerrainLayer.getWidth(); col++) {
					if(currentTerrainLayer.getCell(col, row) != null){
						MapProperties properties = currentTerrainLayer.getCell(col, row).getTile().getProperties();
						int heightPhy = Integer.parseInt(properties.get("heightPhy", String.class));
						int heightVis = Integer.parseInt(properties.get("heightVis", String.class));
						boolean walkable = Boolean.parseBoolean((properties.get("walkable", String.class)));
						boolean ramp = Boolean.parseBoolean((properties.get("ramp", String.class)));
						if(terrainAlreadySet[row][col]){
							System.err.println("Overlapping of terrain tiles found (in cartesian coordinates) at " + col + ", " + row);
							System.exit(1);
						}
						heightPhyMap[row][col] = heightPhy;
						heightVisMap[row][col] = heightVis;
						walkableMap[row][col] = walkable;
						rampMap[row][col] = ramp;
						
						terrainAlreadySet[row][col] = true;
					}
				}
			}
		}
	}
	
	private void readObstacleData(TiledMap mapdata){
		List<TiledMapTileLayer> dataLayers = new ArrayList<>(15);
		for(MapLayer tmtl : mapdata.getLayers()){
			if(tmtl.getName().contains(OBSTACLELAYERSTRING)){
				dataLayers.add((TiledMapTileLayer)tmtl);
			}
		}
		for(TiledMapTileLayer currentTerrainLayer : dataLayers){
			for(int row = 0; row < currentTerrainLayer.getHeight(); row++) {
				for(int col = 0; col < currentTerrainLayer.getWidth(); col++) {
					if(currentTerrainLayer.getCell(col, row) != null){
						MapProperties properties = currentTerrainLayer.getCell(col, row).getTile().getProperties();
						int heightPhy = Integer.parseInt(properties.get("heightPhy", String.class));
						int heightVis = Integer.parseInt(properties.get("heightVis", String.class));
						boolean walkable = Boolean.parseBoolean((properties.get("walkable", String.class)));
						boolean ramp = Boolean.parseBoolean((properties.get("ramp", String.class)));
						if(obstacleAlreadySet[row][col]){
							System.err.println("Overlapping of obstacle tiles found (in cartesian coordinates) at " + col + ", " + row);
							System.exit(1);
						}
						heightPhyMap[row][col] += heightPhy;
						heightVisMap[row][col] += heightVis;
						walkableMap[row][col] = walkable;
						rampMap[row][col] = ramp;
						
						obstacleAlreadySet[row][col] = true;
					}
				}
			}
		}
	}
	/**
	 * Uses tilecoordinates
	 * @param x
	 * @param y
	 * @return The Tile at x and y
	 */
	Tile createTileAt(int x,int y){
		return new Tile(heightPhyMap[y][x],heightVisMap[y][x],walkableMap[y][x],rampMap[y][x]);
	}
	/**
	 * Uses tile coordinates
	 */
	boolean inBounds(int x,int y){
		return x >= 0 && x < width && y >= 0 && y < height;
	}
	int scaleToMapCoord(int tileCoord){
		return tileCoord * tileSize;
	}
	int scaleToTileCoord(double mapCoord){
		return (int)(mapCoord/tileSize);
	}
	int getHeightOfTerrain(int x,int y){
		return heightPhyMap[y][x];
	}
	int getTileSize(){
		return tileSize;
	}
}
