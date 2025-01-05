package cgg;


public class AdvancementPrinter implements Runnable
{
	private AdvancementData data;

	public AdvancementPrinter(AdvancementData data)
	{
		this.data = data;
	}

	public void run()
	{
		while (!data.done)
		{
			try { Thread.sleep(200); }
			catch (InterruptedException e) {  }
			System.out.print("processing: "+(int)data.percentage()+"%\r");
		}
	}
}
// FIXME worst case, the printing is done and the sample method waits a FIFTH of a second for this thread to join
