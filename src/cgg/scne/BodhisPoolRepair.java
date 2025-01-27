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

	public boolean grounded() { return (position.y()+radius)>0; }

	public void bounds(double x,double z)
	{
		// x axis bounds
		if (position.x()+radius>x||position.x()-radius<-x)
		{
			direction = multiply(direction,vec3(-elastic,1,1));
			double __Correction = Math.abs(position.x())+radius-x;
			position = subtract(position,vec3(__Correction*((position.x()<0)?-1:1),0,0));
		}

		// z axis bounds
		if (position.z()+radius>z||position.z()-radius<-z)
		{
			direction = multiply(direction,vec3(1,1,-elastic));
			double __Correction = Math.abs(position.z())+radius-z;
			position = subtract(position,vec3(0,0,__Correction*((position.z()<0?-1:1))));
		}
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
		pe.add(new PhysicalEntity(vec3(0,-4,0),vec3(.5,0,-.4),.5,1,.8,color(0,0,.5),MaterialState.TRANSMITTER));

		craeveTheVorbiddenLaemp(vec3(-1.7,-2.5,-5.5),color(.7,.7,.7),.7);
		craeveTheVorbiddenLaemp(vec3(1.7,-2.5,-5.5),color(.7,.7,.7),.7);
	}

	public void update()
	{
		for (PhysicalEntity e : pe)
		{
			// gravity application
			if (e.grounded())
			{
				e.direction = multiply(e.direction,vec3(1,-e.elastic,1));
				e.position = subtract(e.position,vec3(0,e.position.y()+e.radius,0));
			}
			else e.direction = add(e.direction,vec3(0,PF_G,0));

			// keep physical entities inbounds
			e.bounds(5,5);

			// prevent jitterbounce
			e.direction = multiply(
					e.direction,
					vec3(
							(e.direction.x()<.001&&e.direction.x()>-.001) ? 0 : 1,
							(e.direction.y()<.001&&e.direction.y()>-.001) ? 0 : 1,
							(e.direction.z()<.001&&e.direction.z()>-.001) ? 0 : 1
						)
				);

			// update position
			e.position = add(e.position,e.direction);
		}
	}

	public void render_setup()
	{
		groot = new GraphNode();
		Structures.floor(groot,vec3(0,.5,0),10,4);

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
