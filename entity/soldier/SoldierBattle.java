package com.mygdx.entity.soldier;


import com.mygdx.ai.blackboard.Markable;
import com.mygdx.ai.blackboard.Trackable;
import com.mygdx.ai.leaf.SoldierRoutineable;
import com.mygdx.graphic.Animator;
import com.mygdx.map.GameMap.HitBoxable;
import com.mygdx.map.Path;
import com.mygdx.misc.Pair;
import com.mygdx.physics.PrecisePoint;
import com.mygdx.physics.PrecisePoint3;

abstract class SoldierBattle implements SoldierRoutineable,Trackable,Markable{
	private final Animator animator;
	protected final SoldierBattleState soldierBattleState;
	private final SoldierBattleMediator soldierMediator;
	
	private static final int ANIMATIONBOXSIZE = 70;

	
	interface SoldierBattleMediator{
		public boolean canSee(PrecisePoint3 observer,PrecisePoint3 target);
		public void shootAt(SoldierBattle shooter,float accuracy,PrecisePoint3 shooterVantage,PrecisePoint3 target);
		public PrecisePoint3 refineTargetForPlayer(PrecisePoint target);
		public Path findPath(PrecisePoint start,PrecisePoint target,int maxDistance);
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
		System.out.println("player shoots");
		PrecisePoint3 refinedTarget = soldierMediator.refineTargetForPlayer(target);
		soldierMediator.shootAt(this, soldierBattleState.getCurrentAccuracy(), soldierBattleState.getVantagePoint(),refinedTarget);
	}
	
	protected final void shootForAi(SoldierBattle victim){
		
		/*
		 * This works hand-in-hand with the generics of EnemyMarker and EnemyTracker
		 * This also works with the refineTargetForAi method. 
		 */
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
	
	final void teleportTo(PrecisePoint location){
		soldierBattleState.getCenter().set(location);
	}
	
	// Shootable
	@Override
	public void beginShoot(PrecisePoint3 target){
		soldierBattleState.face(target.create2DProjection());
		soldierBattleState.weaponState.beginShoot();
		soldierBattleState.setToShooting();
	}
	
	@Override
	public boolean finishedShooting() {
		return soldierBattleState.weaponState.finishedShooting();
	}
	
	@Override
	public void completeShoot() {
		soldierBattleState.setToIdle();
		soldierBattleState.weaponState.completeShoot();
	}
	
	@Override
	public void cancelShoot() {
		soldierBattleState.setToIdle();
		soldierBattleState.weaponState.cancelShooting();
	}
	
	@Override
	public boolean hasAmmo() {
		return soldierBattleState.weaponState.hasAmmo();
	}
	
	//ShootBurstable
	@Override
	public boolean hasAmmoForBurst(){
		return soldierBattleState.weaponState.hasAmmo();
	}
	@Override
	public void cancelBurst(){
		soldierBattleState.weaponState.cancelBurst();
	}
	
	// Reloadable
	@Override
	public void beginReload() {
		soldierBattleState.weaponState.beginReload();
		soldierBattleState.setToReloading();
	}

	@Override
	public boolean finishedReload() {
		return soldierBattleState.weaponState.finishedReloading();
	}

	@Override
	public void completeReload() {
		soldierBattleState.weaponState.completeReload();
		soldierBattleState.setToIdle();
	}

	@Override
	public void cancelReload() {
		soldierBattleState.weaponState.cancelReloading();
		soldierBattleState.setToIdle();
	}

	@Override
	public boolean doesNotNeedToReload() {
		return soldierBattleState.weaponState.atFullCapacity();
	}
	
	
	@Override
	public PrecisePoint3 getLocationForBlackBoard(){
		return soldierBattleState.getVantagePoint();
	}
	
	public String toString(){
		return soldierBattleState.toString();
	}
}
