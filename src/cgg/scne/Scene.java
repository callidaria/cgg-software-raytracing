package cgg.scne;

import java.util.ArrayList;
import static tools.Functions.*;
import tools.*;
import cgg.a02.Camera;
import cgg.geom.*;
import cgg.lght.*;
import cgg.mtrl.*;


public abstract class Scene
{
	private static final double ATT_LINEAR = 1.;
	private static final double ATT_QUAD = .7;
	private static final double ATT_CUBE = 1.8;

	public Camera camera;
	public GraphNode groot;
	public ArrayList<Illumination> lights;
	public ArrayList<Geometry> emitter;

	protected Scene()
	{
		this.camera = new Camera();
		_init();
	}

	protected Scene(Vec3 position)
	{
		this.camera = new Camera(position);
		_init();
	}

	protected Scene(Vec3 position,Vec3 rotation)
	{
		this.camera = new Camera(position,rotation);
		_init();
	}

	private void _init()
	{
		this.groot = new GraphNode();
		this.lights = new ArrayList<Illumination>();
		this.emitter = new ArrayList<Geometry>();
	}

	protected void craeveTheVorbiddenLaemp(Vec3 position,Color colour,double intensity)
	{
		emitter.add(new Sphere(position,.25,new SurfaceColour(colour)));
		lights.add(new PointLight(position,colour,intensity,ATT_LINEAR,ATT_QUAD,ATT_CUBE));
	}
}
