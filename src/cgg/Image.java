
package cgg;

import tools.*;

public class Image implements tools.Image {
    private final int width;
    private final int height;

    // ---8<--- missing-implementation
    // Provides storage for the image data. For each pixel in the image
    // three double values are needed to store the pixel components.
    public Image(int width, int height) {
        this.width = width;
        this.height = height;
    }

    // Stores the RGB color components for one particular pixel addressed
    // by it's coordinates in the image.
    public void setPixel(int x, int y, Color color) {
    }

    // Retrieves the RGB color components for one particular pixel addressed
    // by it's coordinates in the image.
    public Color getPixel(int x, int y) {
        return Color.black;
    }
    // --->8---

    public void writePng(String name) {
        // This call also needs to be adjusted once Image() and setPixel()
        // are implemented. null is obviously not the right argument.
        // ImageWriter.writePng(name, null, width, height);
    }

    public void writeHdr(String name) {
        // This call also needs to be adjusted once Image() and setPixel()
        // are implemented. null is obviously not the right argument.
        // ImageWriter.writeHdr(name, null, width, height);
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }
}
