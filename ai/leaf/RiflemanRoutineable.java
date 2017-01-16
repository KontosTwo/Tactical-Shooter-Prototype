package com.mygdx.ai.leaf;


import com.mygdx.ai.leaf.CheckReload.CheckReloadable;
import com.mygdx.ai.leaf.MoveTo.MoveToable;
import com.mygdx.ai.leaf.PathTo.PathToable;
import com.mygdx.ai.leaf.Shoot.Shootable;
import com.mygdx.ai.leaf.ShootBurst.ShootBurstable;
import com.mygdx.ai.leaf.ShootValidEnemy.ShootValidEnemyable;

public interface RiflemanRoutineable extends PathToable,MoveToable,Shootable,ShootBurstable,ShootValidEnemyable,CheckReloadable
{
	/*
	 * This kind of soldier denotes soldiers that:
	 * - wield a rifle-type of gun
	 * - engage in either
	 * suppressive fire
	 * or
	 * flanking
	 * maneuvers. 
	 */
}


