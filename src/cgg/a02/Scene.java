package cgg.a02;

import java.util.ArrayList;
import static tools.Functions.*;
import tools.*;

public class Scene
{
	private Camera camera;
	private ArrayList<Sphere> blands;
	private ArrayList<Sphere> bricks;
	private ArrayList<Sphere> glass;
	private Sphere reflector;

	public Scene(int width,int height)
	{
		// camera setup
		this.camera = new Camera(vec3(0,0,0),90,width,height);

		// bland sphere setup
		blands = new ArrayList<Sphere>();
		blands.add(new Sphere(vec3(-2,2,-3),.75,color(vec3(.5,0,0))));
		blands.add(new Sphere(vec3(-3,-2,-4),.5,color(vec3(0,.5,0))));
		blands.add(new Sphere(vec3(2,2,-3),.3,color(vec3(0,0,.5))));
		blands.add(new Sphere(vec3(2,-2,-8),1,color(vec3(1,.5,0))));

		// brick setup
		bricks = new ArrayList<Sphere>();
		bricks.add(new Sphere(vec3(1,.5,-2),.4,color(vec3(.7,0,0))));
		bricks.add(new Sphere(vec3(-2,-1,-6),.8,color(vec3(.7,.7,.7))));

		// glass setup
		glass = new ArrayList<Sphere>();
		glass.add(new Sphere(vec3(-.5,.35,-1.5),.5,color(vec3(.3,.1,.6))));
		glass.add(new Sphere(vec3(2.5,-2.5,-4),.5,color(vec3(.5,.1,0))));
		glass.add(new Sphere(vec3(-.5,3,-4),.4,color(vec3(0,0,.5))));

		// generate additional background spheres
		Color colour[] = {
			color(vec3(.5,0,0)),
			color(vec3(.5,.5,0)),
			color(vec3(.5,0,.5)),
			color(vec3(0,.5,0)),
			color(vec3(0,.5,.5)),
			color(vec3(0,0,.5)),
			color(vec3(.5,.2,.2)),
			color(vec3(.2,.5,.2)),
			color(vec3(.2,.2,.5)),
			color(vec3(.5,.5,.5))
		};
		for (int i=0;i<10;i++)
		{
			Vec3 position = vec3(-10+20*random(),-10+20*random(),-10-7*random());
			double radius = .5+random();
			blands.add(new Sphere(position,radius,colour[i]));
		}

		// reflector setup
		reflector = new Sphere(vec3(0,0,-4),1,color(vec3(.5,.1,0)));
	}

	public Camera camera() { return camera; }
	public ArrayList<Sphere> blands() { return blands; }
	public ArrayList<Sphere> bricks() { return bricks; }
	public ArrayList<Sphere> glass() { return glass; }
	public Sphere reflector() { return reflector; }
}
