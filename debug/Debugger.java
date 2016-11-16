package com.mygdx.debug;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import com.mygdx.physics.PrecisePoint;

public class Debugger 
{
	private static int tick;
	private static Set<Marker> markers;
	private static Collection<Marker> temporaryMarkers;
	
	static
	{
		tick = 0;
		markers = new HashSet<>();
		temporaryMarkers = new Stack<>();
	}
	public static void render()
	{
		markers.forEach(m -> m.render());
		temporaryMarkers.forEach(m -> m.render());
		temporaryMarkers.clear();
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
	public static void mark(PrecisePoint location){
		mark(location.x,location.y);
	}
	public static void mark(double x,double y)
	{
		markers.add(new Marker(x,y));
	}
	public static void poke(double x,double y){
		temporaryMarkers.add(new Marker(x,y));
	}
}
