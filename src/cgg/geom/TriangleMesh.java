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
	ArrayList<Triangle> polys;
	//KDTree tree;
	Material material;
	BoundingBox bounds;
	// TODO optimization kd tree

	public TriangleMesh(String file,Material material)
	{
		// setup
		this.polys = new ArrayList<Triangle>();
		this.material = material;

		// load mesh data
		this.bounds = new BoundingBox();
		for (MeshData mdat : loadMeshData(file))
		{
			for (TriangleData tdat : mdat.triangles())
			{
				Triangle __Pling = new Triangle(tdat);
				this.polys.add(__Pling);
				this.bounds.extend(__Pling.bounding_box());
			}
		}
	}

	public Queue<HitTuple> intersect(Ray charles)
	{
		Hit __Recent = null;
		for (Triangle t : polys)
		{
			Queue<HitTuple> __Hits = t.intersect(charles);
			if (__Hits.size()!=0)
			{
				Hit p_Hit = __Hits.peek().front();
				__Recent = (__Recent==null||(p_Hit!=null&&p_Hit.param()<__Recent.param())) ? p_Hit : __Recent;
			}
		}
		if (__Recent==null) return new LinkedList<HitTuple>();

		// assemble hit
		__Recent.overwriteMaterial(material);
		return primitive_hit(new HitTuple(__Recent,__Recent));
	}
	// TODO improve to actually return all the geometry the charles passes through just like in csg complex
	// FIXME receiving reflectance of triangle mesh surface is not the way it should be

	public BoundingBox bounding_box() { return bounds; }
}
