package cgg;

import tools.*;

public class Image implements tools.Image {

	private final int width;
	private final int height;
	private double[] data;

    public Image(int width, int height) {
		this.width = width;
		this.height = height;
		this.data = new double[width*height*3];
    }

    public void setPixel(int x, int y, Color color) {
		int __PixelIndex = calculatePixelIndex(x,y);
		data[__PixelIndex] = color.r();
		data[__PixelIndex+1] = color.g();
		data[__PixelIndex+2] = color.b();
    }

	public void mixPixel(int x,int y,Color colour) {
		int __PixelIndex = calculatePixelIndex(x,y);
		data[__PixelIndex] = (data[__PixelIndex]+colour.r())/2;
		data[__PixelIndex+1] = (data[__PixelIndex+1]+colour.g())/2;
		data[__PixelIndex+2] = (data[__PixelIndex+2]+colour.b())/2;
	}

    public Color getPixel(int x, int y) {
		int __PixelIndex = calculatePixelIndex(x,y);
		return new Color(data[__PixelIndex],data[__PixelIndex+1],data[__PixelIndex+2]);
    }

    public void writePng(String name) {
		ImageWriter.writePng(name,data,width,height);
    }

    public void writeHdr(String name) {
        // TODO: write the image data to disk in OpenEXR format.
		//ImageWriter.writePng(name,data,width,height);
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
