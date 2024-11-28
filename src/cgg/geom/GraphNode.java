package cgg.geom;

import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;
import tools.*;
import static tools.Functions.*;
import static cgg.Math.*;
import cgg.a02.Ray;
import cgg.a02.Hit;


public class GraphNode implements Geometry
{
	private Mat44 transform;
	private Mat44 inv_transform;
	private Mat44 tinv_transform;
	private ArrayList<Geometry> geometry;
	private BoundingBox bounds;

	public GraphNode(ArrayList<Geometry> geometry)
	{
		this.transform = identity();
		this.geometry = geometry;
		_cacheTransform();
		_createBoundingBox();
	}

	public GraphNode(ArrayList<Geometry> geometry,Vec3 position,Vec3 scale,Vec3 rotation)
	{
		this.transform = multiply(move(position),multiply(scale(scale),rotate(rotation)));
		this.geometry = geometry;
		_cacheTransform();
		_createBoundingBox();
	}

	private void _cacheTransform()
	{
		this.inv_transform = invert(transform);
		this.tinv_transform = transpose(inv_transform);
	}

	private void _createBoundingBox()
	{
		this.bounds = BoundingBox.around(geometry).transform(this.transform);
	}

	public Queue<HitTuple> intersect(Ray ray)
	{
		// transform
		Ray __Local = new Ray(
				multiplyPoint(inv_transform,ray.origin()),
				multiplyDirection(inv_transform,ray.direction()),
				.0001,1000
			);

		// check bounds
		Queue<HitTuple> __Result = new LinkedList<HitTuple>();
		if (!bounds.intersect(ray)) return __Result;

		// check hits
		for (Geometry g : geometry)
		{
			Queue<HitTuple> __Hits = g.intersect(__Local);
			__Result = (recentGeometry(__Result,__Hits)) ? __Hits : __Result;
		}

		// transform result
		for (HitTuple hx : __Result)
		{
			_processTransformation(hx.front());
			_processTransformation(hx.back());
		}
		return __Result;
	}

	public BoundingBox bounding_box()
	{
		return bounds;
	}

	private Hit _processTransformation(Hit hit)
	{
		if (hit==null) return null;
		hit.overwritePosition(multiplyPoint(transform,hit.position()));
		hit.overwriteNormal((length(hit.normal())>.0)
							? normalize(multiplyDirection(tinv_transform,hit.normal()))
							: vec3(0,0,0));
		return hit;
	}
}
