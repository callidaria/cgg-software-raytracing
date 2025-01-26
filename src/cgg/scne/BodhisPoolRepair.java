package cgg.scne;

import static tools.Functions.*;
import tools.*;
import cgg.geom.*;
import cgg.mtrl.*;


public class BodhisPoolRepair extends Scene
{
	public BodhisPoolRepair()
	{
		super(vec3(0,-2.5,5),vec3(0,0,0));

		// flooring
		Structures.floor(groot,vec3(0,0,0),10,4);

		// geometry
		groot.register_geometry(new Sphere(vec3(0,-1,0),.5,new TransmittingMaterial(color(0,0,.5),1.5)));

		craeveTheVorbiddenLaemp(vec3(-1.7,-2.5,-3.5),color(.7,.7,.7),.7);
		craeveTheVorbiddenLaemp(vec3(1.7,-2.5,-1.5),color(.7,.7,.7),.7);
		groot.update_bounds();
	}
}
