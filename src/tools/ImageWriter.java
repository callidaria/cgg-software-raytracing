
package tools;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;

/*
 * A simple image writer that takes an array of pixel components and the image
 * size and writes the corresponding image in 16-bit PNG format with a linear
 * color space to the provided location.
 */
public class ImageWriter {

    public static void open(String name) {
        var open = "";
        var os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            open = "start";
        } else if (os.contains("mac")) {
            open = "open";
        } else if (os.contains("lin")) {
            open = "xdg-open";
        } else {
            return;
        }

        try {
            new ProcessBuilder(open, "images/" + name + ".png").start().waitFor();
            new ProcessBuilder(open, "images/" + name + ".exr").start().waitFor();
        } catch (Exception e) {
        }
    }

    // If imagemagick is installed, a 32-bit double OpenEXR Linear RGB file is
    // created.
    public static boolean writeHdr(String basename, double[] data, int width,
            int height) {
        var pfmName = "images/" + basename + ".pfm";
        var exrName = "images/" + basename + ".exr";
        new File("images").mkdirs();
        var file = new File(pfmName);
        try {
            // Writes out a PFM binary file ...
            var order = "1.0";
            var out = new FileOutputStream(file);
            out.write(
                    String.format("PF\n%d %d\n%s\n", width, height, order).getBytes());
            float[] floatData = new float[data.length];
            for (var i = 0; i != data.length; i++)
                floatData[i] = (float) data[i];
            byte bytes[] = new byte[Float.BYTES * data.length];
            ByteBuffer.wrap(bytes).asFloatBuffer().put(floatData);
            out.write(bytes);
            out.close();

            // ... and uses convert to produce an EXR from it.
            var proc = new ProcessBuilder("convert", pfmName, "-flip", exrName).start();
            proc.waitFor();
        } catch (Exception error) {
            System.out.println(String.format(
                    "Something went wrong writing EXR image: %s:\n%s", pfmName, error));
            return false;
        } finally {
            new File(pfmName).delete();
        }
        return true;
    }

    private ImageWriter() {
    }

    // Writes the provided image data to disk as 16-bit sRGB PNG.
    public static void writePng(String basename, double[] data, int width,
            int height) {
        var filename = "images/" + basename + ".png";
        new File("images").mkdirs();
        try {
            // setup an sRGB image with 16-bit components of the right size.
            ComponentColorModel ccm = new ComponentColorModel(
                    ColorSpace.getInstance(ColorSpace.CS_sRGB), false, false,
                    ComponentColorModel.OPAQUE, DataBuffer.TYPE_USHORT);

            WritableRaster raster = Raster.createBandedRaster(DataBuffer.TYPE_USHORT,
                    width, height, 3, null);
            BufferedImage image = new BufferedImage(ccm, raster, false, null);

            for (int y = 0; y != height; y++) {
                for (int x = 0; x != width; x++) {
                    int i = (width * y + x) * 3;
                    int[] rgb = { (int) (clamp(gamma(data[i + 0])) * 65535.0),
                            (int) (clamp(gamma(data[i + 1])) * 65535.0),
                            (int) (clamp(gamma(data[i + 2])) * 65535.0) };
                    raster.setPixel(x, y, rgb);
                }
            }
            File file = new File(filename);
            ImageIO.write(image, "png", file);
        } catch (IOException error) {
            System.out.println(String.format(
                    "Something went wrong writing PNG image: %s:\n %s", filename, error));
        }
    }

    private static double gamma(double v) {
        return (double) Math.pow(v, 1 / 2.2);
    }

    private static double clamp(double v) {
        return Math.min(Math.max(0, v), 1);
    }
}
