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
	private ArrayList<Illumination> lights;

	private ImageTexture mat_glosswood;
	private ImageTexture mat_plastic;
	private ImageTexture mat_gold;
	private ImageTexture mat_marble;

	public Scene(int width,int height)
	{
		this.objects = new ArrayList<>();
		this.emitter = new ArrayList<>();
		this.lights = new ArrayList<>();

		// projection
		this.camera = new Camera(vec3(.3,-.5,1),60.,vec3(15,7,0),width,height,1.5);

		// materials
		this.mat_glosswood = new ImageTexture("./res/glosswood/material.png");
		this.mat_plastic = new ImageTexture("./res/plastic/material.png");
		this.mat_gold = new ImageTexture("./res/gold/material.png");
		this.mat_marble = new ImageTexture("./res/marble/material.png");

		// geometry
		_die();
		_testing();
		_flooring();
		groot = new GraphNode(objects);

		// lighting
		_craeveTheVorbiddenLaemp(vec3(1.25,-.5,0),color(1),.7);
		_craeveTheVorbiddenLaemp(vec3(-1,0,0),color(1,.5,0),.7);
		_craeveTheVorbiddenLaemp(vec3(-2.75,-1,-7),color(.95,.8,1),.7);
	}

	private void _die()
	{
		// corpus
		Box corpus = new Box(vec3(.0),1,1,1,
							 new PhysicalMaterial(color(.7,.7,.7),mat_glosswood,1));
		Sphere rounding = new Sphere(vec3(.0),.75,
									 new PhysicalMaterial(color(.6,.6,.6),mat_glosswood,1));
		Complex __Die = new Complex(corpus,rounding,JoinOperation.INTERSECTION);

		// number cutouts
		PhysicalMaterial __Cutouts = new PhysicalMaterial(color(.0,.05,.0),mat_plastic,.1);
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

		// noding
		ArrayList<Geometry> __GADie = new ArrayList<>();
		ArrayList<Geometry> __Node = new ArrayList<>();
		__GADie.add(__Die);
		__Node.add(new GraphNode(__GADie,vec3(0,.4,0),vec3(1),vec3(0,30,-25)));
		__Node.add(new GraphNode(__GADie,vec3(.05,-1,0),vec3(1),vec3(0,45,-25)));
		objects.add(new GraphNode(__Node,vec3(1.25,0,-4),vec3(1),vec3(0)));
		objects.add(new GraphNode(__Node,vec3(-4.7,1,-10),vec3(.45),vec3(-5,20,0)));
		objects.add(new GraphNode(__Node,vec3(-4,.9,-10),vec3(.45),vec3(5,10,0)));
	}

	private void _testing()
	{
		// triplesphere
		ArrayList<Geometry> __Node = new ArrayList<Geometry>();
		__Node.add(new Sphere(vec3(-1,0,0),.5,
							  new PhysicalMaterial(color(0,.05,0),mat_plastic,1)));
		__Node.add(new Sphere(vec3(0,0,-1),.5,
							  new PhysicalMaterial(color(.75,.25,0),mat_gold,1)));
		__Node.add(new Sphere(vec3(1,0,0),.5,
							  new PhysicalMaterial(color(.75,0,0),mat_marble,1)));
		objects.add(new GraphNode(__Node,vec3(-1,1,-3),vec3(1),vec3(0,0,0)));

		// sphereflower
		ArrayList<Geometry> __Flowers = new ArrayList<Geometry>();
		__Flowers.add(new GraphNode(__Node));
		__Flowers.add(new GraphNode(__Node,vec3(0,0,-2),vec3(1),vec3(0,180,0)));
		objects.add(new GraphNode(__Flowers,vec3(-1,-1,-9),vec3(1),vec3(90,0,0)));
		objects.add(new GraphNode(__Flowers,vec3(-10,-1,-17),vec3(1),vec3(110,45,25)));
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
	public Geometry groot() { return groot; }
	public ArrayList<Geometry> emitter() { return emitter; }
	public ArrayList<Illumination> lights() { return lights; }
}
