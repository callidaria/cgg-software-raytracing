package cgg.scne;

import static tools.Functions.*;
import tools.*;
import cgg.geom.*;
import cgg.mtrl.*;


// FIXME: scene is broken
public class PhongComplexScene extends Scene
{
	public PhongComplexScene()
	{
		super();

		// geometry
		_flooring();
		_childsplay();
		_die();
		_yummy();
		_deathstar();

		// lighting
		craeveTheVorbiddenLaemp(vec3(1.25,-1,-4),color(1,.5,0),.2);
		craeveTheVorbiddenLaemp(vec3(1.25,-1.5,-2),color(0,.5,1),.4);
		craeveTheVorbiddenLaemp(vec3(1.5,.75,-1),color(.7,.7,.7),.1);
		craeveTheVorbiddenLaemp(vec3(1,.75,-5.4),color(.7,.7,.7),.1);
		craeveTheVorbiddenLaemp(vec3(-1.7,-2.15,-7),color(1,1,1),.05);

		groot.update_bounds();
	}

	private void _childsplay()
	{
		Vec3 __Position = vec3(-1.25,1,-4);
		Box cube = new Box(__Position,1,1,1,new SurfaceMaterial(color(.5,0,0)));
		Sphere sphere0 = new Sphere(__Position,.65,new SurfaceMaterial(color(0,0,.5)));
		Sphere sphere1 = new Sphere(__Position,.75,new SurfaceMaterial(color(0,.5,0)));
		Box inlay = new Box(__Position,.85,.85,.85,new SurfaceMaterial(color(1,.5,0)));
		Complex complex = new Complex(cube,sphere0,JoinOperation.DIFFERENCE);
		complex = new Complex(complex,sphere1,JoinOperation.INTERSECTION);
		complex = new Complex(complex,inlay,JoinOperation.DIFFERENCE);
		groot.register_geometry(complex);
	}

	private void _die()
	{
		// corpus
		Vec3 __Position = vec3(1.25,1,-4);
		Box corpus = new Box(__Position,1,1,1,new SurfaceMaterial(color(.7,.7,.7)));
		Sphere rounding = new Sphere(__Position,.75,new SurfaceMaterial(color(.6,.6,.6)));
		Complex die = new Complex(corpus,rounding,JoinOperation.INTERSECTION);

		// numbers
		Sphere one0 = new Sphere(add(__Position,vec3(.0,-.5,.0)),.1,new SurfaceMaterial(color(.1,.1,.1)));
		Sphere two0 = new Sphere(add(__Position,vec3(-.5,.25,.25)),.1,new SurfaceMaterial(color(.1,.1,.1)));
		Sphere two1 = new Sphere(add(__Position,vec3(-.5,-.2,-.2)),.1,new SurfaceMaterial(color(.1,.1,.1)));
		Sphere three0 = new Sphere(add(__Position,vec3(0,0,.5)),.1,new SurfaceMaterial(color(.1,.1,.1)));
		Sphere three1 = new Sphere(add(__Position,vec3(.25,-.25,.5)),.1,new SurfaceMaterial(color(.1,.1,.1)));
		Sphere three2 = new Sphere(add(__Position,vec3(-.25,.25,.5)),.1,new SurfaceMaterial(color(.1,.1,.1)));
		Complex numbers = new Complex(one0,two0,JoinOperation.UNION);
		numbers = new Complex(numbers,two1,JoinOperation.UNION);
		numbers = new Complex(numbers,three0,JoinOperation.UNION);
		numbers = new Complex(numbers,three1,JoinOperation.UNION);
		numbers = new Complex(numbers,three2,JoinOperation.UNION);
		die = new Complex(die,numbers,JoinOperation.DIFFERENCE);
		groot.register_geometry(die);
	}

	private void _yummy()
	{
		Vec3 __Position = vec3(0,1,-5);
		Box corpus = new Box(__Position,1,1,1,new SurfaceMaterial(color(1,.5,0)));
		Sphere cut = new Sphere(add(__Position,vec3(.5,-.5,.5)),.65,new SurfaceMaterial(color(.5,0,.7)));
		Complex yummy = new Complex(corpus,cut,JoinOperation.DIFFERENCE);
		groot.register_geometry(yummy);
	}

	private void _deathstar()
	{
		Vec3 __Position = vec3(0,-2,-7);
		Sphere sphere0 = new Sphere(__Position,1.,new SurfaceMaterial(color(.5,0,0)));
		Sphere sphere1 = new Sphere(add(__Position,vec3(.5,0,.5)),.5,new SurfaceMaterial(color(0,0,.5)));
		Complex complex = new Complex(sphere0,sphere1,JoinOperation.DIFFERENCE);
		groot.register_geometry(complex);
	}

	private void _flooring()
	{
		Vec3 __Position = vec3(0,2,-4);
		groot.register_geometry(new Box(__Position,10,1,10,new SurfaceMaterial(color(.7,.7,.7))));
	}
}
