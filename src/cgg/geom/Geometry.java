package cgg.geom;

import java.util.Queue;
import cgg.a02.Ray;
import cgg.a02.Hit;


public interface Geometry
{
	public Queue<HitTuple> intersect(Ray ray);
}
