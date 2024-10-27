package cgg.a02;

import static java.lang.Math.*;
import java.util.ArrayList;
import tools.*;
import static tools.Functions.*;

public class RayTracer implements Sampler
{
	private Scene scene;

	public RayTracer(Scene scene)
	{
		this.scene = scene;
	}

	public Color getColor(Vec2 coord)
	{
		// emit ray towards scene & process
		Ray __Ray = scene.camera().generateRay(coord);
		Hit __Hit = processScene(__Ray);

		// process reflective hits
		boolean __ReflectRay = false;
		Hit __Proc = scene.reflector().intersect(__Ray);
		if (recentSurfaceHit(__Hit,__Proc))
		{
			__Hit = __Proc;
			__ReflectRay = true;
		}

		// combine
		if (__Hit==null) return color(0,0,0);
		if (__ReflectRay) return shadeReflective(__Hit);
		return __Hit.colour();
	}

	private Hit processScene(Ray ray)
	{
		// process bland spheres
		Hit hit = iterateSpheres(scene.blands(),ray);

		// process brick spheres
		Hit __Hit = iterateSpheres(scene.bricks(),ray);
		if (__Hit!=null&&recentSurfaceHit(hit,__Hit))
		{
			hit = __Hit;
			hit.setType(1);
		}

		// surface processing
		if (hit==null) return null;
		switch (hit.type())
		{
			case 0: hit.overwriteColour(shade(hit));
				break;
			case 1: hit.overwriteColour(shadeBrick(hit));
				break;
			default:
		}

		return hit;
	}

	private Hit iterateSpheres(ArrayList<Sphere> spheres,Ray ray)
	{
		Hit hit = null;
		for (Sphere sphere : spheres)
		{
			Hit __Proc = sphere.intersect(ray);
			hit = recentSurfaceHit(hit,__Proc) ? __Proc : hit;
		}
		return hit;
	}

	private boolean recentSurfaceHit(Hit h0,Hit h1)
	{
		return h0==null||(h1!=null&&h1.param()<h0.param());
	}

	private static Color shade(Hit hit)
	{
		Vec3 lightDir = normalize(vec3(1,1,.7));
		Color ambient = multiply(.1,hit.colour());
		Color diffuse = multiply(.9*max(0,dot(lightDir,hit.normal())),hit.colour());
		return add(ambient,diffuse);
	}

	private static Color shadeBrick(Hit hit)
	{
		// horizontal brick splitting
		double h_bricks = abs(mod(hit.position(),vec3(.1,.1,.1)).y());
		boolean h_koof = h_bricks<.015;

		// vertical
		Vec3 normal2D = vec3(hit.normal().x(),0,hit.normal().z());
		double v_bricks = abs(toDegrees(cos(dot(normal2D,vec3(0,0,1))/length(normal2D))))%7;
		boolean v_koof = v_bricks<.8;

		// calculate colour & normals
		Color __Colour = (h_koof||v_koof) ? color(.7,.7,.7) : hit.colour();
		if (h_koof) hit.overwriteNormal(interplolate(vec3(0,1,0),vec3(0,-1,0),h_bricks/.015));
		else if (h_koof) hit.overwriteNormal(multiplyPoint(rotate(vec3(0,1,0),(v_bricks-.4)/.4),hit.normal()));
		hit.overwriteColour((h_koof||v_koof) ? color(.7,.7,.7) : hit.colour());
		return shade(hit);
	}
	// FIXME: naming issues?!?? interPLOlate?
	// TODO: make equidistant and rotate on each layer

	private Color shadeReflective(Hit hit)
	{
		// calculate reflective bouncing ray
		Vec3 vd = subtract(hit.position(),scene.camera().position());
		Vec3 __OutBounce = add(vd,multiply(2*dot(multiply(vd,-1),hit.normal()),hit.normal()));
		Ray __Ray = new Ray(hit.position(),__OutBounce,0,10000);

		// receive colour source & combine
		Hit __Hit = processScene(__Ray);
		Color __Fin = color(.005,.005,.005);
		if (__Hit!=null) __Fin = __Fin.mix(__Hit.colour());
		return __Fin;
		// TODO: prettify combination
	}

	// TODO: brickspheres
	// TODO: semi-transparent volumetric gas spheres in background
	// TODO: concaving glass balls
	// TODO: rough spheres
}
