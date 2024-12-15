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
	private ArrayList<Illumination> lights;

	private ImageTexture mat_plastic;
	private ImageTexture mat_gold;
	private ImageTexture mat_marble;

	public Scene()
	{
		objects = new ArrayList<>();
		emitter = new ArrayList<>();
		lights = new ArrayList<>();

		// projection
		camera = new Camera(vec3(0,0,0));

		// materials
		this.mat_plastic = new ImageTexture("./res/plastic/material.png");
		this.mat_gold = new ImageTexture("./res/gold/material.png");
		this.mat_marble = new ImageTexture("./res/marble/material.png");

		// geometry
		_testing();
		_flooring();

		// lighting
		_craeveTheVorbiddenLaemp(vec3(1.25,-1,-4),color(1,.5,0),.7);
		_craeveTheVorbiddenLaemp(vec3(1.25,-2,-1),color(0,.5,1),1);
		_craeveTheVorbiddenLaemp(vec3(1.5,.75,-1),color(.7,.7,.7),.4);
		_craeveTheVorbiddenLaemp(vec3(2.5,.75,-5.4),color(.7,.7,.7),.4);
		_craeveTheVorbiddenLaemp(vec3(-1.7,-2.15,-7),color(1,1,1),.2);
	}

	private void _testing()
	{
		Vec3 __Position = vec3(0,1,-4);
		objects.add(new Sphere(add(__Position,vec3(-1,0,0)),.5,
							   new PhysicalMaterial(color(0,.05,0),mat_plastic,1)));
		objects.add(new Sphere(add(__Position,vec3(0,0,-1)),.5,
							   new PhysicalMaterial(color(.75,.25,0),mat_gold,1)));
		objects.add(new Sphere(add(__Position,vec3(1,0,0)),.5,
							   new PhysicalMaterial(color(.75,0,0),mat_marble,16)));
	}

	private void _flooring()
	{
		Vec3 __Position = vec3(0,2,-4);
		objects.add(new Box(__Position,10,1,10,
							new PhysicalMaterial(new ImageTexture("./res/checker_neo.png"),mat_marble,4)));
	}

	private void _craeveTheVorbiddenLaemp(Vec3 position,Color colour,double intensity)
	{
		emitter.add(new Sphere(position,.25,new SurfaceColour(colour)));
		lights.add(new PointLight(position,colour,intensity,1.,.7,1.8));
	}

	public Camera camera() { return camera; }
	public Geometry groot() { return null; }
	public ArrayList<Geometry> emitter() { return emitter; }
	public ArrayList<Illumination> lights() { return lights; }
}
