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
		Image image0 = new Image(width, height);
		for (int x = 0; x != width; x++) {
			for (int y = 0; y != height; y++) {
				image0.setPixel(x,y,background);
				for (Circle circle : circles) {
					boolean hits = circle.coversPoint(new Vec2(x,y));
					if (circle.coversPoint(new Vec2(x,y))) image0.mixPixel(x,y,circle.colour());
				}
			}
		}
		image0.writePng("a01-constant");

		// generate suncircle
		Image image1 = new Image(width,height);
		for (int x=0;x<width;x++) {
			for (int y=0;y!=height;y++) {
				image1.setPixel(x,y,background);
			}
		}
		image1.writePng("a01-suncircle");
	}
}
