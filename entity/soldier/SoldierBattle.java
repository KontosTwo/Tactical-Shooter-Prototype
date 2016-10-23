package com.mygdx.entity.soldier;


import com.mygdx.ai.leaf.SoldierRoutineable;
import com.mygdx.graphic.Animator;
import com.mygdx.map.GameMap.HitBoxable;
import com.mygdx.map.Path;
import com.mygdx.misc.Pair;
import com.mygdx.physics.PrecisePoint;
import com.mygdx.physics.PrecisePoint3;

abstract class SoldierBattle implements SoldierRoutineable{
	private final Animator animator;
	protected final SoldierBattleState soldierBattleState;
	private final SoldierBattleMediator soldierMediator;
	
	private static final int ANIMATIONBOXSIZE = 70;

	
	interface SoldierBattleMediator{
		public boolean canSee(PrecisePoint3 observer,PrecisePoint3 target);
		public void shootForPlayer(SoldierBattle shooter,float accuracy,PrecisePoint3 shooterVantage,PrecisePoint target);
		public Path findPath(PrecisePoint start,PrecisePoint target,int maxDistance);
	}

	public interface Attacker{
		
	}
	
	SoldierBattle(SoldierBattleMediator sbm,SoldierBattleState sbs){
		soldierBattleState = sbs;
		Pair<String,String> newAnimeData = soldierBattleState.createAnimationFilePath();
		animator = new Animator(soldierBattleState.center.getCenterReference(),newAnimeData.getFirst(),newAnimeData.getSecond());
		animator.setDimensions(ANIMATIONBOXSIZE,ANIMATIONBOXSIZE);
		animator.setCenterToBase();
		soldierMediator = sbm;

	}
	
	void render(){
		animator.render();
	}
	
	final void scanFor(SoldierBattle other){
		if(canSee(other)){
			addToSighted(other);
		}
	}
	
	private boolean canSee(SoldierBattle otherSoldier){
		return canSee(otherSoldier.soldierBattleState.getVantagePoint());
	}
	
	protected abstract void addToSighted(SoldierBattle other);

	protected final boolean canSee(PrecisePoint3 target){
		return withinRangeOfVision(target.create2DProjection())
				&& soldierMediator.canSee(soldierBattleState.getVantagePoint(),target);
	}
	
	private boolean withinRangeOfVision(PrecisePoint target){
		return soldierBattleState.facingTowards(soldierBattleState.center.getCenterReference()
				,target);
	}
	
	
	protected final void shootForPlayer(PrecisePoint target){
		soldierBattleState.face(target.x,target.y);
		soldierMediator.shootForPlayer(this, soldierBattleState.getCurrentAccuracy(), soldierBattleState.getVantagePoint(),target);
	}
	
	
	protected final Path findPath(PrecisePoint start,PrecisePoint target,int maxDistance){
		return soldierMediator.findPath(start,target,maxDistance);
	}
	
	void update(float dt){
		soldierBattleState.update();
		if(soldierBattleState.stateHasChanged()){
			Pair<String,String> newAnimeData = soldierBattleState.createAnimationFilePath();
			animator.changeAnimation(newAnimeData.getFirst(), newAnimeData.getSecond());
		}
		animator.update(dt);	
	}
	
	final void damageUsingMainWeapon(SoldierBattle other){
		System.out.println(this.toString() + " shooting: " + other.toString());
	}
	
	final HitBoxable getBody(){
		return soldierBattleState.getBody();
	}
	

	
	
	@Override
	public void beginShoot(PrecisePoint3 target) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean hasAmmo() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean finishedShooting() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void completeShoot() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void failShoot() {
		// TODO Auto-generated method stub
		
	}
	
	public String toString(){
		return soldierBattleState.toString();
	}
}
