package cgg.a02;

import static tools.Functions.*;
import tools.*;
import cgg.*;

public class Main
{
	public static void main(String[] args)
	{
		// setup frame
		int width = 1500;
		int height = 1500;

		// generate scene
		Scene scene = new Scene(width,height);

		// samplers
		Sampler sampler = new RayTracer(scene);

		// images
		cgg.Image image = new cgg.Image(width,height);

		// draw
		for (int x=0;x<width;x++)
		{
			for (int y=0;y<height;y++)
			{
				Vec2 coord = vec2(x,y);
				image.setPixel(x,y,sampler.getColor(coord));
			}
		}

		// write
		image.writePng("a02-spheres");
	}
}
