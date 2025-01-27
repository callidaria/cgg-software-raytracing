package cgg.scne;

import java.util.ArrayList;
import static tools.Functions.*;
import tools.*;
import cgg.geom.*;
import cgg.lght.*;
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
	private final double PF_G;

	public BodhisPoolRepair(int framerate)
	{
		super(vec3(0,-4,10),vec3(15,0,0));
		PF_G = .981/framerate;

		// geometry
		// odd entities
		pe = new ArrayList<PhysicalEntity>();
		pe.add(new PhysicalEntity(vec3(-1,-.75,0),vec3(0),.5,1.5,.4,color(0,.5,0),MaterialState.EMITTER));
		pe.add(new PhysicalEntity(vec3(1,-1.5,0),vec3(0),.5,1.5,.4,color(.5,.5,0),MaterialState.EMITTER));
		/*
		pe.add(new PhysicalEntity(vec3(-2,-4,-2),_rd(),.5,1.5,.8,color(.5,0,0),MaterialState.TRANSMITTER));
		pe.add(new PhysicalEntity(vec3(-2,-3,2),_rd(),.5,1.5,.8,color(0,.5,0),MaterialState.TRANSMITTER));
		pe.add(new PhysicalEntity(vec3(2,-2,-2),_rd(),.5,1.5,.8,color(0,0,.5),MaterialState.TRANSMITTER));
		pe.add(new PhysicalEntity(vec3(2,-5,2),_rd(),.5,1.5,.8,color(.5,.5,0),MaterialState.TRANSMITTER));
		*/
		pe.add(new PhysicalEntity(vec3(-2,-4,-2),_rd(),.5,1.5,.7,color(.5,0,0),MaterialState.TRANSMITTER));
		pe.add(new PhysicalEntity(vec3(-2,-3,-2),_rd(),.5,1.5,.7,color(0,.5,0),MaterialState.TRANSMITTER));
		pe.add(new PhysicalEntity(vec3(4,-2,-2),_rd(),.5,1.5,.7,color(0,0,.5),MaterialState.TRANSMITTER));
		pe.add(new PhysicalEntity(vec3(-4,-5,-2),_rd(),.5,1.5,.7,color(.5,.5,0),MaterialState.TRANSMITTER));
		pe.add(new PhysicalEntity(vec3(-2,-4,2),_rd(),.5,1.5,.7,color(.5,0,0),MaterialState.TRANSMITTER));
		pe.add(new PhysicalEntity(vec3(-2,-3,2),_rd(),.5,1.5,.7,color(0,.5,0),MaterialState.TRANSMITTER));
		pe.add(new PhysicalEntity(vec3(4,-2,2),_rd(),.5,1.5,.7,color(0,0,.5),MaterialState.TRANSMITTER));
		pe.add(new PhysicalEntity(vec3(-4,-5,2),_rd(),.5,1.5,.7,color(.5,.5,0),MaterialState.TRANSMITTER));

		// phyiscal based entities
		/*
		pe.add(new PhysicalEntity(vec3(-4,-4,-4),_rd(),.1,4,.1,color(.5,.5,0),MaterialState.METAL));
		pe.add(new PhysicalEntity(vec3(4,-6,-4),_rd(),.1,4,.4,color(0,0,.5),MaterialState.METAL));
		*/
		/*
		pe.add(new PhysicalEntity(vec3(-4,-1,4),_rd(),.5,1,.4,color(0,.5,.5),MaterialState.ROUGH));
		pe.add(new PhysicalEntity(vec3(4,-3,4),_rd(),.5,1,.4,color(.5,0,.5),MaterialState.ROUGH));
		*/
		/*
		pe.add(new PhysicalEntity(vec3(-3,-2,1),_rd(),.25,1.2,.4,color(.5,0,0),MaterialState.SMOOTH));
		pe.add(new PhysicalEntity(vec3(3,-4,-1),_rd(),.25,1.2,.4,color(0,.5,0),MaterialState.SMOOTH));
		*/
	}

	private Vec3 _rd() { return vec3((random()-.5)*.5,0,(random()-.5)*.5); }
	private Vec3 _r2d() { return vec3(random()-.5,0,0); }

	public void update()
	{
		for (int i=0;i<pe.size();i++)
		{
			PhysicalEntity e = pe.get(i);

			// update position
			e.position = add(e.position,e.direction);

			// gravity application
			if (e.grounded())
			{
				e.direction = multiply(e.direction,vec3(1,-e.elastic,1));
				e.position = subtract(e.position,vec3(0,e.position.y()+e.radius,0));
			}
			e.direction = add(e.direction,vec3(0,PF_G,0));

			// collision
			e.bounds(5,5);
			for (int j=i+1;j<pe.size();j++)
			{
				PhysicalEntity f = pe.get(j);

				// spacial entity relationship & collision test
				Vec3 __Twd = subtract(f.position,e.position);
				double __TMass = e.mass+f.mass;
				double __Dist = length(__Twd)-(e.radius+f.radius);
				if (__Dist>0) continue;

				// geometric properties of elastic collision
				double v0 = length(e.direction), v1 = length(f.direction);
				e.direction = normalize(e.direction);
				f.direction = normalize(f.direction);
				__Twd = normalize(__Twd);
				Vec3 __Twi = multiply(__Twd,-1);

				// combine trajectory after collision
				double p = 2*(e.mass*v0+f.mass*v1)/__TMass;
				double v0_i = p-v0, v1_i = p-v1;
				double offc0 = dot(__Twi,f.direction), offc1 = dot(__Twd,e.direction);
				if (offc1>0)
					e.direction = add(multiply(e.direction,v1_i*(1-offc1)),multiply(__Twi,v0_i*offc0*e.elastic));
				if (offc0>0)
					f.direction = add(multiply(f.direction,v0_i*(1-offc0)),multiply(__Twd,v1_i*offc1*f.elastic));

				// rejecting overintersection
				e.position = add(e.position,multiply(__Twd,__Dist*(f.mass/__TMass)+.001));
				f.position = add(f.position,multiply(__Twi,__Dist*(e.mass/__TMass)+.001));
			}

			// reject jitterbounce
			e.direction = multiply(
					e.direction,
					vec3(
							(e.direction.x()<.001&&e.direction.x()>-.001) ? 0 : 1,
							(e.direction.y()<.001&&e.direction.y()>-.001) ? 0 : 1,
							(e.direction.z()<.001&&e.direction.z()>-.001) ? 0 : 1
						)
				);
		}
	}

	public void render_setup(int frame)
	{
		groot = new GraphNode();
		lights = new ArrayList<Illumination>();
		emitter = new ArrayList<Geometry>();

		// basic setup
		Structures.floor(groot,vec3(0,.5,0),10,4);
		craeveTheVorbiddenLaemp(vec3(-1.7,-2.5,-5.5),color(.7,.7,.7),.7);
		craeveTheVorbiddenLaemp(vec3(1.7,-2.5,-5.5),color(.7,.7,.7),.7);

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
			case EMITTER:
				em = new AbsoluteMaterial(hue(((double)frame/400.+e.colour.r())%1.));
				break;
				/*
			case METAL:
				em = new PhysicalMaterial(e.colour,color(1,.15,1));
				break;
			case ROUGH:
				em = new PhysicalMaterial(e.colour,color(0,.8,1));
				break;
			case SMOOTH:
				em = new PhysicalMaterial(e.colour,color(0,.2,1));
				break;
				*/
			}

			// write to node
			groot.register_geometry(new Sphere(e.position,e.radius,em));
		}

		groot.update_bounds();
	}
}
