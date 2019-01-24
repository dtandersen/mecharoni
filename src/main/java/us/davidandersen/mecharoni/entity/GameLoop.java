package us.davidandersen.mecharoni.entity;

public class GameLoop
{
	private final DefaultGameContext context;

	private UpdateMethod updateMethod;

	public GameLoop()
	{
		context = new DefaultGameContext();
	}

	public void setTimeStep(final float timeStep)
	{
		context.setTimeStep(timeStep);
	}

	public void run()
	{
		while (!updateMethod.isFinished())
		{
			updateMethod.update(context);
			context.incrementTime();
		}
	}

	public void setUpdateMethod(final UpdateMethod updateMethod)
	{
		this.updateMethod = updateMethod;
	}

	private final class DefaultGameContext implements GameContext
	{
		private float time;

		private float timeStep;

		public void incrementTime()
		{
			time += timeStep;
		}

		public void setTimeStep(final float timeStep)
		{
			this.timeStep = timeStep;
		}

		@Override
		public float getTime()
		{
			return time;
		}
	}

	interface UpdateMethod
	{
		void update(GameContext context);

		boolean isFinished();
	}

	interface GameContext
	{
		float getTime();
	}
}
