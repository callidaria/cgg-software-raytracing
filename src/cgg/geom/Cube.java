package cgg.geom;

import static java.lang.Math.*;
import java.util.LinkedList;
import java.util.Queue;
import static tools.Functions.*;
import static cgg.Math.*;
import tools.*;
import cgg.a02.Ray;
import cgg.a02.Hit;


class TValueTuple
{
	public double near;
	public double far;

	public TValueTuple()
	{
		this.near = .0;
		this.far = Double.MAX_VALUE;
	}
}

// FIXME: this is not a cube but a quad. fix due to marketing issues
public class Cube implements Geometry
{
	private Vec3 bmin;
	private Vec3 bmax;
	private Color colour;

	public Cube(Vec3 position,double width,double height,double depth,Color colour)
	{
		Vec3 __HDim = vec3(width*.5,height*.5,depth*.5);
		this.bmin = subtract(position,__HDim);
		this.bmax = add(position,__HDim);
		this.colour = colour;
	}

	public Cube(Vec3 bmin,Vec3 bmax,Color colour)
	{
		this.bmin = bmin;
		this.bmax = bmax;
		this.colour = colour;
	}

	public Queue<HitTuple> intersect(Ray r)
	{
		// calculating quad intersections
		TValueTuple t = new TValueTuple();
		_clipAxis(t,bmin.x(),bmax.x(),r.origin().x(),r.direction().x());
		_clipAxis(t,bmin.y(),bmax.y(),r.origin().y(),r.direction().y());
		_clipAxis(t,bmin.z(),bmax.z(),r.origin().z(),r.direction().z());

		// ee in case of no intersection & assemble
		if (t.near>t.far) return new LinkedList<HitTuple>();
		Hit __Front = _assembleHit(r,t.near);
		Hit __Back = _assembleHit(r,t.far);

		// combine
		return primitive_hit(new HitTuple(__Front,__Back));
	}

	private void _clipAxis(TValueTuple t,double bmin,double bmax,double origin,double direction)
	{
		double __DFac = 1/direction;  // FIXME: div by zero possible for cube .5 xy
		double t0 = (bmin-origin)*__DFac;
		double t1 = (bmax-origin)*__DFac;
		t.near = max(t.near,min(t0,t1));
		t.far = min(t.far,max(t0,t1));
	}

	private Hit _assembleHit(Ray r,double t)
	{
		if (r.paramInRange(t))
		{
			Vec3 __Position = r.calculatePosition(t);
			Vec3 __Normal = vec3(1,0,0);  // TODO
			return new Hit(t,__Position,__Normal,colour);
		}
		return null;
	}
}
