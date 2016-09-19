package com.mygdx.misc;


public class Pair<X, Y> 
{ 
  public X x; 
  public Y y; 
  
  public Pair() 
  { 
	  x = null;
	  y = null;
  } 
  public Pair(X x,Y y)
  {
	  this.x = x;
	  this.y = y;
  }
  public String toString()
  {
	  return "" + x.toString() + " " + y.toString();
  }
} 

