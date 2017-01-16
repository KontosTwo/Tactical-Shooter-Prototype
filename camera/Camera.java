package com.mygdx.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.graphic.Animator.CameraBoundaryProvider;
import com.mygdx.physics.MyMath;
import com.mygdx.physics.MyVector2;
import com.mygdx.physics.PrecisePoint;

public class Camera implements CameraBoundaryProvider {
	private Vector3 CameraBoundaryProviderHelper;
	private final CameraActionCreator actionFactory;
	private final OrthographicCamera camera;

	public Camera() {
		super();
		CameraBoundaryProviderHelper = new Vector3();
		actionFactory = new CameraActionCreator(this);
		camera = new OrthographicCamera();
	}

	public void update() {

	}

	public PrecisePoint unproject(PrecisePoint point){
		Vector3 truePoint = camera.unproject(new Vector3(point.x,point.y,0));
		point.set(truePoint.x, truePoint.y);
		return point;
	}


	public Matrix4 getCombined(){
		return camera.combined;
	}

	void translate(double x,double y){
		camera.position.x += x;
		camera.position.y += y;
	}

	void translate(MyVector2 vector){
		camera.position.x += vector.getX();
		camera.position.y += vector.getY();
	}

	void snapTo(double x,double y){
		camera.position.x = (float)x;
		camera.position.y = (float)y;
	}

	@Override
	public float getLeftBoundary() {
		CameraBoundaryProviderHelper.x = 0;
		return unproject(CameraBoundaryProviderHelper).x;
	}
	@Override
	public float getRightBoundary() {
		CameraBoundaryProviderHelper.x = viewportWidth;
		return unproject(CameraBoundaryProviderHelper).x;
	}
	@Override
	public float getTopBoundary() {
		CameraBoundaryProviderHelper.y = 0;
		return unproject(CameraBoundaryProviderHelper).y;
	}
	@Override
	public float getBottomBoundary() {
		CameraBoundaryProviderHelper.y = viewportHeight;
		return unproject(CameraBoundaryProviderHelper).y;
	}
}
