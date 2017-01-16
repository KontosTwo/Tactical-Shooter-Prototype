package com.mygdx.script;

/**
 * Runs one Sequencialable at a time. The Sequencialable
 * should only affect one actor. 
 * Scripts can be interrupted by the cancelScript method or the pushing
 * of another script
 */
public class Scripter{
	
	private Sequencialable sequence;
	private boolean sequenceIsRunning;
	
	/**
	 * Allows any implemented class to execute an action over time.
	 * The action will have clearly defined starting conditions,
	 * starting action, updating action, ending condition, ending action, etc
	 */
	public interface Sequencialable{
		/**
		 * @Precondition sequenceInstaCompleted is false
		 */
		public void startSequence();
		public void updateSequence(float dt);
		public boolean completed();
		public void completeSequence();
		public void cancelSequence();
		
		/**
		 * Calculations to help sequenceInstaCompleted
		 * reach a decision
		 */
		public void calculateInstaCompleted();
		
		/**
		 * Whether the Sequencialable has already completed.
		 * @Precondition calculateInstaCompleted was called
		 */
		public boolean sequenceInstaCompleted();
	}
	
	public Scripter(){
		sequenceIsRunning = false;
	}
	public void cancelScript(){
		if(sequenceIsRunning){
			sequenceIsRunning = false;
			sequence.cancelSequence();
		}
	}
	/**
	 * Forcibly terminates the current script if it exists,
	 * and starts another one
	 */
	public void pushSequence(Sequencialable pushed){
		if(sequenceIsRunning){
			sequence.cancelSequence();
		}
		sequence = pushed;
		
		// check is sequence is already completed
		sequence.calculateInstaCompleted();
		if(sequence.sequenceInstaCompleted()){
			sequenceIsRunning = false;
		}else{
			sequence.startSequence();	
			sequenceIsRunning = true;
		}
		
	}
	
	public void update(float dt) 
	{
		if(sequenceIsRunning){
			if(sequence.completed()){
				sequence.completeSequence();
				sequenceIsRunning = false;
			}
			else{
				sequence.updateSequence(dt);
			}
		}
	}
	
	public boolean isActive(){
		return sequenceIsRunning;
	}
}