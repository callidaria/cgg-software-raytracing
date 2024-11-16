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
		_flooring();
		_testing();

		// lighting
		/*
		phong_lights.add(new DirectionalLight(vec3(1,-1,1),color(.7,.7,.7),1.));
		phong_lights.add(new DirectionalLight(vec3(-1,1,-1),color(0,0,.1),1.));
		*/
		_craeveTheVorbiddenLaemp(vec3(-1,0,-1),color(1,1,1),2);
		_craeveTheVorbiddenLaemp(vec3(1,-1,-4),color(1,.5,0),2);
		_craeveTheVorbiddenLaemp(vec3(2,-2,0),color(.7,.7,.7),4);
		_craeveTheVorbiddenLaemp(vec3(-1.25,-2,-5),color(0,0,.5),1);
	}

	private void _testing()
	{
		Vec3 __Position = vec3(0,1,-4);
		//objects.add(new Sphere(add(__Position,vec3(-1,0,0)),.5,new SurfaceMaterial("./res/checker.png")));
		objects.add(new Sphere(add(__Position,vec3(-1,0,0)),.5,
							   new PhysicalMaterial(color(0,.05,0),"./res/plastic/material.png",1)));
		objects.add(new Sphere(add(__Position,vec3(0,0,-1)),.5,
							   new PhysicalMaterial(color(1,.25,0),"./res/gold/material.png",1)));
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
		//objects.add(new Box(__Position,10,1,10,new SurfaceMaterial(color(.7,.7,.7))));
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
