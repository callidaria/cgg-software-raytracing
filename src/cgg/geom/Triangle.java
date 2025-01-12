package cgg.geom;

import java.util.LinkedList;
import java.util.Queue;
import static tools.Functions.*;
import tools.*;
import static cgg.Math.*;
import cgg.mtrl.*;
import cgg.a02.Ray;
import cgg.a02.Hit;


public class Triangle implements Geometry
{
	private Vertex v0;
	private Vertex v1;
	private Vertex v2;
	private BoundingBox bounds;

	public Triangle(Vertex v0,Vertex v1,Vertex v2)
	{
		this.v0 = v0;
		this.v1 = v1;
		this.v2 = v2;
		bounds = BoundingBox.around(v0.position(),v1.position(),v2.position());
	}

	public Queue<HitTuple> intersect(Ray ray)
	{
		// normal
		Vec3 __ParaCross = cross(subtract(v1.position(),v0.position()),subtract(v2.position(),v0.position()));
		Vec3 __ParaNormal = normalize(__ParaCross);
		double __InvSize = 1./(length(__ParaCross)/2.);

		// calculate intersection
		double t = dot(subtract(v0.position(),ray.origin()),__ParaNormal)/dot(ray.direction(),__ParaNormal);
		if (!ray.paramInRange(t)) return new LinkedList<HitTuple>();
		Vec3 __InterPos = ray.calculatePosition(t);

		// barycentric coordinate
		Vec3 __BCentric = vec3(triangleSize(v1,v2,__InterPos)*__InvSize,
							   triangleSize(v2,v0,__InterPos)*__InvSize,
							   triangleSize(v0,v2,__InterPos)*__InvSize
			);
		if (!almostEqual(__BCentric.x()+__BCentric.y()+__BCentric.z(),1.)) return new LinkedList<HitTuple>();

		// assemble successful hit
		Vec3 __Normal = add(multiply(__BCentric.x(),v0.normal()),
							multiply(__BCentric.y(),v1.normal()),
							multiply(__BCentric.z(),v2.normal())
			);
		Vec2 __UV = add(multiply(v0.uv(),__BCentric.x()),
						multiply(v1.uv(),__BCentric.y()),
						multiply(v2.uv(),__BCentric.z())
			);
		Color __Colour = add(multiply(v0.color(),__BCentric.x()),
							 multiply(v1.color(),__BCentric.y()),
							 multiply(v2.color(),__BCentric.z())
			);
		Hit hits = new Hit(t,__InterPos,__UV,__Normal,new PhysicalMaterial(__Colour,color(0,1,1)));
		System.out.println(hits);
		return primitive_hit(new HitTuple(hits,hits));
	}

	public BoundingBox bounding_box() { return bounds; }
}
