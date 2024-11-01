package cgg;

import java.util.LinkedList;
import java.util.Queue;
import cgg.a02.Hit;
import cgg.geom.HitTuple;


public class Math
{
	public static Queue<HitTuple> primitive_hit(HitTuple tuple)
	{
		Queue<HitTuple> out = new LinkedList<>();
		out.add(tuple);
		return out;
	}

	public static boolean secondHitRecent(Hit h0,Hit h1)
	{
		return h0==null||(h1!=null&&h1.param()<h0.param());
	}
}
