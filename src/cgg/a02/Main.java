package cgg.a02;

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
		Camera camera = new Camera(vec3(0,0,0),90,width,height);
		Sphere sphere = new Sphere(vec3(0,0,-2),1,color(vec3(1,1,1)));

		// samplers
		Sampler sampler = new RayTracer(camera,sphere);

		// images
		cgg.Image image = new cgg.Image(width,height);

		// draw
		for (int x=0;x<width;x++)
		{
			for (int y=0;y<height;y++)
			{
				Vec2 coord = vec2(x,y);
				image.setPixel(x,y,sampler.getColor(coord));
			}
		}

		// write
		image.writePng("a02-spheres");
	}
}
