package com.mygdx.graphic;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.utils.Array;

public final class MapRenderer extends OrthogonalTiledMapRenderer
{
	private LinkedList <TiledMapTileLayer> front;
	private LinkedList <TiledMapTileLayer> back;
	
	public MapRenderer()
	{
		super(null,BatchCoordinator.getNightShader());
		front = new LinkedList <TiledMapTileLayer>();
		back = new LinkedList <TiledMapTileLayer>();
	}
	
	public void setScale(int scale)
	{
		unitScale = scale;
	}
	public void renderFront()
	{
		BatchCoordinator.sendRenderRequest((SpriteBatch)batch, () ->
		{
			AnimatedTiledMapTile.updateAnimationBaseTime();
			Iterator <TiledMapTileLayer>iterator = front.iterator();
			while(iterator.hasNext())
			{
				renderTileLayer(iterator.next());
			}
		});
	}
	
	public void renderMiddle()
	{
		
	}
	
	public void renderBack()
	{
		BatchCoordinator.sendRenderRequest((SpriteBatch)batch, () ->
		{
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
	
	public void addToFront(TiledMapTileLayer tmtl)
	{
		front.add(tmtl);
	}
	
	public void addToBack(TiledMapTileLayer tmtl)
	{
		back.add(tmtl);
	}
	public void setView(OrthographicCamera cam)
	{
		super.setView(cam);
	}
}
