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

	public static boolean recentGeometry(Queue<HitTuple> h0,Queue<HitTuple> h1)
	{
		return (h0.size()==0)||(h1.size()>0&&h1.peek().front().param()<h0.peek().front().param());
	}

	public static boolean secondHitRecent(Hit h0,Hit h1)
	{
		return h1==null||(h0!=null&&h1.param()<h0.param());
	}

	public static boolean secondHitRecentOtherDefault(Hit h0,Hit h1)
	{
		return h0==null||(h1!=null&&h1.param()<h0.param());
	}

	public static Hit closestHit(Hit h0,Hit h1)
	{
		return secondHitRecent(h0,h1) ? h1 : h0;
	}

	public static Hit farthestHit(Hit h0,Hit h1)
	{
		return secondHitRecent(h0,h1) ? h0 : h1;
	}
}
