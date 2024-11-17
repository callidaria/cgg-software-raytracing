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
	private final ImageTexture LUT_BRDF = new ImageTexture("./res/lut/brdf.png");

	public RayTracer(Stage scene)
	{
		this.scene = scene;
	}

	public Color getColor(Vec2 coord)
	{
		Ray __Ray = scene.camera().generateRay(coord);
		return _processScene(__Ray,0);
	}


	private Color _processScene(Ray ray,int depth)
	{
		if (depth>4) return color(0,0,0);
		Queue<HitTuple> __Hits = new LinkedList<>();

		// emitter
		if (depth<1)
		{
			for (Geometry g : scene.emitter())
			{
				Queue<HitTuple> __Proc = g.intersect(ray);
				if (recentGeometry(__Hits,__Proc)) __Hits = __Proc;
			}
		}

		// opaque geometry
		for (Geometry g : scene.objects())
		{
			Queue<HitTuple> __Proc = g.intersect(ray);
			if (recentGeometry(__Hits,__Proc)) __Hits = __Proc;
		}

		// switch shading
		if (__Hits.size()==0) return background;
		Hit __Recent = __Hits.peek().front();
		Color __Result = switch (__Recent.material())
		{
		case PhysicalMaterial c -> _shadePhysical(__Recent,ray,depth);
		case SurfaceMaterial c -> _shadePhong(__Recent);
		case SurfaceColour c -> _shadeLaemp(__Recent);
		default -> error;
		};

		// colour correction
		__Result = subtract(color(1.),exp(multiply(__Result,-scene.camera().exposure())));
		return pow(__Result,1./2.2);
		// FIXME multiple colour corrections when bouncing
	}

	private Color _shade(Hit hit)
	{
		Vec3 lightDir = normalize(vec3(1,-1,.7));
		Color ambient = multiply(.05,hit.colour());
		Color diffuse = multiply(.9*max(0,dot(lightDir,hit.normal())),hit.colour());
		return add(ambient,diffuse);
	}

	private Color _shadePhysical(Hit hit,Ray ray,int depth)
	{
		// extract colour information
		// colour preferredly to be a constant colour because the loader does not translate into sRGB colourspace
		// FIXME or else albedo textures are utterly useless
		Color p_Colour = hit.material().getComponent(MaterialComponent.COLOUR,hit);
		Color p_Material = hit.material().getComponent(MaterialComponent.MATERIAL,hit);

		// translate colour to surface info
		double __Metallic = p_Material.r();
		double __Roughness = p_Material.g();
		double __Cavity = p_Material.b();

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
			if (_shadowCast(hit,__LightDir,p_Light.distance(hit.position()))) continue;
			// FIXME shadows look horrible in this pipeline. make it work!
			// SIRE! colour correction is working against us! cant live with or without it my beautiful...
			// TODO make this all work together with csg system (do it last, should be easy)

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
			lR = multiply(lR,vec3(p_Colour));  // FIXME define a method for such cases to avoid cast
			lR = add(divide(lR,PI),__CT);  // combine fresnel & cook torrance brdf
			lR = multiply(multiply(lR,vec3(p_Light.physicalInfluence(hit.position()))),__dtLightIn);
			__Result = add(__Result,lR);
		}
		Color out = color(__Result);

		// gi (testing)
		// ...fresnel again (probata et commendatae!)
		// the HLSL code uses saturate() (dumb name) here, but max is all we need, dot product is never <0
		double __Attitude = max(dot(hit.normal(),__CameraDir),.0);
		Vec3 __GIFresnel = subtract(max(vec3(1.-__Roughness),__Fresnel0),__Fresnel0);
		__GIFresnel = multiply(__GIFresnel,pow(clamp(1.-__Attitude,.0,1.),5.));
		__GIFresnel = add(__Fresnel0,__GIFresnel);

		// integral lookup table
		// table source from epic games has been flipped to conform with tex coords starting in the upper left
		// how much does this save? correct: almost nothing (single signswap), but we do what we can over here
		// and yes i just ripped it from the paper directly instead of computing it, no time for shenanigansry
		Color __LUT = LUT_BRDF.getColor(vec2(__Attitude,__Roughness));

		// sampling from the environment
		// cant do this in a lookup table can you?
		Vec3 __GIResult = vec3(0);
		Vec3 __R = subtract(multiply(2*__Attitude,hit.normal()),__CameraDir);
		double __SmpWeight = .0;  // dont forget this one, the goddamn paper forgets to declare this one!
		final int SAMPLES = 1;
		for (int i=0;i<SAMPLES;i++)
		{
			// fpd avoidance trickery (there is a book with this hack & its generally used)
			// bitshifting in java is a different kind of adventure
			int __VDCorput = (i<<16)|(i>>>16);
			__VDCorput = ((__VDCorput&0x55555555)<<1|(__VDCorput&0xAAAAAAAA)>>>1);
			__VDCorput = ((__VDCorput&0x33333333)<<2|(__VDCorput&0xCCCCCCCC)>>>2);
			__VDCorput = ((__VDCorput&0x0F0F0F0F)<<4|(__VDCorput&0xF0F0F0F0)>>>4);
			__VDCorput = ((__VDCorput&0x00FF00FF)<<8|(__VDCorput&0xFF00FF00)>>>8);
			Vec2 __Hammersley = vec2(
					(float)i/(float)SAMPLES,
					Float.intBitsToFloat(__VDCorput)*2.3283064365386963e-10
				);

			// importance sample (your lobez quark! where is my oomox after implementing this huh?)
			// the paper regenerates our aSq in two steps? we are just gonna reuse it, just aSqing questions!
			double phi = 2*PI*__Hammersley.y();
			double thCos = sqrt((1-__Hammersley.y())/(1+(aSq-1)*__Hammersley.y()));
			double thSin = sqrt(1-pow(thCos,2.));
			Vec3 __IS = vec3(thSin*cos(phi),thSin*sin(phi),thCos);
			Vec3 up = (abs(__R.z())<.999) ? vec3(0,0,1) : vec3(1,0,0);
			Vec3 xTan = normalize(cross(up,__R));
			Vec3 yTan = cross(__R,xTan);
			__IS = add(multiply(xTan,__IS.x()),multiply(yTan,__IS.y()),multiply(__R,__IS.z()));

			// samples generate lobecast
			Vec3 __PEnvLight = subtract(multiply(2*dot(__CameraDir,__IS),__IS),__CameraDir);
			double __DEnvLight = max(dot(hit.normal(),__PEnvLight),.0);
			if (__DEnvLight<.0001) continue;
			Ray __GIR = new Ray(hit.position(),__PEnvLight,.001,1000.);
			__GIResult = add(__GIResult,multiply(vec3(_processScene(__GIR,++depth)),__DEnvLight));
			__SmpWeight += __DEnvLight;
		}

		// convolute samples and mix
		Vec3 __GI = divide(__GIResult,__SmpWeight);
		__GI = multiply(__GI,add(multiply(__GIFresnel,__LUT.r()),__LUT.g()));
		out = mix(out,color(__GI),.5);

		return out;
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
