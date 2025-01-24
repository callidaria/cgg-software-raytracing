package cgg.geom;

import java.util.List;
import java.util.Queue;
import tools.*;
import static tools.Functions.*;
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
		this.bounds = BoundingBox.empty();
		for (Triangle t : triangles) this.bounds.extend(t.bounding_box());

		// node state
		if (triangles.size()>3)
		{
			BoundingBox __SubBox = bounds.splitLeft();

			// packaging triangles into seperate tree subprogressions
			ArrayList<Triangle> __LeftTriangles = new ArrayList<Triangle>();
			ArrayList<Triangle> __RightTriangles = new ArrayList<Triangle>();
			for (Triangle t : triangles)
			{
				if (__SubBox.contains(t)) __LeftTriangles.add(t);
				else __RightTriangles.add(t);
			}

			// passing sorted triangle groups into tree hierarchy
			this.left = new KDTree(__LeftTriangles);
			this.right = new KDTree(__RightTriangles);
		}

		// leaf state
		else this.triangles = triangles;
	}

	public Queue<HitTuple> intersect(Ray ray)
	{
		// node state
		if (triangles==null)
		{
			HitTuple __LeftRecent = left.intersect(ray).peek();
			HitTuple __RightRecent = right.intersect(ray).peek();
		}

		// leaf state
		Hit __Recent = null;
		for (Triangle t : triangles)
		{
			Queue<HitTuple> __Hits = t.intersect(ray);
			if (__Hits.size()>0)
			{
				Hit p_Hit = __Hits.peek().front();
				__Recent = (__Recent==null||(p_Hit!=null&&p_Hit.param()<__Recent.param())) ? p_Hit : __Recent;
			}
		}
		return __Recent;
	}

	public BoundingBox bounding_box() { return bounds; }
}
