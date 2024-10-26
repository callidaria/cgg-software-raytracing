package cgg.a02;

import tools.*;
import static tools.Functions.*;

public class RayTracer implements Sampler
{
	private Camera camera;
	private Sphere sphere;

	public RayTracer(Camera camera,Sphere sphere)
	{
		this.camera = camera;
		this.sphere = sphere;
	}

	public Color getColor(Vec2 coord)
	{
		Ray __Ray = camera.generateRay(coord);
		Hit __Hit = sphere.intersect(__Ray);
		if (__Hit==null) return color(0,0,0);
		return color(__Hit.normal());
	}
}
