package cgg;

import java.lang.InterruptedException;
import java.util.stream.Stream;
import static tools.Functions.*;
import tools.*;


class AdvancementPrinter implements Runnable
{
	private Image ref;

	public AdvancementPrinter(Image ref)
	{
		this.ref = ref;
	}

	public void run()
	{
		while (!ref.done())
		{
			try { Thread.sleep(200); }
			catch (InterruptedException e) {  }
			System.out.print("processing: "+ref.progress()+"%\r");
		}
	}
}
// FIXME worst case, the printing is done and the sample method waits a FIFTH of a second for this thread to join


public class Image implements tools.Image
{
	private final double pxs;
	private double[] data;
	private int adv;
	private boolean done;

    public Image()
	{
		this.pxs = 1./(double)(Config.WIDTH*Config.HEIGHT);
		this.data = new double[Config.WIDTH*Config.HEIGHT*3];
		this.adv = 0;
		this.done = false;
    }

	public void sample(Sampler sampler)
	{
		// progress communication thread
		Thread __AllFaxNoPrinter = new Thread(new AdvancementPrinter(this));
		__AllFaxNoPrinter.start();

		// multithreading pixel processing
		StopWatch __Timing = new StopWatch();
		Stream.iterate(0,y -> y<Config.HEIGHT,y -> y+1)
			.unordered()
			.parallel()
			.forEach(y -> Stream.iterate(0,x -> x<Config.WIDTH,x -> x+1)
					 .forEach(x -> {
							 setPixel(x,y,sampler.getColor(vec2(x,y)));
							 adv++;
						 }));
		done = true;
		// FIXME work on cpu idle issue and find out why this variant uses more memory than the by pixel one

		// finishing progress communication
		try { __AllFaxNoPrinter.join(); }
		catch (InterruptedException e) { }

		// print statistics
		__Timing.stop();
		System.out.println();
		System.out.printf("render time: %.2fs\n",__Timing.time_seconds());
		System.out.printf("average pixel time: %fms\n",(double)__Timing.time_milliseconds()*pxs);
		System.out.println("done.");
	}

    public void setPixel(int x, int y, Color color)
	{
		int __PixelIndex = calculatePixelIndex(x,y);
		data[__PixelIndex] = color.r();
		data[__PixelIndex+1] = color.g();
		data[__PixelIndex+2] = color.b();
    }

    public Color getPixel(int x, int y)
	{
		int __PixelIndex = calculatePixelIndex(x,y);
		return new Color(data[__PixelIndex],data[__PixelIndex+1],data[__PixelIndex+2]);
    }

    public void writePng(String name)
	{
		ImageWriter.writePng(name,data,Config.WIDTH,Config.HEIGHT);
    }

    public void writeHdr(String name)
	{
		ImageWriter.writeHdr(name,data,Config.WIDTH,Config.HEIGHT);
    }

	public boolean done()
	{
		return done;
	}

	public int progress()
	{
		return (int)((double)adv*pxs*100);
	}

	private int calculatePixelIndex(int x,int y)
	{
		return (y*Config.WIDTH+x)*3;
	}
}
