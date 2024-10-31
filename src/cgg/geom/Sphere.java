package cgg.geom;

import static java.lang.Math.*;
import java.util.ArrayList;
import static tools.Functions.*;
import static cgg.Math.*;
import tools.*;
import cgg.a02.Ray;
import cgg.a02.Hit;

public class Sphere implements Geometry
{
	private Vec3 center;
	private double radius;
	private double sq_radius;
	private Color colour;

	public Sphere(Vec3 center,double radius,Color colour)
	{
		this.center = center;
		this.radius = radius;
		this.sq_radius = pow(radius,2);
		this.colour = colour;
	}

	public ArrayList<HitTuple> intersect(Ray r)
	{
		// component precalculation
		Vec3 __Center = subtract(r.origin(),center);
		double a = dot(r.direction(),r.direction());
		double b = 2*dot(__Center,r.direction());
		double c = dot(__Center,__Center)-sq_radius;

		// discriminant & early exit
		double sq_comp = pow(b,2)-4*a*c;
		if (sq_comp<0) return new ArrayList<HitTuple>();

		// calculate param
		double t0 = (-b+sqrt(sq_comp))/(2*a);
		double t1 = (-b-sqrt(sq_comp))/(2*a);
		double t;
		if (r.paramInRange(t0)&&r.paramInRange(t1)) t = (t0<t1) ? t0 : t1;
		else t = (r.paramInRange(t0)) ? t0 : t1;
		// FIXME: unelegant, my eyes bleed. i'm too tired to think about this now. lets move on...

		// assemble hit if param in range
		Vec3 __Position = r.calculatePosition(t);
		Vec3 __Normal = divide(subtract(__Position,center),radius);
		return primitive_hit(new HitTuple(new Hit(t,__Position,__Normal,colour),null));
		// TODO: back intersection
	}
}
