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

public class Box implements Geometry
{
	private Vec3 position;
	private Vec3 hdim;
	private Vec3 bmin;
	private Vec3 bmax;
	private Material material;

	public Box(Vec3 position,double width,double height,double depth,Material material)
	{
		this.position = position;
		this.hdim = multiply(vec3(width,height,depth),.5);
		this.bmin = subtract(position,hdim);
		this.bmax = add(position,hdim);
		this.material = material;
	}

	public Queue<HitTuple> intersect(Ray r)
	{
		// calculating quad intersections
		TValueTuple t = new TValueTuple();
		_clipAxis(t,bmin.x(),bmax.x(),r.origin().x(),r.direction().x());
		_clipAxis(t,bmin.y(),bmax.y(),r.origin().y(),r.direction().y());
		_clipAxis(t,bmin.z(),bmax.z(),r.origin().z(),r.direction().z());

		// ee in case of no intersection & assemble
		if (t.near>t.far||(!r.paramInRange(t.near)&&!r.paramInRange(t.far))) return new LinkedList<HitTuple>();
		Hit __Front = _assembleHit(r,t.near);
		Hit __Back = _assembleHit(r,t.far);

		// combine
		__Front = (__Back!=null&&__Front==null) ? hit_pointblank(r.origin(),material) : __Front;
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
		if (!r.paramInRange(t)) return null;
		Vec3 __Position = r.calculatePosition(t);
		Vec3 __Origin = subtract(__Position,position);

		// normals
		Vec3 lp = divide(__Origin,hdim);
		boolean hx = (abs(lp.x())>abs(lp.y()))&&(abs(lp.x())>abs(lp.z()));
		boolean hy = !hx&&(abs(lp.y())>abs(lp.z()));
		Vec3 __Normal = normalize(vec3(hx?lp.x():0,hy?lp.y():0,!(hx||hy)?lp.z():0));

		// texture coordinates
		Vec2 __UV = multiply(vec2(((hx)?lp.z():lp.x())+1.,((hy)?lp.z():lp.y())+1.),vec2(.5,.5));

		// finalize
		return new Hit(t,__Position,__UV,__Normal,material);
	}
}
