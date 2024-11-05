package cgg.a03;

import java.util.ArrayList;
import static tools.Functions.*;
import tools.*;
import cgg.*;
import cgg.geom.*;
import cgg.lght.*;
import cgg.a02.Camera;

public class Scene
{
	public Camera camera;
	public ArrayList<Geometry> objects;
	public ArrayList<PhongIllumination> phong_lights;

	public Scene(int width,int height)
	{
		// projection
		camera = new Camera(vec3(0,0,0),60.,width,height);

		// geometry
		objects = new ArrayList<>();
		_flooring();
		_childsplay();
		_die();
		_yummy();
		_deathstar();

		// lighting
		phong_lights = new ArrayList<>();
		//phong_lights.add(new DirectionalLight(vec3(1,1,.7),color(1,1,1),1));
		phong_lights.add(new PointLight(vec3(-1.25,0,-4),color(1,1,1),1));
		phong_lights.add(new PointLight(vec3(1.25,-1,-4),color(1,.5,0),.4));
	}

	private void _childsplay()
	{
		Vec3 __Position = vec3(-1.25,1,-4);
		Box cube = new Box(__Position,1,1,1,color(.5,0,0));
		Sphere sphere0 = new Sphere(__Position,.65,color(0,0,.5));
		Sphere sphere1 = new Sphere(__Position,.75,color(0,.5,0));
		Box inlay = new Box(__Position,.85,.85,.85,color(1,.5,0));
		Complex complex = new Complex(cube,sphere0,JoinOperation.DIFFERENCE);
		complex = new Complex(complex,sphere1,JoinOperation.INTERSECTION);
		complex = new Complex(complex,inlay,JoinOperation.DIFFERENCE);
		objects.add(complex);
	}

	private void _die()
	{
		// corpus
		Vec3 __Position = vec3(1.25,1,-4);
		Box corpus = new Box(__Position,1,1,1,color(.7,.7,.7));
		Sphere rounding = new Sphere(__Position,.75,color(.6,.6,.6));
		Complex die = new Complex(corpus,rounding,JoinOperation.INTERSECTION);

		// numbers
		Sphere one0 = new Sphere(add(__Position,vec3(.0,-.5,.0)),.1,color(.1,.1,.1));
		Sphere two0 = new Sphere(add(__Position,vec3(-.5,.25,.25)),.1,color(.1,.1,.1));
		Sphere two1 = new Sphere(add(__Position,vec3(-.5,-.2,-.2)),.1,color(.1,.1,.1));
		Sphere three0 = new Sphere(add(__Position,vec3(0,0,.5)),.1,color(.1,.1,.1));
		Sphere three1 = new Sphere(add(__Position,vec3(.25,-.25,.5)),.1,color(.1,.1,.1));
		Sphere three2 = new Sphere(add(__Position,vec3(-.25,.25,.5)),.1,color(.1,.1,.1));
		Complex numbers = new Complex(one0,two0,JoinOperation.UNION);
		numbers = new Complex(numbers,two1,JoinOperation.UNION);
		numbers = new Complex(numbers,three0,JoinOperation.UNION);
		numbers = new Complex(numbers,three1,JoinOperation.UNION);
		numbers = new Complex(numbers,three2,JoinOperation.UNION);
		die = new Complex(die,numbers,JoinOperation.DIFFERENCE);
		objects.add(die);
	}

	private void _yummy()
	{
		Vec3 __Position = vec3(0,1,-5);
		Box corpus = new Box(__Position,1,1,1,color(1,.5,0));
		Sphere cut = new Sphere(add(__Position,vec3(.5,-.5,.5)),.65,color(.5,0,.7));
		Complex yummy = new Complex(corpus,cut,JoinOperation.DIFFERENCE);
		objects.add(yummy);
	}

	private void _deathstar()
	{
		Vec3 __Position = vec3(0,-2,-7);
		Sphere sphere0 = new Sphere(__Position,1.,color(.5,0,0));
		Sphere sphere1 = new Sphere(add(__Position,vec3(.5,0,.5)),.5,color(0,0,.5));
		Complex complex = new Complex(sphere0,sphere1,JoinOperation.DIFFERENCE);
		objects.add(complex);
	}

	private void _flooring()
	{
		Vec3 __Position = vec3(0,2,-4);
		objects.add(new Box(__Position,10,1,10,color(.7,.7,.7)));
	}
}
