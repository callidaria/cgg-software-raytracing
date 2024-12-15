package cgg.a07;

import static tools.Functions.*;
import tools.*;
import cgg.*;


public class Main
{
	public static void main(String[] args)
	{
		// setup
		Scene scene = new Scene();
		Sampler rt = new HaltonSampler(new RayTracer(scene));
		cgg.Image image = new cgg.Image();
		image.sample(rt);
		image.writePng("a07-image");
	}
}
