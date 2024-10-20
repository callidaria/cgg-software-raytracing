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

		// generate multicircles
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

		// draw multicircles
		Vec2 center = new Vec2(width/2,height/2);
		Image image_multi = new Image(width,height);
		Image image_disc = new Image(width,height);
		Image image_orbz = new Image(width,height);
		Image image_halo = new Image(width,height);
		Image image_waves = new Image(width,height);
		for (int x=0;x<width;x++) {
			for (int y=0;y<height;y++) {

				// clear
				image_multi.setPixel(x,y,background);
				image_disc.setPixel(x,y,background);
				image_orbz.setPixel(x,y,background);
				image_halo.setPixel(x,y,background);
				image_waves.setPixel(x,y,background);

				// iterate entities
				for (Circle circle : circles) {

					// wave collision
					image_waves.mixPixel(x,y,circle.waveEmission(new Vec2(x,y)));

					// ee for all processing with distance lifetime
					if (!circle.coversPoint(new Vec2(x,y))) continue;

					// multicircles
					// barebones
					image_multi.mixPixel(x,y,circle.colour());

					// colourdiscs
					double rot = circle.orientation(center,new Vec2(x,y));
					image_disc.mixPixel(x,y,
							new Color(Math.abs(Math.tan(rot)),Math.abs(Math.tan(rot)),Math.abs(Math.cos(rot)))
							.mul(circle.colour())
						);
					// FIXME: prettify colour spectrum

					// fading orbzz
					image_orbz.addPixelColour(x,y,circle.colour().mul(circle.fadingInfluence(new Vec2(x,y))));

					// halos TODO protodistance + colour addition doesnt work for some reason
					double influence = Math.max(Math.cos(rot),.0);
					image_halo.addPixelColour(x,y,circle.colour().mul(Math.cos(rot)));
				}
			}
		}
		image_multi.writePng("a01-constant");
		image_disc.writePng("a01-colourdisc");
		image_orbz.writePng("a01-discs");
		image_halo.writePng("a01-halo");
		image_waves.writePng("a01-waves");
	}
}
