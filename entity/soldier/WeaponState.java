package com.mygdx.entity.soldier;

import com.mygdx.physics.Util;


class WeaponState{
	private final int shootingRate;
	private int shootingProgress;
	private boolean currentlyShooting;
	
	private int reloadRate;
	private int reloadingProgress;
	private boolean currentlyReloading;
	
	private final int maxAmmo;
	private int ammo;
	
	private final float maxAccuracy;
	private float accuracy;
	
	private int gunHeight;
	
	private int burstAmount;
	private int burstDeviation;
	
	private static final int STANDGUNHEIGHT = 50;
	private static final int CROUCHGUNHEIGHT = 30;
	private static final int CRAWLGUNHEIGHT = 10;
	
	private WeaponState(Weapon weapon){
		this.shootingRate = weapon.firingRate;
		this.reloadRate = weapon.reload;
		this.maxAmmo = weapon.capacity;
		this.maxAccuracy = weapon.accuracy;
		
		shootingProgress = 0;
		reloadingProgress = 0;
		ammo = maxAmmo;
		accuracy = maxAccuracy;
		currentlyShooting = false;
		currentlyReloading = false;
		
		burstAmount = weapon.burst;
		burstDeviation = weapon.burstDev;
	}
	
	static WeaponState createTsokos(){
		return new WeaponState(Weapon.TSOKOS);
	}
	
	void update(){
		updateShooting();
		updateReloading();
	}
	
	private void updateShooting(){
		if(currentlyShooting){
			if(shootingProgress <shootingRate){
				shootingProgress ++;
			}
		}
	}
	
	private void updateReloading(){
		if(currentlyReloading){
			if(reloadingProgress <reloadRate){				
				reloadingProgress ++;
			}
		}
	}
	
	void beginShoot(){
		currentlyShooting = true;
		ammo --;
	}
	
	boolean finishedShooting(){
		return shootingProgress >= shootingRate;
	}
	
	void cancelShooting(){
		currentlyShooting = false;
		shootingProgress = 0;
	}
	
	void completeShoot(){
		currentlyShooting = false;
		shootingProgress = 0;
	}
	
	void cancelBurst(){
		currentlyShooting = false;
		shootingProgress = 0;
	}
	
	void beginReload(){
		currentlyReloading = true;
	}
	
	boolean finishedReloading(){
		return reloadingProgress >= reloadRate;
	}
	
	void cancelReloading(){
		currentlyReloading = false;
		reloadingProgress = 0;
	}
	
	void completeReload(){
		currentlyReloading = false;
		reloadingProgress = 0;
		ammo = maxAmmo;
	}
	
	void stand(){
		gunHeight = STANDGUNHEIGHT;
	}

	void crouch(){
		gunHeight = CROUCHGUNHEIGHT;
	}
	void lay(){
		gunHeight = CRAWLGUNHEIGHT;
	}
	
	boolean hasAmmo(){
		return ammo > 0;
	}
	
	boolean atFullCapacity(){
		return ammo == maxAmmo;
	}
	
	int getBurstAmount(){
		return burstAmount;
		//return  (int) Util.atLeastOne(burstAmount + (Math.random()*burstDeviation) - (burstDeviation/2));
	}
	
	float getAccuracy(){
		return accuracy;
	}
	int getGunHeight(){
		return gunHeight;
	}
	
	private enum Weapon
	{
		GAUSSRIFLE(2,5,1,1,6,49,10,2,1), // Fed Assault Rifle. Ayane's weapon
		TSOKOS(7,9,7,2,13,49,10,3,0), // Fed fully automatic shotgun. Chanion's weapon
		;

		private final int pierce;
		private final int damage;
		private final float accuracy;
		private final int radius;
		private final int firingRate;
		private final int reload;
		private final int capacity;
		private final int burst;
		private final int burstDev; // must be an even number


		Weapon(int pierce,int damage,float accuracy,int radius,int rate,int reload,int capacity,int burst,int burstDev)
		{
			this.pierce = pierce;
			this.damage = damage;
			this.accuracy = accuracy;
			this.radius = radius;
			this.firingRate = rate;
			this.reload = reload;
			this.capacity = capacity;
			this.burst = burst;
			this.burstDev = burstDev;
		}

	}
}
