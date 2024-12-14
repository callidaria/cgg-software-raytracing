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

	// textures
	private ImageTexture tex_checker;

	// materials
	private ImageTexture mat_marble;

	public Scene(int width,int height)
	{
		// setup
		this.camera = new Camera(vec3(0),60,vec3(0),width,height,1);
		this.groot = new GraphNode();
		this.lights = new ArrayList<Illumination>();
		this.emitter = new ArrayList<Geometry>();

		// texture
		this.tex_checker = new ImageTexture("./res/checker_neo.png");

		// material
		this.mat_marble = new ImageTexture("./res/marble/material.png");

		// geometry
		_flooring();

		// assemble
		groot.update_bounds();

		// lighting
		_craeveTheVorbiddenLaemp(vec3(0,0,-7),color(1,1,1),.7);
	}

	private void _flooring()
	{
		Vec3 __Position = vec3(0,2,-4);
		groot.register_geometry(new Box(vec3(0,2,-4),10,1,10,new PhysicalMaterial(tex_checker,mat_marble,4)));
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
