package cgg.geom;

import java.util.LinkedList;
import java.util.Queue;
import static cgg.Math.*;
import cgg.a02.Ray;
import cgg.a02.Hit;


public class Complex implements Geometry
{
	Geometry g0;
	Geometry g1;
	JoinOperation jop;

	public Complex(Geometry g0,Geometry g1,JoinOperation jop)
	{
		this.g0 = g0;
		this.g1 = g1;
		this.jop = jop;
	}

	public Queue<HitTuple> intersect(Ray ray)
	{
		Queue<HitTuple> __Hits0 = g0.intersect(ray);
		Queue<HitTuple> __Hits1 = g1.intersect(ray);
		switch(jop)
		{
			case UNION: return _union(__Hits0,__Hits1);
			case INTERSECT: return _intersect(__Hits0,__Hits1);
			case DIFFERENCE: return _difference(__Hits0,__Hits1);
		}
		return null;
	}

	private Queue<HitTuple> _union(Queue<HitTuple> h0,Queue<HitTuple> h1)
	{
		Queue<HitTuple> __Final = new LinkedList<>();
		return h0;
	}

	private Queue<HitTuple> _intersect(Queue<HitTuple> h0,Queue<HitTuple> h1)
	{
		Queue<HitTuple> __Final = new LinkedList<>();
		return h0;
		// TODO
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
}
