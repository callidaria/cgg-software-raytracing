package cgg.a04;

import java.util.ArrayList;
import static tools.Functions.*;
import tools.*;
import cgg.*;
import cgg.geom.*;
import cgg.lght.*;
import cgg.a02.Camera;


public class Scene implements Stage
{
	private Camera camera;
	private ArrayList<Geometry> objects;
	private ArrayList<Geometry> emitter;
	private ArrayList<PhongIllumination> phong_lights;

	public Scene(int width,int height)
	{
		objects = new ArrayList<>();
		emitter = new ArrayList<>();
		phong_lights = new ArrayList<>();

		// projection
		camera = new Camera(vec3(0,0,0),60.,width,height);

		// geometry
		_flooring();
		_testing();

		// lighting
		//phong_lights.add(new DirectionalLight(vec3(1,-.7,-.7),color(.7,.7,.7),1));
		_craeveTheVorbiddenLaemp(vec3(1.25,-1,-4),color(1,.5,0),.7);
		_craeveTheVorbiddenLaemp(vec3(1.25,-1.5,-2),color(0,.5,1),1);
		_craeveTheVorbiddenLaemp(vec3(1.5,.75,-1),color(.7,.7,.7),.4);
		_craeveTheVorbiddenLaemp(vec3(1,.75,-5.4),color(.7,.7,.7),.4);
		_craeveTheVorbiddenLaemp(vec3(-1.7,-2.15,-7),color(1,1,1),.2);
	}

	private void _testing()
	{
		Vec3 __Position = vec3(0,1,-4);
		objects.add(new Sphere(add(__Position,vec3(-1,0,0)),.5,color(.5,0,0)));
		objects.add(new Box(add(__Position,vec3(1,0,0)),1,1,1,color(0,0,.5)));
	}

	private void _flooring()
	{
		Vec3 __Position = vec3(0,2,-4);
		objects.add(new Box(__Position,10,1,10,color(.7,.7,.7)));
	}

	private void _craeveTheVorbiddenLaemp(Vec3 position,Color colour,double intensity)
	{
		emitter.add(new Sphere(position,.25,colour));
		phong_lights.add(new PointLight(position,colour,intensity));
	}

	public Camera camera() { return camera; }
	public ArrayList<Geometry> objects() { return objects; }
	public ArrayList<Geometry> emitter() { return emitter; }
	public ArrayList<PhongIllumination> phong_lights() { return phong_lights; }
}
