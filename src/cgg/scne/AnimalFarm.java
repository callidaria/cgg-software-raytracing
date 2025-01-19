package cgg.scne;

import static tools.Functions.*;
import tools.*;
import cgg.geom.*;
import cgg.mtrl.*;


public class AnimalFarm extends Scene
{
	public AnimalFarm()
	{
		super(vec3(0,-2.5,4.5),vec3(0,0,0));

		// cornell box
		groot.register_geometry(_cornellBox(vec3(0),5,false));

		// reference object
		/*
		groot.register_geometry(
				new Sphere(vec3(-1.75,-.5,-7),.5,new PhysicalMaterial(color(0,0,.7),color(1,.1,1)))
			);
		*/
		groot.register_geometry(new Box(vec3(-1.75,-.5,-2.5),1,1,1,
										new PhysicalMaterial(color(0,0,.7),color(0,1,1))));

		// animal
		GraphNode __Well = new GraphNode(vec3(1.25,0,-2.5),vec3(1),vec3(0,0,0));
		__Well.register_geometry(
				new TriangleMesh("./res/mesh/box.obj",new PhysicalMaterial(color(0,0,.7),color(0,1,1)))
			);
		__Well.update_bounds();
		groot.register_geometry(__Well);

		craeveTheVorbiddenLaemp(vec3(-1.7,-4,-3.5),color(.7,.7,.7),.7);
		craeveTheVorbiddenLaemp(vec3(1.7,-4,-1.5),color(.7,.7,.7),.7);
		groot.update_bounds();
	}

	private Geometry _cornellBox(Vec3 pos,double size,boolean cinematic)
	{
		// precalculation
		double hsize = size*.5;
		Color rwall = cinematic ? new Color(0,0,.5) : new Color(0,.7,0);
		PhysicalMaterial neutral = new PhysicalMaterial(color(1),color(0,1,1));

		// definition
		GraphNode out = new GraphNode(pos,vec3(1),vec3(0));

		out.register_geometry(new Box(vec3(0,.5,-hsize),size,1,size,neutral));
		/*
		out.register_geometry(new Box(vec3(0,-hsize,-size-.5),size,size,1,neutral));
		out.register_geometry(new Box(vec3(0,-size-.5,-hsize),size,1,size,neutral));
		out.register_geometry(new Box(vec3(-hsize-.5,-hsize,-hsize),1,size,size,
									  new PhysicalMaterial(color(.7,0,0),color(0,1,1))));
		out.register_geometry(new Box(vec3(hsize+.5,-hsize,-hsize),1,size,size,
									  new PhysicalMaterial(rwall,color(0,1,1))));
		*/

		out.update_bounds();
		return out;
	}
}
