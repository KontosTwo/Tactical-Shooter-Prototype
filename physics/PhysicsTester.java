package com.mygdx.physics;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sun.javafx.geom.Line2D;

public class PhysicsTester {
	float ay = 0;
	float ax = 0;
	float vx = -20;
	float vy = -2000;
	
	@Test
	public void test() 
	{
		System.out.println(getYAtX(-5));
	}
	public float getYAtX(float x)
	{
		return ay + ((x-ax)/vx)*vy;
	}
}
