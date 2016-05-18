package com.mygdx.misc;

public class  Triple <X,Y,Z>
{
  public X x; 
  public Y y; 
  public Z z; 
  
  public Triple() 
  { 
	  x = null;
	  y = null;
	  z = null;
  } 
  public Triple(X x,Y y,Z z)
  {
	  this.x = x;
	  this.y = y;
	  this.z = z;
  }
  public String toString()
  {
	  return "" + x.toString() + " " + y.toString() + " " + z.toString();
  }
}
