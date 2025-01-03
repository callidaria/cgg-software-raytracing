package cgg;

import static java.lang.Math.*;
import java.util.ArrayList;
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
	private Vec3[] diffuse;

	// lookup
	private final ImageTexture LUT_BRDF;
	private final ArrayList<ArrayList<Vec2>> DIFFUSE_SETS;
	private final ArrayList<ArrayList<Vec2>> SPECULAR_SETS;

	public RayTracer(Stage scene)
	{
		this.scene = scene;

		// lookup
		this.LUT_BRDF = new ImageTexture("./res/lut/brdf.png");
		this.DIFFUSE_SETS = LUT.lookup().subsets_raster(Config.DIFFUSE_SAMPLES);
		this.SPECULAR_SETS = LUT.lookup().subsets_raster(Config.SPECULAR_SAMPLES);

		// diffuse precalculation
		System.out.print("pre-processing global diffuse... ");
		/*
		int bsize = Config.WIDTH*Config.HEIGHT;
		Vec3[] __Diffuse = new Vec3[bsize];
		diffuse = new Vec3[bsize];
		for (int i=0;i<bsize;i++)
		{
			// intersection
			Vec2 __Coord = vec2(i%Config.WIDTH,(int)i/Config.WIDTH);
			Ray __Ray = scene.camera().generateRay(__Coord);
			Hit __Hit = _recentIntersection(__Ray,0);
			if (__Hit==null)
			{
				__Diffuse[i] = vec3(0);
				continue;
			}

			// material
			Color p_Colour = __Hit.material().getComponent(MaterialComponent.COLOUR,__Hit);
			Color p_Material = __Hit.material().getComponent(MaterialComponent.MATERIAL,__Hit);
			double __Metallic = p_Material.r();
			double __Roughness = p_Material.g();
			Vec3 __CameraDir = normalize(multiply(__Ray.direction(),-1));
			double __Attitude = max(dot(__Hit.normal(),__CameraDir),.0);
			Vec3 __Fresnel0 = mix(vec3(.04,.04,.04),vec3(p_Colour),__Metallic);
			Vec3 __GIFresnel = subtract(max(vec3(1.-__Roughness),__Fresnel0),__Fresnel0);
			__GIFresnel = multiply(__GIFresnel,pow(clamp(1.-__Attitude,.0,1.),5.));
			__GIFresnel = add(__Fresnel0,__GIFresnel);
			// FIXME code duplications all over the place

			// diffuse colour
			__Diffuse[i] = _diffuseComponent(__Coord,0,__Hit,__GIFresnel,__Metallic);
			diffuse[i] = __Diffuse[i];
		}
		*/
		System.out.println("done.");
		// TODO parallel preprocessing

		// diffuse map convolution through bilateral filtering
		// java language specs guarantee 0 as initial value for each array element
		System.out.print("filtering diffuse buffer... ");
		/*
		for (int y=0;y<Config.HEIGHT;y++)
		{
			for (int x=0;x<Config.WIDTH;x++)
			{
				if (y<2||y>Config.HEIGHT-3||x<2||x>Config.WIDTH-3)
				{
					diffuse[y*Config.WIDTH+x] = vec3(0);
					continue;
				}
				// FIXME write pixel partly when diameter reaches out of bounds

				// bilateral pixel processing
				Vec3 __Center = __Diffuse[y*Config.WIDTH+x];
				Vec3 __Result = vec3(0);
				double __Weight = 0;

				for (int i=-Config.BF_DIAMETER;i<Config.BF_DIAMETER;i++)
				{
					for (int j=-Config.BF_DIAMETER;j<Config.BF_DIAMETER;j++)
					{
						// current sample
						int __NX = x-i;
						int __NY = y-j;
						Vec3 __Sample = __Diffuse[__NY*Config.WIDTH+__NX];

						// gauss procedere
						Vec3 __CSign = subtract(__Sample,__Center);
						double __Gauss = exp((-pow(__NX-x,2)+pow(__NY-y,2))/(2*pow(Config.BF_SIGMA1,2)));
						__Gauss *= exp((-pow(__CSign.x(),2)+pow(__CSign.y(),2)+pow(__CSign.z(),2))
									   /(2*pow(Config.BF_SIGMA0,2)));
						/*
						double __Gauss0 = exp(-(pow(__CSign.x(),2)+pow(__CSign.y(),2)+pow(__CSign.z(),2))
											  /(2*Config.BF_SIGMA0));
						double __Gauss1 = exp(-(pow(__NX-x,2)+pow(__NY-y,2))/(2*Config.BF_SIGMA1));
		*//*
						// FIXME

						// weight
						//double __PixelWeight = __Gauss0*__Gauss1;
						__Result = add(__Result,multiply(__Sample,__Gauss));
						__Weight += __Gauss;
					}
				}

				// weighing pixel result
				diffuse[y*Config.WIDTH+x] = divide(__Result,__Weight+.0001);
			}
		}
		  */
		// FIXME boundscheck for higher diameters
		// FIXME breakdown into vertical & horizontal substeps for incredible performance benefits
		System.out.println("done.");
	}

	public Color getColor(Vec2 coord)
	{
		// compute geometry intersection
		Ray __Ray = scene.camera().generateRay(coord);
		Color __Result = _processScene(__Ray,coord,0);

		// colour correction
		__Result = subtract(color(1.),exp(multiply(__Result,-Config.EXPOSURE)));
		return pow(__Result,1./Config.GAMMA);
	}

	private Color _processScene(Ray ray,Vec2 coord,int depth)
	{
		if (depth>Config.RECURSION_DEPTH) return color(0,0,0);
		Hit __Recent = _recentIntersection(ray,depth);
		if (__Recent==null) return Config.CLEARCOLOUR;
		return switch (__Recent.material())
		{
		case PhysicalMaterial c -> _shadePhysical(__Recent,ray,coord,depth);
		case SurfaceMaterial c -> _shadePhong(__Recent);
		case SurfaceColour c -> _shadeLaemp(__Recent);
		default -> Config.ERRORCOLOUR;
		};
	}

	private Hit _recentIntersection(Ray ray,int depth)
	{
		Queue<HitTuple> __Hits = new LinkedList<>();

		// emitter
		if (depth<1)
		{
			for (Geometry g : scene.emitter())
			{
				Queue<HitTuple> __Proc = g.intersect(ray);
				__Hits = (recentGeometry(__Hits,__Proc)) ? __Proc : __Hits;
			}
		}

		// opaque geometry
		Queue<HitTuple> __Proc = scene.groot().intersect(ray);
		__Hits = (recentGeometry(__Hits,__Proc)) ? __Proc : __Hits;

		// switch shading
		if (__Hits.size()==0) return null;
		return __Hits.peek().front();
	}

	private Color _shade(Hit hit)
	{
		Vec3 lightDir = normalize(vec3(1,-1,.7));
		Color ambient = multiply(.05,hit.colour());
		Color diffuse = multiply(.9*max(0,dot(lightDir,hit.normal())),hit.colour());
		return add(ambient,diffuse);
	}

	private Color _shadePhysical(Hit hit,Ray ray,Vec2 coord,int depth)
	{
		// §§test output
		//if (depth==0) return color(diffuse[(int)coord.y()*Config.WIDTH+(int)coord.x()]);

		// extract colour information
		// colour preferredly to be a constant because the loader does not translate into sRGB colourspace
		// FIXME or else albedo textures are utterly useless
		Color p_Colour = hit.material().getComponent(MaterialComponent.COLOUR,hit);
		Color p_Material = hit.material().getComponent(MaterialComponent.MATERIAL,hit);

		// translate colour to surface info
		double __Metallic = p_Material.r();
		double __Roughness = p_Material.g();
		double __Cavity = p_Material.b();

		// precalculations
		double aSq = pow(__Roughness,4.);
		Vec3 __CameraDir = /*normalize(subtract(/*scene.camera().position()ray.d,hit.position()));*/
			normalize(multiply(ray.direction(),-1));
		Vec3 __Fresnel0 = mix(vec3(.04,.04,.04),vec3(p_Colour),__Metallic);
		double __dtLightOut = max(dot(hit.normal(),__CameraDir),.0);
		double __SchlickOut = _schlickBeckmannApprox(__dtLightOut,__Roughness);

		// direct lighting
		Vec3 __Result = vec3(0,0,0);
		for (Illumination p_Light : scene.lights())
		{
			// shadow checking
			Vec3 __LightDir = p_Light.direction(hit.position());
			if (_shadowCast(hit,__LightDir,p_Light.distance(hit.position()))) continue;

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

		// gi segment
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

		// global diffuse component
		/*
		Vec3 __DGI = (depth==0)
				? diffuse[(int)coord.y()*Config.WIDTH+(int)coord.x()]
				: _diffuseComponent(coord,depth,hit,__GIFresnel,__Metallic);
		*/
		Vec3 __DGI = _diffuseComponent(coord,depth,hit,__GIFresnel,__Metallic);

		// specular component
		// sampling from the environment
		// cant do this in a lookup table can you?
		// the paper likes to do a little trolling over here, it assumes V=N=R but then also uses V,N and R.
		// this is completely scuffed and should be banned by international law, but we'll do i guess
		Vec3 __GIResult = vec3(.0);
		Vec3 __R = subtract(multiply(2*__Attitude,hit.normal()),__CameraDir);
		double __SmpWeight = .0;  // don't forget this one, the goddamn paper forgets to declare this one!
		//int smp_specular = (int)(coord.y()*Config.WIDTH+coord.x())%SPECULAR_SETS.length;
		for (Vec2 __Hammersley : LUT.map_subset(SPECULAR_SETS,coord,Config.SPECULAR_SAMPLES))
		{
			// fpd avoidance trickery (there is a book with this hack & its generally used)
			// bitshifting in java is a different kind of adventure
			// it seems like java offers us only "baby's first toybox bitshifting for beginners"
			// there is no actual utility or even unsigneds
			// because i'm sick and tired of this, this has been preprocessed in c and imported as lut
			//Vec2 __Hammersley = SPECULAR_SETS[smp_specular][i];

			// importance sample (your lobez quark! where is my oomox after implementing this huh?)
			// the paper regenerates our aSq in two steps? we are just gonna reuse it, just aSqing questions!
			// another very good example for confusing things in the epic paper
			double phi = 2*PI*__Hammersley.x();
			double thCos = sqrt((1-__Hammersley.y())/(1+(aSq-1)*__Hammersley.y()));
			double thSin = sqrt(1-pow(thCos,2));
			Vec3 __IS = vec3(thSin*cos(phi),thSin*sin(phi),thCos);
			Vec3 up = (abs(__R.z())<.999) ? vec3(0,0,1) : vec3(1,0,0);
			Vec3 xTan = normalize(cross(up,__R));
			Vec3 yTan = cross(__R,xTan);
			__IS = add(multiply(xTan,__IS.x()),multiply(yTan,__IS.y()),multiply(__R,__IS.z()));

			// samples generate lobecast
			Vec3 __PEnvLight = subtract(multiply(2*dot(__R,__IS),__IS),__R);
			double __DEnvLight = clamp(dot(__R,__PEnvLight),.0,1.);
			if (__DEnvLight<.0001) continue;
			Ray __GIR = new Ray(hit.position(),__PEnvLight);
			__GIResult = add(__GIResult,multiply(vec3(_processScene(__GIR,coord,depth+1)),__DEnvLight));
			__SmpWeight += __DEnvLight;
		}

		// convolute samples and mix
		Vec3 __GI = divide(__GIResult,__SmpWeight);
		__GI = multiply(__GI,add(multiply(__GIFresnel,__LUT.r()),__LUT.g()));
		__GI = multiply(add(__GI,__DGI),__Cavity);
		out = mix(out,color(__GI),.5);

		return out;
	}

	private Vec3 _diffuseComponent(Vec2 coord,int depth,Hit hit,Vec3 fresnel,double metallic)
	{
		Color p_Colour = hit.material().getComponent(MaterialComponent.COLOUR,hit);
		Vec3 out = vec3(0,0,0);
		for (Vec2 __Hammersley : LUT.map_subset(DIFFUSE_SETS,coord,Config.DIFFUSE_SAMPLES))
		{
			// hämis hämis hämisphere!
			/*
			double u = 2*PI*__Hammersley.x();
			double v = sqrt(1-pow(__Hammersley.y(),2.));
			Vec3 __DiffDirection = vec3(v*cos(u),__Hammersley.y(),v*sin(u));
			Vec3 __DiffSample = normalize(__DiffDirection);
			*/
			Vec3 __DiffSample = normalize(randomDirection());
			__DiffSample = normalize(add(hit.normal(),__DiffSample));

			// trace sample
			Ray __DIR = new Ray(hit.position(),__DiffSample);
			out = add(out,vec3(_processScene(__DIR,coord,depth+1)));
		}
		out = multiply(divide(out,Config.DIFFUSE_SAMPLES),vec3(p_Colour));
		out = multiply(out,subtract(vec3(1),fresnel));
		out = multiply(out,subtract(vec3(1),metallic));
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
		for (Illumination p_Light : scene.lights())
		{
			Color __LightIntensity = p_Light.intensity(hit.position());
			__Ambient = multiply(p_Colour,multiply(__LightIntensity,.7));
		}
		__Ambient = divide(__Ambient,scene.lights().size());

		Color __Result = color(0,0,0);
		for (Illumination p_Light : scene.lights())
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
		Ray __ShadowRay = new Ray(hit.position(),ldir);
		return scene.groot().intersect(__ShadowRay).size()>0;
	}

	private Color _shadeLaemp(Hit hit)
	{
		return multiply(hit.material().getComponent(MaterialComponent.COLOUR,hit),.7);
	}

	private Color _shadePosition(Hit hit,double intent) { return color(multiply(hit.position(),intent)); }
	private Color _shadeTexture(Hit hit) { return color(hit.uv().x(),hit.uv().y(),0); }
	private Color _shadeNormals(Hit hit) { return color(hit.normal()); }
}
