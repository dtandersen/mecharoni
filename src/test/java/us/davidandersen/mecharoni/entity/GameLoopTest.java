package us.davidandersen.mecharoni.entity;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.Test;
import us.davidandersen.mecharoni.entity.GameLoop.GameContext;
import us.davidandersen.mecharoni.entity.GameLoop.UpdateMethod;

class GameLoopTest
{
	@Test
	void run1Second()
	{
		final GameLoop sim = new GameLoop();
		sim.setTimeStep(.1f);
		final UpdateCounter update = new UpdateCounter(1);
		sim.setUpdateMethod(update);
		sim.run();

		assertThat(update.getTimesCalled(), is(11));
	}

	@Test
	void run5Second()
	{
		final GameLoop sim = new GameLoop();
		sim.setTimeStep(.2f);
		final UpdateCounter update = new UpdateCounter(5);
		sim.setUpdateMethod(update);
		sim.run();

		assertThat(update.getTimesCalled(), is(26));
	}

	@Test
	void startAtZero()
	{
		final GameLoop sim = new GameLoop();
		sim.setTimeStep(.2f);
		final TimeChecker update = new TimeChecker();
		sim.setUpdateMethod(update);
		sim.run();

		assertThat(update.getStartTime(), is(0f));
	}

	private static class TimeChecker implements UpdateMethod
	{
		private boolean ran = false;

		private float startTime;

		@Override
		public void update(final GameContext context)
		{
			startTime = context.getTime();
			ran = true;
		}

		public float getStartTime()
		{
			return startTime;
		}

		@Override
		public boolean isFinished()
		{
			return ran;
		}

	}

	private static class UpdateCounter implements UpdateMethod
	{
		private int timesCalled;

		private final float endTime;

		private boolean finished;

		public UpdateCounter(final float endTime)
		{
			this.endTime = endTime;
		}

		public int getTimesCalled()
		{
			return timesCalled;
		}

		@Override
		public void update(final GameContext context)
		{
			final float t = context.getTime();
			finished = t > (endTime + .000001);
			if (finished)
			{
				return;
			}
			timesCalled++;
		}

		@Override
		public boolean isFinished()
		{
			return finished;
		}
	}
}
