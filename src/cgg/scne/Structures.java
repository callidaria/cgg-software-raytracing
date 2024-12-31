package cgg.scne;

import static tools.Functions.*;
import tools.*;
import cgg.geom.*;
import cgg.mtrl.*;


public class Structures
{
	private Structures() {  }

	public static void floor(GraphNode node,Vec3 pos,double size,double texel)
	{
		node.register_geometry(
				new Box(pos,size,1,size,new PhysicalMaterial(Materials.tex_checker,Materials.mat_marble,texel))
			);
	}

	public static Geometry die(Vec3 pos,Vec3 rot,Color col,double size,double metal,double roughness)
	{
		// corpus
		Box corpus = new Box(pos,size,size,size,
							 new PhysicalMaterial(col,color(metal,roughness,1)));
		Sphere rounding = new Sphere(pos,.75*size,
									 new PhysicalMaterial(multiply(col,1.5),color(metal,roughness,1)));
		Complex __Die = new Complex(corpus,rounding,JoinOperation.INTERSECTION);

		// number cutouts
		double cs = size*.1;
		PhysicalMaterial __Cutouts = new PhysicalMaterial(color(.0,.05,.0),color(0,.7,1));
		Sphere one0 = new Sphere(add(pos,multiply(vec3(.0,-.5,.0),size)),cs,__Cutouts);
		Sphere two0 = new Sphere(add(pos,multiply(vec3(-.5,.25,.25),size)),cs,__Cutouts);
		Sphere two1 = new Sphere(add(pos,multiply(vec3(-.5,-.2,-.2),size)),cs,__Cutouts);
		Sphere three0 = new Sphere(add(pos,multiply(vec3(0,0,.5),size)),cs,__Cutouts);
		Sphere three1 = new Sphere(add(pos,multiply(vec3(.25,-.25,.5),size)),cs,__Cutouts);
		Sphere three2 = new Sphere(add(pos,multiply(vec3(-.25,.25,.5),size)),cs,__Cutouts);
		Complex numbers = new Complex(one0,two0,JoinOperation.UNION);
		numbers = new Complex(numbers,two1,JoinOperation.UNION);
		numbers = new Complex(numbers,three0,JoinOperation.UNION);
		numbers = new Complex(numbers,three1,JoinOperation.UNION);
		numbers = new Complex(numbers,three2,JoinOperation.UNION);
		__Die = new Complex(__Die,numbers,JoinOperation.DIFFERENCE);
		GraphNode __Node = new GraphNode(vec3(0),vec3(1),rot);

		// finalize
		__Node.register_geometry(__Die);
		__Node.update_bounds();
		return __Node;
	}
}
