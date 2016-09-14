package com.mygdx.entity.soldier;

import com.mygdx.graphic.Animator;
import com.mygdx.physics.MovableBox;
import com.mygdx.physics.MovablePoint;
import com.mygdx.physics.PrecisePoint;

abstract class SoldierBattle
{
	private final Animator animator;
	protected final MovablePoint center;
	private final MovableBox body;
	protected final SoldierBattleState state;
	private final SoldierBattleMediator mediator;
	
	
	private static final float WALKSPEED = 3;
	private static final int CRAWLSPEED = 1;
	private static final int STANDHEIGHT = 70;
	private static final int CROUCHHEIGHT = 40;
	private static final int CRAWLHEIGHT = 10;
	private static final int STANDGUNHEIGHT = 70;
	private static final int CROUCHGUNHEIGHT = 40;
	private static final int CRAWLGUNHEIGHT = 10;
	private static final int HEADHEIGHT = 50;
	
	private static final int BODYX = 10;
	private static final int BODYY = 10;
	private static final int BODYZ = 70;

	private int reloadProgress;
	private int shootingProgress;
	
	interface SoldierBattleMediator
	{
		public boolean see(MovableBox observer,MovableBox target);
		public void shoot(SoldierBattle shooter,float accuracy,int xTarget,int yTarget,int zTarget);
	}
	
	SoldierBattle(SoldierBattleMediator sbm,SoldierBattleState sbs)
	{
		center = new MovablePoint();
		body = new MovableBox(center.getCenterReference(),BODYX,BODYY,BODYZ);
		state = sbs;
		animator = new Animator(center.getCenterReference(),sbs.getAnimePath(),sbs.getDataPath());
		mediator = sbm;
		reloadProgress = 0;
		shootingProgress = 0;
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
	
	private boolean withinRangeOfVision(SoldierBattle other)
	{
		return state.facingTowards(center.getCenterReference(), other.center.getCenterReference());
	}
	private boolean seeDespiteTerrain(SoldierBattle other)
	{
		return mediator.see(body,other.body);
	}
	
	protected final void shoot(int xTarget,int yTarget,int zTarget)
	{
		mediator.shoot(this, state.getCurrentAccuracy(), xTarget, yTarget, zTarget);
	}
	
	void update(float dt)
	{
		center.update();
		state.update();
		animator.updateAnimation(state.getAnimePath(), state.getDataPath());
		animator.update(dt);
	}
	
	
	
	protected abstract void addToSighted();
	
}
