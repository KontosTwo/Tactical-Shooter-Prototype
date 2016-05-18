package com.mygdx.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public interface CameraHoggable 
{
	//public void hogCamera(Camera cam,Vector2 lead);
	public Vector2 provideCenterCamera();
}
