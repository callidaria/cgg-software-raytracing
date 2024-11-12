package cgg.a02;

import static java.lang.Math.*;
import static tools.Functions.*;
import tools.*;
import cgg.mtrl.*;

public class Sphere
{
	private Vec3 center;
	private double radius;
	private double sq_radius;
	private SurfaceMaterial colour;

	public Sphere(Vec3 center,double radius,Color colour)
	{
		this.center = center;
		this.radius = radius;
		this.sq_radius = pow(radius,2);
		this.colour = new SurfaceMaterial(colour);
	}

	public Hit intersect(Ray r)
	{
		// component precalculation
		Vec3 __Center = subtract(r.origin(),center);
		double a = dot(r.direction(),r.direction());
		double b = 2*dot(__Center,r.direction());
		double c = dot(__Center,__Center)-sq_radius;

		// discriminant & early exit
		double sq_comp = pow(b,2)-4*a*c;
		if (sq_comp<0) return null;

		// calculate param
		double t0 = (-b+sqrt(sq_comp))/(2*a);
		double t1 = (-b-sqrt(sq_comp))/(2*a);
		double t;
		if (r.paramInRange(t0)&&r.paramInRange(t1)) t = (t0<t1) ? t0 : t1;
		else if (r.paramInRange(t0)||r.paramInRange(t1)) t = (r.paramInRange(t0)) ? t0 : t1;
		else return null;
		// FIXME: unelegant, my eyes bleed. i'm too tired to think about this now. lets move on...

		// assemble hit if param in range
		Vec3 __Position = r.calculatePosition(t);
		Vec3 __Normal = divide(subtract(__Position,center),radius);
		return new Hit(t,__Position,vec2(0,0),__Normal,colour);
	}
}
