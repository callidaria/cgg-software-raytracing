package cgg.a03;

import static tools.Functions.*;
import tools.*;
import cgg.*;

public class Main
{
	public static void main(String[] args)
	{
		// setup
		Scene scene = new Scene();
		Sampler rt = new RayTracer(scene);
		cgg.Image image = new cgg.Image();

		// draw
		for (int x=0;x<Config.WIDTH;x++)
		{
			for (int y=0;y<Config.HEIGHT;y++)
				image.setPixel(x,y,rt.getColor(vec2(x,y)));
		}

		// write
		image.writePng("a03-spheres");
	}
}
