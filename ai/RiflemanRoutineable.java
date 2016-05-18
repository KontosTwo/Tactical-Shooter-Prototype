package com.mygdx.ai;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.mygdx.ai.BurstShoot.BurstShootable;
import com.mygdx.ai.MoveTo.MoveToable;
import com.mygdx.ai.PathTo.PathToable;
import com.mygdx.ai.Reload.Reloadable;
import com.mygdx.ai.Shoot.Shootable;

public interface RiflemanRoutineable extends PathToable,MoveToable,Shootable,BurstShootable,
Reloadable,Idleable,CheckShootPossibleable
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


