package com.mygdx.entity.soldier;


import com.mygdx.graphic.Animator;
import com.mygdx.map.GameMap.HitBoxable;
import com.mygdx.map.Path;
import com.mygdx.misc.Pair;
import com.mygdx.physics.MyVector3;
import com.mygdx.physics.PrecisePoint;
import com.mygdx.physics.PrecisePoint3;

abstract class SoldierBattle {
	private final Animator animator;
	protected final SoldierBattleState soldierBattleState;
	private final SoldierBattleMediator mediator;
	
	private static final int ANIMATIONBOXSIZE = 70;
	//private static final int ANIMATIONBOXOFFSET = ;

	
	interface SoldierBattleMediator{
		public boolean see(PrecisePoint3 observer,PrecisePoint3 target);
		public void shootForPlayer(SoldierBattle shooter,float accuracy,PrecisePoint target);
		public Path findPath(PrecisePoint start,PrecisePoint target,int maxDistance);
	}

	public interface Attacker{
		
	}
	
	SoldierBattle(SoldierBattleMediator sbm,SoldierBattleState sbs){
		soldierBattleState = sbs;
		Pair<String,String> newAnimeData = soldierBattleState.createAnimationFilePath();
		animator = new Animator(soldierBattleState.center.getCenterReference(),newAnimeData.x,newAnimeData.y);
		animator.setDimensions(ANIMATIONBOXSIZE,ANIMATIONBOXSIZE);
		animator.setCenterToBase();
		mediator = sbm;

	}
	
	final void render(){
		animator.render();
	}
	
	final void scanFor(SoldierBattle other){
		if(withinRangeOfVision(other) && seeDespiteTerrain(other)){
			addToSighted();
		}
	}
	

	private boolean withinRangeOfVision(SoldierBattle other){
		return soldierBattleState.facingTowards(soldierBattleState.center.getCenterReference()
				, other.soldierBattleState.center.getCenterReference());
	}
	private boolean seeDespiteTerrain(SoldierBattle otherSoldier){
		return see(otherSoldier.soldierBattleState.getVantagePoint());
	}

	protected abstract void addToSighted();
	
	protected final void shootForPlayer(PrecisePoint target){
		soldierBattleState.face(target.x,target.y);
		mediator.shootForPlayer(this, soldierBattleState.getCurrentAccuracy(), target);
	}
	
	protected final boolean see(PrecisePoint3 target){
		return mediator.see(soldierBattleState.getVantagePoint(),target);
	}
	
	protected final Path findPath(PrecisePoint start,PrecisePoint target,int maxDistance){
		return mediator.findPath(start,target,maxDistance);
	}
	
	void update(float dt){
		soldierBattleState.update();
		if(soldierBattleState.stateHasChanged()){
			Pair<String,String> newAnimeData = soldierBattleState.createAnimationFilePath();
			animator.changeAnimation(newAnimeData.x, newAnimeData.y);
		}
		animator.update(dt);	
	}
	
	HitBoxable getBody(){
		return soldierBattleState.getBody();
	}
}
