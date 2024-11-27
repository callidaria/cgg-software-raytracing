package cgg;

import java.util.stream.Stream;
import static tools.Functions.*;
import tools.*;


public class Image implements tools.Image {

	private final int width;
	private final int height;
	private final int pxs;
	private double[] data;
	private int adv;

    public Image(int width, int height) {
		this.width = width;
		this.height = height;
		this.pxs = width*height;
		this.data = new double[width*height*3];
		this.adv = 0;
    }

	public void sample(Sampler sampler)
	{
		Stream.iterate(0,y->y<height,y->y+1)
			.unordered()
			.parallel()
			.forEach(y->Stream.iterate(0,x->x<width,x->x+1)
					 .forEach(x->{
							 setPixel(x,y,sampler.getColor(vec2(x,y)));
							 adv++;
							 if (adv%(width*17)==0) System.out.println(((double)adv/(double)pxs)*100+"% done");
				}));
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

	private int calculatePixelIndex(int x,int y) {
		return (y*width+x)*3;
	}
}
