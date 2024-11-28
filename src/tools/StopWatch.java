package tools;

public class StopWatch {
	private long mark;
	private long time;

	public StopWatch() {
		mark = System.currentTimeMillis();
	}

	public void stop(/*String name*/) {
		time = System.currentTimeMillis() - mark;
		//System.out.format("%s: time: %.2fs\n", name, time / 1e3);
	}

	public long time_milliseconds()
	{
		return time;
	}

	public double time_seconds()
	{
		return time/1e3;
	}
}
