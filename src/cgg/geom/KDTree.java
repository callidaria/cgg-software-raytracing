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

	public KDTree(List<Triangle> triangles)
	{
		this.bounds = BoundingBox.empty();

		// node state
		if (triangles.size()>3)
		{
			// TODO sort by distance maybe
		}

		// leaf state
		else
		{
			this.triangles = triangles;
			for (Triangle t : triangles) this.bounds.extend(t.bounding_box());
		}
	}
	// TODO modify to use both nodes and leafs seperately

	public Queue<HitTuple> intersect(Ray ray)
	{
		// TODO
		return null;
	}

	public BoundingBox bounding_box() { return bounds; }
}
