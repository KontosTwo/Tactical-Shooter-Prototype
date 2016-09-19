package com.mygdx.debug;

import java.util.ArrayList;

public class Debugger 
{
	private static int tick;
	private static ArrayList<Marker> markers;
	
	static
	{
		tick = 0;
		markers = new ArrayList<>();
	}
	public static void render()
	{
		markers.forEach(m -> m.render());
	}
	public static void update(float dt)
	{
		markers.forEach(m -> m.update(dt));
		tick ++;
	}
	public static void tick()
	{
		System.out.println(tick);
	}
	public static void tick(String message)
	{
		System.out.println(tick + " " + message);
	}
	public static void mark(double x,double y)
	{
		markers.add(new Marker(x,y));
	}
}
