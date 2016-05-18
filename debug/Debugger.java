package com.mygdx.debug;

public class Debugger 
{
	private static int tick;
	
	static
	{
		tick = 0;
	}
	
	Debugger()
	{

	}
	
	public static void update()
	{
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
}
