package com.mygdx.map;

import java.util.LinkedList;

import com.mygdx.misc.Point;

class Node 
{
	private Node parent;
	private int x;
	private int y;
	private int g;
	private int h;
	
	public Node(int x,int y)
	{
		this.x = x;
		this.y = y;
		g = 0;
		h = 0;
	}
	public boolean matches(int x,int y)
	{
		return this.x == x && this.y == y;
	}
	
	
	public LinkedList <Node> getAdjacent(int xBound,int yBound,GameMap gameMap,LinkedList<Node> closedList)
	{
		LinkedList <Node> ret = new LinkedList<Node>();
		for(int i = -1; i < 2; i ++)
		{
			for(int j = -1; j < 2; j ++)
			{
				if(!(i == 0 && j == 0))
				{
					Node current = new Node(this.x + i,this.y + j);
					int currentX = x + i;
					int currentY = y + j;
					if(currentX >= 0 && currentX < xBound && currentY >= 0 && currentY < yBound && gameMap.validMove(this.x, this.y, currentX, currentY) && !closedList.contains(current))
					{
						ret.add(current);
						//System.out.println(current);
	
					}
				}
			}
		}
		return ret;
	}
	public void setAsParentAs(Node n)
	{
		n.parent = this;
	}
	
	public static Node lowestFInOpen(LinkedList <Node> open)
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
	public void setGCost(Node target)
	{		
		int gCost = 0;
		boolean found = false;
		Node current = this;
		while(!current.equals(target))
		{
			if(current.x != current.parent.x && current.y != current.parent.y)
			{
				gCost += 1.4;
			}
			else
			{
				gCost += 1;
			}
			current = current.parent;
		}
		g = gCost;	
	}
	public void setHCost(Node n)
	{
		h = (Math.abs(n.x - this.x) + Math.abs(n.y - this.y));
	}
	private int getFCost()
	{
		return h + g;
	}
	public Point asPoint(int tileSize)
	{
		return new Point((x*tileSize) + (tileSize/2),(y*tileSize) + (tileSize/2));
	}
	public static LinkedList<Point> calcPath(Node start,Node end,int tileSize)
	{
		LinkedList<Point> ret = new LinkedList<Point>();
		boolean found = false;
		Node current = end;
		while(!found)
		{
			if(current != start)
			{
				ret.add(current.asPoint(tileSize));
				current = current.parent;
			}
			else
			{
				ret.add(current.asPoint(tileSize));
				found = true;
			}
		}		
		// I removed the starting node
		return ret;

	}
	public boolean moreCostlyThan(Node n)
	{
		return this.g > n.g;
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
