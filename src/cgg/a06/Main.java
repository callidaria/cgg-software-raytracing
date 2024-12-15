package cgg.a06;

import static tools.Functions.*;
import tools.*;
import cgg.*;


public class Main
{
	public static void main(String[] args)
	{
		Scene scene = new Scene();
		Sampler rt = new StratifiedSampler(new RayTracer(scene));
		cgg.Image image = new cgg.Image();
		image.sample(rt);
		image.writePng("a06-image");
	}
}
