package cgg.a02;

import java.util.ArrayList;
import static tools.Functions.*;
import tools.*;

public class Scene
{
	private Camera camera;
	private ArrayList<Sphere> blands;
	private Sphere reflector;

	public Scene(int width,int height)
	{
		// camera setup
		this.camera = new Camera(vec3(0,0,0),90,width,height);

		// bland sphere setup
		blands = new ArrayList<Sphere>();
		blands.add(new Sphere(vec3(-2,2,-3),1,color(vec3(.5,0,0))));
		blands.add(new Sphere(vec3(-3,-2,-4),1,color(vec3(0,.5,0))));
		blands.add(new Sphere(vec3(2,2,-2),1,color(vec3(0,0,.5))));
		blands.add(new Sphere(vec3(2,-2,-8),1,color(vec3(1,.5,0))));

		// reflector setup
		reflector = new Sphere(vec3(0,0,-4),1,color(vec3(.5,.2,.1)));
	}

	public Camera camera() { return camera; }
	public ArrayList<Sphere> blands() { return blands; }
	public Sphere reflector() { return reflector; }
}
