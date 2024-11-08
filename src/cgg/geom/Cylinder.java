package cgg.geom;

import static java.lang.Math.*;
import java.util.LinkedList;
import java.util.Queue;
import static tools.Functions.*;
import static cgg.Math.*;
import tools.*;
import cgg.a02.Ray;
import cgg.a02.Hit;


public class Cylinder implements Geometry
{
	private Vec3 position;
	private double height;
	private double radius;
	private double sq_radius;
	private Color colour;

	public Cylinder(Vec3 position,double height,double radius,Color colour)
	{
		this.position = position;
		this.height = height;
		this.radius = radius;
		this.sq_radius = pow(radius,2);
		this.colour = colour;
	}

	public Queue<HitTuple> intersect(Ray r)
	{
		// spherical component precalculation
		double a = pow(r.direction().x(),2)+pow(r.direction().z(),2);
		double b = 2*(r.direction().x()*(r.origin().x()-position.x())
					  + r.direction().z()*(r.origin().z()-position.z()));
		double c = pow((r.origin().x()-position.x()),2)+pow(r.origin().z()-position.z(),2)-sq_radius;

		// discriminant & early exit
		double sq_comp = pow(b,2)-4*a*c;
		if (sq_comp<0) return new LinkedList<HitTuple>();

		// calculate param
		double t0 = (-b+sqrt(sq_comp))/(2*a),t1 = (-b-sqrt(sq_comp))/(2*a);
		double ts0 = min(t0,t1),ts1 = max(t0,t1);

		// assemble hits
		boolean p0 = _probe(r,ts0),p1 = _probe(r,ts1);
		if (!p0&&!p1) return new LinkedList<HitTuple>();
		Hit __Front = (!p0&&p1) ? _assembleCaps(r,ts0,1) : _assembleSpherical(r,ts0,1);
		Hit __Back = (p0&&!p1) ? _assembleCaps(r,ts1,-1) : _assembleSpherical(r,ts1,-1);

		// combine hits as primitive geometry output
		if (__Front==null&&__Back==null) return new LinkedList<HitTuple>();
		__Front = (__Front==null&&__Back!=null) ? new Hit(0,r.origin(),vec3(0,0,0),colour) : __Front;
		return primitive_hit(new HitTuple(__Front,__Back));
	}

	private boolean _probe(Ray r,double t)
	{
		Vec3 __Position = r.calculatePosition(t);
		return __Position.y()<position.y()+height&&__Position.y()>position.y();
	}

	private Hit _assembleSpherical(Ray r,double t,int nmod)
	{
		if (!r.paramInRange(t)) return null;
		Vec3 __Position = r.calculatePosition(t);
		Vec3 __Normal = normalize(vec3(__Position.x()-position.x(),0,__Position.z()-position.z()));
		return new Hit(t,__Position,__Normal,colour);
	}

	private Hit _assembleCaps(Ray r,double t,int nmod)
	{
		// calculate position
		Vec3 __Position = r.calculatePosition(t);
		boolean __Attitude = __Position.y()<position.y();
		double y = (__Attitude) ? position.y() : position.y()+height;
		double s = (y-__Position.y())/r.direction().y();
		__Position = add(__Position,multiply(r.direction(),s));

		// calculate normal
		Vec3 __Normal = multiply((__Attitude) ? vec3(0,1,0) : vec3(0,-1,0),nmod);
		return new Hit(t,__Position,vec3(0,nmod,0),colour);
	}

	/*
	private Hit _assembleHit(Ray r,double t,int nmod)
	{
		Vec3 __Position = r.calculatePosition(t);
		if (!r.paramInRange(t)||!(__Position.y()<position.y()+height&&__Position.y()>position.y())) return null;
		Vec3 __Normal = vec3(__Position.x()-position.x(),0,__Position.z()-position.z());
		boolean caphit = length(__Normal)<radius*.9999;
		if (caphit) __Normal = vec3(0,-1,0);
		//if (caphit) __Normal = (__Position.y()<position.y()+radius*.5) ? vec3(0,1,0) : vec3(0,-1,0);
		else __Normal = normalize(__Normal);
		__Normal = multiply(__Normal,nmod);
		__Normal = vec3(0,-1,0);
		return new Hit(t,__Position,__Normal,colour);
	}
	*/
}
