package cgg.a01;

import java.util.Random;
import java.util.ArrayList;
import cgg.Config;
import cgg.Image;
import tools.*;


public class Main {

	public static void main(String[] args) {

		// setup frame
		int width = Config.WIDTH;
		int height = Config.HEIGHT;
		Vec2 center = new Vec2(width/2,height/2);

		// generate multicircles
		Random rand = new Random();
		ArrayList<Circle> circles = new ArrayList<Circle>();
		for (int i=0;i<17;i++) {
			circles.add(new Circle(
					new Vec2(rand.nextInt(width+1),rand.nextInt(height+1)),
					rand.nextInt(65)+10,
					new Color(rand.nextFloat(1.f),rand.nextFloat(1.f),rand.nextFloat(1.f))
				));
		}

		// samplers
		Sampler smp_ccircles = new ConstantCircles(circles);
		Sampler smp_orbz = new Orbz(circles);
		Sampler smp_nineties = new NinetiesFlair(circles,center);
		Sampler smp_halo = new Halo(circles,center);
		Sampler smp_waves = new Waves(circles);

		// images
		Image image_multi = new Image();
		Image image_orbz = new Image();
		Image image_disc = new Image();
		Image image_halo = new Image();
		Image image_waves = new Image();

		// draw multicircles
		for (int x=0;x<width;x++) {
			for (int y=0;y<height;y++) {

				Vec2 coordinate = new Vec2(x,y);
				image_multi.setPixel(x,y,smp_ccircles.getColor(coordinate));
				image_orbz.setPixel(x,y,smp_orbz.getColor(coordinate));
				image_disc.setPixel(x,y,smp_nineties.getColor(coordinate));
				image_halo.setPixel(x,y,smp_halo.getColor(coordinate));
				image_waves.setPixel(x,y,smp_waves.getColor(coordinate));
			}
		}

		// write
		image_multi.writePng("a01-constant");
		image_orbz.writePng("a01-discs");
		image_disc.writePng("a01-colourdisc");
		image_halo.writePng("a01-halo");
		image_waves.writePng("a01-waves");
	}
}
