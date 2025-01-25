package cgg.geom;

import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import tools.*;
import static tools.BoundingBox.*;
import static tools.Functions.*;
import static cgg.Math.*;
import cgg.a02.*;


class KDTree implements Geometry
{
	List<Triangle> triangles;
	KDTree left;
	KDTree right;
	BoundingBox bounds;
	// TODO update this later to not only be applicable to triangles

	public KDTree(List<Triangle> triangles)
	{
		this.triangles = null;

		// intersection bounds
		this.bounds = BoundingBox.empty;
		for (Triangle t : triangles) this.bounds.extend(t.bounding_box());

		// leaf state
		if (triangles.size()<4)
		{
			this.triangles = triangles;
			return;
		}

		// node state
		// calculate most prominent axis of geometry field
		Vec3 __Dim = bounds.size();
		int __PA = (__Dim.y()>__Dim.x()) ? 1 : 0;
		__PA = (__Dim.z()>((__PA==0)?__Dim.x():__Dim.y())) ? 2 : __PA;

		// sort polygons by distance on prominent axis
		LinkedList<Triangle> __Sorted = new LinkedList<Triangle>();
		for (Triangle t : triangles)
		{
			double __Target = t.center_1D(__PA);

			// iterate until desired place
			int i = 0;
			while (i<__Sorted.size())
			{
				if (__Target>__Sorted.get(i).center_1D(__PA)) break;
				i++;
			}

			// insert into sorted triangle list
			__Sorted.add(i,t);
		}

		// packaging triangles into seperate tree subprogressions
		int __Until = (int)(__Sorted.size()*.5);
		this.left = new KDTree(__Sorted.subList(0,__Until));
		this.right = new KDTree(__Sorted.subList(__Until,__Sorted.size()));
	}

	public Queue<HitTuple> intersect(Ray ray)
	{
		// node state
		if (triangles==null)
		{
			Queue<HitTuple> __LeftRecent = left.intersect(ray);
			Queue<HitTuple> __RightRecent = right.intersect(ray);
			return (recentGeometry(__LeftRecent,__RightRecent)) ? __RightRecent : __LeftRecent;
		}

		// leaf state
		// iterate geometry
		Hit __Recent = null;
		for (Triangle t : triangles)
		{
			Queue<HitTuple> __Hits = t.intersect(ray);

			// test for recent geometry
			if (__Hits.size()>0)
			{
				Hit p_Hit = __Hits.peek().front();
				__Recent = (__Recent==null||(p_Hit!=null&&p_Hit.param()<__Recent.param())) ? p_Hit : __Recent;
			}
		}

		// assemble hit
		if (__Recent==null) return new LinkedList<HitTuple>();
		return primitive_hit(new HitTuple(__Recent,__Recent));
	}

	public BoundingBox bounding_box() { return bounds; }
}
