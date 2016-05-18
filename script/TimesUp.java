package com.mygdx.script;

class TimesUp implements Triggerable
{
	 private final int timer;
	 private int currentTime;
	 
	 TimesUp(int timer)
	 {
		 this.timer = timer;
		 currentTime = 0;
	 }
	 
	@Override
	public boolean triggered() 
	{
		return currentTime > timer;
	}

	@Override
	public void update(float dt) 
	{
		currentTime ++;
	}

}
