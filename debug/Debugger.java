package com.mygdx.debug;

import java.util.HashSet;
import java.util.Set;

public class Debugger 
{
	private static int tick;
	private static Set<Marker> markers;
	
	static
	{
		tick = 0;
		markers = new HashSet<>();
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
