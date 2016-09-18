package com.mygdx.graphic;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.camera.Camera;
public final class BatchCoordinator 
{
	private static final HashMap<SpriteBatch,ArrayList<RenderRequest> >renderRequests = new HashMap<>();
	private static final HashMap<SpriteBatch,ArrayList<MapRenderRequest> >mapRenderRequests = new HashMap<>();
	private static Camera camera;
	/**
	 * A functional interface whose code
	 * contains the logic that renders
	 */
	interface RenderRequest
	{
		public void render();
	}
	interface MapRenderRequest
	{
		public void render(Camera camera);
	}
	
	private enum BatchType
	{
		NIGHT(.2f,.2f,.5f,1f),
		DAY(1f,0f,0f,1f),
		GAS(1f,1f,1f,1f);
		
		private BatchType(float r,float g,float b,float t)
		{
			red = r;
			green = g;
			blue = b;
			transparency = t;
		}
		
		private SpriteBatch batch;
		private float red;
		private float green;
		private float blue;
		private float transparency;
	}
	
	public static void createBatch(Camera cam)
	{
		for(BatchType bt : BatchType.values())
		{
			if(bt.batch == null)
			{
				bt.batch = new SpriteBatch();
				bt.batch.setColor(bt.red, bt.green, bt.blue, bt.transparency);
			}
		}
		camera = cam;
	}	
	
	public static void coordinatedRender()
	{
		for(SpriteBatch sb :mapRenderRequests.keySet())
		{
			// can only set projection matrix one at a time
			sb.setProjectionMatrix(camera.combined);
			sb.begin();
			for(MapRenderRequest request : mapRenderRequests.get(sb))
			{
				request.render(camera);
			}
			sb.end();
		}
		mapRenderRequests.clear();
		
		for(SpriteBatch sb :renderRequests.keySet())
		{
			// can only set projection matrix one at a time
			sb.setProjectionMatrix(camera.combined);
			sb.begin();
			for(RenderRequest request : renderRequests.get(sb))
			{
				request.render();
			}
			sb.end();
		}
		renderRequests.clear();
	}
	static void sendRenderRequest(SpriteBatch sb,RenderRequest request)
	{
		if(!renderRequests.containsKey(sb))
		{
			renderRequests.put(sb, new ArrayList<RenderRequest>());
		}
		renderRequests.get(sb).add(request);
	}
	static void sendMapRenderRequest(SpriteBatch sb,MapRenderRequest request)
	{
		if(!mapRenderRequests.containsKey(sb))
		{
			mapRenderRequests.put(sb, new ArrayList<MapRenderRequest>());
		}
		mapRenderRequests.get(sb).add(request);
	}
	static SpriteBatch getNightShader()
	{
		return BatchType.NIGHT.batch;
	}
	static SpriteBatch getDayShader()
	{
		return BatchType.DAY.batch;
	}
	static SpriteBatch getTransparentShader()
	{
		return BatchType.GAS.batch;
	}
}
