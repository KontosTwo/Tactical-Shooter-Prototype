package com.mygdx.ai.leaf;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.mygdx.ai.leaf.MoveTo.MoveToable;
import com.mygdx.ai.leaf.PathTo.PathToable;
import com.mygdx.ai.leaf.Shoot.Shootable;

public interface RiflemanRoutineable extends PathToable,MoveToable,Shootable
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


