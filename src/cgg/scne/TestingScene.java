package cgg.scne;

import static tools.Functions.*;
import tools.*;
import cgg.geom.*;
import cgg.mtrl.*;


public class TestingScene extends Scene
{
	public TestingScene()
	{
		super(vec3(0,-1,0),vec3(0,0,0));

		// flooring
		Structures.floor(groot,vec3(0,1,-5),10,4);

		// geometry
		GraphNode __TestingSpheres = new GraphNode(vec3(0,0,-4),vec3(1),vec3(0));
		__TestingSpheres.register_geometry(
				new Sphere(vec3(-1,0,0),.5,
						   new PhysicalMaterial(color(0,.05,0),Materials.mat_plastic,1),Materials.nrm_plastic)
			);
		__TestingSpheres.register_geometry(
				new Sphere(vec3(0,0,-1),.5,
						   new PhysicalMaterial(color(.75,.25,0),Materials.mat_gold,1),Materials.nrm_gold)
			);
		__TestingSpheres.register_geometry(
				new Sphere(vec3(1,0,0),.5,
						   new PhysicalMaterial(color(.75,.0,0),Materials.mat_marble,1),Materials.nrm_marble)
			);
		__TestingSpheres.update_bounds();
		groot.register_geometry(__TestingSpheres);

		// lighting
		craeveTheVorbiddenLaemp(vec3(0,-1.5,-7),color(1,1,1),.4);
		/*
		craeveTheVorbiddenLaemp(vec3(1.25,-1,-4),color(1,.5,0),.7);
		craeveTheVorbiddenLaemp(vec3(1.25,-2,-1),color(0,.5,1),1);
		craeveTheVorbiddenLaemp(vec3(1.5,.75,-1),color(.7,.7,.7),.4);
		craeveTheVorbiddenLaemp(vec3(2.5,.75,-5.4),color(.7,.7,.7),.4);
		craeveTheVorbiddenLaemp(vec3(-1.7,-2.15,-7),color(1,1,1),.2);
		*/

		groot.update_bounds();
	}
}
