package cgg.scne;

import java.util.ArrayList;
import static tools.Functions.*;
import tools.*;
import cgg.geom.*;
import cgg.mtrl.*;


enum MaterialState
{
	EMITTER,
	TRANSMITTER,
	METAL,
	ROUGH,
	SMOOTH
}


class PhysicalEntity
{
	public Vec3 position;
	public Vec3 direction;
	public double radius;
	public double mass;
	public double elastic;
	public Color colour;
	public MaterialState material;

	public PhysicalEntity(Vec3 position,Vec3 direction,double radius,double mass,double elastic,
						  Color colour,MaterialState material)
	{
		this.position = position;
		this.direction = direction;
		this.radius = radius;
		this.mass = mass;
		this.elastic = elastic;
		this.colour = colour;
		this.material = material;
	}
}


public class BodhisPoolRepair extends Scene
{
	private ArrayList<PhysicalEntity> pe;
	private final double PF_G = .981/30;

	public BodhisPoolRepair()
	{
		super(vec3(0,-2.5,7),vec3(0,0,0));

		// geometry
		pe = new ArrayList<PhysicalEntity>();
		pe.add(new PhysicalEntity(vec3(0,-4,0),vec3(0,0,0),.5,1,.25,color(0,0,.5),MaterialState.TRANSMITTER));
		groot.register_geometry(new Sphere(vec3(0,-1,0),.5,new TransmittingMaterial(color(0,0,.5),1.5)));

		craeveTheVorbiddenLaemp(vec3(-1.7,-2.5,-5.5),color(.7,.7,.7),.7);
		craeveTheVorbiddenLaemp(vec3(1.7,-2.5,-5.5),color(.7,.7,.7),.7);
	}

	public void update()
	{
		// gravity application
		for (PhysicalEntity e : pe)
		{
			e.direction = add(e.direction,vec3(0,PF_G,0));
			e.position = add(e.position,e.direction);
		}
	}

	public void render_setup()
	{
		groot = new GraphNode();
		Structures.floor(groot,vec3(0,0,0),10,4);

		// assemble geometry
		for (PhysicalEntity e : pe)
		{
			// material identification
			Material em = new UnknownMaterial();
			switch (e.material)
			{
			case TRANSMITTER:
				em = new TransmittingMaterial(e.colour,1.5);
				break;
			}

			// write to node
			groot.register_geometry(new Sphere(e.position,e.radius,em));
		}

		groot.update_bounds();
	}
}
