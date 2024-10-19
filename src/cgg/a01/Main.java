package cgg.a01;

import java.util.Random;
import java.util.ArrayList;
import cgg.Image;
import tools.*;

public class Main {

	public static void main(String[] args) {

		// setup frame
		int width = 400;
		int height = 400;
		Color background = new Color(.0f,.0f,.0f);

		// setup multicircles
		Random rand = new Random();
		ArrayList<Circle> circles = new ArrayList<Circle>();
		for (int i=0;i<17;i++) {
			circles.add(new Circle(
					new Vec2(rand.nextInt(401),rand.nextInt(401)),
					rand.nextInt(75),
					new Color(rand.nextFloat(1.f),rand.nextFloat(1.f),rand.nextFloat(1.f))
				));
			// FIXME: colour generation mad ugly
		}

		// generate multicircles
		Image image_multi = new Image(width,height);
		Image image_disc = new Image(width,height);
		for (int x=0;x<width;x++) {
			for (int y=0;y<height;y++) {

				// clear
				image_multi.setPixel(x,y,background);
				image_disc.setPixel(x,y,background);

				// iterate entities
				for (Circle circle : circles) {
					if (!circle.coversPoint(new Vec2(x,y))) continue;

					// multicircles
					// barebones
					image_multi.mixPixel(x,y,circle.colour());

					// colourdiscs
					double rot = circle.orientation(new Vec2(width/2,height/2),new Vec2(x,y));
					image_disc.mixPixel(x,y,new Color(
							Math.abs(Math.tan(rot)),Math.abs(Math.tan(rot)),Math.abs(Math.cos(rot)))
						);
					// FIXME: prettify colour spectrum
				}
			}
		}
		image_multi.writePng("a01-constant");
		image_disc.writePng("a01-colourdisc");

		// generate suncircle
		Image image_radiation = new Image(width,height);
		for (int x=0;x<width;x++) {
			for (int y=0;y!=height;y++) {
				image_radiation.setPixel(x,y,background);
			}
		}
		image_radiation.writePng("a01-suncircle");
	}
}
