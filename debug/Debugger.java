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
	
	private enum MarkerType{
		
	}
	
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
	public static void mark(PrecisePoint location,String name){
		mark(location.x,location.y,name);
	}
	public static void mark(double x,double y,String name)
	{
		markers.add(new Marker(x,y,getMarkerFilePathFromName(name)));
	}
	public static void poke(double x,double y,String name){
		temporaryMarkers.add(new Marker(x,y,getMarkerFilePathFromName(name)));
	}
	public static void poke(PrecisePoint location,String filepath){
		poke(location.x,location.y,filepath);
	}
	
	private static String getMarkerFilePathFromName(String name){
		String filepath = "";
		switch(name){
			case "beige" : filepath = "full.png";
				break;
			case "teal" : filepath = "full2.png";
				break;
			default: filepath = "full2.png";
				break;
		}
		return filepath;
	}
}
