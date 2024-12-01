package cgg.a06;

import static tools.Functions.*;
import tools.*;
import cgg.*;


public class Main
{
	public static void main(String[] args)
	{
		// setup frame
		int width = 400;
		int height = 400;

		// generate scene
		Scene scene = new Scene(width,height);

		// samplers
		Sampler rt = new StratifiedSampler(new RayTracer(scene),1);

		// images
		cgg.Image image = new cgg.Image(width,height);
		image.sample(rt);
		image.writePng("a06-image");
	}
}
