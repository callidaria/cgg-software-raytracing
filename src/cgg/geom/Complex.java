package cgg.geom;

import java.util.LinkedList;
import java.util.Queue;
import tools.*;
import static cgg.Math.*;
import cgg.a02.Ray;
import cgg.a02.Hit;


public class Complex implements Geometry
{
	private Geometry g0;
	private Geometry g1;
	private JoinOperation jop;
	private BoundingBox bounds;

	public Complex(Geometry g0,Geometry g1,JoinOperation jop)
	{
		this.g0 = g0;
		this.g1 = g1;
		this.jop = jop;
		this.bounds = (g1!=null) ? BoundingBox.around(g0.bounding_box(),g1.bounding_box()) : g0.bounding_box();
		// TODO bounding boxes of intersection and difference should not be same as union!
		//		this can be sped up slightly when correctly boxed in precalculation
	}

	public Queue<HitTuple> intersect(Ray ray)
	{
		if (!bounds.intersect(ray)) return new LinkedList<HitTuple>();
		if (g1==null) return g0.intersect(ray);
		Queue<HitTuple> __Hits0 = g0.intersect(ray);
		Queue<HitTuple> __Hits1 = g1.intersect(ray);
		switch(jop)
		{
			case UNION: return _union(__Hits0,__Hits1);
			case INTERSECTION: return _intersect(__Hits0,__Hits1);
			case DIFFERENCE: return _difference(__Hits0,__Hits1);
		}
		return null;
	}

	private Queue<HitTuple> _union(Queue<HitTuple> h0,Queue<HitTuple> h1)
	{
		Queue<HitTuple> __Final = new LinkedList<>();
		HitTuple __Hit = h0.poll();
		HitTuple __Inter = h1.poll();

		// iterate target geometry and combine in union
		while (__Hit!=null||__Inter!=null)
		{
			if (__Hit==null)
			{
				__Final.add(__Inter);
				__Inter = h1.poll();
				continue;
			}
			if (__Inter==null)
			{
				__Final.add(__Hit);
				__Hit = h0.poll();
				continue;
			}

			// check for intersection
			if (secondHitRecent(__Inter.front(),__Hit.back()))
			{
				__Final.add(__Hit);
				__Hit = h0.poll();
				continue;
			}
			if (secondHitRecent(__Hit.front(),__Inter.back()))
			{
				__Final.add(__Inter);
				__Inter = h1.poll();
				continue;
			}

			// unify intersecting geometry
			Hit __Front = closestHit(__Hit.front(),__Inter.front());
			Hit __Back = farthestHit(__Hit.back(),__Inter.back());
			__Hit = new HitTuple(__Front,__Back);
			__Inter = h1.poll();
		}
		return __Final;
	}

	private Queue<HitTuple> _intersect(Queue<HitTuple> h0,Queue<HitTuple> h1)
	{
		Queue<HitTuple> __Final = new LinkedList<>();
		HitTuple __Hit = h0.poll();
		HitTuple __Inter = h1.poll();

		// iterate target geometry and reduce to intersection
		while (__Hit!=null)
		{
			if (__Inter==null) return __Final;

			// check for intersection
			if (secondHitRecent(__Inter.front(),__Hit.back()))
			{
				__Hit = h0.poll();
				continue;
			}
			if (secondHitRecent(__Hit.front(),__Inter.back()))
			{
				__Inter = h1.poll();
				continue;
			}

			// assemble intersection
			Hit __Front = farthestHit(__Hit.front(),__Inter.front());
			Hit __Back = closestHit(__Hit.back(),__Inter.back());
			__Final.add(new HitTuple(__Front,__Back));

			// iterate geometry
			__Hit = secondHitRecent(__Hit.back(),__Back) ? new HitTuple(__Back,__Hit.back()) : h0.poll();
			__Inter = secondHitRecent(__Inter.back(),__Back) ? new HitTuple(__Back,__Inter.back()) : h1.poll();
		}
		return __Final;
	}

	private Queue<HitTuple> _difference(Queue<HitTuple> h0,Queue<HitTuple> h1)
	{
		Queue<HitTuple> __Final = new LinkedList<>();
		HitTuple __Hit = h0.poll();
		HitTuple __Inter = h1.poll();

		// iterate target geometry only (h0) and subtract
		while (__Hit!=null)
		{
			// no subtractions
			if (__Inter==null)
			{
				__Final.add(__Hit);
				__Hit = h0.poll();
				continue;
			}

			// check for intersection
			if (secondHitRecent(__Inter.front(),__Hit.back()))
			{
				__Final.add(__Hit);
				__Hit = h0.poll();
				continue;
			}
			if (secondHitRecent(__Hit.front(),__Inter.back()))
			{
				__Inter = h1.poll();
				continue;
			}

			// assemble difference
			// intersection front inside geometry
			if (secondHitRecent(__Inter.front(),__Hit.front()))
			{
				__Final.add(new HitTuple(__Hit.front(),__Inter.front()));
				if (!secondHitRecent(__Inter.back(),__Hit.back()))
				{
					__Hit = h0.poll();
					continue;
				}
				__Hit = new HitTuple(__Inter.back(),__Hit.back());
				__Inter = h1.poll();
			}

			// intersection front pre geometry
			else
			{
				if (secondHitRecent(__Inter.back(),__Hit.back()))
				{
					__Hit = h0.poll();
					continue;
				}
				__Hit = new HitTuple(__Inter.back(),__Hit.back());
				__Final.add(__Hit);
				__Inter = h1.poll();
			}
		}
		return __Final;
	}

	public BoundingBox bounding_box() { return bounds; }
}
