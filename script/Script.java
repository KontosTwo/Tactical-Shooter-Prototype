package com.mygdx.script;

import java.util.List;

import com.mygdx.script.Scripter.Sequencialable;

 public class Script implements Sequencialable
{
	/*
	 *Each level contains the data for all Sequences, which are cutscenes, individual events, etc
	 *These events are independent of the functioning of entityManager
	 *Put several "Sequence Threads" into TogetherSequence
	 *Each sequence thread will execute once a WaitForEvent is triggered
	 *each sequence thread must fulfill one condition: each sequence in the
	 *thread must come after another. If they can ever be executed at the same
	 *time, then they belong in separate seqeunce threads
	 */
	private TogetherSequence script;
	
	public Script(List<Sequencialable> sequence)
	{
		script = new TogetherSequence(sequence);
	}
	
	public void loadScript(int level)
	{
		
	}
	
	@Override
	public void startSequence() 
	{
		script.startSequence();
	}

	@Override
	public boolean completed() 
	{
		return script.completed();
	}

	@Override
	public void completeSequence() 
	{
		script.completeSequence();
	}

	@Override
	public void updateSequence(float dt) 
	{
		script.updateSequence(dt);
	}
	@Override
	public void cancelSequence() 
	{
		script.cancelSequence();
	}

	@Override
	public boolean sequenceInstaCompleted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void calculateInstaCompleted() {
		// TODO Auto-generated method stub
		
	}

}
