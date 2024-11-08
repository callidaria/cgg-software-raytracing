package cgg;

import static java.lang.Math.*;
import java.util.LinkedList;
import java.util.Queue;
import static tools.Functions.*;
import tools.*;
import static cgg.Math.*;
import cgg.geom.*;
import cgg.lght.*;
import cgg.a02.Ray;
import cgg.a02.Hit;
import cgg.a03.Scene;


enum ObjectType { ERROR,OBJECT,LAEMP }

public class RayTracer implements Sampler
{
	private Scene scene;
	private final Color background = color(0,0,0);
	private final Color error = color(0,.9,1);

	public RayTracer(Scene scene)
	{
		this.scene = scene;
	}

	public Color getColor(Vec2 coord)
	{
		Ray __Ray = scene.camera.generateRay(coord);
		Queue<HitTuple> __Hits = new LinkedList<>();
		ObjectType __Type = ObjectType.ERROR;

		// emitter
		for (Geometry g : scene.emitter)
		{
			Queue<HitTuple> __Proc = g.intersect(__Ray);
			if (recentGeometry(__Hits,__Proc))
			{
				__Hits = __Proc;
				__Type = ObjectType.LAEMP;
			}
		}

		// opaque geometry
		for (Geometry g : scene.objects)
		{
			Queue<HitTuple> __Proc = g.intersect(__Ray);
			if (recentGeometry(__Hits,__Proc))
			{
				__Hits = __Proc;
				__Type = ObjectType.OBJECT;
			}
		}

		// switch shading
		if (__Hits.size()==0) return background;
		Hit __Recent = __Hits.peek().front();
		switch (__Type)
		{
		case OBJECT: return _shadePhong(__Recent);
			case LAEMP: return _shadeLaemp(__Recent);
		}
		return error;
	}

	private Color _shade(Hit hit)
	{
		Vec3 lightDir = normalize(vec3(1,-1,.7));
		Color ambient = multiply(.05,hit.colour());
		Color diffuse = multiply(.9*max(0,dot(lightDir,hit.normal())),hit.colour());
		return add(ambient,diffuse);
	}

	private Color _shadePhong(Hit hit)
	{
		// ambient component
		Color __Ambient = color(0,0,0);
		for (PhongIllumination p_Light : scene.phong_lights)
		{
			Color __LightIntensity = p_Light.intensity(hit.position());
			__Ambient = multiply(hit.colour(),multiply(__LightIntensity,.7));
		}
		__Ambient = divide(__Ambient,scene.phong_lights.size());

		Color __Result = color(0,0,0);
		for (PhongIllumination p_Light : scene.phong_lights)
		{
			// precalculations
			Color __LightIntensity = p_Light.intensity(hit.position());
			Color __Albedo = multiply(__LightIntensity,.7);

			// shadow calculation
			Vec3 __LightDirection = p_Light.direction(hit.position());
			Ray __ShadowRay = new Ray(hit.position(),__LightDirection,.0001,p_Light.distance(hit.position()));
			boolean __InShadow = false;
			for (Geometry g : scene.objects)
			{
				if (g.intersect(__ShadowRay).size()>0)
				{
					__InShadow = true;
					break;
				}
			}
			if (__InShadow) continue;

			// diffuse component
			double __Attitude = dot(hit.normal(),__LightDirection);
			Color __Diffuse = multiply(hit.colour(),multiply(__Albedo,max(0,__Attitude)));
			__Result = add(__Result,__Diffuse);

			// specular component
			if (__Attitude>0)
			{
				Vec3 r = subtract(multiply(hit.normal(),2*dot(__LightDirection,hit.normal())),__LightDirection);
				r = normalize(r);
				Vec3 v = normalize(subtract(scene.camera.position(),hit.position()));
				Color __Specular = multiply(.2,multiply(__LightIntensity,pow(max(dot(r,v),0),50)));
				__Result = add(__Result,__Specular);
			}
		}
		return clamp(__Result);
	}

	private Color _shadeLaemp(Hit hit)
	{
		return multiply(hit.colour(),.7);
	}

	private Color _shadePosition(Hit hit,double intent)
	{
		return color(multiply(hit.position(),intent));
	}

	private Color _shadeNormals(Hit hit)
	{
		return color(hit.normal());
	}
}
