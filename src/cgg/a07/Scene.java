package cgg.a07;

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
	private GraphNode groot;
	private ArrayList<Illumination> lights;
	private ArrayList<Geometry> emitter;
	private int dice_count;

	// textures
	private ImageTexture tex_checker;

	// materials
	private ImageTexture mat_fabric;
	private ImageTexture mat_plastic;
	private ImageTexture mat_gold;
	private ImageTexture mat_marble;

	// normal maps
	private NormalMap nrm_plastic;
	private NormalMap nrm_gold;
	private NormalMap nrm_marble;

	public Scene()
	{
		// setup
		//this.camera = new Camera(vec3(0,-1,0),vec3(20,0,0));
		this.camera = new Camera(vec3(0,-10,0),vec3(35,0,0));
		this.groot = new GraphNode();
		this.lights = new ArrayList<Illumination>();
		this.emitter = new ArrayList<Geometry>();

		// textures
		this.tex_checker = new ImageTexture("./res/checker_neo.png");

		// material
		this.mat_fabric = new ImageTexture("./res/fabric/material.png");
		this.mat_plastic = new ImageTexture("./res/plastic/material.png");
		this.mat_gold = new ImageTexture("./res/gold/material.png");
		this.mat_marble = new ImageTexture("./res/marble/material.png");

		// normal maps
		this.nrm_plastic = new NormalMap("./res/plastic/normals.png");
		this.nrm_gold = new NormalMap("./res/gold/normals.png");
		this.nrm_marble = new NormalMap("./res/marble/normals.png");

		// geometry
		//_testing();
		this.dice_count = 0;
		_flooring(vec3(0,1,-50),100);
		//groot.register_geometry(_diceStack(vec3(0,0,-10)));
		GraphNode midline = new GraphNode();
		for (int z=0;z>-100;z-=10) midline.register_geometry(_diceGroup(vec3(0,0,z)));
		midline.update_bounds();
		GraphNode leftline = new GraphNode();
		for (int z=0;z>-100;z-=10) leftline.register_geometry(_diceGroup(vec3(-10,0,z)));
		leftline.update_bounds();
		GraphNode rightline = new GraphNode();
		for (int z=0;z>-100;z-=10) rightline.register_geometry(_diceGroup(vec3(10,0,z)));
		rightline.update_bounds();
		System.out.println(dice_count);

		// assemble
		groot.register_geometry(midline);
		groot.register_geometry(leftline);
		groot.register_geometry(rightline);
		groot.update_bounds();

		// lighting
		_craeveTheVorbiddenLaemp(vec3(0,-4,-7),color(1,1,1),.4);
		//_craeveTheVorbiddenLaemp(vec3(-2,-2,0),color(1,1,1),.4);
	}

	private Geometry _diceGroup(Vec3 chunk)
	{
		GraphNode out = new GraphNode(chunk,vec3(1),vec3(0));
		for (int x=-5;x<5;x++)
		{
			for (int z=0;z>-10;z--)
				out.register_geometry(_diceStack(vec3(x,0,z)));
		}
		out.update_bounds();
		return out;
	}

	private Geometry _diceStack(Vec3 position)
	{
		GraphNode out = new GraphNode(add(position,multiply(randomDirection(),.15)),vec3(1),vec3(0));
		for (int i=0;i<random()*6+1;i++)
			out.register_geometry(_die(
						vec3(0,-.5*i,0),  // ???
						vec3(0,20*random(),0),
						randomHue(),
						.5,
						random(),
						random()
				));
		out.update_bounds();
		return out;
	}

	private Geometry _die(Vec3 pos,Vec3 rotation,Color col,double size,double metal,double roughness)
	{
		// corpus
		Box corpus = new Box(pos,size,size,size,
							 new PhysicalMaterial(col,color(metal,roughness,1)));
		Sphere rounding = new Sphere(pos,.75*size,
									 new PhysicalMaterial(multiply(col,1.5),color(metal,roughness,1)));
		Complex __Die = new Complex(corpus,rounding,JoinOperation.INTERSECTION);

		// number cutouts
		double cs = size*.1;
		PhysicalMaterial __Cutouts = new PhysicalMaterial(color(.0,.05,.0),color(0,.7,1));
		Sphere one0 = new Sphere(add(pos,multiply(vec3(.0,-.5,.0),size)),cs,__Cutouts);
		Sphere two0 = new Sphere(add(pos,multiply(vec3(-.5,.25,.25),size)),cs,__Cutouts);
		Sphere two1 = new Sphere(add(pos,multiply(vec3(-.5,-.2,-.2),size)),cs,__Cutouts);
		Sphere three0 = new Sphere(add(pos,multiply(vec3(0,0,.5),size)),cs,__Cutouts);
		Sphere three1 = new Sphere(add(pos,multiply(vec3(.25,-.25,.5),size)),cs,__Cutouts);
		Sphere three2 = new Sphere(add(pos,multiply(vec3(-.25,.25,.5),size)),cs,__Cutouts);
		Complex numbers = new Complex(one0,two0,JoinOperation.UNION);
		numbers = new Complex(numbers,two1,JoinOperation.UNION);
		numbers = new Complex(numbers,three0,JoinOperation.UNION);
		numbers = new Complex(numbers,three1,JoinOperation.UNION);
		numbers = new Complex(numbers,three2,JoinOperation.UNION);
		__Die = new Complex(__Die,numbers,JoinOperation.DIFFERENCE);
		GraphNode __Node = new GraphNode(vec3(0),vec3(1),rotation);
		__Node.register_geometry(__Die);
		__Node.update_bounds();
		dice_count++;
		return __Node;
	}

	private void _testing()
	{
		GraphNode __Node = new GraphNode(vec3(0,0,-4),vec3(1),vec3(0));
		__Node.register_geometry(new Sphere(vec3(-1,0,0),.5,
							  new PhysicalMaterial(color(0,.05,0),mat_plastic,1),
							  nrm_plastic));
		__Node.register_geometry(new Sphere(vec3(0,0,-1),.5,
							  new PhysicalMaterial(color(.75,.25,0),mat_gold,1),
							  nrm_gold));
		__Node.register_geometry(new Sphere(vec3(1,0,0),.5,
							  new PhysicalMaterial(color(.75,0,0),mat_marble,1),
							  nrm_marble));

		// write
		__Node.update_bounds();
		groot.register_geometry(__Node);
	}

	private void _flooring(Vec3 position,int size)
	{
		groot.register_geometry(new Box(position,size,1,size,new PhysicalMaterial(tex_checker,mat_marble,30)));
	}

	private void _craeveTheVorbiddenLaemp(Vec3 position,Color colour,double intensity)
	{
		emitter.add(new Sphere(position,.25,new SurfaceColour(colour)));
		lights.add(new PointLight(position,colour,intensity,1.,.7,1.8));
	}

	public Camera camera() { return camera; }
	public Geometry groot() { return groot; }
	public ArrayList<Illumination> lights() { return lights; }
	public ArrayList<Geometry> emitter() { return emitter; }
}
