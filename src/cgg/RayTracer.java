package cgg;

import static java.lang.Math.*;
import java.util.LinkedList;
import java.util.Queue;
import static tools.Functions.*;
import tools.*;
import static cgg.Math.*;
import cgg.geom.*;
import cgg.lght.*;
import cgg.mtrl.*;
import cgg.a02.Ray;
import cgg.a02.Hit;
import cgg.a03.Scene;


public class RayTracer implements Sampler
{
	private Stage scene;
	private final Color background = color(0,0,0);
	private final Color error = color(0,.9,1);

	public RayTracer(Stage scene)
	{
		this.scene = scene;
	}

	public Color getColor(Vec2 coord)
	{
		Ray __Ray = scene.camera().generateRay(coord);
		Queue<HitTuple> __Hits = new LinkedList<>();

		// emitter
		for (Geometry g : scene.emitter())
		{
			Queue<HitTuple> __Proc = g.intersect(__Ray);
			if (recentGeometry(__Hits,__Proc)) __Hits = __Proc;
		}

		// opaque geometry
		for (Geometry g : scene.objects())
		{
			Queue<HitTuple> __Proc = g.intersect(__Ray);
			if (recentGeometry(__Hits,__Proc)) __Hits = __Proc;
		}

		// switch shading
		if (__Hits.size()==0) return background;
		Hit __Recent = __Hits.peek().front();
		Color __Result = switch (__Recent.material())
		{
		case PhysicalMaterial c -> _shadePhysical(__Recent);
		case SurfaceMaterial c -> _shadePhong(__Recent);
		case SurfaceColour c -> _shadeLaemp(__Recent);
		default -> error;
		};

		// colour correction
		__Result = subtract(color(1.),exp(multiply(__Result,-scene.camera().exposure())));
		return pow(__Result,1./2.2);
	}

	private Color _shade(Hit hit)
	{
		Vec3 lightDir = normalize(vec3(1,-1,.7));
		Color ambient = multiply(.05,hit.colour());
		Color diffuse = multiply(.9*max(0,dot(lightDir,hit.normal())),hit.colour());
		return add(ambient,diffuse);
	}

	private Color _shadePhysical(Hit hit)
	{
		// extract colour information
		Color p_Colour = hit.material().getComponent(MaterialComponent.COLOUR,hit);
		Color p_Material = hit.material().getComponent(MaterialComponent.MATERIAL,hit);

		// translate colour to surface info
		double __Metallic = p_Material.r();
		double __Roughness = p_Material.g();
		double __Occlusion = p_Material.b();
		p_Colour = color(.75,0,0);
		/*
		__Metallic = 0;
		__Roughness = .3;
		*/

		// precalculations
		double aSq = pow(__Roughness,4.);
		Vec3 __CameraDir = normalize(subtract(scene.camera().position(),hit.position()));
		Vec3 __Fresnel0 = mix(vec3(.04,.04,.04),vec3(p_Colour),__Metallic);
		double __dtLightOut = max(dot(hit.normal(),__CameraDir),.0);
		double __SchlickOut = _schlickBeckmannApprox(__dtLightOut,__Roughness);

		// processing lights
		Vec3 __Result = vec3(0,0,0);
		for (PhongIllumination p_Light : scene.phong_lights())
		{
			// shadow checking
			Vec3 __LightDir = p_Light.direction(hit.position());
			//if (_shadowCast(hit,__LightDir,p_Light.distance(hit.position()))) continue;
			// TODO: test this in a complex environment
			// TODO: reenable shadows and merge cutely with pbs environment

			// distribution component
			Vec3 __Halfway = normalize(add(__CameraDir,__LightDir));
			double __TBR = aSq/(PI*pow(pow(max(dot(hit.normal(),__Halfway),.0),2.)*(aSq-1.)+1.,2.));

			// fresnel component as through approximation
			Vec3 __Fresnel = add(
					__Fresnel0,
					multiply(
						subtract(vec3(1.),__Fresnel0),
						vec3(pow(clamp(1.-max(dot(__Halfway,__CameraDir),.0),.0,1.),5.))
					)
				);

			// geometry component
			double __dtLightIn = max(dot(hit.normal(),__LightDir),.0);
			double __Smith = _schlickBeckmannApprox(__dtLightIn,__Roughness)*__SchlickOut;

			// specular brdf
			Vec3 __CT = divide(multiply(multiply(__TBR,__Fresnel),__Smith),4.*__dtLightIn*__dtLightOut+.0001);
			Vec3 lR = multiply(subtract(vec3(1.),__Fresnel),1.-__Metallic);  // no fresnel on metallic surfaces
			lR = multiply(lR,vec3(p_Colour));  // FIXME: define a method for such cases to avoid cast
			lR = add(divide(lR,PI),__CT);  // combine fresnel & cook torrance brdf
			lR = multiply(multiply(lR,vec3(p_Light.physicalInfluence(hit.position()))),__dtLightIn);
			__Result = add(__Result,lR);
		}

		return color(__Result);
	}

	private double _schlickBeckmannApprox(double l,double r)
	{
		double __Direct = pow(r+1.,2)/8.;
		return l/(l*(1.-__Direct)+__Direct);
	}

	private Color _shadePhong(Hit hit)
	{
		// extract colour
		Color p_Colour = hit.material().getComponent(MaterialComponent.COLOUR,hit);

		// ambient component
		Color __Ambient = color(0,0,0);
		for (PhongIllumination p_Light : scene.phong_lights())
		{
			Color __LightIntensity = p_Light.intensity(hit.position());
			__Ambient = multiply(p_Colour,multiply(__LightIntensity,.7));
		}
		__Ambient = divide(__Ambient,scene.phong_lights().size());

		Color __Result = color(0,0,0);
		for (PhongIllumination p_Light : scene.phong_lights())
		{
			// precalculations
			Color __LightIntensity = p_Light.intensity(hit.position());
			Color __Albedo = multiply(__LightIntensity,.7);

			// shadow calculation
			Vec3 __LightDirection = p_Light.direction(hit.position());
			if (_shadowCast(hit,__LightDirection,p_Light.distance(hit.position()))) continue;

			// diffuse component
			double __Attitude = dot(hit.normal(),__LightDirection);
			Color __Diffuse = multiply(p_Colour,multiply(__Albedo,max(0,__Attitude)));
			__Result = add(__Result,__Diffuse);

			// specular component
			if (__Attitude>0)
			{
				Vec3 r = subtract(multiply(hit.normal(),2*dot(__LightDirection,hit.normal())),__LightDirection);
				r = normalize(r);
				Vec3 v = normalize(subtract(scene.camera().position(),hit.position()));
				Color __Specular = multiply(.2,multiply(__LightIntensity,pow(max(dot(r,v),0),50)));
				__Result = add(__Result,__Specular);
			}
		}
		return clamp(__Result);
	}

	private boolean _shadowCast(Hit hit,Vec3 ldir,double ldist)
	{
		Ray __ShadowRay = new Ray(hit.position(),ldir,.0001,ldist);
		for (Geometry g : scene.objects())
		{
			if (g.intersect(__ShadowRay).size()>0) return true;
		}
		return false;
	}

	private Color _shadeLaemp(Hit hit)
	{
		return multiply(hit.material().getComponent(MaterialComponent.COLOUR,hit),.7);
	}

	private Color _shadePosition(Hit hit,double intent)
	{
		return color(multiply(hit.position(),intent));
	}

	private Color _shadeTexture(Hit hit)
	{
		return color(hit.uv().x(),hit.uv().y(),0);
	}

	private Color _shadeNormals(Hit hit)
	{
		return color(hit.normal());
	}
}
