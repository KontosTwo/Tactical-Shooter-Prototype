package com.mygdx.ai;

class AlwaysFail implements RoutineSequencialable
{
	private RoutineSequencialable routine;
	
	AlwaysFail(RoutineSequencialable r)
	{
		routine = r;
	}
	
	@Override
	public void startSequence() 
	{
		routine.startSequence();
	}

	@Override
	public void update(float dt)
	{
		routine.update(dt);
	}

	@Override
	public boolean sequenceIsComplete() 
	{
		return routine.sequenceIsComplete();
	}

	@Override
	public void completeSequence() 
	{
		routine.completeSequence();
	}

	@Override
	public void cancelSequence() {
		routine.cancelSequence();
	}

	@Override
	public boolean succeeded() {
		return false;
	}

	@Override
	public boolean failed() {
		// this routine will return fail the moment it is complete
		return routine.sequenceIsComplete() || routine.succeeded();
	}

	@Override
	public boolean instaSucceeded() {
		return false;
	}

	@Override
	public boolean instaFailed() {
		return false;
	}

}
