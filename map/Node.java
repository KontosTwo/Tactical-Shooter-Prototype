package com.mygdx.map;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.mygdx.debug.Debugger;
import com.mygdx.physics.Point;

class Node 
{
	private Node parent;
	private int x;
	private int y;
	private int gCost;
	private int hCost;
	
	Node(int x,int y)
	{
		this.x = x;
		this.y = y;
		gCost = 0;
		hCost = 0;
	}
	int getX(){
		return x;
	}
	
	int getY(){
		return y;
	}
		
	boolean matches(int x,int y)
	{
		return this.x == x && this.y == y;
	}
	
	 void setAsParentAs(Node n)
	{
		n.parent = this;
	}
	
	 static Node lowestFInOpen(List <Node> open)
	{
		Node ret = null;
		int lowest = Integer.MAX_VALUE;
		for(Node n : open)
		{
			if(n.getFCost()<lowest)
			{
				lowest = n.getFCost();
				ret = n;
			}
		}
		return ret;
	}
	 void setGCost(Node target)
	{		
		int newGCost = 0;
		boolean found = false;
		Node current = this;
		while(!current.equals(target))
		{
			if(current.x != current.parent.x && current.y != current.parent.y)
			{
				newGCost += 1.4;
			}
			else
			{
				newGCost += 1;
			}
			current = current.parent;
		}
		gCost = newGCost;	
	}
	 void setHCost(Node n)
	{
		hCost = (Math.abs(n.x - this.x) + Math.abs(n.y - this.y));
	}
	private int getFCost()
	{
		return hCost + gCost;
	}
	
	/**
	 * The Node calling createPath should be the
	 * last node (the intended destination of the
	 * pathfinder)
	 */
	List<Point> createPath(int tileSize){
		LinkedList<Point> path = new LinkedList<Point>();
		
		// always add the first node as the first point
		path.add(createMapPoint(tileSize));
		
		// add all linked nodes
		Node next = parent;
		while(next != null){
			path.add(next.createMapPoint(tileSize));
			next = next.parent;
		}
		
		// since we iterate from the destination, the path is in reverse order
		Collections.reverse(path);
		
		return path;
	}
	
	private Point createMapPoint(int tileSize){
		return new Point((x*tileSize) + (tileSize/2),(y*tileSize) + (tileSize/2));
	}
	
	 boolean moreCostlyThan(Node n)
	{
		return this.gCost > n.gCost;
	}
	public String toString()
	{
		return x + " " + y;
	}
	public int hashCode()
	{
		int hash = 1;
        hash = hash * 17 + x;
        hash = hash * 31 + y;
        return hash;
	}
	public boolean equals(Object o)
	{
		return ((Node)o).x == this.x &&  ((Node)o).y == this.y;
	}
}
