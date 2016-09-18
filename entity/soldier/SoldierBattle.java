package com.mygdx.entity.soldier;

import com.mygdx.graphic.Animator;
import com.mygdx.misc.Tuple;
import com.mygdx.physics.MovableBox;
import com.mygdx.physics.MovablePoint;
import com.mygdx.physics.PrecisePoint;

abstract class SoldierBattle 
{
	private final Animator animator;
	protected final SoldierBattleState soldierBattleState;
	private final SoldierBattleMediator mediator;
	
	


	
	private static final int ANIMATIONBOXSIZE = 70;
	//private static final int ANIMATIONBOXOFFSET = ;

	
	interface SoldierBattleMediator
	{
		public boolean see(MovableBox observer,MovableBox target);
		public void shoot(SoldierBattle shooter,float accuracy,int xTarget,int yTarget,int zTarget);
	}
	
	SoldierBattle(SoldierBattleMediator sbm,SoldierBattleState sbs)
	{
		soldierBattleState = sbs;
		Tuple<String,String> newAnimeData = soldierBattleState.createAnimationFilePath();
		animator = new Animator(soldierBattleState.center.getCenterReference(),newAnimeData.x,newAnimeData.y);
		animator.setDimensions(ANIMATIONBOXSIZE,ANIMATIONBOXSIZE);
		animator.doodadify();
		mediator = sbm;

	}
	final void render()
	{
		animator.render();
	}
	final void scanFor(SoldierBattle other)
	{
		if(withinRangeOfVision(other) && seeDespiteTerrain(other))
		{
			addToSighted();
		}
	}
	protected abstract void addToSighted();

	private boolean withinRangeOfVision(SoldierBattle other)
	{
		return soldierBattleState.facingTowards(soldierBattleState.center.getCenterReference(), other.soldierBattleState.center.getCenterReference());
	}
	private boolean seeDespiteTerrain(SoldierBattle other)
	{
		//return mediator.see(body,other.body);
		return false;
	}
	
	protected final void shoot(int xTarget,int yTarget,int zTarget)
	{
		mediator.shoot(this, soldierBattleState.getCurrentAccuracy(), xTarget, yTarget, zTarget);
	}
	
	void update(float dt)
	{
		soldierBattleState.update();
		if(soldierBattleState.stateHasChanged())
		{
			Tuple<String,String> newAnimeData = soldierBattleState.createAnimationFilePath();
			animator.changeAnimation(newAnimeData.x, newAnimeData.y);
		}
		animator.update(dt);
		
	}

	
}
