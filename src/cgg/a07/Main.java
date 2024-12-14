package cgg.a07;

import static tools.Functions.*;
import tools.*;
import cgg.*;


public class Main
{
	public static void main(String[] args)
	{
		// setup frame
		int width = 100;
		int height = 100;

		// generate scene
		Scene scene = new Scene(width,height);

		// samplers
		//Sampler rt = new StratifiedSampler(new RayTracer(scene),3);
		Sampler rt = new HaltonSampler(new RayTracer(scene),3);

		// images
		cgg.Image image = new cgg.Image(width,height);
		image.sample(rt);
		image.writePng("a07-image");
	}
}
