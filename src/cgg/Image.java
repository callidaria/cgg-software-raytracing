
package cgg;

import tools.*;

public class Image implements tools.Image {

    // ---8<--- missing-implementation
    // TODO Provides storage for the image data. For each pixel in the image
    // three double values are needed to store the pixel components.
    public Image(int width, int height) {
    }

    // TODO Stores the RGB color components for one particular pixel addressed
    // by it's coordinates in the image.
    public void setPixel(int x, int y, Color color) {
    }

    // TODO Retrieves the RGB color components for one particular pixel addressed
    // by it's coordinates in the image.
    public Color getPixel(int x, int y) {
        return Color.black;
    }
    // --->8---

    public void writePng(String name) {
        // TODO This call also needs to be adjusted once Image() and setPixel()
        // are implemented. Use
        // ImageWriter.writePng(String name, double[] data, int width, int height) to
        // write the image data to disk in PNG format.
    }

    public void writeHdr(String name) {
        // TODO This call also needs to be adjusted once Image() and setPixel()
        // are implemented. Use
        // ImageWriter.writePng(String name, double[] data, int width, int height) to
        // write the image data to disk in OpenEXR format.
    }

    public int width() {
        // TODO This is just a dummy value to make the compiler happy. This
        // needs to be adjusted such that the actual width of the Image is
        // returned.
        return 0;
    }

    public int height() {
        // TODO This is just a dummy value to make the compiler happy. This
        // needs to be adjusted such that the actual height of the Image is
        // returned.
        return 0;
    }
}
