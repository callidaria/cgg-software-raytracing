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
		groot.register_geometry(_cornellBox(5,false));

		craeveTheVorbiddenLaemp(vec3(0,-4,-2.5),color(.7,.7,.7),.7);
		groot.update_bounds();
	}

	private Geometry _cornellBox(double size,boolean cinematic)
	{
		// precalculation
		double hsize = size*.5;
		Color rwall = cinematic ? new Color(0,0,.5) : new Color(0,.7,0);
		PhysicalMaterial neutral = new PhysicalMaterial(color(1),color(0,1,1));

		// definition
		GraphNode out = new GraphNode();
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
