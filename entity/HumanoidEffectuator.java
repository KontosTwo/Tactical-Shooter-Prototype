package com.mygdx.entity;

import java.util.LinkedList;
import java.util.PriorityQueue;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.misc.Pair;
import com.mygdx.physics.Point;
import com.mygdx.whyishouldlearntocodebeforestartingaproject.SoldierBattleConcrete;

public interface HumanoidEffectuator
{
	public void shoot(double y1,double x1,double height1,double y2,double x2,double z2,SoldierBattleConcrete h,double accuracy);
	public boolean see(double x1,double y1,double z1,double x2,double y2,double z2);// see
	public boolean seeGround(double x1,double y1,double z1,double x2,double y2);// must hypothetically see the ground at x2,y2. z2 is ignored
	public boolean seePhysical(double x1,double y1,double z1,double x2,double y2,double z2);// judging. See if one can shoot another. Visual obstructions that do not block bullets are ignored
	public void mark(double x,double y);// SHREK
	public Pair<Boolean,LinkedList<Point>> findPath(double sx,double sy,double tx,double ty);
	public Pair<Boolean,Vector2> findCover(double x,double y,int searchDistance);
	public double judgeCover(double x1,double y1,double z1,double x2,double y2,double z2); // the double returned is the "advantage" in cover you hold over someone
	public void remove(Entity e);
	public void grenade(double x1,double y1,double x2,double y2);
	public Pair<Boolean,SoldierBattleConcrete> seeEnemy(SoldierBattleConcrete humanoid);
	public boolean hitMarkerNear(SoldierBattleConcrete h); // checks if any shots landed near this humanoid
	public void scanBattleField(SoldierBattleConcrete h); // alows the humanoid to look around the battle field and react to other entities
	public boolean enemyWithinRange(Vector2 position,SoldierBattleConcrete h,int radius);
	public void foundObscuredEnemy(SoldierBattleConcrete target,SoldierBattleConcrete sender);
	//public void foundVisibleEnemy(Humanoid target,Humanoid sender);
	// removed cuz visible enemy means VISIBLE. If another soldier cannot see it but another can, why notify that soldier?
}
