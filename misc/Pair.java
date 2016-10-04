package com.mygdx.misc;


public class Pair<X, Y> { 
	
  private X first; 
  private Y second; 
  private boolean firstIsAssigned;
  private boolean secondIsAssigned;
  
  public Pair() { 
	  first = null;
	  second = null;
	  firstIsAssigned = false;
	  secondIsAssigned = false;
  } 
  public void setFirst(X value){
	  firstIsAssigned = true;
	  first = value;
  }
  public void setSecond(Y value){
	  secondIsAssigned = true;
	  second = value;
  }
  public boolean firstIsAssigned(){
	  return firstIsAssigned;
  }
  public boolean secondIsAssigned(){
	  return secondIsAssigned;
  }
  public X getFirst(){
	  return first;
  }
  public Y getSecond(){
	  return second;
  }
  public String toString(){
	  return "" + first.toString() + " " + second.toString();
  }
} 

