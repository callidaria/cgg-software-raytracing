package cgg.a02;

import static java.lang.Math.*;
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
		// emit ray towards scene
		Ray __Ray = scene.camera().generateRay(coord);

		// process bland hits
		Hit __Hit = null;
		for (Sphere sphere : scene.blands())
		{
			Hit __Proc = sphere.intersect(__Ray);
			__Hit = (__Hit==null||(__Proc!=null&&__Proc.param()<__Hit.param())) ? __Proc : __Hit;
		}

		// process reflective hits
		boolean __ReflectRay = false;
		Hit __Proc = scene.reflector().intersect(__Ray);
		if (__Hit==null||(__Proc!=null&&__Proc.param()<__Hit.param()))
		{
			__Hit = __Proc;
			__ReflectRay = true;
		}

		// combine
		if (__Hit==null) return color(0,0,0);
		if (__ReflectRay) return shadeReflective(__Hit);
		return shade(__Hit);
	}

	private static Color shade(Hit hit)
	{
		Vec3 lightDir = normalize(vec3(1,1,.7));
		Color ambient = multiply(.1,hit.colour());
		Color diffuse = multiply(.9*max(0,dot(lightDir,hit.normal())),hit.colour());
		return add(ambient,diffuse);
	}

	private Color shadeReflective(Hit hit)
	{
		// calculate reflective bouncing ray
		Vec3 vd = subtract(hit.position(),scene.camera().position());
		Vec3 __OutBounce = add(vd,multiply(2*dot(multiply(vd,-1),hit.normal()),hit.normal()));
		Ray __Ray = new Ray(hit.position(),__OutBounce,0,10000);

		// receive colour source & combine
		Hit __Hit = null;
		for (Sphere sphere : scene.blands())
		{
			Hit __Proc = sphere.intersect(__Ray);
			__Hit = (__Hit==null||(__Proc!=null&&__Proc.param()<__Hit.param())) ? __Proc : __Hit;
		}
		Color __Fin = color(.005,.005,.005);
		if (__Hit!=null) __Fin = __Fin.mix(__Hit.colour());
		return __Fin;
		// TODO: dont reproduce code from scene iteration
		// TODO: prettify combination
	}

	// TODO: ziegelsteinspheres
	// TODO: semi-transparent volumetric gas spheres
	// TODO: rough spheres
}
