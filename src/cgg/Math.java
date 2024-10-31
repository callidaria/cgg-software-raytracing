package cgg;

import java.util.ArrayList;
import cgg.a02.Hit;
import cgg.geom.HitTuple;

public class Math
{
	public static ArrayList<HitTuple> primitive_hit(HitTuple tuple)
	{
		ArrayList<HitTuple> out = new ArrayList<HitTuple>();
		out.add(tuple);
		return out;
	}

	public static boolean secondHitRecent(Hit h0,Hit h1)
	{
		return h0==null||(h1!=null&&h1.param()<h0.param());
	}
}
