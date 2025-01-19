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

	public KDTree()
	{
		// TODO
	}

	public Queue<HitTuple> intersect(Ray ray)
	{
		// TODO
		return null;
	}

	public BoundingBox bounding_box() { return bounds; }
}
