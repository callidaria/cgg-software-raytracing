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
	private ArrayList<Illumination> lights;
	private ArrayList<Geometry> dice;
	private ArrayList<Geometry> spheres;

	// materials
	private ImageTexture mat_plastic;
	private ImageTexture mat_gold;
	private ImageTexture mat_marble;

	// normal maps
	private NormalMap nrm_plastic;
	private NormalMap nrm_gold;
	private NormalMap nrm_marble;

	public Scene()
	{
		this.objects = new ArrayList<>();
		this.emitter = new ArrayList<>();
		this.lights = new ArrayList<>();

		// projection
		this.camera = new Camera(vec3(0),vec3(0));
		this.dice = new ArrayList<Geometry>();
		this.spheres = new ArrayList<Geometry>();

		// materials
		this.mat_plastic = new ImageTexture("./res/plastic/material.png");
		this.mat_gold = new ImageTexture("./res/gold/material.png");
		this.mat_marble = new ImageTexture("./res/marble/material.png");

		// normal maps
		this.nrm_plastic = new NormalMap("./res/plastic/normals.png");
		this.nrm_gold = new NormalMap("./res/gold/normals.png");
		this.nrm_marble = new NormalMap("./res/marble/normals.png");

		// geometry
		_childsplay();
		for (int i=0;i<5;i++) _sphere(vec3((i-2)*1.5,1,0),color(.1*i,0,.5-.1*i),0,i*.2);
		for (int i=0;i<5;i++) _die(vec3((i-2)*1.5,-.5,0),color(1-.2*i,.5+i*.05,0),1,i*.2);
		_flooring();
		objects.add(new GraphNode(dice,vec3(0,0,-5.5),vec3(1),vec3(0,0,0)));
		objects.add(new GraphNode(spheres,vec3(0,0,-5.5),vec3(1),vec3(0,0,0)));
		groot = new GraphNode(objects);

		// lighting
		_craeveTheVorbiddenLaemp(vec3(-2,-.5,-3.5),color(1),.7);
		_craeveTheVorbiddenLaemp(vec3(0,1,-4),color(1,.5,0),.4);
		_craeveTheVorbiddenLaemp(vec3(2,-1,-5),color(1,1,1),1);
	}

	private void _childsplay()
	{
		Vec3 __Position = vec3(0,1,-4);
		Box cube = new Box(__Position,1,1,1,new PhysicalMaterial(color(.5,0,0),color(1,0,1)));
		Sphere sphere0 = new Sphere(__Position,.65,new PhysicalMaterial(color(0,0,.5),color(0,.2,1)));
		Sphere sphere1 = new Sphere(__Position,.75,new PhysicalMaterial(color(0,.5,0),color(0,.2,1)));
		Box inlay = new Box(__Position,.85,.85,.85,new PhysicalMaterial(color(1,.5,0),color(1,0,1)));
		Complex complex = new Complex(cube,sphere0,JoinOperation.DIFFERENCE);
		complex = new Complex(complex,sphere1,JoinOperation.INTERSECTION);
		complex = new Complex(complex,inlay,JoinOperation.DIFFERENCE);
		objects.add(complex);
	}

	private void _sphere(Vec3 pos,Color col,double metal,double roughness)
	{
		Sphere sphere = new Sphere(pos,.5,new PhysicalMaterial(col,color(metal,roughness,1)));
		spheres.add(sphere);
	}

	private void _die(Vec3 pos,Color col,double metal,double roughness)
	{
		// corpus
		Box corpus = new Box(pos,1,1,1,
							 new PhysicalMaterial(col,color(metal,roughness,1)));
		Sphere rounding = new Sphere(pos,.75,
									 new PhysicalMaterial(multiply(col,1.5),color(metal,roughness,1)));
		Complex __Die = new Complex(corpus,rounding,JoinOperation.INTERSECTION);

		// number cutouts
		PhysicalMaterial __Cutouts = new PhysicalMaterial(color(.0,.05,.0),color(0,.7,1));
		Sphere one0 = new Sphere(add(pos,vec3(.0,-.5,.0)),.1,__Cutouts);
		Sphere two0 = new Sphere(add(pos,vec3(-.5,.25,.25)),.1,__Cutouts);
		Sphere two1 = new Sphere(add(pos,vec3(-.5,-.2,-.2)),.1,__Cutouts);
		Sphere three0 = new Sphere(add(pos,vec3(0,0,.5)),.1,__Cutouts);
		Sphere three1 = new Sphere(add(pos,vec3(.25,-.25,.5)),.1,__Cutouts);
		Sphere three2 = new Sphere(add(pos,vec3(-.25,.25,.5)),.1,__Cutouts);
		Complex numbers = new Complex(one0,two0,JoinOperation.UNION);
		numbers = new Complex(numbers,two1,JoinOperation.UNION);
		numbers = new Complex(numbers,three0,JoinOperation.UNION);
		numbers = new Complex(numbers,three1,JoinOperation.UNION);
		numbers = new Complex(numbers,three2,JoinOperation.UNION);
		__Die = new Complex(__Die,numbers,JoinOperation.DIFFERENCE);
		dice.add(__Die);
	}

	private void _testing()
	{
		ArrayList<Geometry> __Node = new ArrayList<Geometry>();
		__Node.add(new Sphere(vec3(-1,0,0),.5,
							  new PhysicalMaterial(color(0,.05,0),mat_plastic,1),
							  nrm_plastic));
		__Node.add(new Sphere(vec3(0,0,-1),.5,
							  new PhysicalMaterial(color(.75,.25,0),mat_gold,1),
							  nrm_gold));
		__Node.add(new Sphere(vec3(1,0,0),.5,
							  new PhysicalMaterial(color(.75,0,0),mat_marble,1),
							  nrm_marble));
		objects.add(new GraphNode(__Node,vec3(0,1,-4),vec3(1),vec3(0,0,0)));
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
