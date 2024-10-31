package cgg;

import static java.lang.Math.*;
import java.util.ArrayList;
import static tools.Functions.*;
import tools.*;
import cgg.geom.*;
import cgg.a02.Ray;
import cgg.a02.Hit;
import cgg.a03.Scene;


public class RayTracer implements Sampler
{
	private Scene scene;
	private final Color background = color(0,0,0);

	public RayTracer(Scene scene)
	{
		this.scene = scene;
	}

	public Color getColor(Vec2 coord)
	{
		Ray __Ray = scene.camera.generateRay(coord);
		ArrayList<HitTuple> __Hits = scene.complex.intersect(__Ray);
		return (__Hits.size()>0) ? shade(__Hits.get(0).front()) : background;
	}

	public Color shade(Hit hit)
	{
		Vec3 lightDir = normalize(vec3(1,1,.7));
		Color ambient = multiply(.05,hit.colour());
		Color diffuse = multiply(.9*max(0,dot(lightDir,hit.normal())),hit.colour());
		return add(ambient,diffuse);
	}
}
