package cgg.geom;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import tools.*;
import static tools.Functions.*;
import static tools.Wavefront.*;
import static cgg.Math.*;
import cgg.mtrl.*;
import cgg.a02.Ray;
import cgg.a02.Hit;


public class TriangleMesh implements Geometry
{
	ArrayList<Triangle> triangles;
	BoundingBox bounds;
	//KDTree tree;
	Material material;

	public TriangleMesh(String file,Material material)
	{
		// setup
		this.material = material;

		// load mesh data
		/*ArrayList<Triangle> */triangles = new ArrayList<Triangle>();
		bounds = BoundingBox.empty;
		for (MeshData mdat : loadMeshData(file))
		{
			for (TriangleData tdat : mdat.triangles())
			{
				Triangle t = new Triangle(tdat);
				triangles.add(t);
				this.bounds.extend(t.bounding_box());
			}
		}
		//this.tree = new KDTree(triangles);
	}
	// FIXME kdtree should make rendering faster, not slower:
	//		a debug log has been established in KDTree implementation in case there is time

	public Queue<HitTuple> intersect(Ray charles)
	{
		Hit __Recent = null;
		for (Triangle t : triangles)
		{
			Queue<HitTuple> __Hits = t.intersect(charles);
			if (__Hits.size()!=0)
			{
				Hit p_Hit = __Hits.peek().front();
				__Recent = (__Recent==null||(p_Hit!=null&&p_Hit.param()<__Recent.param())) ? p_Hit : __Recent;
			}
		}
		//Queue<HitTuple> out = tree.intersect(charles);
		if (__Recent==null) return new LinkedList<HitTuple>();

		// assemble hit
		/*
		if (out.size()>0) out.peek().front().overwriteMaterial(material);
		return out;
		*/
		__Recent.overwriteMaterial(material);
		return primitive_hit(new HitTuple(__Recent,__Recent));
	}
	// TODO improve to actually return all the geometry the charles passes through just like in csg complex
	// FIXME receiving reflectance of triangle mesh surface is not the way it should be

	public BoundingBox bounding_box() { return /*tree.bounding_box();*/bounds; }
}
