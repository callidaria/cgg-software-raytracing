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

		// process glass spheres
		boolean __GlassRay = false;
		__Proc = iterateSpheres(scene.glass(),__Ray);
		if (recentSurfaceHit(__Hit,__Proc))
		{
			__Hit = __Proc;
			__GlassRay = true;
		}

		// combine
		if (__Hit==null) return color(0,0,0);
		if (__GlassRay) return shadeGlass(__Ray,__Hit);
		if (__ReflectRay) return shadeReflective(__Ray,__Hit);
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

	private Hit processOpaqueScene(Ray ray)
	{
		Hit __Hit = processScene(ray);
		Hit __Proc = scene.reflector().intersect(ray);
		if (__Proc!=null&&recentSurfaceHit(__Hit,__Proc))
		{
			__Proc.overwriteColour(shadeReflective(ray,__Proc));
			return __Proc;
		}
		return __Hit;
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
		hit.overwriteNormal(randomRoughness(hit.normal(),7));
		// TODO: find out if shade with roughness is hotter or notter
		Vec3 lightDir = normalize(vec3(1,1,.7));
		Color ambient = multiply(.05,hit.colour());
		Color diffuse = multiply(.9*max(0,dot(lightDir,hit.normal())),hit.colour());
		return add(ambient,diffuse);
	}

	private static Color shadeBrick(Hit hit)
	{
		// horizontal brick splitting
		double h_bricks = abs(hit.position().y())%.1;
		boolean h_koof = h_bricks<.015;

		// vertical
		int tilt = (int)(hit.position().y()/.1);
		Vec3 normal2D = multiplyPoint(rotate(vec3(0,1,0),tilt*12),vec3(hit.normal().x(),0,hit.normal().z()));
		double deg_tilt = cos(dot(normal2D,vec3(0,0,1))/length(normal2D));
		double v_bricks = abs(deg_tilt)%.1;
		boolean v_koof = v_bricks<.015;

		// calculate colour & normals
		if (h_koof) hit.overwriteNormal(interplolate(vec3(0,1,0),vec3(0,-1,0),h_bricks/.015));
		else if (v_koof) hit.overwriteNormal(multiplyPoint(rotate(vec3(0,1,0),(v_bricks-.0075)/.0075*-70),hit.normal()));
		hit.overwriteColour((h_koof||v_koof) ? color(.7,.7,.7) : hit.colour());
		return shade(hit);
	}
	// FIXME: naming issues?!?? interPLOlate?

	private Color shadeReflective(Ray ray,Hit hit)
	{
		// calculate reflective bouncing ray
		Vec3 __OutBounce = bounce(ray.direction(),hit.normal());
		__OutBounce = randomRoughness(__OutBounce,7);
		Ray __Ray = new Ray(hit.position(),__OutBounce,0,10000);

		// receive colour source & combine
		Hit __Hit = processScene(__Ray);
		Color __Fin = shade(hit);
		if (__Hit!=null) __Fin = __Fin.mix(__Hit.colour(),.9);
		return __Fin;
	}
	// TODO: random rotation adjustment on surface bounce

	private Color shadeGlass(Ray ray,Hit hit)
	{
		// calculate glass ray deformation
		Ray __RayThrough = new Ray(hit.position(),multiply(hit.normal(),-1),0,10000);
		Ray __RayBounce = new Ray(hit.position(),randomRoughness(bounce(ray.direction(),hit.normal()),2),0,10000);

		// assemble material colour
		double fresnel_mod = pow(angle(ray.direction(),hit.normal())/(Math.PI*.5),4);
		Color __MatColour = multiply(hit.colour(),.1*fresnel_mod);

		// receive colour source & combine
		Hit __HitThrough = processOpaqueScene(__RayThrough);
		Hit __HitBounce = processOpaqueScene(__RayBounce);
		__MatColour = (__HitThrough!=null) ? __MatColour.mix(__HitThrough.colour()) : __MatColour;
		__MatColour = (__HitBounce!=null) ? __MatColour.mix(__HitBounce.colour(),.1) : __MatColour;
		return __MatColour;
	}

	// TODO: semi-transparent volumetric gas spheres in background
}
