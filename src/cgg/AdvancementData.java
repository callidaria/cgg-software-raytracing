package cgg;


public class AdvancementData
{
	public double dvf;
	public int adv;
	public boolean done;

	public AdvancementData(int maximum)
	{
		this.dvf = 1./(double)maximum;
		this.adv = 0;
		this.done = false;
	}

	public double percentage() { return (double)adv*dvf*100; }
}
