package cgg.scne;

import java.util.ArrayList;
import static tools.Functions.*;
import tools.*;
import cgg.geom.*;
import cgg.mtrl.*;


public class MaterialLineupScene extends Scene
{
	private ArrayList<Geometry> dice;
	private ArrayList<Geometry> spheres;

	public MaterialLineupScene()
	{
		super();
		this.dice = new ArrayList<Geometry>();
		this.spheres = new ArrayList<Geometry>();

		// geometry
		_childsplay();
		for (int i=0;i<5;i++) _sphere(vec3((i-2)*1.5,1,0),color(.1*i,0,.5-.1*i),0,i*.2);
		for (int i=0;i<5;i++) _die(vec3((i-2)*1.5,-.5,0),color(1-.2*i,.5+i*.05,0),1,i*.2);
		Structures.floor(groot,vec3(0,2,-4),10,4);

		// lighting
		craeveTheVorbiddenLaemp(vec3(-2,-.5,-3.5),color(1),.7);
		craeveTheVorbiddenLaemp(vec3(0,1,-4),color(1,.5,0),.4);
		craeveTheVorbiddenLaemp(vec3(2,-1,-5),color(1,1,1),1);

		// assemble
		groot.register_geometry(new GraphNode(dice,vec3(0,0,-5.5),vec3(1),vec3(0,0,0)));
		groot.register_geometry(new GraphNode(spheres,vec3(0,0,-5.5),vec3(1),vec3(0,0,0)));
		groot.update_bounds();
	}

	private void _childsplay()
	{
		Vec3 __Position = vec3(0,1,-4);
		Box cube = new Box(__Position,1,1,1,new PhysicalMaterial(color(.5,0,0),color(1,0,1)));
		Sphere sphere0 = new Sphere(__Position,.65,new PhysicalMaterial(color(0,0,.5),color(0,.2,1)));
		Sphere sphere1 = new Sphere(__Position,.75,new PhysicalMaterial(color(0,.5,0),color(0,.2,1)));
		Box inlay = new Box(__Position,.85,.85,.85,new PhysicalMaterial(color(1,.5,0),color(1,0,1)));
		Complex complex = new Complex(cube,sphere0,JoinOperation.DIFFERENCE);
		complex = new Complex(complex,sphere1,JoinOperation.INTERSECTION);
		complex = new Complex(complex,inlay,JoinOperation.DIFFERENCE);
		groot.register_geometry(complex);
	}

	private void _sphere(Vec3 pos,Color col,double metal,double roughness)
	{
		Sphere sphere = new Sphere(pos,.5,new PhysicalMaterial(col,color(metal,roughness,1)));
		spheres.add(sphere);
	}

	private void _die(Vec3 pos,Color col,double metal,double roughness)
	{
		// corpus
		Box corpus = new Box(pos,1,1,1,
							 new PhysicalMaterial(col,color(metal,roughness,1)));
		Sphere rounding = new Sphere(pos,.75,
									 new PhysicalMaterial(multiply(col,1.5),color(metal,roughness,1)));
		Complex __Die = new Complex(corpus,rounding,JoinOperation.INTERSECTION);

		// number cutouts
		PhysicalMaterial __Cutouts = new PhysicalMaterial(color(.0,.05,.0),color(0,.7,1));
		Sphere one0 = new Sphere(add(pos,vec3(.0,-.5,.0)),.1,__Cutouts);
		Sphere two0 = new Sphere(add(pos,vec3(-.5,.25,.25)),.1,__Cutouts);
		Sphere two1 = new Sphere(add(pos,vec3(-.5,-.2,-.2)),.1,__Cutouts);
		Sphere three0 = new Sphere(add(pos,vec3(0,0,.5)),.1,__Cutouts);
		Sphere three1 = new Sphere(add(pos,vec3(.25,-.25,.5)),.1,__Cutouts);
		Sphere three2 = new Sphere(add(pos,vec3(-.25,.25,.5)),.1,__Cutouts);
		Complex numbers = new Complex(one0,two0,JoinOperation.UNION);
		numbers = new Complex(numbers,two1,JoinOperation.UNION);
		numbers = new Complex(numbers,three0,JoinOperation.UNION);
		numbers = new Complex(numbers,three1,JoinOperation.UNION);
		numbers = new Complex(numbers,three2,JoinOperation.UNION);
		__Die = new Complex(__Die,numbers,JoinOperation.DIFFERENCE);
		dice.add(__Die);
	}
}
