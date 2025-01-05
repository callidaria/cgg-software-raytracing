package cgg;

import java.lang.InterruptedException;
import java.util.stream.Stream;
import static tools.Functions.*;
import tools.*;


public class Image implements tools.Image
{
	private double[] data;

    public Image()
	{
		this.data = new double[Config.WIDTH*Config.HEIGHT*3];
    }

	public void sample(Sampler sampler)
	{
		// progress communication thread
		AdvancementData adv_data = new AdvancementData(Config.WIDTH*Config.HEIGHT);
		Thread __AllFaxNoPrinter = new Thread(new AdvancementPrinter(adv_data));
		__AllFaxNoPrinter.start();

		// multithreading pixel processing
		StopWatch __Timing = new StopWatch();
		Stream.iterate(0,y -> y<Config.HEIGHT,y -> y+1)
			.unordered()
			.parallel()
			.forEach(y -> Stream.iterate(0,x -> x<Config.WIDTH,x -> x+1)
					 .forEach(x -> {
							 setPixel(x,y,sampler.getColor(vec2(x,y)));
							 adv_data.adv++;
						 }));
		adv_data.done = true;
		// FIXME work on cpu idle issue and find out why this variant uses more memory than the by pixel one

		// print statistics
		__Timing.stop();
		System.out.printf("render time: %.2fs\n",__Timing.time_seconds());

		// finishing progress communication
		try { __AllFaxNoPrinter.join(); }
		catch (InterruptedException e) { }
		System.out.println("\ndone.");
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

    public void writePng(String name) { ImageWriter.writePng(name,data,Config.WIDTH,Config.HEIGHT); }
    public void writeHdr(String name) { ImageWriter.writeHdr(name,data,Config.WIDTH,Config.HEIGHT); }
	private int calculatePixelIndex(int x,int y) { return (y*Config.WIDTH+x)*3; }
}
