package cgg.a04;

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
		Sampler rt = new RayTracer(scene);

		// images
		cgg.Image image = new cgg.Image(width,height);
		image.sample(rt);
		image.writePng("a04-image");
	}
}
