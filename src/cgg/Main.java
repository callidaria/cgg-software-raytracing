package cgg;

import static tools.Functions.*;
import tools.*;
import cgg.scne.*;


public class Main
{
	public static void main(String[] args)
	{
		Scene scene = new AnimalFarm();
		Sampler rt = new HaltonSampler(new RayTracer(scene));
		Image image = new Image();
		image.sample(rt);
		image.writePng("a08-image");
	}
}
