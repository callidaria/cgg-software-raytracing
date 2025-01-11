package cgg;

import static tools.Functions.*;
import tools.*;
import cgg.scne.*;


public class Main
{
	public static void main(String[] args)
	{
		// geometry
		Scene scene = new AnimalFarm();

		// raytracer
		RayTracer rt = new RayTracer(scene);
		rt.bake();

		// image
		Image image = new Image();
		image.sample(new HaltonSampler(rt));
		image.writePng("a08-image");
	}
}
