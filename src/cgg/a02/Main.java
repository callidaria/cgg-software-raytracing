package cgg.a02;

import static tools.Functions.*;
import tools.*;
import cgg.*;

public class Main
{
	public static void main(String[] args)
	{
		Scene scene = new Scene();
		Sampler sampler = new RayTracer(scene);
		cgg.Image image = new cgg.Image();

		// draw
		for (int x=0;x<Config.WIDTH;x++)
		{
			for (int y=0;y<Config.HEIGHT;y++)
				image.setPixel(x,y,sampler.getColor(vec2(x,y)));
		}

		// write
		image.writePng("a02-spheres");
	}
}
