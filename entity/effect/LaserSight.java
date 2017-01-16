package com.mygdx.entity.effect;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mygdx.graphic.Animator;
import com.mygdx.graphic.BatchCoordinator;
import com.mygdx.graphic.MyShapeRenderer;
import com.mygdx.physics.PrecisePoint;
import com.mygdx.physics.PrecisePoint3;

public class LaserSight extends MyShapeRenderer
{
	private final PrecisePoint3 origin;
	private final PrecisePoint3 target;
	
	public LaserSight(){
		origin = new PrecisePoint3();
		target = new PrecisePoint3();
	}
	public void setOrigin(PrecisePoint3 location){
		origin.set(location);
	}
	public void setTarget(PrecisePoint location){
		target.set(location.x,location.y,0);
	}

	@Override
	protected void renderProcess() {
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setProjectionMatrix(BatchCoordinator.camera.getCombined());
		shapeRenderer.line(origin.x,origin.y + origin.z, target.x, target.y);
		shapeRenderer.end();		
	}
}
