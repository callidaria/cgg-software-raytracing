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
		camera = new Camera(vec3(0,0,0),60.,width,height);

		// geometry
		_flooring();
		_testing();

		// lighting
		phong_lights.add(new DirectionalLight(vec3(1,-1,1),color(.7,.7,.7),1.));
		/*
		_craeveTheVorbiddenLaemp(vec3(-1,0,-1),color(1,1,1),.7);
		_craeveTheVorbiddenLaemp(vec3(1,-1,-4),color(1,.5,0),.7);
		_craeveTheVorbiddenLaemp(vec3(-1.25,-2,-5),color(0,0,.5),.4);
		*/
	}

	private void _testing()
	{
		Vec3 __Position = vec3(0,1,-4);
		//objects.add(new Sphere(add(__Position,vec3(-1,0,0)),.5,new SurfaceMaterial("./res/checker.png")));
		objects.add(new Sphere(add(__Position,vec3(-1,0,0)),.5,
							   new PhysicalMaterial("./res/marble/colour.png","./res/marble/material.png")));
		objects.add(new Box(add(__Position,vec3(1,0,0)),1,1,1,new SurfaceMaterial(color(0,0,.5))));
	}

	private void _flooring()
	{
		Vec3 __Position = vec3(0,2,-4);
		objects.add(new Box(__Position,10,1,10,new SurfaceMaterial(color(.7,.7,.7))));
	}

	private void _craeveTheVorbiddenLaemp(Vec3 position,Color colour,double intensity)
	{
		emitter.add(new Sphere(position,.25,new SurfaceColour(colour)));
		phong_lights.add(new PointLight(position,colour,intensity));
	}

	public Camera camera() { return camera; }
	public ArrayList<Geometry> objects() { return objects; }
	public ArrayList<Geometry> emitter() { return emitter; }
	public ArrayList<PhongIllumination> phong_lights() { return phong_lights; }
}
