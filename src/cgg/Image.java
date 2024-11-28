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
			System.out.print("processing: "+ref.progress()+"%\r");
			try { Thread.sleep(1000); }
			catch (InterruptedException e) {  }
		}
	}
}
// FIXME worst case, the printing is done and the sample method waits a full second for this thread to join


public class Image implements tools.Image {

	private final int width;
	private final int height;
	private final double pxs;
	private double[] data;
	private int adv;
	private boolean done;

    public Image(int width, int height) {
		this.width = width;
		this.height = height;
		this.pxs = 1./(double)(width*height);
		this.data = new double[width*height*3];
		this.adv = 0;
		this.done = false;
    }

	public void sample(Sampler sampler)
	{
		// progress communication thread
		Thread __AllFaxNoPrinter = new Thread(new AdvancementPrinter(this));
		__AllFaxNoPrinter.start();

		// multithreading pixel processing
		Stream.iterate(0,y -> y<height,y -> y+1)
			.unordered()
			.parallel()
			.forEach(y -> Stream.iterate(0,x -> x<width,x -> x+1)
					 .forEach(x -> { setPixel(x,y,sampler.getColor(vec2(x,y)));adv++; }));
		done = true;

		// finishing progress communication
		try { __AllFaxNoPrinter.join(); }
		catch (InterruptedException e) { }
		System.out.println("\ndone.");
	}

    public void setPixel(int x, int y, Color color) {
		int __PixelIndex = calculatePixelIndex(x,y);
		data[__PixelIndex] = color.r();
		data[__PixelIndex+1] = color.g();
		data[__PixelIndex+2] = color.b();
    }

    public Color getPixel(int x, int y) {
		int __PixelIndex = calculatePixelIndex(x,y);
		return new Color(data[__PixelIndex],data[__PixelIndex+1],data[__PixelIndex+2]);
    }

    public void writePng(String name) {
		ImageWriter.writePng(name,data,width,height);
    }

    public void writeHdr(String name) {
		ImageWriter.writeHdr(name,data,width,height);
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

	public boolean done()
	{
		return done;
	}

	public int progress()
	{
		return (int)((double)adv*pxs*100);
	}

	private int calculatePixelIndex(int x,int y) {
		return (y*width+x)*3;
	}
}
