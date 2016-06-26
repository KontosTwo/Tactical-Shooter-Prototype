package com.mygdx.misc;

public class TimeCapsule<E>
{
	/*
	 * Stores any object, and notifies when time has passed. 
	 */
	private int age;
	private final E capsule;
	private final int waitingTime;
	
	public TimeCapsule(E capsule,int waitingTime)
	{
		age = 0;
		this.capsule = capsule;
		this.waitingTime = waitingTime;
	}
	public void update()
	{
		age ++;
	}
	public boolean ready()
	{
		return age > waitingTime;
	}
	public E retrieve()
	{
		return capsule;
	}
	public String toString()
	{
		return "" + capsule.toString();
	}
}
