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

	public Scene(int width,int height)
	{
		// setup
		this.camera = new Camera(vec3(0),60,vec3(0),width,height,1);
		this.groot = new GraphNode();
		this.lights = new ArrayList<Illumination>();
		this.emitter = new ArrayList<Geometry>();

		// assemble
		groot.update_bounds();
		// TODO
	}

	public Camera camera() { return camera; }
	public Geometry groot() { return groot; }
	public ArrayList<Illumination> lights() { return lights; }
	public ArrayList<Geometry> emitter() { return emitter; }
}
