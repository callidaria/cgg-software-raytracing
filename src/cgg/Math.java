package cgg;

import java.util.LinkedList;
import java.util.Queue;
import static tools.Functions.*;
import tools.*;
import cgg.mtrl.*;
import cgg.a02.Hit;
import cgg.geom.HitTuple;


public class Math
{
	public static Hit hit_pointblank(Vec3 position,Material material)
	{
		return new Hit(0,position,vec2(0,0),vec3(0,0,0),material);
	}

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
