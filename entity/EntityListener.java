package com.mygdx.entity;

import java.util.LinkedList;
import java.util.PriorityQueue;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.misc.Point;
import com.mygdx.misc.Tuple;

public interface EntityListener // the interface for interacting with entityManager. Entity subclasses should contain an instance of this to interact with
{
	public void shoot(double y1,double x1,double height1,double y2,double x2,double z2,Humanoid h,double accuracy);
	public boolean see(double x1,double y1,double z1,double x2,double y2,double z2);// see
	public boolean seeGround(double x1,double y1,double z1,double x2,double y2);// must hypothetically see the ground at x2,y2
	public boolean seePhysical(double x1,double y1,double z1,double x2,double y2,double z2);// judging. See if one can shoot another. Visual obstructions that do not block bullets are ignored
	public void mark(double x,double y);// SHREK
	public Tuple<Boolean,LinkedList<Point>> findPath(int sx,int sy,int tx,int ty);
	public Tuple<Boolean,Vector2> findCover(double x,double y,int searchDistance);
	public double judgeCover(int x1,int y1,int z1,int x2,int y2,int z2); // the double returned is the "advantage" in cover you hold over someone
	public void remove(Entity e);
	public void grenade(int x1,int y1,int x2,int y2);
	public Tuple<Boolean,Humanoid> seeEnemy(Humanoid humanoid);
	public boolean hitMarkerNear(Humanoid h); // checks if any shots landed near this humanoid
	public void scanBattleField(Humanoid h); // alows the humanoid to look around the battle field and react to other entities
	public boolean enemyWithinRange(Vector2 position,Humanoid h,int radius);
	public void foundObscuredEnemy(Humanoid target,Humanoid sender);
	//public void foundVisibleEnemy(Humanoid target,Humanoid sender);
	// removed cuz visible enemy means VISIBLE. If another soldier cannot see it but another can, why notify that soldier?
}
