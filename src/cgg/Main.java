package cgg;

import static tools.Functions.*;
import tools.*;
import cgg.scne.*;


public class Main
{
	public static void main(String[] args)
	{
		// geometry
		Scene scene = new BodhisPoolRepair();

		// raytracer
		RayTracer rt = new RayTracer(scene);
		rt.bake();

		// image
		Image image = new Image();
		image.sample(new HaltonSampler(rt));
		image.writePng("cgg-competition-ws-24-103717");
	}
}
