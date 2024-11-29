package cgg.a06;

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
		Sampler rt = new StratifiedSampler(new RayTracer(scene),2);

		// images
		cgg.Image image = new cgg.Image(width,height);
		image.sample(rt);
		image.writePng("a06-image");
	}
}
