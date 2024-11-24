package cgg.a05;

import java.util.ArrayList;
import static tools.Functions.*;
import tools.*;
import cgg.*;
import cgg.geom.*;
import cgg.lght.*;
import cgg.mtrl.*;
import cgg.a02.Camera;


public class Scene implements Stage
{
	private Camera camera;
	private Geometry groot;
	private ArrayList<Geometry> objects;
	private ArrayList<Geometry> emitter;
	private ArrayList<PhongIllumination> phong_lights;

	public Scene(int width,int height)
	{
		this.objects = new ArrayList<>();
		this.emitter = new ArrayList<>();
		this.phong_lights = new ArrayList<>();

		// projection
		this.camera = new Camera(vec3(0,0,0),60.,vec3(0,0,0),width,height,1.5);

		// geometry
		_die();
		//_testing();
		_flooring();
		this.groot = new GraphNode(objects.toArray(new Geometry[objects.size()]));

		// lighting
		_craeveTheVorbiddenLaemp(vec3(1.25,.5,0),color(1),.7);
		_craeveTheVorbiddenLaemp(vec3(-1.25,0,0),color(1,.5,0),.7);
		_craeveTheVorbiddenLaemp(vec3(-.5,-.5,-7),color(.1,0,.75),.7);
		/*
		_craeveTheVorbiddenLaemp(vec3(1.25,-1,-4),color(1,.5,0),.7);
		_craeveTheVorbiddenLaemp(vec3(1.25,-2,-1),color(0,.5,1),1);
		_craeveTheVorbiddenLaemp(vec3(1.5,.75,-1),color(.7,.7,.7),.4);
		_craeveTheVorbiddenLaemp(vec3(2.5,.75,-5.4),color(.7,.7,.7),.4);
		_craeveTheVorbiddenLaemp(vec3(-1.7,-2.15,-7),color(1,1,1),.2);
		*/
	}

	private void _die()
	{
		// corpus
		Box corpus = new Box(vec3(.0),1,1,1,
							 new PhysicalMaterial(color(.7,.7,.7),"./res/glosswood/material.png",1));
		Sphere rounding = new Sphere(vec3(.0),.75,
									 new PhysicalMaterial(color(.6,.6,.6),"./res/glosswood/material.png",1));
		Complex __Die = new Complex(corpus,rounding,JoinOperation.INTERSECTION);

		// number cutouts
		PhysicalMaterial __Cutouts = new PhysicalMaterial(color(.0,.1,.0),"./res/plastic/material.png",.1);
		Sphere one0 = new Sphere(vec3(.0,-.5,.0),.1,__Cutouts);
		Sphere two0 = new Sphere(vec3(-.5,.25,.25),.1,__Cutouts);
		Sphere two1 = new Sphere(vec3(-.5,-.2,-.2),.1,__Cutouts);
		Sphere three0 = new Sphere(vec3(0,0,.5),.1,__Cutouts);
		Sphere three1 = new Sphere(vec3(.25,-.25,.5),.1,__Cutouts);
		Sphere three2 = new Sphere(vec3(-.25,.25,.5),.1,__Cutouts);
		Complex numbers = new Complex(one0,two0,JoinOperation.UNION);
		numbers = new Complex(numbers,two1,JoinOperation.UNION);
		numbers = new Complex(numbers,three0,JoinOperation.UNION);
		numbers = new Complex(numbers,three1,JoinOperation.UNION);
		numbers = new Complex(numbers,three2,JoinOperation.UNION);
		__Die = new Complex(__Die,numbers,JoinOperation.DIFFERENCE);
		// FIXME cutouts dont reflect light attractively on a regular basis

		// noding
		Geometry __Geom[] = { __Die };
		objects.add(new GraphNode(__Geom,vec3(1.25,.55,-4),vec3(1),vec3(25,25,-25)));
	}

	private void _testing()
	{
		Geometry __Spheres[] = {
			new Sphere(vec3(-1,0,0),.5,new PhysicalMaterial(color(0,.05,0),"./res/plastic/material.png",1)),
			new Sphere(vec3(0,0,-1),.5,new PhysicalMaterial(color(.75,.25,0),"./res/gold/material.png",1)),
			new Sphere(vec3(1,0,0),.5,new PhysicalMaterial(color(.75,0,0),"./res/marble/material.png",1))
		};
		objects.add(new GraphNode(__Spheres,vec3(0,1,-4),vec3(1),vec3(0,0,0)));
	}

	private void _flooring()
	{
		Vec3 __Position = vec3(0,2,-4);
		Geometry __Flooring[] = {
			new Box(__Position,10,1,10,
					new PhysicalMaterial("./res/checker_neo.png","./res/marble/material.png",4))
		};
		objects.add(new GraphNode(__Flooring));
	}

	private void _craeveTheVorbiddenLaemp(Vec3 position,Color colour,double intensity)
	{
		emitter.add(new Sphere(position,.25,new SurfaceColour(colour)));
		phong_lights.add(new PointLight(position,colour,intensity,1.,.7,1.8));
	}

	public Camera camera() { return camera; }
	public Geometry groot() { return groot; }
	public ArrayList<Geometry> objects() { return objects; }
	public ArrayList<Geometry> emitter() { return emitter; }
	public ArrayList<PhongIllumination> phong_lights() { return phong_lights; }
}
