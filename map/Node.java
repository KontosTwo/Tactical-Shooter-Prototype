package com.mygdx.map;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import com.mygdx.debug.Debugger;
import com.mygdx.physics.PrecisePoint;

final class Node {
	private Node parent;
	private final int x;
	private final int y;
	private int gCost;
	private int hCost;
	private int fCost;
	
	Node(int x,int y){
		this.x = x;
		this.y = y;
		gCost = 0;
		hCost = 0;
		fCost = 0;
	}
	int getX(){
		return x;
	}
	
	int getY(){
		return y;
	}
	
	 void setAsParentAs(Node n){
		n.parent = this;
	}
	 boolean withinRangeOfOrigin(int maxDistanceFromOrigin){
		 return fCost <= maxDistanceFromOrigin;
	 }
	
	 static Node lowestFInOpen(Collection <Node> open){
		Node ret = null;
		int lowest = Integer.MAX_VALUE;
		for(Node n : open)
		{
			if(n.fCost<lowest)
			{
				lowest = n.fCost;
				ret = n;
			}
		}
		return ret;
	}
	 
	void calculateCost(Node start,Node target){
		setGCost(start);
		setHCost(target);
		setFCost();
	}
	private void setGCost(Node start){		
		int newGCost = 0;
		Node current = this;
		
		//calculates the length of the path from this node to the starting node
		while(!current.equals(start)){
			if(current.x != current.parent.x && current.y != current.parent.y){
				newGCost += 1.4;
			}
			else{
				newGCost += 1;
			}
			current = current.parent;
		}
		
		gCost = newGCost;	
	}
	
	/**
	 * Calculates Manhattan distance between this Node and Node n
	 */
	private void setHCost(Node n){
		hCost = (Math.abs(n.x - this.x) + Math.abs(n.y - this.y));
	}
	
	private void setFCost(){
		fCost =  hCost + gCost;
	}
	
	/**
	 * The Node calling createPath should be the
	 * last node (the intended destination of the
	 * pathfinder)
	 */
	List<PrecisePoint> createPath(int tileSize){
		LinkedList<PrecisePoint> path = new LinkedList<>();
		
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
		
		// eliminate the first node. The actor is already at the first node. 
		path.pollFirst();
		return path;
	}
	
	private PrecisePoint createMapPoint(int tileSize){
		return new PrecisePoint((x*tileSize) + (tileSize/2),(y*tileSize) + (tileSize/2));
	}
	
	 boolean moreCostlyThan(Node n)
	{
		return this.gCost > n.gCost;
	}
	static Comparator<Node> createComparator(){
		 return new Comparator<Node>(){
			@Override
			public int compare(Node o1, Node o2) {			
				return o1.fCost - o2.fCost;
			}
		 };
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
	public boolean equals(Object other){
		if(this == other){
			return true;
		}
		if(other == null){
			return false;
		}
		if(!(other instanceof Node)){
			return false;
		}
		return ((Node)other).x == this.x &&  ((Node)other).y == this.y;
	}
}
