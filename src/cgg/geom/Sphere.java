package cgg.geom;

import static java.lang.Math.*;
import java.util.LinkedList;
import java.util.Queue;
import static tools.Functions.*;
import static cgg.Math.*;
import tools.*;
import cgg.mtrl.*;
import cgg.a02.Ray;
import cgg.a02.Hit;


public class Sphere implements Geometry
{
	private Vec3 center;
	private double radius;
	private double sq_radius;
	private Material material;
	private BoundingBox bounds;

	public Sphere(Vec3 center,double radius,Material material)
	{
		this.center = center;
		this.radius = radius;
		this.sq_radius = pow(radius,2);
		this.material = material;
		this.bounds = new BoundingBox(radius).transform(move(this.center));
	}

	public Queue<HitTuple> intersect(Ray r)
	{
		// check bounds
		if (!bounds.intersect(r)) return new LinkedList<HitTuple>();

		// component precalculation
		Vec3 __Center = subtract(r.origin(),center);
		double a = dot(r.direction(),r.direction());
		double b = 2*dot(__Center,r.direction());
		double c = dot(__Center,__Center)-sq_radius;

		// discriminant & early exit
		double sq_comp = pow(b,2)-4*a*c;
		if (sq_comp<0) return new LinkedList<HitTuple>();

		// calculate param
		double t0 = (-b+sqrt(sq_comp))/(2*a), t1 = (-b-sqrt(sq_comp))/(2*a);
		double ts0 = min(t0,t1), ts1 = max(t0,t1);

		// assemble hits
		if (!r.paramInRange(ts0)&&!r.paramInRange(ts1)) return new LinkedList<HitTuple>();
		Hit __Front = _assembleHit(r,ts0,1);
		Hit __Back = _assembleHit(r,ts1,-1);

		// combine hits as primitive geometry output
		__Front = (__Front==null&&__Back!=null) ? hit_pointblank(r.origin(),material) : __Front;
		return primitive_hit(new HitTuple(__Front,__Back));
	}

	public BoundingBox bounding_box()
	{
		return bounds;
	}

	private Hit _assembleHit(Ray r,double t,int nmod)
	{
		if (!r.paramInRange(t)) return null;
		Vec3 __Position = r.calculatePosition(t);
		Vec3 __Origin = subtract(__Position,center);
		Vec2 __UV = vec2((atan2(__Origin.z(),__Origin.x())+PI)/(2*PI),(PI-acos(__Origin.y()/radius))/PI);
		Vec3 __Normal = multiply(divide(__Origin,radius),nmod);
		return new Hit(t,__Position,__UV,__Normal,material);
	}
}
