package cgg.a04;

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
	private ArrayList<Geometry> objects;
	private ArrayList<Geometry> emitter;
	private ArrayList<PhongIllumination> phong_lights;

	public Scene(int width,int height)
	{
		objects = new ArrayList<>();
		emitter = new ArrayList<>();
		phong_lights = new ArrayList<>();

		// projection
		camera = new Camera(vec3(0,0,0),60.,width,height,1.5);

		// geometry
		/*
		_childsplay();
		_die();
		*/
		//_yummy();
		/*
		_deathstar();
		*/
		_testing();
		_flooring();

		// lighting
		_craeveTheVorbiddenLaemp(vec3(1.25,-1,-4),color(1,.5,0),.7);
		_craeveTheVorbiddenLaemp(vec3(1.25,-2,-1),color(0,.5,1),1);
		_craeveTheVorbiddenLaemp(vec3(1.5,.75,-1),color(.7,.7,.7),.4);
		_craeveTheVorbiddenLaemp(vec3(2.5,.75,-5.4),color(.7,.7,.7),.4);
		_craeveTheVorbiddenLaemp(vec3(-1.7,-2.15,-7),color(1,1,1),.2);
	}

	private void _die()
	{
		// materials
		PhysicalMaterial mcorpus = new PhysicalMaterial(color(1.,1.,1.),"./res/marble/material.png",1);
		PhysicalMaterial mcutout = new PhysicalMaterial(color(.0,.0,.0),"./res/plastic/material.png",.1);
		// FIXME always loading same texture to colour with different constant is very inefficient

		// corpus
		Vec3 __Position = vec3(1.25,1,-4);
		Box corpus = new Box(__Position,1,1,1,mcorpus);
		Sphere rounding = new Sphere(__Position,.75,mcorpus);
		Complex die = new Complex(corpus,rounding,JoinOperation.INTERSECTION);

		// numbers
		Sphere one0 = new Sphere(add(__Position,vec3(.0,-.5,.0)),.1,mcutout);
		Sphere two0 = new Sphere(add(__Position,vec3(-.5,.25,.25)),.1,mcutout);
		Sphere two1 = new Sphere(add(__Position,vec3(-.5,-.2,-.2)),.1,mcutout);
		Sphere three0 = new Sphere(add(__Position,vec3(0,0,.5)),.1,mcutout);
		Sphere three1 = new Sphere(add(__Position,vec3(.25,-.25,.5)),.1,mcutout);
		Sphere three2 = new Sphere(add(__Position,vec3(-.25,.25,.5)),.1,mcutout);
		Complex numbers = new Complex(one0,two0,JoinOperation.UNION);
		numbers = new Complex(numbers,two1,JoinOperation.UNION);
		numbers = new Complex(numbers,three0,JoinOperation.UNION);
		numbers = new Complex(numbers,three1,JoinOperation.UNION);
		numbers = new Complex(numbers,three2,JoinOperation.UNION);
		die = new Complex(die,numbers,JoinOperation.DIFFERENCE);
		objects.add(die);
	}

	private void _childsplay()
	{
		Vec3 __Position = vec3(-1.25,1,-4);
		Box cube = new Box(__Position,1,1,1,
						   new PhysicalMaterial(color(.5,0,0),"./res/plastic/material.png",1));
		Sphere sphere0 = new Sphere(__Position,.65,
									new PhysicalMaterial(color(0,0,.5),"./res/plastic/material.png",1));
		Sphere sphere1 = new Sphere(__Position,.75,
									new PhysicalMaterial(color(0,.5,0),"./res/plastic/material.png",1));
		Box inlay = new Box(__Position,.85,.85,.85,
							new PhysicalMaterial(color(1,.5,0),"./res/plastic/material.png",1));
		Complex complex = new Complex(cube,sphere0,JoinOperation.DIFFERENCE);
		complex = new Complex(complex,sphere1,JoinOperation.INTERSECTION);
		complex = new Complex(complex,inlay,JoinOperation.DIFFERENCE);
		objects.add(complex);
	}

	private void _yummy()
	{
		Vec3 __Position = vec3(0,1,-5);
		Box corpus = new Box(__Position,1,1,1,
							 new PhysicalMaterial(color(.75,.25,0),"./res/plastic/material.png",.1));
		Sphere cut = new Sphere(add(__Position,vec3(.5,-.5,.5)),.65,
								new PhysicalMaterial(color(.1,0,.3),"./res/fabric/material.png",1));
		Complex yummy = new Complex(corpus,cut,JoinOperation.DIFFERENCE);
		objects.add(yummy);
	}

	private void _deathstar()
	{
		Vec3 __Position = vec3(0,-2,-7);
		Sphere sphere0 = new Sphere(__Position,1.,
									new PhysicalMaterial(color(0,0,.75),"./res/marble/material.png",1.));
		Sphere sphere1 = new Sphere(add(__Position,vec3(.5,0,.5)),.5,
									new PhysicalMaterial(color(.5,0,0),"./res/gold/material.png",1.));
		Complex complex = new Complex(sphere0,sphere1,JoinOperation.DIFFERENCE);
		objects.add(complex);
	}

	private void _testing()
	{
		Vec3 __Position = vec3(0,1,-4);
		objects.add(new Sphere(add(__Position,vec3(-1,0,0)),.5,
							   new PhysicalMaterial(color(0,.05,0),"./res/plastic/material.png",1)));
		objects.add(new Sphere(add(__Position,vec3(0,0,-1)),.5,
							   new PhysicalMaterial(color(.75,.25,0),"./res/gold/material.png",1)));
		objects.add(new Sphere(add(__Position,vec3(1,0,0)),.5,
							   new PhysicalMaterial(color(.75,0,0),"./res/marble/material.png",1)));
		/*
		objects.add(new Box(add(__Position,vec3(-1,0,0)),1,1,1,
							new PhysicalMaterial(color(0,.05,0),"./res/plastic/material.png")));
		*/
	}

	private void _flooring()
	{
		Vec3 __Position = vec3(0,2,-4);
		objects.add(new Box(__Position,10,1,10,
							new PhysicalMaterial("./res/checker_neo.png","./res/marble/material.png",4)));
	}

	private void _craeveTheVorbiddenLaemp(Vec3 position,Color colour,double intensity)
	{
		emitter.add(new Sphere(position,.25,new SurfaceColour(colour)));
		phong_lights.add(new PointLight(position,colour,intensity,1.,.7,1.8));
	}

	public Camera camera() { return camera; }
	public ArrayList<Geometry> objects() { return objects; }
	public ArrayList<Geometry> emitter() { return emitter; }
	public ArrayList<PhongIllumination> phong_lights() { return phong_lights; }
}
