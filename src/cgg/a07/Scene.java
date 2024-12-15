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

	// materials
	private ImageTexture mat_fabric;

	public Scene()
	{
		// setup
		this.camera = new Camera(vec3(0,-1,0),vec3(20,0,0));
		this.groot = new GraphNode();
		this.lights = new ArrayList<Illumination>();
		this.emitter = new ArrayList<Geometry>();

		// material
		this.mat_fabric = new ImageTexture("./res/fabric/material.png");

		// geometry
		//_testing();
		_flooring(vec3(0,3,-50),100);
		groot.register_geometry(_diceStack(vec3(0,2,-7)));
		//groot.register_geometry(_die(vec3(0,2,-7),color(.7,0,0),1,1,.2));

		// assemble
		groot.update_bounds();

		// lighting
		_craeveTheVorbiddenLaemp(vec3(0,-2,-10),color(1,1,1),.7);
	}

	private Geometry _diceStack(Vec3 position)
	{
		GraphNode out = new GraphNode(position,vec3(1),vec3(0));
		for (int i=0;i<random()*4;i++)
			out.register_geometry(_die(
						vec3(0,i*-1,0),
						vec3(0,20*random(),0),
						randomHue(),
						1,
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
		GraphNode __Node = new GraphNode(vec3(0),vec3(1),rotation);
		__Node.register_geometry(__Die);
		__Node.update_bounds();
		return __Node;
	}

	private void _flooring(Vec3 position,int size)
	{
		groot.register_geometry(new Box(position,size,1,size,new PhysicalMaterial(color(0,.5,0),mat_fabric,4)));
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
