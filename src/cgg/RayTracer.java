package cgg;

import static java.lang.Math.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Stream;
import static tools.Functions.*;
import tools.*;
import static cgg.Math.*;
import cgg.geom.*;
import cgg.lght.*;
import cgg.mtrl.*;
import cgg.scne.*;
import cgg.a02.Ray;
import cgg.a02.Hit;


public class RayTracer implements Sampler
{
	private Scene scene;
	private Vec3[] diffuse;

	// lookup
	private final ImageTexture LUT_BRDF;
	/*
	private final ArrayList<ArrayList<Vec2>> DIFFUSE_SETS;
	private final ArrayList<ArrayList<Vec2>> SPECULAR_SETS;
	*/

	public RayTracer(Scene scene)
	{
		this.scene = scene;

		// lookup
		this.LUT_BRDF = new ImageTexture("./res/lut/brdf.png");
		/*
		this.DIFFUSE_SETS = LUT.lookup().subsets_raster(Config.DIFFUSE_SAMPLES);
		this.SPECULAR_SETS = LUT.lookup().subsets_raster(Config.SPECULAR_SAMPLES);
		*/
	}

	public void bake()
	{
		// diffuse precalculation
		int bsize = Config.WIDTH*Config.HEIGHT;
		this.diffuse = _globalIllumination(bsize);

		// diffuse map convolution through bilateral filtering
		// java language specs guarantee 0 as initial value for each array element
		// looping for x and y individually, multiplication and addition is faster than modulo and division
		/*
		double sigd = pow(Config.BF_SIGMA_D,2);
		double sigr = pow(Config.BF_SIGMA_R,2);
		*/
		/*
		this.diffuse = _bilateralFilter(diffuse,bsize,16,sigd,sigr);
		this.diffuse = _bilateralFilter(diffuse,bsize,4,sigd,sigr);
		this.diffuse = _bilateralFilter(diffuse,bsize,2,sigd,sigr);
		*/
		//this.diffuse = _bilateralFilter(diffuse,bsize,1,sigd,sigr);
		// FIXME breakdown into vertical & horizontal substeps for incredible performance benefits
		// TODO find out if this breakdown even does something interesting without dithering
		// TODO fix, it still looks horrible
	}

	private Vec3[] _globalIllumination(int bsize)
	{
		Vec3[] out = new Vec3[bsize];

		// threading
		System.out.println("pre-processing global diffuse...");
		AdvancementData adv_data = new AdvancementData(bsize);
		Thread __AllFaxNoPrinter = new Thread(new AdvancementPrinter(adv_data));
		__AllFaxNoPrinter.start();

		// diffuse precalculation
		StopWatch __Timing = new StopWatch();
		Stream.iterate(0,i -> i<bsize,i -> i+1)
			.unordered()
			.parallel()
			.forEach(i -> {
					out[i] = _precalculateDiffuse(i);
					adv_data.adv++;
				});
		adv_data.done = true;
		__Timing.stop();

		// end threading and print stats
		try { __AllFaxNoPrinter.join(); }
		catch (InterruptedException e) {  }
		System.out.printf("\ndone in %.2fs\n",__Timing.time_seconds());
		return out;
	}

	private Vec3 _precalculateDiffuse(int i)
	{
		// intersection
		Vec2 __Coord = vec2(i%Config.WIDTH,(int)i/Config.WIDTH);
		Ray __Ray = scene.camera.generateRay(__Coord);
		Hit __Hit = _recentIntersection(__Ray,0);
		if (__Hit==null) return vec3(0);

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
		return _diffuseComponent(__Coord,0,__Hit,__GIFresnel,__Metallic);
	}

	private Vec3[] _bilateralFilter(Vec3[] ds,int bsize,int steps,double sigd,double sigr)
	{
		Vec3[] out = new Vec3[bsize];

		// threading
		System.out.println("filtering diffuse buffer "+steps+"x...");
		AdvancementData adv_data = new AdvancementData(bsize);
		Thread __AllFaxNoPrinter = new Thread(new AdvancementPrinter(adv_data));
		__AllFaxNoPrinter.start();

		// filtering
		StopWatch __Timing = new StopWatch();
		Stream.iterate(0,y -> y<Config.HEIGHT,y -> y+1)
			.unordered()
			.parallel()
			.forEach(y -> Stream.iterate(0,x -> x<Config.WIDTH,x -> x+1)
					 .forEach(x -> {
							 out[y*Config.WIDTH+x] = _sigmaBilateralFilter(ds,x,y,steps,sigd,sigr);
							 adv_data.adv++;
						 })
				);
		adv_data.done = true;
		__Timing.stop();

		// end threading and print statistics
		try { __AllFaxNoPrinter.join(); }
		catch (InterruptedException e) {  }
		System.out.printf("\ndone in %.2fs\n",__Timing.time_seconds());
		return out;
	}

	private Vec3 _sigmaBilateralFilter(Vec3[] ds,int x,int y,int steps,double sigd,double sigr)
	{
		int rlin = y*Config.WIDTH+x;
		Vec3 __Center = ds[rlin];
		double __Weight = 0;
		Vec3 out = vec3(0);
		for (int i=-Config.BF_DIAMETER;i<=Config.BF_DIAMETER;i++)
		{
			for (int j=-Config.BF_DIAMETER;j<=Config.BF_DIAMETER;j++)
			{
				int __NX = x-i*steps;
				int __NY = y-j*steps;

				// out of bounds ee
				if (__NX<0||__NX>=Config.WIDTH||__NY<0||__NY>=Config.HEIGHT) continue;

				// gauss kernel procedere
				Vec3 __Sample = ds[__NY*Config.WIDTH+__NX];
				double __Gauss = exp(
						-((pow(x-__NX,2)+pow(y-__NY,2))/(2.*sigd))
						-(pow(luma(__Center)-luma(__Sample),2)/(2*sigr))
					);

				// weight
				out = add(out,multiply(__Sample,__Gauss));
				__Weight += __Gauss;
			}
		}

		// normalize pixel result
		return divide(out,max(__Weight,.0001));
	}

	public Color getColor(Vec2 coord)
	{
		// compute geometry intersection
		Ray __Ray = scene.camera.generateRay(coord);
		Color __Result = _processScene(__Ray,coord,0);

		// colour correction
		__Result = subtract(color(1.),exp(multiply(__Result,-Config.EXPOSURE)));
		return pow(__Result,1./Config.GAMMA);
	}

	private Color _processScene(Ray ray,Vec2 coord,int depth)
	{
		if (depth>Config.RECURSION_DEPTH) return color(0,0,0);
		Hit __Recent = _recentIntersection(ray,depth);

		// intersection colour
		Color __GX = Config.CLEARCOLOUR;
		if (__Recent!=null)
		{
			__GX = switch (__Recent.material())
			{
			case PhysicalMaterial c -> _shadePhysical(__Recent,ray,coord,depth);
			case TransmittingMaterial c -> _shadeTransmitting(__Recent,ray,coord,depth);
			case SurfaceMaterial c -> _shadePhong(__Recent);
			case SurfaceColour c -> _shadeLaemp(__Recent);
			default -> Config.ERRORCOLOUR;
			};
		}

		// combine with volumetric component
		//if (depth==0) __GX = add(__GX,_computeVolumetric(ray,__Recent));
		return __GX;
	}

	private Hit _recentIntersection(Ray ray,int depth)
	{
		Queue<HitTuple> __Hits = new LinkedList<>();

		// emitter
		if (depth<1)
		{
			for (Geometry g : scene.emitter)
			{
				Queue<HitTuple> __Proc = g.intersect(ray);
				__Hits = (recentGeometry(__Hits,__Proc)) ? __Proc : __Hits;
			}
		}

		// opaque geometry
		Queue<HitTuple> __Proc = scene.groot.intersect(ray);
		__Hits = (recentGeometry(__Hits,__Proc)) ? __Proc : __Hits;

		// switch shading
		if (__Hits.size()==0) return null;
		return __Hits.peek().front();
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
		Vec3 __CameraDir = /*normalize(subtract(/*scene.camera.position()ray.d,hit.position()));*/
			normalize(multiply(ray.direction(),-1));
		Vec3 __Fresnel0 = mix(vec3(.04,.04,.04),vec3(p_Colour),__Metallic);
		double __dtLightOut = max(dot(hit.normal(),__CameraDir),.0);
		double __SchlickOut = _schlickBeckmannApprox(__dtLightOut,__Roughness);

		// direct lighting
		Vec3 __Result = vec3(0,0,0);
		for (Illumination p_Light : scene.lights)
		{
			// shadow checking
			Vec3 __LightDir = p_Light.direction(hit.position());
			if (_shadowCast(hit,__LightDir)) continue;

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
		Vec3 __DGI = (depth==0)
				? diffuse[(int)coord.y()*Config.WIDTH+(int)coord.x()]
				: _diffuseComponent(coord,depth,hit,__GIFresnel,__Metallic);
		//Vec3 __DGI = _diffuseComponent(coord,depth,hit,__GIFresnel,__Metallic);

		// specular component
		// sampling from the environment
		// cant do this in a lookup table can you?
		// the paper likes to do a little trolling over here, it assumes V=N=R but then also uses V,N and R.
		// this is completely scuffed and should be banned by international law, but we'll do i guess
		Vec3 __GIResult = vec3(.0);
		Vec3 __R = subtract(multiply(2*__Attitude,hit.normal()),__CameraDir);
		double __SmpWeight = .0;  // don't forget this one, the goddamn paper forgets to declare this one!
		//for (Vec2 __Hammersley : LUT.map_subset(SPECULAR_SETS,coord,Config.SPECULAR_SAMPLES))
		for (int i=0;i<LUT.lookup().lookup_static_range();i++)
		{
			// fpd avoidance trickery (there is a book with this hack & its generally used)
			// bitshifting in java is a different kind of adventure
			// it seems like java offers us only "baby's first toybox bitshifting for beginners"
			// there is no actual utility or even unsigneds
			// because i'm sick and tired of this, this has been preprocessed in c and imported as lut
			Vec2 __Hammersley = LUT.lookup().lookup_static(i);

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
		return mix(out,color(__GI),.5);
	}

	private Color _shadeTransmitting(Hit hit,Ray ray,Vec2 coord,int depth)
	{
		Vec3 p_Normal = hit.normal();
		double __N0 = 1.;
		double __N1 = hit.material().getComponent(MaterialComponent.MASS,hit).r();

		// check for exit ray
		double __CosTheta = dot(p_Normal,ray.direction());
		if (__CosTheta>0)
		{
			// recalculate
			p_Normal = multiply(p_Normal,-1);
			__CosTheta = dot(p_Normal,ray.direction());

			// swap factors
			__N0 = __N1;
			__N1 = 1.;
		}

		// calculate refraction
		double __Schlick = transmissionSchlick(__CosTheta,__N0,__N1);
		Color __Refraction = color(0,0,0);
		if (__Schlick<.99)
		{
			Vec3 __RefractDirection = refract(ray.direction(),p_Normal,__CosTheta,__N0,__N1);
			if (__RefractDirection!=null)
			{
				Ray __RefractRay = new Ray(hit.position(),__RefractDirection);
				__Refraction = _processScene(__RefractRay,coord,depth);
				__Refraction = multiply(__Refraction,1-__Schlick);
			}
			else __Schlick = 1.;
		}

		// calculate reflection
		Color __Reflection = color(0,0,0);
		if (__Schlick>.01)
		{
			Ray __ReflectRay = new Ray(hit.position(),bounce(ray.direction(),p_Normal));
			__Reflection = _processScene(__ReflectRay,coord,depth+1);
			__Reflection = multiply(__Reflection,__Schlick);
		}

		// combine components
		return add(__Reflection,__Refraction);
		// FIXME there is some weird blue tint over refraction component
	}

	private Vec3 _diffuseComponent(Vec2 coord,int depth,Hit hit,Vec3 fresnel,double metallic)
	{
		// ee in case of metallic or special reflectance situation
		Vec3 out = vec3(0,0,0);
		if (/*greater(fresnel,Config.E_FRESNEL_DIFFUSE)||*/metallic>Config.E_METALLIC_DIFFUSE) return out;

		// iterate over diffuse samples
		//for (Vec2 __Hammersley : LUT.map_subset(DIFFUSE_SETS,coord,Config.DIFFUSE_SAMPLES))
		//for (int i=0;i<LUT.lookup().lookup_static_range();i++)
		for (int i=0;i<Config.DIFFUSE_SAMPLES;i++)
		{
			Vec2 __Hammersley = LUT.lookup().lookup_static(i);

			// hämis hämis hämisphere!
			/*
			double u = 2*PI*__Hammersley.x();
			double v = sqrt(1-pow(__Hammersley.y(),2.));
			Vec3 __DiffDirection = vec3(v*cos(u),v*sin(u),__Hammersley.y());
			//Vec3 __DiffDirection = vec3(v*cos(u),__Hammersley.y(),v*sin(u));
			Vec3 __DiffSample = normalize(add(hit.normal(),__DiffDirection));
			__DiffSample = hit.normal();
			*/
			Vec3 __DiffSample = normalize(randomDirection());
			__DiffSample = normalize(add(hit.normal(),__DiffSample));
			//System.out.println(i+": "+__DiffDirection+" -> "+__DiffSample);

			// trace sample
			Ray __DIR = new Ray(hit.position(),__DiffSample);
			out = add(out,vec3(_processScene(__DIR,coord,depth+1)));
		}
		//System.out.println();
		out = multiply(divide(out,Config.DIFFUSE_SAMPLES),
					   vec3(hit.material().getComponent(MaterialComponent.COLOUR,hit)));
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
		for (Illumination p_Light : scene.lights)
		{
			Color __LightIntensity = p_Light.intensity(hit.position());
			__Ambient = multiply(p_Colour,multiply(__LightIntensity,.7));
		}
		__Ambient = divide(__Ambient,scene.lights.size());

		Color __Result = color(0,0,0);
		for (Illumination p_Light : scene.lights)
		{
			// precalculations
			Color __LightIntensity = p_Light.intensity(hit.position());
			Color __Albedo = multiply(__LightIntensity,.7);

			// shadow calculation
			Vec3 __LightDirection = p_Light.direction(hit.position());
			if (_shadowCast(hit,__LightDirection)) continue;

			// diffuse component
			double __Attitude = dot(hit.normal(),__LightDirection);
			Color __Diffuse = multiply(p_Colour,multiply(__Albedo,max(0,__Attitude)));
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

	private boolean _shadowCast(Hit hit,Vec3 ldir)
	{
		Ray __ShadowRay = new Ray(hit.position(),ldir);
		Queue<HitTuple> __Hits = scene.groot.intersect(__ShadowRay);
		if (__Hits.size()==0) return false;
		for (HitTuple __Tuple : __Hits) if (__Tuple.front().param()<1||__Tuple.back().param()<1) return true;
		return false;
	}

	private Color _shadeLaemp(Hit hit)
	{
		return multiply(hit.material().getComponent(MaterialComponent.COLOUR,hit),.7);
	}

	private Color _shadePosition(Hit hit,double intent) { return color(multiply(hit.position(),intent)); }
	private Color _shadeTexture(Hit hit) { return color(hit.uv().x(),hit.uv().y(),0); }
	private Color _shadeNormals(Hit hit) { return color(multiply(hit.normal(),vec3(1,-1,1))); }

	private Color _computeVolumetric(Ray ray,Hit hit)
	{
		Color out = color(0);

		// fallback to maximum range when no geometric intersection
		if (hit==null) hit = new Hit(1000,ray.calculatePosition(1000),vec2(0,0),vec3(0),null);

		// raymarching until intersection
		double n = .0;
		while (n<hit.param())
		{
			// iterate lightsources
			for (Illumination p_Light : scene.lights)
			{
				Vec3 __RayPosition = ray.calculatePosition(n);
				Vec3 __LightDirection = p_Light.direction(__RayPosition);

				// check for obfuscation through geometry
				Ray __Ray = new Ray(__RayPosition,__LightDirection);
				Hit __Obfuscation = _recentIntersection(__Ray,1);

				// accumulate volumetric result
				if (__Obfuscation==null||__Obfuscation.param()>1)
					out = multiply(.001,add(out,p_Light.intensity(__RayPosition)));
			}
			n += .1;
		}

		return out;
	}
}
