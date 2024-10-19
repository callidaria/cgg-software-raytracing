package cgg.a01;

import cgg.Image;
import tools.*;

public class Main {

  public static void main(String[] args) {
    int width = 400;
    int height = 400;

    // This class instance defines the contents of the image.
    // var constant = new ConstantColor(gray);
    Circle circle = new Circle(new Vec2(150,150),75,new Color(.5,.0,.0));
	Color background = new Color(1.,1.,1.);

    // Creates an image and iterates over all pixel positions inside the image.
    var image = new Image(width, height);
    for (int x = 0; x != width; x++) {
		for (int y = 0; y != height; y++) {
			// Sets the color for one particular pixel.
			// image.setPixel(x, y, constant.getColor(vec2(x, y)));
			boolean hits = circle.coversPoint(new Vec2(x,y));
			image.setPixel(x,y,(hits)?circle.colour():background);
		}
	}

    // Write the image to disk.
    image.writePng("a01-constant");
  }
}
