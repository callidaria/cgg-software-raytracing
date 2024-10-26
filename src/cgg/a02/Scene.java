package cgg.a02;

import java.util.ArrayList;
import static tools.Functions.*;
import tools.*;

public class Scene
{
	private Camera camera;
	private ArrayList<Sphere> blands;
	private ArrayList<Sphere> bricks;
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
		bricks.add(new Sphere(vec3(-2,-2,-6),.8,color(vec3(.7,0,0))));

		// reflector setup
		reflector = new Sphere(vec3(0,0,-4),1,color(vec3(.5,.2,.1)));
	}

	public Camera camera() { return camera; }
	public ArrayList<Sphere> blands() { return blands; }
	public ArrayList<Sphere> bricks() { return bricks; }
	public Sphere reflector() { return reflector; }
}
