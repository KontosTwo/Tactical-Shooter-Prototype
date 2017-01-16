package com.mygdx.graphic;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.mygdx.camera.Camera;

public final class MapRenderer extends OrthogonalTiledMapRenderer
{
	private LinkedList <TiledMapTileLayer> back;
	
	public MapRenderer()
	{
		super(null,BatchCoordinator.getNightShader());
		back = new LinkedList <TiledMapTileLayer>();
	}
	
	public void setScale(int scale)
	{
		unitScale = scale;
	}
	
	public void render()
	{
		BatchCoordinator.sendMapRenderRequest((SpriteBatch)batch, (Camera c) ->
		{
			setView(c);
			AnimatedTiledMapTile.updateAnimationBaseTime();
			Iterator <TiledMapTileLayer>iterator = back.iterator();
			while(iterator.hasNext())
			{
				renderTileLayer(iterator.next());
			}
		});
	}
	
	/*public void loadMap(TiledMap tiledMap)
	{
		setMap(tiledMap);
	}*/
	
	public void addTileLayer(TiledMapTileLayer tmtl)
	{
		back.add(tmtl);
	}
}
