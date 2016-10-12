package com.mygdx.graphic;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mygdx.physics.PrecisePoint;
import com.mygdx.physics.PrecisePoint3;

public abstract class MyShapeRenderer {
	protected ShapeRenderer shapeRenderer;
	
	public MyShapeRenderer(){
		shapeRenderer = new ShapeRenderer();
	}
	public final void render(){
		BatchCoordinator.sendShapeRenderRequest(() ->{
			renderProcess();
			
		});
	}
	protected abstract void renderProcess();
}
