package cgg.a06;

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
		this.camera = new Camera(vec3(0),60.,vec3(0),width,height,1.5);

		// geometry
		_testing();
		_flooring();
		groot = new GraphNode(objects);

		// lighting
		_craeveTheVorbiddenLaemp(vec3(-2,-.5,-3.5),color(1),.7);
	}

	private void _testing()
	{
		ArrayList<Geometry> __Node = new ArrayList<Geometry>();
		__Node.add(new Sphere(vec3(-1,0,0),.5,
							  new PhysicalMaterial(color(0,.05,0),"./res/plastic/material.png",1)));
		__Node.add(new Sphere(vec3(0,0,-1),.5,
							  new PhysicalMaterial(color(.75,.25,0),"./res/gold/material.png",1)));
		__Node.add(new Sphere(vec3(1,0,0),.5,
							  new PhysicalMaterial(color(.75,0,0),"./res/marble/material.png",1)));
		objects.add(new GraphNode(__Node,vec3(0,1,-4),vec3(1),vec3(0,0,0)));
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
	public Geometry groot() { return groot; }
	public ArrayList<Geometry> objects() { return objects; }
	public ArrayList<Geometry> emitter() { return emitter; }
	public ArrayList<PhongIllumination> phong_lights() { return phong_lights; }
}
