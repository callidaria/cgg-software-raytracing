package cgg.a03;

import static tools.Functions.*;
import tools.*;
import cgg.*;
import cgg.geom.*;
import cgg.a02.Camera;

public class Scene
{
	public Camera camera;
	public Complex complex;

	public Scene(int width,int height)
	{
		camera = new Camera(vec3(0,0,0),90.,width,height);
		Sphere sphere0 = new Sphere(vec3(-.5,0,-2),1.,color(.5,0,0));
		Sphere sphere1 = new Sphere(vec3(.5,0,-2),1.,color(.5,0,0));
		complex = new Complex(sphere0,sphere1,JoinOperation.UNION);
	}
}
